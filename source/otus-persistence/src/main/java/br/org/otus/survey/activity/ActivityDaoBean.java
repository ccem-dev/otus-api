package br.org.otus.survey.activity;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import br.org.otus.survey.activity.builder.SurveyActivityQueryBuilder;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.configuration.ActivityCategory;
import org.ccem.otus.model.survey.activity.dto.CheckerUpdatedDTO;
import org.ccem.otus.model.survey.activity.dto.StageSurveyActivitiesDto;
import org.ccem.otus.model.survey.activity.status.ActivityStatus;
import org.ccem.otus.permissions.service.user.group.UserPermission;
import org.ccem.otus.persistence.ActivityDao;
import org.ccem.otus.service.ParseQuery;

import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import br.org.mongodb.MongoGenericDao;

@Stateless
public class ActivityDaoBean extends MongoGenericDao<Document> implements ActivityDao {

  public static final String COLLECTION_NAME = "activity";

  public static final String ACRONYM_PATH = "surveyForm.acronym";
  public static final String VERSION_PATH = "surveyForm.version";
  public static final String DISCARDED_PATH = "isDiscarded";
  public static final String TEMPLATE_PATH = "surveyForm.surveyTemplate";
  public static final String RECRUITMENT_NUMBER_PATH = "participantData.recruitmentNumber";
  public static final String CATEGORY_NAME_PATH = "category.name";
  public static final String CATEGORY_LABEL_PATH = "category.label";
  public static final String IS_DISCARDED = "isDiscarded";
  public static final String ID_PATH = "_id";
  public static final String STATUS_HISTORY_NAME = "statusHistory.name";
  public static final String FINALIZED = "FINALIZED";
  private static final String SET = "$set";
  private static final String PARTICIPANT_DATA_EMAIL = "participantData.email";
  private static final String STAGE_PATH = "stageId";

  public ActivityDaoBean() {
    super(COLLECTION_NAME, Document.class);
  }

