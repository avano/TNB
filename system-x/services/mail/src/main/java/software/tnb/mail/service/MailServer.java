package software.tnb.mail.service;

import software.tnb.common.account.NoAccount;
import software.tnb.common.client.NoClient;
import software.tnb.common.deployment.WithDockerImage;
import software.tnb.common.service.Service;
import software.tnb.mail.validation.MailValidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MailServer extends Service<NoAccount, NoClient, MailValidation> implements WithDockerImage {
    private static final Logger LOG = LoggerFactory.getLogger(MailServer.class);
    protected static final int SMTP_PORT = 25;
    protected static final int HTTP_PORT = 8000;
    protected static final int IMAP_PORT = 143;
    protected static final int POP3_PORT = 110;

    protected MailValidation validation;

    public MailValidation validation() {
        if (validation == null) {
            LOG.debug("Creating new Mail validation");
            validation = new MailValidation(smtpValidationHostname(), httpHostname(), imapExternalHostname());
        }
        return validation;
    }

    public String defaultImage() {
        return "quay.io/fuse_qe/james-tnb:memory-3.7.0";
    }

    public abstract String smtpHostname();

    public abstract String imapHostname();

    // A hostname where you can connect using a java email client using imap
    protected abstract String imapExternalHostname();

    public abstract String pop3Hostname();

    public abstract String httpHostname();

    public abstract String smtpValidationHostname();
}
