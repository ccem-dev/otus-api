package org.ccem.otus.service;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.activityImport.ActivityImportDTO;
import org.ccem.otus.model.survey.activity.activityImport.ActivityImportResultDTO;

import java.util.List;

public interface ActivityService {

    String create(SurveyActivity surveyActivity);

    SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException;

    List<SurveyActivity> list(long rn, String userEmail);

    SurveyActivity getByID(String id) throws DataNotFoundException;

    List<SurveyActivity> get(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

    boolean updateCheckerActivity(String checkerUpdated) throws DataNotFoundException;

    List<ActivityImportResultDTO> importActivities(ActivityImportDTO surveyActivities);
}
