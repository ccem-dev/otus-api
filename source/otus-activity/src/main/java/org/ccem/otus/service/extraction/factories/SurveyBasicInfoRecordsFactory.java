package org.ccem.otus.service.extraction.factories;

import java.time.LocalDateTime;

import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.interview.Interview;
import org.ccem.otus.model.survey.activity.status.ActivityStatus;
import org.ccem.otus.model.survey.activity.status.ActivityStatusOptions;

public class SurveyBasicInfoRecordsFactory {

  private static final String HEADER_MESSAGE = "[EXTRACTION]";
  private static final String LAST_INTERVIEWER_NOT_FOUND = "Interviewer value error";
  private static final String CURRENT_STATUS_NAME_NOT_FOUND = "Current status name value error";
  private static final String CURRENT_STATUS_DATE_NOT_FOUND = "Current status date value error";
  private static final String CREATION_DATE_NOT_FOUND = "Current creation date value error";
  private static final String PAPER_DATE_NOT_FOUND = "Paper date value error";
  private static final String PAPER_INTERVIEWER_NOT_FOUND = "Paper interviewer value error";
  private static final String FINALIZED_STATUS_NOT_FOUND = "Finalized status value error";
  private static final String CATEGORY_NOT_FOUND = "Category value error";
  private static final String PARTICIPANT_NOT_FOUND = "Participant value error";
  private static final String PARTICIPANT_FIELD_CENTER_NOT_FOUND = "Participant Field Center by activity error";
  private static final String EXTERNAL_ID_NOT_FOUND = "ExternalID value error";

  public static Long getRecruitmentNumber(SurveyActivity surveyActivity) {
    try {
      return surveyActivity.getParticipantData().getRecruitmentNumber();
    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(PARTICIPANT_NOT_FOUND);
      return null;
    }
  }

  public static String getCategory(SurveyActivity surveyActivity) {
    try {
      return surveyActivity.getCategory().getName();
    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(CATEGORY_NOT_FOUND);
      return null;
    }
  }

  public static String getLastInterviewer(SurveyActivity surveyActivity) {
    try {
      final Interview lastInterview = surveyActivity.getLastInterview().orElse(null);
      final String interviewerEmail = lastInterview != null ? lastInterview.getInterviewer().getEmail() : null;
      return interviewerEmail;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(LAST_INTERVIEWER_NOT_FOUND);
      return null;
    }
  }

  public static String getCurrentStatusName(ActivityStatus currentActivityStatus) {
    try {
      return currentActivityStatus != null ? currentActivityStatus.getName() : null;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(CURRENT_STATUS_NAME_NOT_FOUND);
      return null;
    }
  }

  public static LocalDateTime getCurrentStatusDate(ActivityStatus currentActivityStatus) {
    try {
      return (currentActivityStatus != null) ? currentActivityStatus.getDate() : null;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(CURRENT_STATUS_DATE_NOT_FOUND);
      return null;
    }
  }

  public static LocalDateTime getCreationDate(SurveyActivity surveyActivity) {
    try {
      final ActivityStatus creationStatus = surveyActivity.getLastStatusByName(ActivityStatusOptions.CREATED.getName()).orElse(null);
      final LocalDateTime creationTime = (creationStatus != null) ? creationStatus.getDate() : null;
      return creationTime;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(CREATION_DATE_NOT_FOUND);
      return null;
    }
  }

  public static LocalDateTime getPaperRealizationDate(ActivityStatus paperStatus) {
    try {
      return (paperStatus != null) ? paperStatus.getDate() : null;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(PAPER_DATE_NOT_FOUND);
      return null;
    }
  }

  public static String getPaperInterviewer(ActivityStatus paperStatus) {
    try {
      return (paperStatus != null) ? paperStatus.getUser().getEmail() : null;

    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(PAPER_INTERVIEWER_NOT_FOUND);
      return null;
    }
  }

  public static LocalDateTime getLasFinalizationDate(SurveyActivity surveyActivity) {
    try {
      final ActivityStatus finalizedStatus = surveyActivity.getLastStatusByName(ActivityStatusOptions.FINALIZED.getName()).orElse(null);
      return (finalizedStatus != null) ? finalizedStatus.getDate() : null;
    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(FINALIZED_STATUS_NOT_FOUND);
      return null;
    }
  }

  public static String getParticipantFieldCenter(SurveyActivity surveyActivity) {
    String acronym = null;
    try {
      if (surveyActivity.getParticipantData() != null) {
        if (surveyActivity.getParticipantData().getFieldCenter() != null) {
          acronym = surveyActivity.getParticipantData().getFieldCenter().getAcronym();
        }
      }
      return (acronym != null) ? acronym : null;
    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(PARTICIPANT_FIELD_CENTER_NOT_FOUND);
      return null;
    }
  }

  public static String getExternalID(SurveyActivity surveyActivity) {
    try {
      return surveyActivity.getExternalID();
    } catch (Exception e) {
      SurveyBasicInfoRecordsFactory.printMessageError(EXTERNAL_ID_NOT_FOUND);
      return null;
    }
  }

  public static void printMessageError(String details) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(HEADER_MESSAGE);
    stringBuilder.append(": ");
    stringBuilder.append(details);
    System.err.println(stringBuilder.toString());
  }

}
