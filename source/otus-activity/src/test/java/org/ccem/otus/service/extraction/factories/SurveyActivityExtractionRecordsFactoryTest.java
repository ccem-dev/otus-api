package org.ccem.otus.service.extraction.factories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.User;
import org.ccem.otus.model.survey.activity.configuration.ActivityCategory;
import org.ccem.otus.model.survey.activity.filling.FillContainer;
import org.ccem.otus.model.survey.activity.filling.QuestionFill;
import org.ccem.otus.model.survey.activity.interviewer.Interviewer;
import org.ccem.otus.model.survey.activity.status.ActivityStatus;
import org.ccem.otus.model.survey.activity.status.ActivityStatusOptions;
import org.ccem.otus.participant.model.Participant;
import org.ccem.otus.survey.form.SurveyForm;
import org.ccem.otus.survey.template.SurveyTemplate;
import org.ccem.otus.survey.template.identity.Identity;
import org.ccem.otus.survey.template.item.SurveyItem;
import org.ccem.otus.survey.template.item.questions.numeric.NumericQuestion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class SurveyActivityExtractionRecordsFactoryTest {
  private Long RECRUITMENT_NUMBER = (Long) 322148795L;
  private static final String ACRONYM = "ATCA";
  private static final String USER_NAME = "Fulano";
  private static final String FIELD_CENTER = "RS";
  private static final String LABEL = "Repetição";

  private SurveyActivityExtractionRecordsFactory records;
  private NumericQuestion numericQuestion;
  private Identity identity;
  private List<SurveyItem> itemContainer;
  private SurveyTemplate surveyTemplate;
  private SurveyForm surveyForm;
  private SurveyActivity surveyActivity;
  private SurveyActivityExtractionHeadersFactory headers;
  private ActivityCategory category;
  private User user;
  private List<Interviewer> interviews;

  @Before
  public void setup() {
    this.numericQuestion = new NumericQuestion();
    Whitebox.setInternalState(this.numericQuestion, "customID", "FORM1");
    Whitebox.setInternalState(this.numericQuestion, "templateID", "FORM1");
    this.identity = new Identity();
    Whitebox.setInternalState(this.identity, "acronym",ACRONYM);
    this.itemContainer = new ArrayList<>();
    this.itemContainer.add(this.numericQuestion);
    this.surveyTemplate = new SurveyTemplate();
    Whitebox.setInternalState(this.surveyTemplate, "itemContainer", this.itemContainer);
    Whitebox.setInternalState(this.surveyTemplate, "identity", identity);
    this.surveyForm = new SurveyForm(this.surveyTemplate, "test@test.com");
    this.headers = new SurveyActivityExtractionHeadersFactory(this.surveyForm);

    this.records = new SurveyActivityExtractionRecordsFactory(this.surveyForm, this.headers.getHeaders());

    category = new ActivityCategory(LABEL);
    interviews = new ArrayList<>();

    user = new User();
    Whitebox.setInternalState(user,"name",USER_NAME);
  }

  @Test
  public void getSurveyInformation_method_should_return_instance_of_LinkedHashMap() {
    LinkedHashMap<String, Object> values = this.records.getSurveyInformation();

    Assert.assertTrue(values instanceof LinkedHashMap);
  }

  @Test
  public void getSurveyInformation_method_should_return_values_expected() {
    Participant participant = new Participant(RECRUITMENT_NUMBER);

    ActivityStatusOptions activityStatusOptions = ActivityStatusOptions.FINALIZED;

    ActivityStatus activityStatus = new ActivityStatus();
    Whitebox.setInternalState(activityStatus, "objectType", "ActivityStatus");
    Whitebox.setInternalState(activityStatus, "name", activityStatusOptions);
    Whitebox.setInternalState(activityStatus, "date", LocalDateTime.now());
    Whitebox.setInternalState(activityStatus, "user", user);

    ArrayList<ActivityStatus> statusHistory = new ArrayList<>();
    statusHistory.add(activityStatus);

    FillContainer fillContainer = new FillContainer();
    List<QuestionFill> fillingList = new ArrayList<>();
    QuestionFill questionFill = new QuestionFill();
    Whitebox.setInternalState(questionFill, "questionID", "ATCA4");

    fillingList.add(questionFill);

    Whitebox.setInternalState(fillContainer, "fillingList", fillingList);

    this.surveyActivity = new SurveyActivity();
    Whitebox.setInternalState(this.surveyActivity, "surveyForm", surveyForm);
    Whitebox.setInternalState(this.surveyActivity, "category", category);
    Whitebox.setInternalState(this.surveyActivity, "interviews", interviews);
    Whitebox.setInternalState(this.surveyActivity, "participantData", participant);
    Whitebox.setInternalState(this.surveyActivity, "statusHistory", statusHistory);
    Whitebox.setInternalState(this.surveyActivity, "fillContainer", fillContainer);

    this.records.buildSurveyBasicInfo(surveyActivity, FIELD_CENTER);
    LinkedHashMap<String, Object> values = this.records.getSurveyInformation();

  }

}
