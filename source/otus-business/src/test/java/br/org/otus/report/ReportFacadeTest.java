package br.org.otus.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.org.otus.gateway.gates.OutcomeGatewayService;
import br.org.otus.gateway.gates.ReportGatewayService;
import br.org.otus.gateway.response.GatewayResponse;
import br.org.otus.gateway.response.exception.RequestException;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.ActivityReportTemplate;
import org.ccem.otus.model.ReportTemplate;
import org.ccem.otus.model.dataSources.ReportDataSource;
import org.ccem.otus.model.dataSources.participant.ParticipantDataSource;
import org.ccem.otus.persistence.ReportTemplateDTO;
import org.ccem.otus.service.ReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReportFacadeTest.class, ReportTemplate.class})
public class ReportFacadeTest {
  private static final String USER_MAIL = "otus@otus.com";
  private static final String REPORT_ID = "5a9199056ddc4f48a340b3ec";
  private static final String RESULT = "{\"data\":true}";
  private static final String reportUploadJson = "{\"template\" : \"<span></span>\",\"label\": \"tiago\",\"fieldCenter\": [],\"dataSources\" : [{\"key\" : \"HS\",\"label\": \"tester\", \"dataSource\" : \"Participant\",\"filters\" : {\"statusHistory\" : {\"name\" : \"FINALIZED\",\"position\" : -1},\"acronym\" : \"TF\",\"category\" : \"C0\"}}]}";
  private ReportTemplate report = PowerMockito.spy(new ReportTemplate());
  Long RECRUITMENTNUMBER = 322148795L;
  private ReportTemplate reportTemplate = new ReportTemplate();

  @InjectMocks
  private ReportFacade reportFacade;
  @Mock
  private ReportService reportService;
  @Mock
  private ReportServiceBean reportServiceBean;
  @Mock
  private String userEmail;


  @Mock
 private GatewayResponse gatewayResponse;

  @Mock
  private RequestException requestException;

  @Mock
  private ReportGatewayService reportGatewayService;

  private ActivityReportTemplate activityReportTemplate;

  private ObjectId objectId = new ObjectId("5a9199056ddc4f48a340b3ec");

  @Test
  public void method_getByReportId_should_return_ReportTemplate() throws DataNotFoundException, ValidationException, MalformedURLException {
    ParticipantDataSource participantDataSource = new ParticipantDataSource();
    Whitebox.setInternalState(participantDataSource, "dataSource", "Participant");
    Whitebox.setInternalState(participantDataSource, "result", new ArrayList<>());
    ReportTemplate reportTemplate = new ReportTemplate();
    String template = "<span>teste</span>";
    Whitebox.setInternalState(reportTemplate, "template", template);
    Whitebox.setInternalState(reportTemplate, "dataSources", new ArrayList<>());
    reportTemplate.getDataSources().add(participantDataSource);
    when(reportService.getParticipantReport(RECRUITMENTNUMBER, REPORT_ID)).thenReturn(reportTemplate);
    assertTrue(reportFacade.getParticipantReport(RECRUITMENTNUMBER, REPORT_ID) instanceof ReportTemplate);
  }

