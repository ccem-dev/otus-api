package org.ccem.otus.service;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.offlineActivity.OfflineActivityCollection;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.offlineActivity.OfflineActivityCollectionsDTO;
import org.ccem.otus.service.extraction.model.ActivityProgressResultExtraction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface ActivityService {

  String create(SurveyActivity surveyActivity);

  SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException;

  List<SurveyActivity> list(long rn, String userEmail);

  SurveyActivity getByID(String id) throws DataNotFoundException;

  List<SurveyActivity> get(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

  List<SurveyActivity> getExtraction(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException;

  boolean updateCheckerActivity(String checkerUpdated) throws DataNotFoundException;

  SurveyActivity getActivity(String acronym, Integer version, String categoryName, Long recruitmentNumber) throws DataNotFoundException;

  LinkedList<ActivityProgressResultExtraction> getActivityProgressExtraction(String center) throws DataNotFoundException;

  HashMap<Long, String> getParticipantFieldCenterByActivity(String acronym, Integer version) throws DataNotFoundException;

  void save(String userEmail, OfflineActivityCollection offlineActivityCollection) throws DataNotFoundException;

  OfflineActivityCollectionsDTO fetchOfflineActivityCollections(String userEmail) throws DataNotFoundException;

  OfflineActivityCollection fetchOfflineActivityCollection(String collectionId) throws DataNotFoundException;

  void deactivateOfflineActivityCollection(String offlineCollectionId, List<ObjectId> createdActivityIds);
}
