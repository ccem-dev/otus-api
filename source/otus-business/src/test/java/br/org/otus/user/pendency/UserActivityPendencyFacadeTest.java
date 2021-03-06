package br.org.otus.user.pendency;

import br.org.otus.model.pendency.UserActivityPendency;
import br.org.otus.model.pendency.UserActivityPendencyResponse;
import br.org.otus.response.exception.HttpResponseException;
import br.org.otus.service.pendency.UserActivityPendencyService;
import br.org.otus.user.api.pendency.UserActivityPendencyFacade;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.zip.DataFormatException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class UserActivityPendencyFacadeTest {

  private static final String USER_EMAIL = "user@otus.com";
  private static final String PENDENCY_ID = "5e0658135b4ff40f8916d2b5";
  private static final String ACTIVITY_ID = "5a33cb4a28f10d1043710f7d";
  private static final String SEARCH_SETTINGS_JSON = "";

  @InjectMocks
  private UserActivityPendencyFacade userActivityPendencyFacade;
  @Mock
  private UserActivityPendencyService userActivityPendencyService;

  private UserActivityPendency userActivityPendency;
  private UserActivityPendencyResponse userActivityPendencyResponse;
  private List<UserActivityPendencyResponse> userActivityPendencyResponses;
  private String userActivityPendencyJson;
  private ObjectId userActivityPendencyOID;
  private ValidationException validationException;
  private DataNotFoundException dataNotFoundException;
  private MemoryExcededException memoryExcededException;

  @Before
  public void setUp() throws Exception {
    userActivityPendency = new UserActivityPendency();
    userActivityPendencyResponse = new UserActivityPendencyResponse();
    userActivityPendencyResponses = asList(userActivityPendencyResponse);
    userActivityPendencyJson = UserActivityPendency.serialize(userActivityPendency);
    userActivityPendencyOID = new ObjectId(PENDENCY_ID);
    validationException = PowerMockito.spy(new ValidationException());
    dataNotFoundException = PowerMockito.spy(new DataNotFoundException());
    memoryExcededException = PowerMockito.spy(new MemoryExcededException(""));
  }

  @Test
  public void createMethod_should_invoke_create_from_userActivityPendencyService() {
    when(userActivityPendencyService.create(USER_EMAIL, userActivityPendency)).thenReturn(userActivityPendencyOID);
    assertEquals(PENDENCY_ID, userActivityPendencyFacade.create(USER_EMAIL, userActivityPendencyJson));
  }

  @Test
  public void updateMethod_should_invoke_update_from_userActivityPendencyService() throws DataNotFoundException {
    userActivityPendencyFacade.update(PENDENCY_ID, userActivityPendencyJson);
    verify(userActivityPendencyService, times(1)).update(PENDENCY_ID, userActivityPendency);
  }

  @Test(expected = HttpResponseException.class)
  public void updateMethod_should_handle_DataNotFoundException() throws Exception {
    PowerMockito.doThrow(dataNotFoundException).when(userActivityPendencyService, "update", PENDENCY_ID, userActivityPendency);
    userActivityPendencyFacade.update(PENDENCY_ID, userActivityPendencyJson);
  }

  @Test
  public void deleteMethod_should_invoke_delete_from_userActivityPendencyService() throws DataNotFoundException {
    userActivityPendencyFacade.delete(PENDENCY_ID);
    verify(userActivityPendencyService, times(1)).delete(PENDENCY_ID);
  }

  @Test(expected = HttpResponseException.class)
  public void deleteMethod_should_handle_DataNotFoundException() throws Exception {
    PowerMockito.doThrow(dataNotFoundException).when(userActivityPendencyService, "delete", PENDENCY_ID);
    userActivityPendencyFacade.delete(PENDENCY_ID);
  }

  @Test
  public void getByActivityIdMethod_should_invoke_getByActivityId_from_userActivityPendencyService() throws DataNotFoundException {
    userActivityPendencyFacade.getByActivityId(ACTIVITY_ID);
    verify(userActivityPendencyService, times(1)).getByActivityId(ACTIVITY_ID);
  }

  @Test(expected = HttpResponseException.class)
  public void getByActivityIdMethod_should_handle_DataNotFoundException_for_invalid_id() throws DataNotFoundException {
    when(userActivityPendencyService.getByActivityId("")).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.getByActivityId("");
  }

  /*
   * list all
   */
  @Test
  public void listAllPendencies_method_should_invoke_listAllPendencies_from_userActivityPendencyService() throws DataFormatException, DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listAllPendencies(SEARCH_SETTINGS_JSON);
    verify(userActivityPendencyService, times(1)).listAllPendencies(SEARCH_SETTINGS_JSON);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendencies_should_handle_DataNotFoundException() throws DataFormatException, DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendencies(SEARCH_SETTINGS_JSON)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listAllPendencies(SEARCH_SETTINGS_JSON);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendencies_should_handle_MemoryExcededException() throws DataFormatException, DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendencies(SEARCH_SETTINGS_JSON)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listAllPendencies(SEARCH_SETTINGS_JSON);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendencies_should_handle_DataFormatException() throws DataFormatException, DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendencies(SEARCH_SETTINGS_JSON)).thenThrow(new DataFormatException());
    userActivityPendencyFacade.listAllPendencies(SEARCH_SETTINGS_JSON);
  }

  /*
   * list all to Receiver
   */
  @Test
  public void listAllPendenciesToReceiverMethod_should_invoke_listAllPendenciesToReceiver_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listAllPendenciesToReceiver(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listAllPendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendenciesToReceiverMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendenciesToReceiver(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listAllPendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendenciesToReceiverMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendenciesToReceiver(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listAllPendenciesToReceiver(USER_EMAIL);
  }

  @Test
  public void listOpenedPendenciesToReceiverMethod_should_invoke_listOpenedPendencies_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listOpenedPendenciesToReceiver(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listOpenedPendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listOpenedPendenciesToReceiverMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listOpenedPendenciesToReceiver(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listOpenedPendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listOpenedPendenciesToReceiverMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listOpenedPendenciesToReceiver(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listOpenedPendenciesToReceiver(USER_EMAIL);
  }

  @Test
  public void listDonePendenciesToReceiverMethod_should_invoke_listDonePendencies_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listDonePendenciesToReceiver(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listDonePendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listDonePendenciesToReceiverMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listDonePendenciesToReceiver(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listDonePendenciesToReceiver(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listDonePendenciesToReceiverMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listDonePendenciesToReceiver(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listDonePendenciesToReceiver(USER_EMAIL);
  }

  /*
   * list all from Requester
   */
  @Test
  public void listAllPendenciesFromRequesterMethod_should_invoke_listAllPendencies_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listAllPendenciesFromRequester(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listAllPendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendenciesFromRequesterMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendenciesFromRequester(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listAllPendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listAllPendenciesFromRequesterMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listAllPendenciesFromRequester(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listAllPendenciesFromRequester(USER_EMAIL);
  }

  @Test
  public void listOpenedPendenciesFromRequesterMethod_should_invoke_listOpenedPendencies_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listOpenedPendenciesFromRequester(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listOpenedPendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listOpenedPendenciesFromRequesterMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listOpenedPendenciesFromRequester(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listOpenedPendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listOpenedPendenciesFromRequesterMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listOpenedPendenciesFromRequester(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listOpenedPendenciesFromRequester(USER_EMAIL);
  }

  @Test
  public void listDonePendenciesFromRequesterMethod_should_invoke_listDonePendencies_from_userActivityPendencyService() throws DataNotFoundException, MemoryExcededException {
    userActivityPendencyFacade.listDonePendenciesFromRequester(USER_EMAIL);
    verify(userActivityPendencyService, times(1)).listDonePendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listDonePendenciesFromRequesterMethod_should_handle_DataNotFoundException_in_case_of_no_pendency() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listDonePendenciesFromRequester(USER_EMAIL)).thenThrow(dataNotFoundException);
    userActivityPendencyFacade.listDonePendenciesFromRequester(USER_EMAIL);
  }

  @Test(expected = HttpResponseException.class)
  public void listDonePendenciesFromRequesterMethod_should_handle_MemoryExcededException_in_case_of_many_pendencies() throws DataNotFoundException, MemoryExcededException {
    when(userActivityPendencyService.listDonePendenciesFromRequester(USER_EMAIL)).thenThrow(memoryExcededException);
    userActivityPendencyFacade.listDonePendenciesFromRequester(USER_EMAIL);
  }

}
