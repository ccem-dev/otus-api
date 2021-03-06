package br.org.otus.user.management;

import br.org.otus.email.service.EmailNotifierService;
import br.org.otus.model.User;
import br.org.otus.security.api.SecurityFacade;
import br.org.otus.security.dtos.PasswordResetRequestDto;
import br.org.otus.user.UserDaoBean;
import br.org.otus.user.dto.FieldCenterDTO;
import br.org.otus.user.dto.ManagementUserDto;
import br.org.otus.user.dto.PasswordResetDto;
import br.org.owail.sender.email.Sender;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.http.EmailNotificationException;
import org.ccem.otus.exceptions.webservice.security.EncryptedException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.persistence.FieldCenterDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ManagementUserServiceBean.class})
public class ManagementUserServiceBeanTest {
  private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJtb2RlIjoidXNlciIsImlzcyI6ImRpb2dvLnJvc2FzLmZlcnJlaXJhQGdtYWlsLmNvbSJ9.I5Ysne1C79cO5B_5hIQK9iBSnQ6M8msuyVHD4kdoFSo";
  private static final String PROJECT_REST_URL = "https://api-otus.dev.ccem.ufrgs.br";
  private static final String FIELD_CENTER_ATTRIBUITE = "fieldCenter";
  private static final String ACRONYM_ATTRIBUITE = "acronym";
  private static final String FIELD_CENTER_ACRONYM = "RS";
  private static final String FIELD_CENTER_WITHOUT_ACRONYM = "";
  private static final String EMAIL = "otus@otus.com";
  private static final String NAME = "João";
  private static final String PASSWORD = "123456";

  private static String IP = "192.168.0.1";

  @InjectMocks
  private ManagementUserServiceBean managementUserServiceBean = PowerMockito.spy(new ManagementUserServiceBean());
  @Mock
  private UserDaoBean userDao;
  @Mock
  private EmailNotifierService emailNotifierService;
  @Mock
  private ManagementUserDto managementUserDto;
  @Mock
  private User user;
  @Mock
  private Sender sender;

  private User userManager;
  @Mock
  private FieldCenterDTO fieldCenterDTO;
  @Mock
  private FieldCenterDao fieldCenterDao;
  @Mock
  private FieldCenter fieldCenter;
  @Mock
  private PasswordResetRequestDto requestData;
  @Mock
  private PasswordResetDto passwordResetDto;
  @Mock
  private SecurityFacade securityFacade;

  @Before
  public void setUp() throws DataNotFoundException {
    userManager = new User();
    userManager.setName(NAME);
    when(managementUserDto.getEmail()).thenReturn(EMAIL);
    when(managementUserServiceBean.fetchByEmail(EMAIL)).thenReturn(user);
    Whitebox.setInternalState(managementUserDto, FIELD_CENTER_ATTRIBUITE, fieldCenterDTO);
    when(fieldCenterDao.fetchByAcronym(Mockito.any())).thenReturn(fieldCenter);
  }

