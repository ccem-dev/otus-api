package org.ccem.otus.service;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;

import java.util.ArrayList;
import java.util.List;

public interface ActivityService {

    String create(SurveyActivity surveyActivity);

    SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException;

    List<SurveyActivity> list(long rn, String userEmail);

    SurveyActivity getByID(String id) throws DataNotFoundException;

    List<SurveyActivity> get(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

    AggregateIterable<Document> aggregate(ArrayList<Bson> fetchAttachmentsReportQury);
}
