package br.org.otus.monitoring.builder;

import com.google.gson.GsonBuilder;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActivityStatusQueryBuilderTest {

  private static final Document ACTIVITY_INAPPLICABILITY = new Document();
  private static final List<Long> CENTER_RNS = new ArrayList<>();

  private ActivityStatusQueryBuilder builder;
  private LinkedList<String> stringLinkedList;

  @Before
  public void setUp() {
    builder = new ActivityStatusQueryBuilder();
    stringLinkedList = new LinkedList<>();
    stringLinkedList.add("HVSD");
    stringLinkedList.add("PSEC");
    stringLinkedList.add("ABC");
    stringLinkedList.add("DEF");
  }

  @Test
  public void getActivityStatusQueryByCenter() {
    String expectedQuery = "[{\"$match\":{\"participantData.recruitmentNumber\":{\"$in\":[]},\"isDiscarded\":false}},{\"$project\":{\"_id\":0.0,\"rn\":\"$participantData.recruitmentNumber\",\"acronym\":\"$surveyForm.acronym\",\"lastStatus_Date\":{\"$arrayElemAt\":[{\"$slice\":[\"$statusHistory.date\",-1.0]},0.0]},\"lastStatus_Name\":{\"$arrayElemAt\":[{\"$slice\":[\"$statusHistory.name\",-1.0]},0.0]}}},{\"$sort\":{\"lastStatus_Date\":1.0}},{\"$group\":{\"_id\":\"$rn\",\"activities\":{\"$push\":{\"status\":{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"CREATED\"]},-1.0,{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"SAVED\"]},1.0,{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"FINALIZED\"]},2.0,-1.0]}]}]},\"rn\":\"$rn\",\"acronym\":\"$acronym\"}}}},{\"$addFields\":{\"activityInapplicabilities\":{\"$arrayElemAt\":[{\"$filter\":{\"as\":\"activityInapplicalibity\",\"cond\":{\"$and\":[{\"$eq\":[\"$$activityInapplicalibity.rn\",\"$_id\"]}]}}},0.0]}}},{\"$addFields\":{\"activityInapplicabilities\":{\"$cond\":[{\"$ifNull\":[\"$activityInapplicabilities\",false]},\"$activityInapplicabilities\",[]]}}},{\"$addFields\":{\"headers\":[\"HVSD\",\"PSEC\",\"ABC\",\"DEF\"]}},{\"$unwind\":\"$headers\"},{\"$addFields\":{\"activityFound\":{\"$filter\":{\"input\":\"$activities\",\"as\":\"item\",\"cond\":{\"$eq\":[\"$$item.acronym\",\"$headers\"]}}},\"inapplicabilityFound\":{\"$gt\":[{\"$size\":{\"$filter\":{\"input\":\"$activityInapplicabilities.AI\",\"as\":\"item\",\"cond\":{\"$eq\":[\"$$item.acronym\",\"$headers\"]}}}},0.0]}}},{\"$group\":{\"_id\":\"$_id\",\"filteredActivities\":{\"$push\":{\"$cond\":[\"$inapplicabilityFound\",{\"status\":0.0,\"rn\":\"$_id\",\"acronym\":\"$headers\"},{\"$cond\":[{\"$gt\":[{\"$size\":\"$activityFound\"},0.0]},{\"$arrayElemAt\":[\"$activityFound\",-1.0]},{\"rn\":\"$_id\",\"acronym\":\"$headers\"}]}]}}}},{\"$group\":{\"_id\":{},\"index\":{\"$push\":\"$_id\"},\"data\":{\"$push\":\"$filteredActivities.status\"}}}]";
    assertEquals(expectedQuery, new GsonBuilder().create().toJson(builder.getActivityStatusQuery(CENTER_RNS, stringLinkedList, ACTIVITY_INAPPLICABILITY)));
  }

  @Test
  public void getActivityStatusQuery() {
    String expectedQuery = "[{\"$match\":{\"isDiscarded\":false}},{\"$project\":{\"_id\":0.0,\"rn\":\"$participantData.recruitmentNumber\",\"acronym\":\"$surveyForm.acronym\",\"lastStatus_Date\":{\"$arrayElemAt\":[{\"$slice\":[\"$statusHistory.date\",-1.0]},0.0]},\"lastStatus_Name\":{\"$arrayElemAt\":[{\"$slice\":[\"$statusHistory.name\",-1.0]},0.0]}}},{\"$sort\":{\"lastStatus_Date\":1.0}},{\"$group\":{\"_id\":\"$rn\",\"activities\":{\"$push\":{\"status\":{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"CREATED\"]},-1.0,{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"SAVED\"]},1.0,{\"$cond\":[{\"$eq\":[\"$lastStatus_Name\",\"FINALIZED\"]},2.0,-1.0]}]}]},\"rn\":\"$rn\",\"acronym\":\"$acronym\"}}}},{\"$addFields\":{\"activityInapplicabilities\":{\"$arrayElemAt\":[{\"$filter\":{\"as\":\"activityInapplicalibity\",\"cond\":{\"$and\":[{\"$eq\":[\"$$activityInapplicalibity.rn\",\"$_id\"]}]}}},0.0]}}},{\"$addFields\":{\"activityInapplicabilities\":{\"$cond\":[{\"$ifNull\":[\"$activityInapplicabilities\",false]},\"$activityInapplicabilities\",[]]}}},{\"$addFields\":{\"headers\":[\"HVSD\",\"PSEC\",\"ABC\",\"DEF\"]}},{\"$unwind\":\"$headers\"},{\"$addFields\":{\"activityFound\":{\"$filter\":{\"input\":\"$activities\",\"as\":\"item\",\"cond\":{\"$eq\":[\"$$item.acronym\",\"$headers\"]}}},\"inapplicabilityFound\":{\"$gt\":[{\"$size\":{\"$filter\":{\"input\":\"$activityInapplicabilities.AI\",\"as\":\"item\",\"cond\":{\"$eq\":[\"$$item.acronym\",\"$headers\"]}}}},0.0]}}},{\"$group\":{\"_id\":\"$_id\",\"filteredActivities\":{\"$push\":{\"$cond\":[\"$inapplicabilityFound\",{\"status\":0.0,\"rn\":\"$_id\",\"acronym\":\"$headers\"},{\"$cond\":[{\"$gt\":[{\"$size\":\"$activityFound\"},0.0]},{\"$arrayElemAt\":[\"$activityFound\",-1.0]},{\"rn\":\"$_id\",\"acronym\":\"$headers\"}]}]}}}},{\"$group\":{\"_id\":{},\"index\":{\"$push\":\"$_id\"},\"data\":{\"$push\":\"$filteredActivities.status\"}}}]";
    assertEquals(expectedQuery, new GsonBuilder().create().toJson(builder.getActivityStatusQuery(stringLinkedList, ACTIVITY_INAPPLICABILITY)));
  }
}
