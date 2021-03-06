package br.org.otus.user.pendency;

import br.org.otus.model.pendency.UserActivityPendency;
import br.org.otus.model.pendency.UserActivityPendencyResponse;
import br.org.otus.security.AuthorizationHeaderReader;
import br.org.otus.security.context.SecurityContext;
import br.org.otus.security.context.SessionIdentifier;
import br.org.otus.security.dtos.AuthenticationData;
import br.org.otus.user.api.pendency.UserActivityPendencyFacade;
import br.org.otus.ResourceTestsParent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthorizationHeaderReader.class})
public class UserActivityPendencyResourceTest extends ResourceTestsParent {

  private static final String USER_EMAIL = "user@otus.com";
  private static final String TOKEN = "123456";
  private static final String PENDENCY_ID = "5e0658135b4ff40f8916d2b5";
  private static final String ACTIVITY_ID = "5a33cb4a28f10d1043710f7d";

  private static final String NEW_PENDENCY_DATA = "";
  private static final String EXPECTED_CREATE_RESPONSE = "12355b8e415e9c6746ca2da1";
  private static final String SEARCH_SETTINGS_JSON = "";

  @InjectMocks
  private UserActivityPendencyResource userActivityPendencyResource;
  @Mock
  private UserActivityPendencyFacade userActivityPendencyFacade;
  @Mock
  private HttpServletRequest request;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private SessionIdentifier session;
  @Mock
  private AuthenticationData authenticationData;

  private UserActivityPendency userActivityPendency;
  private UserActivityPendencyResponse userActivityPendencyResponse;
  private List<UserActivityPendencyResponse> userActivityPendencyResponses;
  private String userActivityPendencyJson;

  @Before
  public void setUp() {
    userActivityPendency = new UserActivityPendency();
    userActivityPendencyResponse = new UserActivityPendencyResponse();
    userActivityPendencyResponses = asList(userActivityPendencyResponse);
    userActivityPendencyJson = UserActivityPendency.serialize(userActivityPendency);
  }

  @Test
  public void create_method_should_be_create_pendency_by_userActivityPendencyFacade() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.create(USER_EMAIL, NEW_PENDENCY_DATA)).thenReturn(EXPECTED_CREATE_RESPONSE);
    assertEquals(
      encapsulateExpectedResponse("\"" + EXPECTED_CREATE_RESPONSE + "\""),
      userActivityPendencyResource.create(request, NEW_PENDENCY_DATA));
    verify(userActivityPendencyFacade, Mockito.times(1)).create(USER_EMAIL, NEW_PENDENCY_DATA);
  }

  @Test
  public void update_method_should_be_update_pendency_by_userActivityPendencyFacade() {
    assertEquals(EMPTY_RESPONSE, userActivityPendencyResource.update(PENDENCY_ID, userActivityPendencyJson));
    verify(userActivityPendencyFacade, Mockito.times(1)).update(PENDENCY_ID, userActivityPendencyJson);
  }

  @Test
  public void delete_method_should_be_delete_pendency_by_userActivityPendencyFacade() {
    assertEquals(EMPTY_RESPONSE, userActivityPendencyResource.delete(PENDENCY_ID));
    verify(userActivityPendencyFacade, Mockito.times(1)).delete(PENDENCY_ID);
  }

  @Test
  public void getByActivityId_method_should_return_pendency_by_activityInfo() {
    when(userActivityPendencyFacade.getByActivityId(ACTIVITY_ID)).thenReturn(userActivityPendency);
    assertEquals(
      encapsulateExpectedResponse(userActivityPendencyJson),
      userActivityPendencyResource.getByActivityId(ACTIVITY_ID));
  }

  /*
   * list to Receiver
   */
  @Test
  public void listAllPendencies_method_should_return_all_pendencies(){
    when(userActivityPendencyFacade.listAllPendencies(SEARCH_SETTINGS_JSON)).thenReturn(userActivityPendencyResponses);
    assertEquals(encapsulateExpectedResponseList(), userActivityPendencyResource.listAllPendencies(SEARCH_SETTINGS_JSON));
  }

  /*
   * list to Receiver
   */
  @Test
  public void listAllPendenciesToReceiver_method_should_return_all_pendencies_to_receiver() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listAllPendenciesToReceiver(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listAllPendenciesToReceiver(request));
  }

  @Test
  public void listOpenedPendenciesToReceiver_method_should_return_only_opened_pendencies_to_receiver() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listOpenedPendenciesToReceiver(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listOpenedPendenciesToReceiver(request));
  }

  @Test
  public void listDonePendenciesToReceiver_method_should_return_only_done_pendencies_to_receiver() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listDonePendenciesToReceiver(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listDonePendenciesToReceiver(request));
  }

  /*
   * list from Requester
   */
  @Test
  public void listAllPendenciesFromRequester_method_should_return_all_pendencies_from_requester() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listAllPendenciesFromRequester(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listAllPendenciesFromRequester(request));
  }

  @Test
  public void listOpenedPendenciesFromRequester_method_should_return_only_opened_pendencies_from_requester() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listOpenedPendenciesFromRequester(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listOpenedPendenciesFromRequester(request));
  }

  @Test
  public void listDonePendenciesFromRequester_method_should_return_only_done_pendencies_from_requester() {
    mockContextToSetUserEmail();
    when(userActivityPendencyFacade.listDonePendenciesFromRequester(USER_EMAIL)).thenReturn(userActivityPendencyResponses);
    assertEquals(
      encapsulateExpectedResponseList(),
      userActivityPendencyResource.listDonePendenciesFromRequester(request));
  }

  /*
   * Private Methods
   */

  private String encapsulateExpectedResponseList() {
    return encapsulateExpectedResponse("[" + String.join(",", userActivityPendencyJson) + "]");
  }

  private void mockContextToSetUserEmail() {
    mockStatic(AuthorizationHeaderReader.class);
    when(request.getHeader(Mockito.any())).thenReturn(TOKEN);
    when(AuthorizationHeaderReader.readToken(Mockito.any())).thenReturn(TOKEN);
    when(securityContext.getSession(TOKEN)).thenReturn(session);
    when(session.getAuthenticationData()).thenReturn(authenticationData);
    when(authenticationData.getUserEmail()).thenReturn(USER_EMAIL);
  }

}
