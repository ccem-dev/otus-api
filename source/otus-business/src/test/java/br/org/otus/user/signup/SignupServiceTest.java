package br.org.otus.user.signup;

import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.http.EmailNotificationException;
import org.ccem.otus.exceptions.webservice.security.EncryptedException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.otus.configuration.builder.SystemConfigBuilder;
import br.org.otus.configuration.dto.OtusInitializationConfigDto;
import br.org.otus.email.OtusEmailFactory;
import br.org.otus.email.dto.EmailSenderDto;
import br.org.otus.email.service.EmailNotifierServiceBean;
import br.org.otus.email.user.signup.NewUserGreetingsEmail;
import br.org.otus.email.user.signup.NewUserNotificationEmail;
import br.org.otus.model.User;
import br.org.otus.user.UserDaoBean;
import br.org.otus.user.dto.SignupDataDto;
import br.org.otus.user.management.ManagementUserService;
import br.org.owail.sender.email.Recipient;
import br.org.owail.sender.email.Sender;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SignupServiceBean.class, Recipient.class, OtusEmailFactory.class, SystemConfigBuilder.class})
public class SignupServiceTest {
    private static final String EMAIL = "email@email";
    private static final String NAME = "Teste";

    @InjectMocks
    private SignupServiceBean signupServiceBean;

    @Mock
    private UserDaoBean userDao;

    @Mock
    private EmailNotifierServiceBean emailNotifierService;

    @Mock
    private ManagementUserService managementUserService;

    @Mock
    private SignupDataDto signupDataDto;

    @Mock
    private Recipient recipient;

    @Mock
    private User user;

    @Mock
    private NewUserGreetingsEmail newUserGreetingsEmail;

    @Mock
    private Sender sender;

    @Mock
    private NewUserNotificationEmail newUserNotificationEmail;

    @Mock
    private OtusInitializationConfigDto initializationConfigDto;

    @Mock
    private EmailSenderDto emailSender;

    @Test(expected = AlreadyExistException.class)
    public void method_create_should_verify_if_isUnique() throws EmailNotificationException, EncryptedException, AlreadyExistException, ValidationException, DataNotFoundException {
        Mockito.when(signupDataDto.getEmail()).thenReturn(EMAIL);
        Mockito.when(signupDataDto.isValid()).thenReturn(Boolean.TRUE);
        signupServiceBean.create(signupDataDto);
        Mockito.verify(managementUserService.isUnique(signupDataDto.getEmail()));
    }

    @Test
    public void method_create_should_send_email() throws Exception {
        PowerMockito.mockStatic(Recipient.class);
        PowerMockito.mockStatic(OtusEmailFactory.class);
        PowerMockito.whenNew(User.class).withNoArguments().thenReturn(user);

        Mockito.when(emailNotifierService.getSender()).thenReturn(sender);
        Mockito.when(userDao.findAdmin()).thenReturn(user);
        Mockito.when(user.getEmail()).thenReturn(EMAIL);
        Mockito.when(user.getName()).thenReturn(NAME);
        Mockito.when(Recipient.createTO(NAME, EMAIL)).thenReturn(recipient);
        Mockito.when(OtusEmailFactory.createNewUserGreetingsEmail(sender, recipient)).thenReturn(newUserGreetingsEmail);
        Mockito.when(OtusEmailFactory.createNewUserNotificationEmail(sender, recipient, user)).thenReturn(newUserNotificationEmail);
        Mockito.when(managementUserService.isUnique(EMAIL)).thenReturn(Boolean.TRUE);
        Mockito.when(signupDataDto.getEmail()).thenReturn(EMAIL);
        Mockito.when(signupDataDto.isValid()).thenReturn(Boolean.TRUE);

        signupServiceBean.create(signupDataDto);
        Mockito.verify(emailNotifierService).sendEmailSync(newUserGreetingsEmail);
        Mockito.verify(emailNotifierService).sendEmailSync(newUserNotificationEmail);
    }

    @Test(expected = AlreadyExistException.class)
    public void method_create_should_throw_AlreadyExistException_when_user_exist() throws EmailNotificationException, EncryptedException, AlreadyExistException, ValidationException, DataNotFoundException {
        Mockito.when(managementUserService.isUnique(EMAIL)).thenReturn(Boolean.FALSE);
        Mockito.when(signupDataDto.isValid()).thenReturn(Boolean.TRUE);
        signupServiceBean.create(signupDataDto);
    }

    @Test(expected = AlreadyExistException.class)
    public void method_create_initializationConfig_should_verify_if_isUnique() throws EmailNotificationException, EncryptedException, AlreadyExistException, ValidationException {
        Mockito.when(initializationConfigDto.getEmailSender()).thenReturn(emailSender);
        Mockito.when(emailSender.getEmail()).thenReturn(EMAIL);
        Mockito.when(managementUserService.isUnique(EMAIL)).thenReturn(Boolean.FALSE);
        Mockito.when(initializationConfigDto.isValid()).thenReturn(Boolean.TRUE);
        signupServiceBean.create(initializationConfigDto);
        Mockito.verify(managementUserService.isUnique(initializationConfigDto.getUser().getEmail()));
    }

    @Test
    public void method_create_initializationConfig_should_send_email() throws EmailNotificationException, EncryptedException, AlreadyExistException, ValidationException {
        PowerMockito.mockStatic(OtusEmailFactory.class);
        PowerMockito.mockStatic(SystemConfigBuilder.class);

        PowerMockito.when(SystemConfigBuilder.buildInitialUser(initializationConfigDto)).thenReturn(user);
        Mockito.when(managementUserService.isUnique(EMAIL)).thenReturn(Boolean.TRUE);
        Mockito.when(initializationConfigDto.getEmailSender()).thenReturn(emailSender);
        Mockito.when(emailSender.getEmail()).thenReturn(EMAIL);
        Mockito.when(initializationConfigDto.isValid()).thenReturn(Boolean.TRUE);

        signupServiceBean.create(initializationConfigDto);
        Mockito.verify(emailNotifierService).sendSystemInstallationEmail(initializationConfigDto);
    }

    @Test(expected = AlreadyExistException.class)
    public void method_create_initializationConfig_should_throw_AlreadyExistException_when_user_exist() throws EmailNotificationException, EncryptedException, AlreadyExistException, ValidationException {
        Mockito.when(initializationConfigDto.getEmailSender()).thenReturn(emailSender);
        Mockito.when(emailSender.getEmail()).thenReturn(EMAIL);
        Mockito.when(managementUserService.isUnique(EMAIL)).thenReturn(Boolean.FALSE);
        Mockito.when(initializationConfigDto.isValid()).thenReturn(Boolean.TRUE);
        signupServiceBean.create(initializationConfigDto);
    }

    @Test(expected = ValidationException.class)
    public void method_create_initializationConfig_should_throw_ValidationException_when_dto_invalid() throws EmailNotificationException, ValidationException, AlreadyExistException, EncryptedException {
        Mockito.when(initializationConfigDto.isValid()).thenReturn(Boolean.FALSE);
        signupServiceBean.create(initializationConfigDto);
    }

    @Test(expected = ValidationException.class)
    public void method_create_signup_should_throw_ValidationException_when_dto_invalid() throws EmailNotificationException, ValidationException, AlreadyExistException, EncryptedException, DataNotFoundException {
        Mockito.when(signupDataDto.isValid()).thenReturn(Boolean.FALSE);
        signupServiceBean.create(signupDataDto);
    }
}