  @Test(expected = HttpResponseException.class)
  public void method_getByReportId_should_throw_HttpResponseException() throws HttpResponseException, DataNotFoundException, ValidationException, MalformedURLException {
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).getParticipantReport(RECRUITMENTNUMBER, REPORT_ID);
    reportFacade.getParticipantReport(RECRUITMENTNUMBER, REPORT_ID);
  }

  @Test
  public void method_getReportByParticipant_should_returns_list_reports() throws DataNotFoundException, ValidationException {
    ReportTemplate reportTemplate = new ReportTemplate();
    ArrayList<ReportTemplateDTO> reports = new ArrayList<>();

    Whitebox.setInternalState(reportTemplate, "_id", objectId);
    Whitebox.setInternalState(reportTemplate, "label", "teste");
    reports.add(new ReportTemplateDTO(reportTemplate));

    PowerMockito.when(reportService.getReportByParticipant(RECRUITMENTNUMBER)).thenReturn(reports);

    assertEquals(reports, reportFacade.getReportByParticipant(RECRUITMENTNUMBER));

  }

  @Test(expected = HttpResponseException.class)
  public void method_getReportByParticipant_should_throw_HttpResponseException() throws HttpResponseException, DataNotFoundException, ValidationException {
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).getReportByParticipant(RECRUITMENTNUMBER);
    reportFacade.getReportByParticipant(RECRUITMENTNUMBER);
  }

  @Test
  public void metho_create_should_insert_new_report() throws Exception {
    ReportTemplate reportTemplate = new ReportTemplate();
    Whitebox.setInternalState(reportTemplate, "_id", objectId);
    String report = ReportTemplate.serialize(reportTemplate);
    mockStatic(ReportTemplate.class);
    PowerMockito.when(ReportTemplate.class, "deserialize", Mockito.any()).thenReturn(reportTemplate);
    PowerMockito.when(ReportTemplate.class, "serialize", Mockito.any()).thenReturn(report);
    PowerMockito.when(reportService.create(Mockito.anyObject())).thenReturn(reportTemplate);
    assertEquals(reportTemplate, reportFacade.create(report, USER_MAIL));
  }

  @Test(expected = HttpResponseException.class)
  public void method_create_should_throw_ValidationException() throws ValidationException, Exception {
    mockStatic(ReportTemplate.class);
    reportTemplate.setSender(USER_MAIL);
    PowerMockito.when(ReportTemplate.class, "deserialize", Mockito.any()).thenReturn(reportTemplate);
    doThrow(new HttpResponseException(ResponseBuild.Security.Validation.build(new ValidationException(new Throwable("method_RegisterProject_should_captured_Exception")).getCause().getMessage())))
      .when(reportService).create(Mockito.anyObject());
    reportFacade.create(reportUploadJson, Mockito.anyString());
  }

  @Test
  public void method_delete_should_remove_report() throws Exception {
    PowerMockito.doNothing().when(reportService, "delete", REPORT_ID);
    reportFacade.deleteById(REPORT_ID);
  }

  @Test(expected = HttpResponseException.class)
  public void method_delete_should_throw_DataNotFoundException() throws Exception {
    PowerMockito.doNothing().when(reportService, "delete", REPORT_ID);
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).delete(Mockito.anyString());
    reportFacade.deleteById(REPORT_ID);
  }

  @Test
  public void method_list_should_returns_list_reports() throws ValidationException {
    ReportTemplate reportTemplate = new ReportTemplate();
    ArrayList<String> fieldCenter = new ArrayList<>();
    ArrayList<ReportDataSource> dataSources = new ArrayList<>();
    fieldCenter.add("SP");
    dataSources = reportTemplate.getDataSources();
    List<ReportTemplate> reports = new ArrayList<>();
    Whitebox.setInternalState(reportTemplate, "_id", objectId);
    Whitebox.setInternalState(reportTemplate, "label", "RELATORIO");
    Whitebox.setInternalState(reportTemplate, "template", "<span></span>");
    Whitebox.setInternalState(reportTemplate, "fieldCenter", fieldCenter);
    Whitebox.setInternalState(reportTemplate, "dataSources", dataSources);
    reports.add(reportTemplate);
    PowerMockito.when(reportService.list()).thenReturn(reports);
    assertEquals(reports, reportFacade.list());

  }

  @Test
  public void method_getById_should_return_report() throws DataNotFoundException, ValidationException {
    ReportTemplate reportTemplate = new ReportTemplate();
    ArrayList<String> fieldCenter = new ArrayList<>();
    ArrayList<ReportDataSource> dataSources = new ArrayList<>();
    fieldCenter.add("SP");
    dataSources = reportTemplate.getDataSources();
    List<ReportTemplate> reports = new ArrayList<>();
    Whitebox.setInternalState(reportTemplate, "_id", objectId);
    Whitebox.setInternalState(reportTemplate, "label", "teste");
    Whitebox.setInternalState(reportTemplate, "template", "<span></span>");
    Whitebox.setInternalState(reportTemplate, "fieldCenter", fieldCenter);
    Whitebox.setInternalState(reportTemplate, "dataSources", dataSources);
    reports.add(reportTemplate);
    PowerMockito.when(reportService.getByID(REPORT_ID)).thenReturn(reportTemplate);
    assertEquals(reportTemplate, reportFacade.getById(REPORT_ID));

  }

  @Test(expected = HttpResponseException.class)
  public void method_getById_should_throw_DataNotFoundException() throws Exception {
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).getByID(Mockito.anyString());
    reportFacade.getById(REPORT_ID);
  }

  @Test
  public void method_update_should_alter_report() throws Exception {
    ReportTemplate reportTemplate = new ReportTemplate();
    ReportTemplate updateReport = new ReportTemplate();
    ArrayList<String> fieldCenter = new ArrayList<>();
    ArrayList<ReportDataSource> dataSources = new ArrayList<>();
    fieldCenter.add("SP");
    dataSources = report.getDataSources();

    Whitebox.setInternalState(report, "_id", objectId);
    Whitebox.setInternalState(report, "label", "teste");
    Whitebox.setInternalState(report, "template", "<span></span>");
    Whitebox.setInternalState(report, "fieldCenter", fieldCenter);
    Whitebox.setInternalState(report, "dataSources", dataSources);
    Whitebox.setInternalState(updateReport, "_id", objectId);
    Whitebox.setInternalState(updateReport, "label", "Novo Template");
    Whitebox.setInternalState(updateReport, "template", "<h1></h1>");
    Whitebox.setInternalState(updateReport, "fieldCenter", fieldCenter);
    Whitebox.setInternalState(updateReport, "dataSources", dataSources);
    mockStatic(ReportTemplate.class);
    PowerMockito.when(ReportTemplate.class, "deserialize", Mockito.any()).thenReturn(updateReport);
    PowerMockito.when(ReportTemplate.class, "serialize", Mockito.any()).thenReturn(reportUploadJson);
    PowerMockito.when(reportService.updateFieldCenters(Mockito.anyObject())).thenReturn(updateReport);
    assertEquals(updateReport, reportFacade.updateFieldCenters(reportUploadJson));
  }

  @Test(expected = HttpResponseException.class)
  public void method_update_should_throw_DataNotFoundException() throws HttpResponseException, DataNotFoundException, ValidationException {
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).updateFieldCenters(Mockito.anyObject());
    reportFacade.updateFieldCenters(reportUploadJson);
  }

  @Test(expected = HttpResponseException.class)
  public void method_update_should_throw_ValidationException() throws HttpResponseException, DataNotFoundException, ValidationException {
    doThrow(new ValidationException(new Exception("method_RegisterProject_should_captured_ValidationException"))).when(reportService).updateFieldCenters(Mockito.anyObject());
    reportFacade.updateFieldCenters(reportUploadJson);
  }

  @Test
  public void method_getActivityReport_should_returns_report() throws DataNotFoundException, ValidationException, MalformedURLException {
    activityReportTemplate = new ActivityReportTemplate();
    ActivityReportTemplate updateReport = new ActivityReportTemplate();
    ArrayList<ReportDataSource> dataSources = new ArrayList<>();

    Whitebox.setInternalState(activityReportTemplate, "_id", objectId);
    Whitebox.setInternalState(activityReportTemplate, "label", "teste");
    Whitebox.setInternalState(activityReportTemplate, "template", "<span></span>");
    Whitebox.setInternalState(activityReportTemplate, "dataSources", dataSources);
    Whitebox.setInternalState(updateReport, "_id", objectId);
    Whitebox.setInternalState(updateReport, "label", "teste");
    Whitebox.setInternalState(updateReport, "template", "<span></span>");
    Whitebox.setInternalState(updateReport, "dataSources", dataSources);

    PowerMockito.when(reportService.getActivityReport(REPORT_ID)).thenReturn(activityReportTemplate);

    assertEquals(ActivityReportTemplate.serialize(updateReport), ActivityReportTemplate.serialize(reportFacade.getActivityReport(REPORT_ID)));
  }

  @Test(expected = HttpResponseException.class)
  public void method_getActivityReport_should_throw_HttpResponseException() throws HttpResponseException, DataNotFoundException, ValidationException, MalformedURLException {
    doThrow(new DataNotFoundException(new Exception("method_RegisterProject_should_captured_DataNotFoundException"))).when(reportService).getActivityReport(REPORT_ID);
    reportFacade.getActivityReport(REPORT_ID);
  }

  @Test(expected = HttpResponseException.class)
  public void method_getActivityReport_should_throw_HttpResponseException_ValidationException() throws HttpResponseException, DataNotFoundException, ValidationException, MalformedURLException {
    doThrow(new ValidationException(new Exception("method_RegisterProject_should_captured_ValidationException"))).when(reportService).getActivityReport(REPORT_ID);
    reportFacade.getActivityReport(REPORT_ID);
  }

  @Test
  public void createMethod_should_insert_new_activity_report() throws Exception {
    activityReportTemplate = new ActivityReportTemplate();
    Whitebox.setInternalState(activityReportTemplate, "_id", objectId);
    String report = ActivityReportTemplate.serialize(activityReportTemplate);

    PowerMockito.when(reportService.createActivityReport(Mockito.anyObject())).thenReturn(activityReportTemplate);

    assertEquals(activityReportTemplate, reportFacade.createActivityReport(report, USER_MAIL));
  }

  @Test
  public void getActivityReportListMethod_should_return_activity_report() throws Exception {
    activityReportTemplate = new ActivityReportTemplate();
    List<ActivityReportTemplate> activityReportTemplates = new ArrayList<>();
    activityReportTemplates.add(activityReportTemplate);

    PowerMockito.when(reportService.getActivityReportList(Mockito.anyObject())).thenReturn(activityReportTemplates);

    assertEquals(activityReportTemplates, reportFacade.getActivityReportList(anyString()));
  }

  @Test(expected = HttpResponseException.class)
  public void getActivityReportListMethod_should_return_throw_DataNotFoundException()
    throws DataNotFoundException {
    when(reportService.getActivityReportList(anyString())).thenThrow(
      new DataNotFoundException(new Throwable("method_RegisterProject_should_captured_Exception")));
    reportFacade.getActivityReportList(anyString());
  }

  @Test
  public void updateActivityReportMethod_should_update_activity_report() throws Exception {
    reportFacade.updateActivityReport(anyString(), anyString());

    Mockito.verify(reportService, Mockito.times(1)).updateActivityReport(anyString(), anyString());
  }

  @Test(expected = HttpResponseException.class)
  public void updateActivityReportMethod_should_return_throw_DataNotFoundException()
    throws DataNotFoundException {
    doThrow(new DataNotFoundException(new Exception())).when(reportService).updateActivityReport(anyString(), anyString());

    reportFacade.updateActivityReport(anyString(), anyString());
  }

}
