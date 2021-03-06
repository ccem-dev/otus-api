package br.org.otus.gateway.resource;

import br.org.otus.gateway.MicroservicesEnvironments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ExtractionMicroServiceResources.class})
public class ExtractionMicroServiceResourcesTest extends MicroServiceResourcesTestParent {

  private static final String SURVEY_ID = "123";
  private static final String ACTIVITY_ID = "4567";
  private static final String R_SCRIPT_NAME = "script";

  private ExtractionMicroServiceResources resources;

  @Before
  public void setUp() throws Exception {
    parentSetUp(MicroservicesEnvironments.EXTRACTION);
    resources = new ExtractionMicroServiceResources();
  }

  @Test
  public void getActivityExtractionCreateAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/extractions/activity"),
      resources.getActivityExtractionCreateAddress()
    );
  }

  @Test
  public void getActivityExtractionDeleteAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/extractions/activity/" + SURVEY_ID + "/" + ACTIVITY_ID),
      resources.getActivityExtractionDeleteAddress(SURVEY_ID, ACTIVITY_ID)
    );
  }

  @Test
  public void getSurveyActivityIdsWithExtractionAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/survey/get-survey-activities-ids/" + SURVEY_ID),
      resources.getSurveyActivityIdsWithExtractionAddress(SURVEY_ID)
    );
  }

  @Test
  public void getCsvSurveyExtractionAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/survey/csv/" + SURVEY_ID),
      resources.getCsvSurveyExtractionAddress(SURVEY_ID)
    );
  }

  @Test
  public void getJsonSurveyExtractionAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/survey/json/" + SURVEY_ID),
      resources.getJsonSurveyExtractionAddress(SURVEY_ID)
    );
  }


  @Test
  public void getRScriptCreationAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/rscript/create"),
      resources.getRScriptCreationAddress()
    );
  }

  @Test
  public void getRScriptGetterAddress_method_should_return_expected_url() throws MalformedURLException, URISyntaxException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/rscript/" + R_SCRIPT_NAME),
      resources.getRScriptGetterAddress(R_SCRIPT_NAME)
    );
  }

  @Test
  public void getRScriptDeleteAddress_method_should_return_expected_url() throws MalformedURLException, URISyntaxException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/rscript/" + R_SCRIPT_NAME),
      resources.getRScriptDeleteAddress(R_SCRIPT_NAME)
    );
  }


  @Test
  public void getRScriptJsonSurveyExtractionAddress_method_should_return_expected_url() throws MalformedURLException {
    Assert.assertEquals(
      new URL("http://" + HOST + ":" + PORT + "/survey/rscript"),
      resources.getRScriptJsonSurveyExtractionAddress()
    );
  }

}
