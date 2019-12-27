package br.org.otus.user.api.pendency;

import br.org.otus.model.pendency.UserActivityPendency;
import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;
import br.org.otus.response.info.NotFound;
import br.org.otus.service.pendency.UserActivityPendencyService;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import javax.inject.Inject;
import java.util.List;

public class UserActivityPendencyFacade {

  @Inject
  private UserActivityPendencyService userActivityPendencyService;

  public void create(String userActivityPendencyJson, String userEmail) {
    try {
      UserActivityPendency userActivityPendency = UserActivityPendency.deserialize(userActivityPendencyJson);
      userActivityPendencyService.create(userActivityPendency, userEmail);
    } catch (ValidationException | DataNotFoundException e) {
      throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
    }
  }

  public void update(String userActivityPendencyOID, String userActivityPendencyJson) {
    try {
      UserActivityPendency userActivityPendency = UserActivityPendency.deserialize(userActivityPendencyJson);
      ObjectId oid = new ObjectId(userActivityPendencyOID);
      userActivityPendencyService.update(oid, userActivityPendency);
    } catch (ValidationException | DataNotFoundException e) {
      throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
    }
  }

  public void delete(String userActivityPendencyOID) {
    try {
      ObjectId oid = new ObjectId(userActivityPendencyOID);
      userActivityPendencyService.delete(oid);
    } catch (ValidationException | DataNotFoundException e) {
      throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
    }
  }

  public UserActivityPendency getByActivityId(String activityId) {
    try {
      return userActivityPendencyService.getByActivityId(activityId);
    } catch (DataNotFoundException e) {
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public List<UserActivityPendency> listAllPendencies() {
    try {
      return userActivityPendencyService.listAllPendencies();
    } catch (DataNotFoundException | MemoryExcededException e) {
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public List<UserActivityPendency> listOpenedPendencies() {
    try {
      return userActivityPendencyService.listOpenedPendencies();
    } catch (DataNotFoundException | MemoryExcededException e) {
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public List<UserActivityPendency> listDonePendencies() {
    try {
      return userActivityPendencyService.listDonePendencies();
    } catch (DataNotFoundException | MemoryExcededException e) {
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

}