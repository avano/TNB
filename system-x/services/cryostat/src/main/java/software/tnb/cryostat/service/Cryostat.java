package software.tnb.cryostat.service;

import software.tnb.common.account.Account;
import software.tnb.common.service.Service;
import software.tnb.cryostat.client.CryostatClient;
import software.tnb.cryostat.validation.CryostatValidation;

import java.util.Optional;

public abstract class Cryostat extends Service<Account, CryostatClient, CryostatValidation> {
    public abstract String connectionUrl();

    public abstract CryostatClient client();

    public abstract int getPortMapping(int port);

    public CryostatValidation validation() {
        validation = Optional.ofNullable(validation)
            .orElseGet(() -> new CryostatValidation(client()));
        return validation;
    }
}