  private Document parseQuery(String query) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    return gsonBuilder.create().fromJson(query, Document.class);
  }

  /**
   * Return activities considering that they were not discarded
   *
   * @param rn
   * @return
   */
  @Override
  @UserPermission
  public List<SurveyActivity> find(List<String> permittedSurveys, String userEmail, long rn) {

    ArrayList<SurveyActivity> activities = new ArrayList<SurveyActivity>();
    List<Bson> pipeline = new ArrayList<>();
    pipeline.add(new Document("$match",
      new Document(RECRUITMENT_NUMBER_PATH, rn)
        .append(DISCARDED_PATH, false)
        .append(ACRONYM_PATH, new Document("$in", permittedSurveys))));
    AggregateIterable<Document> result = collection.aggregate(pipeline);

    result.forEach((Block<Document>) document -> {
      activities.add(SurveyActivity.deserialize(document.toJson()));
    });

    return activities;
  }

  @Override
  @UserPermission
  public List<StageSurveyActivitiesDto> findByStageGroup(List<String> permittedSurveys, String userEmail, long rn) throws MemoryExcededException {
    ArrayList<StageSurveyActivitiesDto> activities = new ArrayList<>();

    ArrayList<Bson> pipeline = (new SurveyActivityQueryBuilder())
      .getSurveyActivityListByStageAndAcronymQuery(rn, permittedSurveys);

    AggregateIterable<Document> results = collection.aggregate(pipeline).allowDiskUse(true);
    MongoCursor<Document> iterator = results.iterator();

    while(iterator.hasNext()){
      try{
        activities.add(StageSurveyActivitiesDto.deserialize(iterator.next().toJson()));
      }
      catch (OutOfMemoryError e){
        throw new MemoryExcededException("Activities for " + rn + " exceded memory used");
      }
    }

    return activities;
  }

  @Override
  public ObjectId persist(SurveyActivity surveyActivity) {
    Document parsed = Document.parse(SurveyActivity.serialize(surveyActivity));
    removeOids(parsed);
    collection.insertOne(parsed);

    return parsed.getObjectId("_id");
  }

  @Override
  public SurveyActivity update(SurveyActivity surveyActivity) throws DataNotFoundException {
    Document parsed = Document.parse(SurveyActivity.serialize(surveyActivity));
    removeOids(parsed);
    UpdateResult updateOne = collection.updateOne(eq("_id", surveyActivity.getActivityID()), new Document(SET, parsed), new UpdateOptions().upsert(false));

    if (updateOne.getMatchedCount() == 0) {
      throw new DataNotFoundException(new Throwable("OID {" + surveyActivity.getActivityID().toString() + "} not found."));
    }

    return surveyActivity;
  }

  /**
   * This method return specific activity.
   *
   * @param id database id (_id)
   * @return
   * @throws DataNotFoundException
   */
  @Override
  public SurveyActivity findByID(String id) throws DataNotFoundException {
    ObjectId oid = new ObjectId(id);
    Document result = fetchWithSurveyTemplate(new Document(ID_PATH, oid)).first();

    if (result == null) {
      throw new DataNotFoundException(new Throwable("OID {" + id + "} not found."));
    }

    return SurveyActivity.deserialize(result.toJson());

  }

  @Override
  public List<SurveyActivity> getUndiscarded(String acronym, Integer version)
    throws DataNotFoundException, MemoryExcededException {
    Document query = new Document();
    query.put(ACRONYM_PATH, acronym);
    query.put(VERSION_PATH, version);
    query.put(DISCARDED_PATH, Boolean.FALSE);
    Bson projection = fields(exclude(TEMPLATE_PATH));

    FindIterable<Document> documents = collection.find(query).projection(projection);
    MongoCursor<Document> iterator = documents.iterator();
    ArrayList<SurveyActivity> activities = new ArrayList<>();

    while (iterator.hasNext()) {
      try {
        activities.add(SurveyActivity.deserialize(iterator.next().toJson()));
      } catch (OutOfMemoryError e) {
        activities.clear();
        activities = null;
        throw new MemoryExcededException("Extraction {" + acronym + "} exceded memory used.");
      }
    }

    if (activities.isEmpty()) {
      throw new DataNotFoundException(new Throwable("OID {" + acronym + "} not found."));
    }

    return activities;
  }

  public List<SurveyActivity> getExtraction(String acronym, Integer version) throws DataNotFoundException, MemoryExcededException {
    List<Bson> pipeline = new ArrayList<>();
    pipeline.add(ParseQuery.toDocument("{\n" +
      "    '$match': {\n" +
      "      \"surveyForm.acronym\":" + acronym + ", \n" +
      "      \"surveyForm.version\":" + version + ", \n" +
      "      \"isDiscarded\": false\n" +
      "    }\n" +
      "  }"));
    AggregateIterable<Document> results = collection.aggregate(pipeline).allowDiskUse(true);
    if (results == null) {
      throw new DataNotFoundException("There are no results");
    }

    MongoCursor<Document> iterator = results.iterator();
    ArrayList<SurveyActivity> activities = new ArrayList<>();
    while (iterator.hasNext()) {
      try {
        activities.add(SurveyActivity.deserialize(iterator.next().toJson()));
      } catch (OutOfMemoryError e) {
        activities.clear();
        activities = null;
        throw new MemoryExcededException("Extraction {" + acronym + "} exceded memory used.");
      }
    }

    if (activities.isEmpty()) {
      throw new DataNotFoundException(new Throwable("OID {" + acronym + "} not found."));
    }

    return activities;
  }

  @Override
  public SurveyActivity getLastFinalizedActivity(String acronym, Integer version, String categoryName, Long recruitmentNumber) throws DataNotFoundException {
    Document query = new Document();
    query.put(ACRONYM_PATH, acronym);
    query.put(VERSION_PATH, version);
    query.put(CATEGORY_NAME_PATH, categoryName);
    query.put(IS_DISCARDED, false);
    query.put(STATUS_HISTORY_NAME ,FINALIZED);
    query.put(RECRUITMENT_NUMBER_PATH, recruitmentNumber);


    MongoCursor<Document> iterator = fetchWithSurveyTemplate(query).iterator();
    Document result = null;

    try {
      while (iterator.hasNext()) {
        result = iterator.next();
      }
    } catch (Exception ignored) {
    } finally {
      iterator.close();
    }

    if (result == null) {
      throw new DataNotFoundException(new Throwable("Activity not found"));
    }

    return SurveyActivity.deserialize(result.toJson());
  }

  @Override
  public List<SurveyActivity> findByCategory(String categoryName) {
    ArrayList<SurveyActivity> activities = new ArrayList<>();

    FindIterable<Document> result = collection.find(eq(CATEGORY_NAME_PATH, categoryName));

    result.forEach((Block<Document>) document -> activities.add(SurveyActivity.deserialize(document.toJson())));
    return activities;
  }

  @Override
  public void updateCategory(ActivityCategory activityCategory) {
    Document query = new Document();
    query.put("category.name", activityCategory.getName());

    UpdateResult updateResult = collection.updateOne(query, new Document(SET, new Document(CATEGORY_LABEL_PATH, activityCategory.getLabel())), new UpdateOptions().upsert(false));
  }

  @Override
  public boolean updateCheckerActivity(CheckerUpdatedDTO checkerUpdatedDTO) throws DataNotFoundException {
    String checkerUpdateJson = ActivityStatus.serialize(checkerUpdatedDTO.getActivityStatus());
    Document parsed = Document.parse(checkerUpdateJson);
    Document checkerUpdate = (Document) parsed.get("user");
    String dateUpdated = (String) parsed.get("date");

    UpdateResult updateResult = collection.updateOne(
      and(eq("_id", new ObjectId(checkerUpdatedDTO.getId())),
        eq("statusHistory.name", checkerUpdatedDTO.getActivityStatus().getName())),
      new Document(SET, new Document("statusHistory.$.user", checkerUpdate).append("statusHistory.$.date", dateUpdated)));

    if (updateResult.getMatchedCount() == 0) {
      throw new DataNotFoundException(new Throwable("Activity of Participant not found"));
    }

    return true;
  }


  private AggregateIterable<Document> fetchWithSurveyTemplate(Document matchQuery) {
    List<Bson> pipeline = new ArrayList<>();
    pipeline.add(new Document("$match", matchQuery));
    pipeline.add(parseQuery("{\n" +
      "        $lookup: {\n" +
      "            from : \"survey\",\n" +
      "             let:{\n" +
      "                    \"acronym\":\"$surveyForm.acronym\",\n" +
      "                    \"version\":\"$surveyForm.version\"},\n" +
      "            pipeline:[\n" +
      "                {\n" +
      "                    $match:{\n" +
      "                        $expr:{\n" +
      "                            $and: [\n" +
      "                                {$eq: [\"$$acronym\", \"$surveyTemplate.identity.acronym\"]},\n" +
      "                                {$eq: [\"$$version\", \"$version\"]}\n" +
      "                                ]\n" +
      "                        }\n" +
      "                    }\n" +
      "                },\n" +
      "                {\n" +
      "                                  \"$replaceRoot\":{\n" +
      "                                   \"newRoot\":\"$surveyTemplate\"\n" +
      "                                   }\n" +
      "                }\n" +
      "                ],\n" +
      "                \"as\":\"surveyForm.surveyTemplate\"\n" +
      "        }\n" +
      "    }"));
    pipeline.add(parseQuery("{\"$addFields\":{\"surveyForm.surveyTemplate\":{$arrayElemAt: [\"$surveyForm.surveyTemplate\",0]}}}"));

    return collection.aggregate(pipeline);
  }

  @Override
  public boolean updateParticipantEmail(long rn, String email) {

    UpdateResult updateResult =collection.updateMany(new Document(RECRUITMENT_NUMBER_PATH, rn), new Document(SET , new Document(PARTICIPANT_DATA_EMAIL, email)));

    return updateResult.getModifiedCount() != 0;
  }

  @Override
  public void removeStageFromActivities(ObjectId stageOID) {
    collection.updateMany(
      eq(STAGE_PATH, stageOID),
      new Document("$unset", new Document(STAGE_PATH, ""))
    );
  }

  @Override
  public void discardByID(ObjectId activityOID) throws DataNotFoundException {
    UpdateResult updateResult = collection.updateOne(
      eq(ID_FIELD_NAME, activityOID),
      new Document(SET, new Document("isDiscarded", true))
    );

    if(updateResult.getMatchedCount() == 0){
      throw new DataNotFoundException(new Throwable("Activity with id "+ activityOID.toHexString() + "was not found."));
    }
  }

  @Override
  public List<ObjectId> getActivityIds(String acronym, Integer version, Boolean isDiscardedValue,
                                             List<String> activityIdsToExcludeOfQuery) throws MemoryExcededException {

    ArrayList<ObjectId> activities = new ArrayList<>();

    ArrayList<Bson> pipeline = SurveyActivityQueryBuilder.getActivityIdsQuery(acronym, version, isDiscardedValue, activityIdsToExcludeOfQuery);
    AggregateIterable<Document> results = collection.aggregate(pipeline).allowDiskUse(true);
    MongoCursor<Document> iterator = results.iterator();

    while (iterator.hasNext()) {
      try {
        activities.add(SurveyActivity.deserialize(iterator.next().toJson()).getActivityID());
      } catch (OutOfMemoryError e) {
        activities.clear();
        throw new MemoryExcededException(String.format("Extraction { %s, version %ld } exceeded memory used.", acronym, version));
      }
    }

    return activities;
  }

  private void removeOids(Document parsedActivity) {
    parsedActivity.remove("_id");
    ((Document) parsedActivity.get("surveyForm")).remove("_id"); //todo: remove when this id becomes standard
  }

}
