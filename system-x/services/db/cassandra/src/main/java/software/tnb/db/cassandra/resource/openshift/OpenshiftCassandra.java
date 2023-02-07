package software.tnb.db.cassandra.resource.openshift;

import software.tnb.common.config.OpenshiftConfiguration;
import software.tnb.common.deployment.OpenshiftDeployable;
import software.tnb.common.deployment.WithExternalHostname;
import software.tnb.common.deployment.WithInClusterHostname;
import software.tnb.common.deployment.WithName;
import software.tnb.common.openshift.OpenshiftClient;
import software.tnb.common.utils.IOUtils;
import software.tnb.common.utils.NetworkUtils;
import software.tnb.db.cassandra.service.Cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.google.auto.service.AutoService;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import cz.xtf.core.openshift.OpenShiftWaiters;
import cz.xtf.core.openshift.helpers.ResourceFunctions;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.client.LocalPortForward;

@AutoService(Cassandra.class)
public class OpenshiftCassandra extends Cassandra implements OpenshiftDeployable, WithName, WithInClusterHostname, WithExternalHostname {
    private static final int INTERNAL_PORT = 7000;
    private static final String INTERNAL_PORT_NAME = "internal";

    private CqlSession session;
    private LocalPortForward portForward;
    private int localPort;

    @Override
    public void create() {
        //@formatter:off
        OpenshiftClient.get().apps().statefulSets().createOrReplace(
            new StatefulSetBuilder()
                .withNewMetadata()
                    .withName(name())
                    .addToLabels(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                .endMetadata()
                    .editOrNewSpec()
                        .editOrNewSelector()
                            .addToMatchLabels(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                        .endSelector()
                        .withReplicas(getConfiguration().getReplicas())
                        .editOrNewTemplate()
                            .editOrNewMetadata()
                                .addToLabels(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                            .endMetadata()
                            .withNewSpec()
                                .addNewContainer()
                                    .withName(name())
                                    .withImage(image())
                                    .addAllToEnv(containerEnvironment().entrySet().stream().map(e -> new EnvVar(e.getKey(), e.getValue(), null))
                                        .collect(Collectors.toList()))
                                    .addNewPort()
                                        .withContainerPort(port())
                                        .withName(name())
                                    .endPort()
                                    .addNewPort()
                                        .withContainerPort(INTERNAL_PORT)
                                        .withName(INTERNAL_PORT_NAME)
                                    .endPort()
                                    .withImagePullPolicy("IfNotPresent")
                                    .withNewReadinessProbe()
                                        .withNewTcpSocket()
                                            .withNewPort(name())
                                        .endTcpSocket()
                                        .withInitialDelaySeconds(30) // cannot go much lower before seeing issues
                                        .withTimeoutSeconds(5)
                                        .withFailureThreshold(6)
                                    .endReadinessProbe()
                                .endContainer()
                            .endSpec()
                        .endTemplate()
                    .endSpec()
                .build()
        );

        for (int i = 0; i < getConfiguration().getReplicas(); i++) {
            OpenshiftClient.get().services().createOrReplace(
                new ServiceBuilder()
                    .withNewMetadata()
                        .withName(name() + "-" + i)
                        .addToLabels(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                    .endMetadata()
                    .withNewSpec()
                        .addToSelector("statefulset.kubernetes.io/pod-name", name() + "-" + i)
                        .addNewPort()
                            .withName(name())
                            .withPort(port())
                            .withTargetPort(new IntOrString(port()))
                        .endPort()
                        .addNewPort()
                            .withName(INTERNAL_PORT_NAME)
                            .withPort(INTERNAL_PORT)
                            .withTargetPort(new IntOrString(INTERNAL_PORT))
                        .endPort()
                    .endSpec()
                    .build()
            );
        }

        OpenshiftClient.get().services().createOrReplace(
            new ServiceBuilder()
                .withNewMetadata()
                    .withName(name())
                    .addToLabels(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                .endMetadata()
                .withNewSpec()
                    .addToSelector(OpenshiftConfiguration.openshiftDeploymentLabel(), name())
                    .addNewPort()
                        .withName(name())
                        .withPort(port())
                        .withTargetPort(new IntOrString(port()))
                    .endPort()
                .endSpec()
                .build()
        );
        //@formatter:on
    }

    @Override
    protected CqlSession session() {
        return session;
    }

    @Override
    public int port() {
        return CASSANDRA_PORT;
    }

    @Override
    public String host() {
        return name();
    }

    @Override
    public void undeploy() {
        OpenshiftClient.get().services().withName(name()).delete();
        for (int i = 0; i < getConfiguration().getReplicas(); i++) {
            OpenshiftClient.get().services().withName(name() + "-" + i).delete();
        }
        OpenshiftClient.get().apps().statefulSets().withName(name()).delete();
        OpenShiftWaiters.get(OpenshiftClient.get(), () -> false)
            .areNoPodsPresent(OpenshiftConfiguration.openshiftDeploymentLabel(), name()).timeout(120_000).waitFor();
    }

    @Override
    public void openResources() {
        localPort = NetworkUtils.getFreePort();
        portForward = OpenshiftClient.get().services().withName(name()).portForward(port(), localPort);

        // default timeout pretty much always failed when creating table, increase to 30s
        DriverConfigLoader loader =
            DriverConfigLoader.programmaticBuilder()
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
                .build();

        session = CqlSession.builder()
            .withConfigLoader(loader)
            .addContactPoint(new InetSocketAddress(externalHostname(), localPort))
            .withAuthCredentials(account().username(), account().password())
            .withLocalDatacenter(account().datacenter())
            .build();
    }

    @Override
    public void closeResources() {
        if (session != null) {
            session.close();
            session = null;
        }
        IOUtils.closeQuietly(portForward);
        NetworkUtils.releasePort(localPort);
    }

    @Override
    public boolean isReady() {
        List<Pod> pods = OpenshiftClient.get().getLabeledPods(OpenshiftConfiguration.openshiftDeploymentLabel(), name());
        if (ResourceFunctions.areExactlyNPodsReady(getConfiguration().getReplicas()).apply(pods)) {
            return pods.stream().filter(p -> OpenshiftClient.get().getLogs(p).contains("Startup complete")).count()
                == getConfiguration().getReplicas();
        }
        return false;
    }

    @Override
    public boolean isDeployed() {
        return OpenshiftClient.get().getLabeledPods(OpenshiftConfiguration.openshiftDeploymentLabel(), name()).size() > 0;
    }

    @Override
    public String name() {
        return "cassandra";
    }

    @Override
    public String externalHostname() {
        return "localhost";
    }

    @Override
    public long waitTime() {
        return 300_000L * getConfiguration().getReplicas();
    }
}
