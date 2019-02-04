package org.ccem.otus.persistence;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.configuration.ActivityCategory;
import org.ccem.otus.model.survey.activity.dto.CheckerUpdatedDTO;

import java.util.List;

public interface ActivityDao {

	List<SurveyActivity> find(long rn);

	ObjectId persist(SurveyActivity surveyActivity);

	SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException;

	SurveyActivity findByID(String id) throws DataNotFoundException;

	List<SurveyActivity> getUndiscarded(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

	List<SurveyActivity> findByCategory(String categoryName);

	void updateCategory(ActivityCategory activityCategory);

	boolean updateCheckerActivity(CheckerUpdatedDTO checkerUpdatedDTO) throws DataNotFoundException;
}
