package org.ccem.otus.model.dataSources.activity;

import java.util.ArrayList;

import org.bson.Document;
import org.ccem.otus.model.dataSources.ReportDataSource;

public class ActivityDataSource extends ReportDataSource<ActivityDataSourceResult> {

  private ActivityDataSourceFilters filters;

  @Override
  public void addResult(ActivityDataSourceResult result) {
    super.getResult().add(result);
  }

  @Override
  public ArrayList<Document> buildQuery(Long recruitmentNumber) {
    ArrayList<Document> query = new ArrayList<>();
    this.buildMachStage(recruitmentNumber, query);
    this.buildProjectionStage(query);
    this.appendStatusHistoryFilter(query);

    return query;
  }

  private void buildMachStage(Long recruitmentNumber, ArrayList<Document> query) {
    Document filters = new Document("participantData.recruitmentNumber", recruitmentNumber).append("surveyForm.surveyTemplate.identity.acronym", this.filters.getAcronym());

    if (this.filters.getCategory() != null) {
      appendCategoryFilter(filters, this.filters.getCategory());
    }

    filters.append("isDiscarded", Boolean.FALSE);

    Document matchStage = new Document("$match", filters);
    query.add(matchStage);
  }

  private void buildProjectionStage(ArrayList<Document> query) {
    Document projectionFields = new Document("_id", -1);
    if (this.filters.getStatusHistory() != null) {
      projectionFields.append("statusHistory", 1);
    }
    projectionFields.append("mode", 1);
    Document projectStage = new Document("$project", projectionFields);
    query.add(projectStage);
  }

  private void appendStatusHistoryFilter(ArrayList<Document> query) {
    if (this.filters.getStatusHistory() != null) {
      Document statusHistoryMatchStage = new Document("$match", new Document("statusHistory.name", this.filters.getStatusHistory().getName()));
      query.add(statusHistoryMatchStage);
    }
  }

  private void appendCategoryFilter(Document matchStage, String category) {
    matchStage.append("category.name", category);
  }
}