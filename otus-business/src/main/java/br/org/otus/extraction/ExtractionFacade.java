package br.org.otus.extraction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.service.extraction.SurveyActivityExtraction;
import org.ccem.otus.survey.form.SurveyForm;

import br.org.otus.api.ExtractionService;
import br.org.otus.examUploader.Exam;
import br.org.otus.examUploader.api.ExamUploadFacade;
import br.org.otus.examUploader.business.extraction.ExamUploadExtration;
import br.org.otus.survey.activity.api.ActivityFacade;
import br.org.otus.survey.api.SurveyFacade;

public class ExtractionFacade {

  @Inject
  private ActivityFacade activityFacade;
  @Inject
  private SurveyFacade surveyFacade;
  @Inject
  private ExamUploadFacade examUploadFacade;
  @Inject
  private ExtractionService extractionService;

  public byte[] createActivityExtraction(String acronym, Integer version) throws DataNotFoundException {
    List<SurveyActivity> activities = new ArrayList<>();

    activities = activityFacade.get(acronym, version);
    SurveyForm surveyForm = surveyFacade.get(acronym, version);
    SurveyActivityExtraction extractor = new SurveyActivityExtraction(surveyForm, activities);
    try {
      return extractionService.createExtraction(extractor);
    } catch (DataNotFoundException e) {
      throw new DataNotFoundException(new Throwable("RESULTS TO EXTRACTION {" + acronym + "} not found."));
    }
  }

  public List<Integer> listSurveyVersions(String acronym) {
    return surveyFacade.listVersions(acronym);
  }

  public byte[] createLaboratoryExamsValuesExtraction(String center) {
    List<Exam> exams = new ArrayList<>();

    ExamUploadExtration extractor;

    return null;
  }

}
