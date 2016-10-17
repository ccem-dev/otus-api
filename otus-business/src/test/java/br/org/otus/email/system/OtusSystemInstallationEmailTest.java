package br.org.otus.email.system;

import br.org.otus.email.OtusEmailFactory;
import br.org.owail.sender.email.Email;
import br.org.owail.sender.email.Recipient;
import br.org.owail.sender.email.Sender;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;

public class OtusSystemInstallationEmailTest {
    private Sender sender;
    private Recipient recipient;

    @Before
    public void setup() {
        sender = new Sender(anyString(), anyString(), anyString());
        recipient = Recipient.createTO("Recipient Name", "recipient@email.com");
    }

    @Test
    public void createSystemInstallationEmail_method_should_return_an_instance_of_SystemInstallationEmail() {
        Object email = OtusEmailFactory.createSystemInstallationEmail(sender, recipient);

        assertThat(email, instanceOf(SystemInstallationEmail.class));
    }

    @Test
    public void createSystemInstallationEmail_method_should_return_an_email_with_recipient_defined() {
        Email email = OtusEmailFactory.createSystemInstallationEmail(sender, recipient);

        assertThat(email.getRecipients(), not(empty()));
    }

    @Test
    public void createSystemInstallationEmail_method_should_return_an_email_with_from_defined() {
        Email email = OtusEmailFactory.createSystemInstallationEmail(sender, recipient);

        assertThat(email.getFrom(), notNullValue());
    }
}
