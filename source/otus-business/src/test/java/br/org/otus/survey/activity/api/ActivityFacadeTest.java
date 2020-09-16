package br.org.otus.survey.activity.api;

import br.org.otus.model.User;
import br.org.otus.outcomes.FollowUpFacade;
import br.org.otus.response.exception.HttpResponseException;
import br.org.otus.response.exception.ResponseInfo;
import br.org.otus.user.management.ManagementUserService;
import com.google.gson.Gson;
import com.nimbusds.jwt.SignedJWT;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.mode.ActivityMode;
import org.ccem.otus.service.ActivityService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ActivityFacade.class, SignedJWT.class})
public class ActivityFacadeTest {
  private static final long RECRUITMENT_NUMBER = 5112345;
  private static final String ACRONYM = "CISE";
  private static final ObjectId OID = new ObjectId();
  private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJvdHVzQGdtYWlsLmNvbSIsIm1vZGUiOiJ1c2VyIiwianRpIjoiYzc1ODIzNWMtYjQzMy00NDQ2LWFhMDMtYmU0NmI3ODU3NWEyIiwiaWF0IjoxNTg1MTc2NDg5LCJleHAiOjE1ODUxODAwODl9.wlSmhUXYW6Apqg5skGPLDGCyuA0sDYyVtZIBM8RxkLs";
  private static final String TOKEN_BEARER = "Bearer " + TOKEN;
  private static final String SURVEY_ACTIVITY_EXCEPTION = "notExist";
  private static final String JSON = "" + "{\"objectType\" : \"Activity\"," + "\"extents\" : \"StudioObject\"}";
  private static final Integer VERSION = 1;
  private static final String USER_EMAIL = "otus@gmail.com";
  private static final String checkerUpdated = "{\"id\":\"5c0e5d41e69a69006430cb75\",\"activityStatus\":{\"objectType\":\"ActivityStatus\",\"name\":\"INITIALIZED_OFFLINE\",\"date\":\"2018-12-10T12:33:29.007Z\",\"user\":{\"name\":\"Otus\",\"surname\":\"Solutions\",\"extraction\":true,\"extractionIps\":[\"999.99.999.99\"],\"phone\":\"21987654321\",\"fieldCenter\":{},\"email\":\"otus@gmail.com\",\"admin\":false,\"enable\":true,\"meta\":{\"revision\":0,\"created\":0,\"version\":0},\"$loki\":2}}}";
  private static final String CENTER = "RS";
  private static final boolean NOTIFY = false;

  @InjectMocks
  ActivityFacade activityFacade;
  @Mock
  private ActivityService activityService;
  @Mock
  private FollowUpFacade followUpFacade;
  @Mock
  private SurveyActivity surveyActivity;
  @Mock
  private ManagementUserService managementUserService;
  private SurveyActivity surveyActivityFull;
  private SurveyActivity autofillSurveyActivity;


