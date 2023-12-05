package software.tnb.db.mssql.service;

import software.tnb.db.common.account.SQLAccount;
import software.tnb.db.common.service.SQL;
import software.tnb.db.mssql.account.MSSQLAccount;

import java.util.Map;

public abstract class MSSQL extends SQL {
    protected static final int PORT = 1433;

    @Override
    public String defaultImage() {
        return "mcr.microsoft.com/mssql/rhel/server:2019-latest";
    }

    @Override
    protected Class<? extends SQLAccount> accountClass() {
        return MSSQLAccount.class;
    }

    @Override
    public String jdbcConnectionUrl() {
        return String.format("jdbc:sqlserver://%s:%d;databaseName=%s;TrustServerCertificate=True", host(), port(), account().database());
    }

    @Override
    public Map<String, String> containerEnvironment() {
        return Map.of(
            "ACCEPT_EULA", "Y",
            "SA_PASSWORD", account().password()
        );
    }

    @Override
    public int port() {
        return PORT;
    }
}
