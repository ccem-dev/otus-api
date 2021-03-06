package br.org.otus.configuration.api;

import br.org.otus.response.exception.HttpResponseException;
import br.org.otus.response.info.AlreadyExist;
import br.org.otus.response.info.NotFound;
import model.Stage;
import model.StageDto;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import org.ccem.otus.service.ActivityService;
import service.StageService;

import javax.inject.Inject;
import java.util.List;

public class StageFacade {

  @Inject
  private StageService stageService;

  @Inject
  private ActivityService activityService;


  public String create(String stageJson) {
    try {
      return stageService.create(Stage.deserialize(stageJson)).toString();
    } catch (AlreadyExistException e) {
      throw new HttpResponseException(AlreadyExist.build(e.getMessage()));
    }
  }

  public void update(String stageId, String stageJson) {
    try{
      Stage stage = Stage.deserialize(stageJson);
      stage.setId(new ObjectId(stageId));
      stageService.update(stage);
    }
    catch (AlreadyExistException e){
      throw new HttpResponseException(AlreadyExist.build(e.getMessage()));
    }
    catch (DataNotFoundException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public void delete(String stageId) {
    try{
      ObjectId stageOID = new ObjectId(stageId);
      stageService.delete(stageOID);
      activityService.removeStageFromActivities(stageOID);
    }
    catch (DataNotFoundException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public Stage getByID(String stageId) {
    try{
      return stageService.getByID(new ObjectId(stageId));
    }
    catch (DataNotFoundException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public List<Stage> getAll() {
    try{
      return stageService.getAll();
    }
    catch (MemoryExcededException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public void updateSurveyAcronymsOfStage(String stageJson) {
    try{
      stageService.updateSurveyAcronymsOfStage(Stage.deserialize(stageJson));
    }
    catch (DataNotFoundException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

  public void updateStagesOfSurveyAcronym(String stageDtoJson) {
    try{
      StageDto stageDto = StageDto.deserialize(stageDtoJson);
      stageService.updateStagesOfSurveyAcronym(stageDto.getAcronym(), stageDto.getStageIdsToAdd(), stageDto.getStageIdsToRemove());
    }
    catch (DataNotFoundException e){
      throw new HttpResponseException(NotFound.build(e.getMessage()));
    }
  }

}
