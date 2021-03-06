package service;

import model.Stage;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.common.MemoryExcededException;
import persistence.StageDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class StageServiceBean implements StageService {

  @Inject
  private StageDao stageDao;


  @Override
  public ObjectId create(Stage stage) throws AlreadyExistException {
    return stageDao.create(stage);
  }

  @Override
  public void update(Stage stage) throws DataNotFoundException, AlreadyExistException {
    stageDao.update(stage);
  }

  @Override
  public void delete(ObjectId stageOID) throws DataNotFoundException {
    stageDao.delete(stageOID);
  }

  @Override
  public Stage getByID(ObjectId stageOID) throws DataNotFoundException {
    return stageDao.getByID(stageOID);
  }

  @Override
  public List<Stage> getAll() throws MemoryExcededException {
    return stageDao.getAll();
  }

  @Override
  public void updateSurveyAcronymsOfStage(Stage stage) throws DataNotFoundException {
    stageDao.updateSurveyAcronymsOfStage(stage);
  }

  @Override
  public void updateStagesOfSurveyAcronym(String acronym, List<ObjectId> stageOIDsToAdd, List<ObjectId> stageOIDsToRemove) throws DataNotFoundException {
    stageDao.updateStagesOfSurveyAcronym(acronym, stageOIDsToAdd, stageOIDsToRemove);
  }
}
