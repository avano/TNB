package software.tnb.http.resource.local;

import software.tnb.common.deployment.Deployable;
import software.tnb.http.service.HTTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

@AutoService(HTTP.class)
public class LocalHTTP extends HTTP implements Deployable {
    private static final Logger LOG = LoggerFactory.getLogger(HTTP.class);
    private HTTPContainer container;

    @Override
    public void deploy() {
        LOG.info("Starting Http container");
        container = new HTTPContainer(image());
        container.start();
        LOG.info("Http container started");
    }

    @Override
    public void undeploy() {
        if (container != null) {
            LOG.info("Stopping Http container");
            container.stop();
        }
    }

    @Override
    public void openResources() {

    }

    @Override
    public void closeResources() {

    }

    @Override
    public String getLog() {
        return container.getLogs();
    }

    @Override
    public String httpUrl() {
        return String.format("http://%s:%d/", container.getHost(), container.getHttpPort());
    }

    @Override
    public String httpsUrl() {
        return String.format("https://%s:%d/", container.getHost(), container.getHttpsPort());
    }
}
