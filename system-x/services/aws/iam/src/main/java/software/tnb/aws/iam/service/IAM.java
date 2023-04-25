package software.tnb.aws.iam.service;

import software.tnb.aws.common.account.AWSAccount;
import software.tnb.aws.common.service.AWSService;
import software.tnb.aws.iam.validation.IAMValidation;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.auto.service.AutoService;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;

@AutoService(IAM.class)
public class IAM extends AWSService<AWSAccount, IamClient, IAMValidation> {
    @Override
    protected IamClient client() {
        // IAM client doesn't have the "create" method as other clients, probably because it's not tied to any region
        if (client == null) {
            client = IamClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(() -> AwsBasicCredentials.create(account().accessKey(), account().secretKey()))
                .build();
        }
        return client;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        LOG.debug("Creating new IAM validation");
        validation = new IAMValidation(client());
    }
}