  @Before
  public void setUp() {
    surveyActivityFull = new Gson().fromJson(JSON, SurveyActivity.class);
    autofillSurveyActivity = SurveyActivity.deserialize("{\"objectType\":\"Activity\",\"_id\":\"5f5f79927a37f42cb997efe4\",\"surveyForm\":{\"_id\":null,\"sender\":\"diogo.rosas.ferreira@gmail.com\",\"sendingDate\":\"2017-05-16T17:21:35.932Z\",\"objectType\":\"SurveyForm\",\"surveyFormType\":\"FORM_INTERVIEW\",\"surveyTemplate\":null,\"name\":\"TERMO DE CONSENTIMENTO LIVRE E ESCLARECIDO\",\"acronym\":\"TCLEC\",\"version\":1,\"isDiscarded\":false,\"requiredExternalID\":false},\"mode\":\"AUTOFILL\",\"category\":{\"name\":\"C0\",\"objectType\":\"ActivityCategory\",\"label\":\"Normal\",\"disabled\":false,\"isDefault\":true},\"participantData\":{\"_id\":\"5ea343bdb174c405c9bba6cc\",\"recruitmentNumber\":555555,\"name\":\"Fulano Detal Bezerra Pereira Menezes Rodrigues Gomes\",\"sex\":\"M\",\"birthdate\":{\"objectType\":\"ImmutableDate\",\"value\":\"1949-04-22 00:00:00.000\"},\"fieldCenter\":{\"_id\":\"587d366a7b65e477dc410ab9\",\"name\":\"Rio Grande do Sul\",\"code\":5,\"acronym\":\"RS\",\"country\":null,\"state\":null,\"address\":null,\"complement\":null,\"zip\":null,\"phone\":null,\"backgroundColor\":\"rgba(75, 192, 192, 0.2)\",\"borderColor\":\"rgba(75, 192, 192, 1)\",\"locationPoint\":null},\"late\":false,\"email\":\"fdrtec@gmail.com\",\"password\":null,\"tokenList\":null,\"registeredBy\":null,\"identified\":true},\"interviews\":[{\"objectType\":\"Interview\",\"date\":\"2020-09-11T13:34:03.801Z\",\"interviewer\":{\"objectType\":\"Interviewer\",\"name\":null,\"email\":null}}],\"fillContainer\":{\"fillingList\":[{\"objectType\":\"QuestionFill\",\"questionID\":\"TCLEC1\",\"customID\":null,\"answer\":{\"value\":\"2\",\"objectType\":\"AnswerFill\",\"type\":\"SingleSelectionQuestion\"},\"forceAnswer\":false,\"metadata\":{\"objectType\":\"MetadataFill\",\"value\":null},\"comment\":\"\"},{\"objectType\":\"QuestionFill\",\"questionID\":\"TCLEC2\",\"customID\":null,\"answer\":{\"value\":\"2\",\"objectType\":\"AnswerFill\",\"type\":\"SingleSelectionQuestion\"},\"forceAnswer\":false,\"metadata\":{\"objectType\":\"MetadataFill\",\"value\":null},\"comment\":\"\"},{\"objectType\":\"QuestionFill\",\"questionID\":\"TCLEC3\",\"customID\":null,\"answer\":{\"value\":null,\"objectType\":\"AnswerFill\",\"type\":\"FileUploadQuestion\"},\"forceAnswer\":false,\"metadata\":{\"objectType\":\"MetadataFill\",\"value\":\"2\"},\"comment\":\"\"}]},\"statusHistory\":[{\"objectType\":\"ActivityStatus\",\"name\":\"CREATED\",\"date\":\"2020-09-11T13:26:21.980Z\",\"user\":{\"name\":\"Fabiano\",\"surname\":\"Dias Ramires\",\"phone\":\"51998577574\",\"email\":\"fdrtec@gmail.com\"}},{\"objectType\":\"ActivityStatus\",\"name\":\"INITIALIZED_ONLINE\",\"date\":\"2020-09-11T13:33:18.772Z\",\"user\":{\"name\":\"Fabiano\",\"surname\":\"Dias Ramires\",\"phone\":\"51998577574\",\"email\":\"fdrtec@gmail.com\"}},{\"objectType\":\"ActivityStatus\",\"name\":\"FINALIZED\",\"date\":\"2020-09-11T13:34:03.800Z\",\"user\":{\"name\":\"Fabiano\",\"surname\":\"Dias Ramires\",\"phone\":\"51998577574\",\"email\":\"fdrtec@gmail.com\"}}],\"isDiscarded\":false,\"navigationTracker\":{\"objectType\":\"NavigationTracker\",\"items\":[{\"objectType\":\"NavigationTrackingItem\",\"id\":\"TCLEC1\",\"state\":\"ANSWERED\",\"previous\":null,\"inputs\":[],\"outputs\":[\"TCLEC2\"]},{\"objectType\":\"NavigationTrackingItem\",\"id\":\"TCLEC2\",\"state\":\"ANSWERED\",\"previous\":\"TCLEC1\",\"inputs\":[\"TCLEC1\"],\"outputs\":[\"TCLEC3\"]},{\"objectType\":\"NavigationTrackingItem\",\"id\":\"TCLEC3\",\"state\":\"ANSWERED\",\"previous\":\"TCLEC2\",\"inputs\":[\"TCLEC2\"],\"outputs\":[\"TCLEC4\"]},{\"objectType\":\"NavigationTrackingItem\",\"id\":\"TCLEC4\",\"state\":\"VISITED\",\"previous\":\"TCLEC3\",\"inputs\":[\"TCLEC3\"],\"outputs\":[]}],\"lastVisitedIndex\":4},\"externalID\":null}");
  }

  @Test
  public void method_should_verify_list_with_rn() {
    Mockito.when(activityService.list(RECRUITMENT_NUMBER, USER_EMAIL)).thenReturn(new ArrayList<>());
    activityFacade.list(RECRUITMENT_NUMBER, USER_EMAIL);
    verify(activityService, times(1)).list(RECRUITMENT_NUMBER, USER_EMAIL);
  }

  @Test
  public void method_should_verify_get_with_id() throws DataNotFoundException {
    Mockito.when(activityService.getByID(ACRONYM)).thenReturn(surveyActivity);
    activityFacade.getByID(ACRONYM);
    verify(activityService, times(1)).getByID(ACRONYM);
  }

  @Test
  public void method_should_verify_get_with_id_and_version()
    throws DataNotFoundException, InterruptedException, MemoryExcededException {
    List<SurveyActivity> list = new ArrayList<SurveyActivity>();
    list.add(surveyActivity);
    list.add(surveyActivity);
    Mockito.when(activityService.get(ACRONYM, VERSION)).thenReturn(list);
    activityFacade.get(ACRONYM, VERSION);
    verify(activityService, times(1)).get(ACRONYM, VERSION);
  }

