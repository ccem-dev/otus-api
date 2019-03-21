
package org.ccem.otus.persistence;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.configuration.ActivityCategory;
import org.ccem.otus.model.survey.activity.dto.CheckerUpdatedDTO;

import com.mongodb.client.AggregateIterable;

public interface ActivityDao {

  List<SurveyActivity> find(List<String> permittedSurveys, String userEmail, long rn);

  ObjectId persist(SurveyActivity surveyActivity);

  SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException;

  SurveyActivity findByID(String id) throws DataNotFoundException;

  List<SurveyActivity> getUndiscarded(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

  List<SurveyActivity> findByCategory(String categoryName);

  void updateCategory(ActivityCategory activityCategory);

  AggregateIterable<Document> aggregate(List<Bson> query);

  boolean updateCheckerActivity(CheckerUpdatedDTO checkerUpdatedDTO) throws DataNotFoundException;
}
