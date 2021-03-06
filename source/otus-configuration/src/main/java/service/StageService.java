package service;

import model.Stage;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;

import java.util.List;

public interface StageService {

  ObjectId create(Stage stage) throws AlreadyExistException;

  void update(Stage stage) throws DataNotFoundException, AlreadyExistException;

  void delete(ObjectId stageOID) throws DataNotFoundException;

  Stage getByID(ObjectId stageOID) throws DataNotFoundException;

  List<Stage> getAll() throws MemoryExcededException;

  void updateSurveyAcronymsOfStage(Stage stage) throws DataNotFoundException;

  void updateStagesOfSurveyAcronym(String acronym, List<ObjectId> stageOIDsToAdd, List<ObjectId> stageOIDsToRemove) throws DataNotFoundException;

}
