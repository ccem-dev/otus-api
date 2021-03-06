package br.org.otus.survey.activity.sharing;

import br.org.otus.commons.FindByTokenService;
import br.org.otus.logs.LogEventFacade;
import br.org.otus.response.exception.HttpResponseException;
import br.org.otus.response.info.Validation;
import br.org.otus.security.dtos.ParticipantTempTokenRequestDto;
import br.org.otus.security.services.TemporaryParticipantTokenService;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.security.TokenException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.logs.enums.ActivitySharingProgressLog;
import org.ccem.otus.logs.events.ActivitySharedLog;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.model.survey.activity.mode.ActivityMode;
import org.ccem.otus.model.survey.activity.sharing.ActivitySharing;
import org.ccem.otus.model.survey.activity.sharing.ActivitySharingDto;
import org.ccem.otus.participant.model.Participant;
import org.ccem.otus.participant.service.ParticipantService;
import org.ccem.otus.service.ActivityService;
import org.ccem.otus.service.sharing.ActivitySharingService;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.zip.DataFormatException;

public class ActivitySharingFacade {

  private static final String NOT_AUTOFILL_INVALID_SHARED_LINK_REQUEST_MESSAGE = "Only autofill activities could generate url.";

  @Inject
  private ActivitySharingService activitySharingService;

  @Inject
  private ActivityService activityService;

  @Inject
  private ParticipantService participantService;

  @Inject
  private FindByTokenService findByTokenService;

  @Inject
  private TemporaryParticipantTokenService temporaryParticipantTokenService;

  @Inject
  private LogEventFacade logEventFacade;


  public ActivitySharingDto getSharedURL(String activityId, String userToken) throws HttpResponseException {
    try {
      ObjectId userOID = findByTokenService.findUserByToken(userToken).get_id();
      logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.ACCESS));
      SurveyActivity surveyActivity = checkIfActivityModeIsAutoFill(activityId);
      Participant participant = participantService.getByRecruitmentNumber(surveyActivity.getParticipantData().getRecruitmentNumber());
      String token = temporaryParticipantTokenService.generateTempToken(
        new ParticipantTempTokenRequestDto(participant.getRecruitmentNumber(), activityId)
      );
      ActivitySharing activitySharing = new ActivitySharing(surveyActivity.getActivityID(), userOID, token);
      return getOrCreateSharedURL(activitySharing, userOID);
    } catch (DataFormatException | DataNotFoundException | ValidationException | ParseException | TokenException e) {
      throw new HttpResponseException(Validation.build(e.getMessage(), e.getCause()));
    }
  }

  private ActivitySharingDto getOrCreateSharedURL(ActivitySharing activitySharing, ObjectId userOID) {
    ActivitySharingDto activitySharingDto;
    try {
      activitySharingDto = activitySharingService.getSharedURL(activitySharing);
      if (activitySharingDto != null) {
        logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.SEARCH));
      }
    } catch (DataNotFoundException e) {
      activitySharingDto = activitySharingService.createSharedURL(activitySharing);
      if (activitySharingDto != null) {
        logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.CREATE));
      }
    }
    return activitySharingDto;
  }

  public ActivitySharingDto renovateSharedURL(String activitySharingId, String userToken) throws HttpResponseException {
    try {
      ObjectId userOID = findByTokenService.findUserByToken(userToken).get_id();
      logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.ACCESS));
      ActivitySharingDto activitySharingDto = activitySharingService.renovateSharedURL(activitySharingId);
      if (activitySharingDto != null) {
        logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.RENEW));
      }
      return activitySharingDto;
    } catch (DataNotFoundException | ValidationException | ParseException e) {
      throw new HttpResponseException(Validation.build(e.getMessage(), e.getCause()));
    }
  }

  public void deleteSharedURL(String activitySharingId, String userToken) throws HttpResponseException {
    try {
      activitySharingService.deleteSharedURL(activitySharingId);
      ObjectId userOID = findByTokenService.findUserByToken(userToken).get_id();
      logEventFacade.log(new ActivitySharedLog(userOID, ActivitySharingProgressLog.DELETION));
    } catch (DataNotFoundException | ValidationException | ParseException e) {
      throw new HttpResponseException(Validation.build(e.getMessage(), e.getCause()));
    }
  }

  private SurveyActivity checkIfActivityModeIsAutoFill(String activityID) throws DataNotFoundException, DataFormatException {
    SurveyActivity surveyActivity = activityService.getByID(activityID);
    if (!surveyActivity.getMode().name().equals(ActivityMode.AUTOFILL.toString())) {
      throw new DataFormatException(NOT_AUTOFILL_INVALID_SHARED_LINK_REQUEST_MESSAGE);
    }
    return surveyActivity;
  }

}
