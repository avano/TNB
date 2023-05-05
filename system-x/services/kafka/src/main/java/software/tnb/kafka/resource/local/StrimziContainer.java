
package software.tnb.kafka.resource.local;

import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.TestcontainersConfiguration;

public class StrimziContainer extends GenericContainer<StrimziContainer> {

    private static final String CONTAINER_NAME = "kafka";
    private static final int KAFKA_PORT = 9092;

    public StrimziContainer(String image, Network network) {
        super(image);

        withNetwork(network);

        withCreateContainerCmdModifier(
            cmd -> {
                cmd.withHostName(CONTAINER_NAME);
                cmd.withName(CONTAINER_NAME);
            }
        );

        String listenerAddress = "localhost";
        String dockerHost = TestcontainersConfiguration.getInstance().getEnvironment().get("DOCKER_HOST");
        if (dockerHost != null) {
            listenerAddress = StringUtils.substringBetween(dockerHost, "tcp://", ":2375");
        }
        withExposedPorts(KAFKA_PORT);
        addFixedExposedPort(KAFKA_PORT, KAFKA_PORT);
        withEnv("LOG_DIR", "/tmp/log");
        withCommand("sh", "-c", String.format("bin/kafka-server-start.sh config/server.properties "
                + "--override zookeeper.connect=%s:%d "
                + "--override advertised.listeners=PLAINTEXT://%s:%d",
            ZookeeperContainer.CONTAINER_NAME, ZookeeperContainer.ZOOKEEPER_PORT,
            listenerAddress, KAFKA_PORT));

        waitingFor(Wait.forListeningPort());
    }

    public int getKafkaPort() {
        return getMappedPort(KAFKA_PORT);
    }
}
