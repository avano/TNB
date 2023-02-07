package software.tnb.db.cassandra.service.configuration;

import software.tnb.common.service.configuration.ServiceConfiguration;

public class CassandraConfiguration extends ServiceConfiguration {
    private static final String CASSANDRA_REPLICAS = "cassandra.replicas";

    public CassandraConfiguration replicas(int replicas) {
        set(CASSANDRA_REPLICAS, replicas);
        return this;
    }

    public int getReplicas() {
        return get(CASSANDRA_REPLICAS, Integer.class);
    }
}
