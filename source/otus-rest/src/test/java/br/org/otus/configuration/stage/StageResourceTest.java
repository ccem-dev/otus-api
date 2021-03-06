package br.org.otus.configuration.stage;

import br.org.otus.configuration.api.StageFacade;
import br.org.otus.ResourceTestsParent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class StageResourceTest extends ResourceTestsParent {

  private static final String STAGE_ID = "5f77920624439758ce4a43ab";
  private static final String STAGE_JSON = "{}";
  private static final String STAGE_DTO_JSON = "{}";

  @InjectMocks
  private StageResource stageResource;

  @Mock
  private StageFacade stageFacade;


  @Test
  public void create_method_should_call_stageFacade_create_method(){
    when(stageFacade.create(STAGE_JSON)).thenReturn(STAGE_ID);
    assertEquals(
      encapsulateExpectedResponse("\""+STAGE_ID+"\""),
      stageResource.create(STAGE_JSON));
  }

  @Test
  public void update_method_should_call_stageFacade_update_method(){
    String result = stageResource.update(STAGE_ID, STAGE_JSON);
    verify(stageFacade, Mockito.times(1)).update(STAGE_ID, STAGE_JSON);
    assertEquals(EMPTY_RESPONSE, result);
  }

  @Test
  public void delete_method_should_call_stageFacade_delete_method(){
    String result = stageResource.delete(STAGE_ID);
    verify(stageFacade, Mockito.times(1)).delete(STAGE_ID);
    assertEquals(EMPTY_RESPONSE, result);
  }

  @Test
  public void getByID_method_should_call_stageFacade_getByID_method(){
    String result = stageResource.getByID(STAGE_ID);
    verify(stageFacade, Mockito.times(1)).getByID(STAGE_ID);
    assertEquals("{}", result);
  }

  @Test
  public void getAll_method_should_call_stageFacade_getAll_method(){
    String result = stageResource.getAll();
    verify(stageFacade, Mockito.times(1)).getAll();
    assertEquals(encapsulateExpectedResponse("[]"), result);
  }

  @Test
  public void updateSurveyAcronymsOfStage_method_should_call_stageFacade_updateSurveyAcronymsOfStage_method(){
    String result = stageResource.updateSurveyAcronymsOfStage(STAGE_JSON);
    verify(stageFacade, Mockito.times(1)).updateSurveyAcronymsOfStage(STAGE_JSON);
    assertEquals(EMPTY_RESPONSE, result);
  }

  @Test
  public void updateStagesOfSurveyAcronym_method_should_call_stageFacade_updateSurveyAcronymsOfStage_method(){
    String result = stageResource.updateStagesOfSurveyAcronym(STAGE_DTO_JSON);
    verify(stageFacade, Mockito.times(1)).updateStagesOfSurveyAcronym(STAGE_DTO_JSON);
    assertEquals(EMPTY_RESPONSE, result);
  }

}
