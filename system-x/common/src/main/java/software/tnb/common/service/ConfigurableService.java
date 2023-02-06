package software.tnb.common.service;

import software.tnb.common.service.configuration.ServiceConfiguration;

import org.junit.platform.commons.util.ReflectionUtils;

public abstract class ConfigurableService<C extends ServiceConfiguration> implements Service {
    private final C configuration;

    public abstract Class<C> configurationClass();

    public ConfigurableService() {
        configuration = ReflectionUtils.newInstance(configurationClass());
    }

    public C getConfiguration() {
        return configuration;
    }
}