  @Test(expected = HttpResponseException.class)
  public void method_should_throw_HttpResponseException_getById_invalid() throws Exception {
    Mockito.when(activityService.getByID(SURVEY_ACTIVITY_EXCEPTION)).thenThrow(new HttpResponseException(null));
    activityFacade.getByID(SURVEY_ACTIVITY_EXCEPTION);
  }

  @Test
  public void method_should_verify_create_with_surveyActivity() {
    Mockito.when(activityService.create(surveyActivity)).thenReturn(OID.toString());

    activityFacade.create(surveyActivity, NOTIFY);
    verify(activityService, times(1)).create(surveyActivity);
  }

  @Test(expected = HttpResponseException.class)
  public void createMethodTest_should_trigger_requiredExternalID_validation() {
    when(surveyActivity.hasRequiredExternalID()).thenReturn(true);
    activityFacade.create(surveyActivity, NOTIFY);
  }

  @Test
  public void method_updateActivity_should_update_the_last_status_user_when_mode_is_user() throws Exception {
    br.org.otus.model.User user = new User();
    user.setEmail(USER_EMAIL);
    Mockito.when(managementUserService.fetchByEmail(USER_EMAIL)).thenReturn(user);

    SignedJWT signedJWT = spy(SignedJWT.parse(TOKEN));
    mockStatic(SignedJWT.class);
    when(SignedJWT.class, "parse", TOKEN).thenReturn(signedJWT);
    when(activityService, "update", autofillSurveyActivity).thenReturn(autofillSurveyActivity);

    activityFacade.updateActivity(autofillSurveyActivity, TOKEN_BEARER);
    String nameLastStatusHistory = autofillSurveyActivity.getLastStatus().get().getName();
    String activityId = String.valueOf(autofillSurveyActivity.getActivityID());

    verify(followUpFacade, times(1)).statusUpdateEvent(nameLastStatusHistory, activityId);
  }

  @Test(expected = HttpResponseException.class)
  public void method_should_throw_HttpResponseException_updateActivity_invalid() throws Exception {
    Mockito.when(activityService.update(surveyActivity)).thenThrow(new DataNotFoundException(new Throwable("Activity of Participant not found")));
    activityFacade.updateActivity(surveyActivity, TOKEN);
  }

  @Test
  public void updateCheckerActivityMethod_should_invoke_updateCheckerActivity_of_ActivityService() throws DataNotFoundException {
    activityFacade.updateCheckerActivity(checkerUpdated);
    verify(activityService, times(1)).updateCheckerActivity(checkerUpdated);
  }

  @Test(expected = HttpResponseException.class)
  public void updateCheckerActivityMethod_should_throw_HttpResponseException_when_ObjectId_invalid() throws Exception {
    Mockito.when(activityService.updateCheckerActivity(checkerUpdated)).thenThrow(new DataNotFoundException(new Throwable("Activity of Participant not found")));
    activityFacade.updateCheckerActivity(checkerUpdated);
  }

  @Test
  public void getActivityProgressExtraction_method_should_call_method_getActivityProgressExtraction_of_service() throws DataNotFoundException {
    activityFacade.getActivityProgressExtraction(CENTER);
    verify(activityService, times(1)).getActivityProgressExtraction(CENTER);
  }

  @Test
  public void getParticipantFieldCenterByActivity_method_should_call_method_getParticipantFieldCenterByActivity_of_service() throws DataNotFoundException {
    activityFacade.getParticipantFieldCenterByActivity(ACRONYM, VERSION);
    verify(activityService, times(1)).getParticipantFieldCenterByActivity(ACRONYM, VERSION);
  }

  @Test(expected = HttpResponseException.class)
  public void getParticipantFieldCenterByActivityMethod_should_throw_HttpResponseException_when_activity_invalid() throws Exception {
    Mockito.when(activityService.getParticipantFieldCenterByActivity(ACRONYM, VERSION)).thenThrow(new DataNotFoundException(new Throwable("Activity of Participant not found")));
    activityFacade.getParticipantFieldCenterByActivity(ACRONYM, VERSION);
  }

  @Test
  public void method_should_create_participant_event_when_surveyActivity_is_autofill() {
    Mockito.when(activityService.create(surveyActivity)).thenReturn(OID.toString());
    Mockito.when(surveyActivity.getMode()).thenReturn(ActivityMode.AUTOFILL);

    activityFacade.create(surveyActivity, NOTIFY);
    verify(followUpFacade, times(1)).createParticipantActivityAutoFillEvent(surveyActivity, NOTIFY);
  }

  @Test
  public void method_should_verify_create_for_follow_up_with_surveyActivity() {
    Mockito.when(activityService.create(surveyActivity)).thenReturn(ACRONYM);
    activityFacade.createFollowUp(surveyActivity);
    verify(activityService, times(1)).create(surveyActivity);
  }

  @Test(expected = HttpResponseException.class)
  public void createFollowUpMethodTest_should_trigger_requiredExternalID_validation() {
    when(surveyActivity.hasRequiredExternalID()).thenReturn(true);
    activityFacade.createFollowUp(surveyActivity);
  }
}
