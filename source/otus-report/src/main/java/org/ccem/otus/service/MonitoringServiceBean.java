package org.ccem.otus.service;

import br.org.otus.laboratory.project.exam.examInapplicability.ExamInapplicability;
import br.org.otus.laboratory.project.exam.examInapplicability.persistence.ExamInapplicabilityDao;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.model.monitoring.*;
import org.ccem.otus.model.monitoring.laboratory.LaboratoryProgressDTO;
import org.ccem.otus.model.survey.activity.configuration.ActivityInapplicability;
import org.ccem.otus.participant.persistence.ParticipantDao;
import org.ccem.otus.persistence.*;
import org.ccem.otus.persistence.laboratory.LaboratoryProgressDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class MonitoringServiceBean implements MonitoringService {

  @Inject
  private MonitoringDao monitoringDao;

  @Inject
  private FieldCenterDao fieldCenterDao;

  @Inject
  private ParticipantDao participantDao;

  @Inject
  private ActivityFlagReportDao activityFlagReportDao;

  @Inject
  private SurveyDao surveyDao;

  @Inject
  private SurveyMonitoringDao surveyMonitoringDao;

  @Inject
  private ExamMonitoringDao examMonitoringDao;

  @Inject
  private LaboratoryProgressDao laboratoryProgressDao;

  @Inject
  private ActivityInapplicabilityDao activityInapplicabilityDao;

  @Inject
  private ExamInapplicabilityDao examInapplicabilityDao;

  private List<Bson> pipeline;

  @Override
  public List<MonitoringDataSourceResult> get(String acronym) throws ValidationException {
    MonitoringDataSource monitoringDataSource = new MonitoringDataSource();
    return monitoringDao.get(monitoringDataSource.buildQuery(acronym));
  }

  @Override
  public ArrayList<MonitoringCenter> getMonitoringCenter() throws DataNotFoundException {

    ArrayList<MonitoringCenter> results = new ArrayList<>();
    ArrayList<String> centers = fieldCenterDao.listAcronyms();

    for (String acronymCenter : centers) {

      FieldCenter fieldCenter = fieldCenterDao.fetchByAcronym(acronymCenter);
      Long goals = participantDao.countParticipantActivities(acronymCenter);

      MonitoringCenter monitoring = new MonitoringCenter();
      monitoring.setName(fieldCenter.getName());
      monitoring.setGoal(goals);
      monitoring.setBackgroundColor(fieldCenter.getBackgroundColor());
      monitoring.setBorderColor(fieldCenter.getBorderColor());

      results.add(monitoring);
      monitoring = null;
    }
    return results;
  }

  @Override
  public ProgressReport getActivitiesProgress() throws DataNotFoundException {
    LinkedList<String> surveyAcronyms = new LinkedList<>(surveyDao.listAcronyms());

    groupActivityInapplicabilityStage();
    Document activityInapplicabilities = activityInapplicabilityDao.aggregate(pipeline).first();
    Document activitiesProgressReportDocument = activityFlagReportDao.getActivitiesProgressReport(surveyAcronyms, activityInapplicabilities);

    return getProgressReport(surveyAcronyms, activitiesProgressReportDocument);
  }

  @Override
  public ProgressReport getActivitiesProgress(String center) throws DataNotFoundException {
    LinkedList<String> surveyAcronyms = new LinkedList<>(surveyDao.listAcronyms());

    List<Long> rns = this.participantDao.getRecruitmentNumbersByFieldCenter(center);
    groupActivityInapplicabilityStageByCenter(rns);
    Document activityInapplicabilities = activityInapplicabilityDao.aggregate(pipeline).first();
    Document activitiesProgressReportDocument = activityFlagReportDao.getActivitiesProgressReport(center, rns, surveyAcronyms, activityInapplicabilities);

    return getProgressReport(surveyAcronyms, activitiesProgressReportDocument);
  }

  private ProgressReport getProgressReport(LinkedList<String> surveyAcronyms, Document activitiesProgressReportDocument) {
    ProgressReport progressReport = ProgressReport.deserialize(activitiesProgressReportDocument.toJson());
    progressReport.setColumns(surveyAcronyms);
    return progressReport;
  }

  @Override
  public ArrayList<ParticipantActivityReportDto> getParticipantActivities(Long rn) {
    return surveyMonitoringDao.getParticipantActivities(rn);
  }

  @Override
  public ParticipantExamReportDto getParticipantExams(Long rn) throws DataNotFoundException {
    return examMonitoringDao.getParticipantExams(rn);
  }

  @Override
  public void setActivityApplicability(ActivityInapplicability applicability) throws DataNotFoundException {
    activityInapplicabilityDao.update(applicability);
  }

  @Override
  public void deleteActivityApplicability(Long rn, String acronym) throws DataNotFoundException {
    activityInapplicabilityDao.delete(rn, acronym);
  }

  @Override
  public void deleteExamInapplicability(ExamInapplicability applicability) {
    examInapplicabilityDao.delete(applicability);
  }

  @Override
  public void setExamInapplicability(ExamInapplicability applicability) {
    examInapplicabilityDao.update(applicability);
  }

  @Override
  public LaboratoryProgressDTO getDataOrphanByExams() throws DataNotFoundException {
    return laboratoryProgressDao.getDataOrphanByExams();
  }

  @Override
  public LaboratoryProgressDTO getDataQuantitativeByTypeOfAliquots(String center) throws DataNotFoundException {
    return laboratoryProgressDao.getDataQuantitativeByTypeOfAliquots(center);
  }

  @Override
  public LaboratoryProgressDTO getDataOfPendingResultsByAliquot(String center) throws DataNotFoundException {
    return laboratoryProgressDao.getDataOfPendingResultsByAliquot(center);
  }

  @Override
  public LaboratoryProgressDTO getDataOfStorageByAliquot(String center) throws DataNotFoundException {
    return laboratoryProgressDao.getDataOfStorageByAliquot(center);
  }

  @Override
  public LaboratoryProgressDTO getDataByExam(String center) throws DataNotFoundException {
    return laboratoryProgressDao.getDataByExam(center);
  }

  @Override
  public LaboratoryProgressDTO getDataToCSVOfPendingResultsByAliquots(String center) throws DataNotFoundException {
    return laboratoryProgressDao.getDataToCSVOfPendingResultsByAliquots(center);
  }

  @Override
  public LaboratoryProgressDTO getDataToCSVOfOrphansByExam() throws DataNotFoundException {
    return laboratoryProgressDao.getDataToCSVOfOrphansByExam();
  }

  private void groupActivityInapplicabilityStageByCenter(List<Long> rns) {
    pipeline = new ArrayList<>();
    pipeline.add(new Document("$match",new Document("recruitmentNumber",new Document("$in",rns))));
    pipeline.add(ParseQuery.toDocument("{" +
      "         $group:{_id:\"$recruitmentNumber\",AI:{$push:{acronym : \"$acronym\",recruitmentNumber :\"$recruitmentNumber\"}}}" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "         $group:{_id:\"\",participantAI:{$push:{rn:\"$_id\",AI:\"$AI\"}}}" +
      "    }"));
  }

  private void groupActivityInapplicabilityStage() {
    pipeline = new ArrayList<>();
    pipeline.add(ParseQuery.toDocument("{" +
      "         $group:{_id:\"$recruitmentNumber\",AI:{$push:{acronym : \"$acronym\",recruitmentNumber :\"$recruitmentNumber\"}}}" +
      "    }"));
    pipeline.add(ParseQuery.toDocument("{\n" +
      "         $group:{_id:\"\",participantAI:{$push:{rn:\"$_id\",AI:\"$AI\"}}}" +
      "    }"));
  }
}