package software.tnb.aws.sqs.validation;

import software.tnb.aws.sqs.account.SQSAccount;
import software.tnb.common.utils.WaitUtils;
import software.tnb.common.validation.Validation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

public class SQSValidation implements Validation {
    private static final Logger LOG = LoggerFactory.getLogger(SQSValidation.class);

    private final SqsClient client;
    private final SQSAccount account;

    public SQSValidation(SqsClient client, SQSAccount account) {
        this.client = client;
        this.account = account;
    }

    public void createQueue(String name) {
        createQueueWithAttributes(name, null);
    }

    public void createContentBasedDeduplicationFifoQueue(String name) {
        if (!name.endsWith(".fifo")) {
            throw new IllegalArgumentException("Fifo queue name must end with .fifo");
        }
        createQueueWithAttributes(name, Map.of(QueueAttributeName.FIFO_QUEUE, "true", QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "true"));
    }

    public void createQueueWithAttributes(String name, Map<QueueAttributeName, String> attributes) {
        LOG.debug("Creating SQS queue {}", name);
        client.createQueue(b -> b.queueName(name).attributes(attributes));
        WaitUtils.waitFor(() -> queueExists(name), "Waiting until the queue " + name + " is created");
    }

    /**
     * Add this access policy to allow AWS users to use SQS queue as SNS topic subscriber.
     *
     * @param queue queue name
     * @see <a href="https://camel.apache.org/components/3.10.x/aws2-sns-component.html#_sns_fifo">Camel SNS documentation</a>
     */
    public void setPermissiveAccessPolicy(String queue) {
        Map<QueueAttributeName, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.POLICY,
            "{\n"
                + "\"Version\": \"2008-10-17\",\n"
                + "\"Id\": \"__default_policy_ID\",\n"
                + "\"Statement\": [\n"
                + "  {\n"
                + "    \"Sid\": \"__owner_statement\",\n"
                + "    \"Effect\": \"Allow\",\n"
                + "    \"Principal\": {\n"
                + "      \"AWS\": \"*\"\n"
                + "    },\n"
                + "    \"Action\": \"SQS:*\",\n"
                + "    \"Resource\": \"" + account.queueArnPrefix() + queue + "\"\n"
                + "  }\n"
                + "]\n"
                + "}");
        client.setQueueAttributes(builder -> builder.queueUrl(account.queueUrlPrefix() + queue).attributes(attributes
        ));
    }

    public void deleteQueue(String name) {
        LOG.debug("Deleting SQS queue {}", name);
        try {
            client.deleteQueue(b -> b.queueUrl(account.queueUrlPrefix() + name));
        } catch (QueueDoesNotExistException ignored) {
        }
    }

    public void sendMessage(String queue, String message) {
        LOG.debug("Sending message \"{}\" to queue {}", message, queue);
        client.sendMessage(b -> b.queueUrl(account.queueUrlPrefix() + queue).messageBody(message));
    }

    public List<Message> getMessages(String queue, int count) {
        return getMessages(queue, null, count);
    }

    public List<Message> getMessages(String queue, Collection<String> attributeNames, int count) {
        return WaitUtils.withTimeout(() -> {
            List<Message> messages = new ArrayList<>();
            while (messages.size() != count) {
                List<Message> current = client.receiveMessage(b -> b.queueUrl(account.queueUrlPrefix() + queue)
                        .attributeNamesWithStrings(attributeNames).maxNumberOfMessages(10)).messages();
                for (Message m : current) {
                    if (!messages.contains(m)) {
                        messages.add(m);
                    }
                }
            }
            return messages;
        });
    }

    public void deleteMessage(String queue, String receiptHandle) {
        LOG.debug("Deleting message with receipt handle {} from queue {}", receiptHandle, queue);
        client.deleteMessage(b -> b.queueUrl(account.queueUrlPrefix() + queue).receiptHandle(receiptHandle));
    }

    public boolean queueExists(String queue) {
        // request builder contains only prefix method, so check if any of the "prefixed" queues matches the name exactly
        // queue url is in form https://sqs.<region>.amazonaws.com/<accound-id>/<queue-name>
        return client.listQueues(b -> b.queueNamePrefix(queue)).queueUrls().stream()
            .anyMatch(url -> queue.equals(StringUtils.substringAfterLast(url, "/")));
    }

    public int getQueueSize(String queue) {
        return Integer.parseInt(client.getQueueAttributes(qab -> qab.queueUrl(client.getQueueUrl(qub -> qub.queueName(queue)).queueUrl())
                .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES))
            .attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
    }

    public void purgeQueue(String queue) {
        LOG.debug("Purging SQS queue {}", queue);
        try {
            client.purgeQueue(b -> b.queueUrl(account.queueUrlPrefix() + queue));
            //The message deletion process takes up to 60 seconds. We recommend waiting for 60 seconds regardless of your queue's size.
            WaitUtils.sleep(60000L);
        } catch (QueueDoesNotExistException ignored) {
        }
    }
}