  @Test
  public void method_enable_should_fetch_user_by_email()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enable(managementUserDto);
    Mockito.verify(userDao).fetchByEmail(EMAIL);
  }

  @Test
  public void method_enable_should_change_status_to_enable()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enable(managementUserDto);
    Mockito.verify(user).enable();
  }

  @Test
  public void method_enable_should_update_user()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enable(managementUserDto);
    Mockito.verify(userDao).update(user);
  }

  @Test
  public void method_disable_should_fetch_user_by_email()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(user.isAdmin()).thenReturn(Boolean.FALSE);
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disable(managementUserDto);
    Mockito.verify(userDao).fetchByEmail(EMAIL);
  }

  @Test
  public void method_disable_should_change_status_to_enable()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(user.isAdmin()).thenReturn(Boolean.FALSE);
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disable(managementUserDto);
    Mockito.verify(user).disable();
  }

  @Test
  public void method_disable_should_update_user()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(user.isAdmin()).thenReturn(Boolean.FALSE);
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disable(managementUserDto);
    Mockito.verify(userDao).update(user);
  }

  @Test
  public void method_disable_should_verify_if_is_admin()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(user.isAdmin()).thenReturn(Boolean.TRUE);
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disable(managementUserDto);
    Mockito.verify(user).isAdmin();
    Mockito.verify(userDao, Mockito.never()).update(user);
  }

  @Test(expected = ValidationException.class)
  public void method_enable_should_throw_ValidationException_when_dto_invalid()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.FALSE);
    managementUserServiceBean.enable(managementUserDto);
  }

  @Test(expected = ValidationException.class)
  public void method_disable_should_throw_ValidationException_when_dto_invalid()
    throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.FALSE);
    managementUserServiceBean.disable(managementUserDto);
  }

  @Test
  public void method_enableExtraction_should_fetch_user_by_email() throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enableExtraction(managementUserDto);
    Mockito.verify(userDao).fetchByEmail(EMAIL);
  }

  @Test
  public void method_enableExtraction_should_change_status_to_enable()
    throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enableExtraction(managementUserDto);
    Mockito.verify(user).enableExtraction();
  }

  @Test
  public void method_enableExtraction_should_update_user() throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.enableExtraction(managementUserDto);
    Mockito.verify(userDao).update(user);
  }

  @Test(expected = ValidationException.class)
  public void method_enableExtraction_should_throw_ValidationException_when_dto_invalid()
    throws ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.FALSE);
    managementUserServiceBean.enableExtraction(managementUserDto);
  }

  @Test
  public void method_disableExtraction_should_fetch_user_by_email() throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disableExtraction(managementUserDto);
    Mockito.verify(userDao).fetchByEmail(EMAIL);
  }

  @Test
  public void method_disableExtraction_should_change_status_to_disabled()
    throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disableExtraction(managementUserDto);
    Mockito.verify(user).disableExtraction();
  }

  @Test
  public void method_disableExtraction_should_update_user() throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserServiceBean.disableExtraction(managementUserDto);
    Mockito.verify(userDao).update(user);
  }

  @Test(expected = ValidationException.class)
  public void method_disableExtraction_should_throw_ValidationException_when_dto_invalid()
    throws ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.FALSE);
    managementUserServiceBean.disableExtraction(managementUserDto);
  }

  @Test
  public void method_updateExtractionIps_should_update_user() throws ValidationException, DataNotFoundException {
    Mockito.when(userDao.fetchByEmail(EMAIL)).thenReturn(user);
    Mockito.when(managementUserDto.getEmail()).thenReturn(EMAIL);
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.TRUE);
    managementUserDto.extractionIps = new ArrayList();
    managementUserDto.extractionIps.add(IP);
    managementUserServiceBean.updateExtractionIps(managementUserDto);
    Mockito.verify(userDao).update(user);
  }

  @Test(expected = ValidationException.class)
  public void method_updateExtractionIps_should_throw_ValidationException_when_dto_invalid()
    throws ValidationException, DataNotFoundException {
    Mockito.when(managementUserDto.isValid()).thenReturn(Boolean.FALSE);
    managementUserServiceBean.updateExtractionIps(managementUserDto);
  }

  @Test
  public void listMethod_should_return_administrationUsersDtos() throws Exception {
    List<User> users = Arrays.asList(userManager);
    when(userDao.fetchAll()).thenReturn(users);
    assertEquals(NAME, managementUserServiceBean.list().get(0).name);
    verify(userDao, times(1)).fetchAll();
  }

  @Test
  public void updateFieldCenterMethod_should_invoke_update_of_UserDao_with_acronymFieldCenter() throws Exception {
    Whitebox.setInternalState(fieldCenterDTO, ACRONYM_ATTRIBUITE, FIELD_CENTER_ACRONYM);
    managementUserServiceBean.updateFieldCenter(managementUserDto);
    verify(userDao, times(1)).update(user);
    verify(user, times(1)).setFieldCenter(fieldCenter);
    verify(user, times(0)).setFieldCenter(null);
  }

  @Test
  public void updateFieldCenterMethod_should_invoke_update_of_UserDao_without_acronymFieldCenter() throws Exception {
    Whitebox.setInternalState(fieldCenterDTO, ACRONYM_ATTRIBUITE, FIELD_CENTER_WITHOUT_ACRONYM);
    managementUserServiceBean.updateFieldCenter(managementUserDto);
    verify(userDao, times(1)).update(user);
    verify(user, times(0)).setFieldCenter(fieldCenter);
    verify(user, times(1)).setFieldCenter(null);
  }

  @Test
  public void updateUserPassword_Method_invoke_internal_methods() throws EncryptedException {
    when(passwordResetDto.getEmail()).thenReturn(EMAIL);
    when(passwordResetDto.getPassword()).thenReturn(PASSWORD);
    managementUserServiceBean.updateUserPassword(passwordResetDto);
    verify(passwordResetDto, times(1)).encrypt();
    verify(userDao, times(1)).updatePassword(EMAIL, PASSWORD);
    verify(securityFacade, times(1)).removePasswordResetRequests(EMAIL);
  }

  @Test
  public void isUnique_Method_should_return_false() {
    when(userDao.emailExists(EMAIL)).thenReturn(true);
    assertFalse(managementUserServiceBean.isUnique(EMAIL));
  }

  @Test
  public void isUnique_Method_with_nonUniqueEmail_should_return_true() {
    when(userDao.emailExists(EMAIL)).thenReturn(false);
    assertTrue(managementUserServiceBean.isUnique(EMAIL));
  }

}
