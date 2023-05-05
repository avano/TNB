package software.tnb.ftp.ftp.resource.local;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.List;
import java.util.Map;

public class FTPContainer extends GenericContainer<FTPContainer> {
    public static final int FTP_COMMAND_PORT = 2121;
    public static final int FTP_DATA_PORT_START = 2122;
    public static final int FTP_DATA_PORT_END = 2130;

    public FTPContainer(String image, Map<String, String> env, List<Integer> ports) {
        super(image);
        withEnv(env);
        for (int i = 2121; i < 2131; i++) {
            addFixedExposedPort(i, i);
        }
        waitingFor(Wait.forLogMessage(".*FtpServer started.*", 1));
    }
}
