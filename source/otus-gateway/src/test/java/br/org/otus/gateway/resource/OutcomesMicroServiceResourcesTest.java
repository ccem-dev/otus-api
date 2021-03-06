package br.org.otus.gateway.resource;

import br.org.otus.gateway.MicroservicesEnvironments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OutcomesMicroServiceResources.class})
public class OutcomesMicroServiceResourcesTest extends MicroServiceResourcesTestParent {

  private static final String ACTIVITY_ID = "5f5fade308a0fc339325a8c8";

  private OutcomesMicroServiceResources resources;

  @Before
  public void setUp() throws Exception {
    parentSetUp(MicroservicesEnvironments.OUTCOMES);
    resources = new OutcomesMicroServiceResources();
  }

  @Test
  public void getCreateOutcomeAddress_method_should_return_expected_url() throws MalformedURLException {
    url = new URL("http://"+HOST+":"+PORT+"/followUp/add");
    Assert.assertEquals(resources.getCreateFollowUpAddress(), url);
  }

  @Test
  public void getUpdateOutcomeAddress_method_should_return_expected_url() throws MalformedURLException {
    url = new URL("http://"+HOST+":"+PORT+"/followUp/update");
    Assert.assertEquals(resources.getUpdateFollowUpAddress(), url);
  }

  @Test
  public void getListOutcomesAddress_method_should_return_expected_url() throws MalformedURLException {
    url = new URL("http://"+HOST+":"+PORT+"/followUp/list");
    Assert.assertEquals(resources.getListFollowUpsAddress(), url);
  }

  @Test
  public void getAccomplishedParticipantEventAddressByActivity_method_should_return_expected_url() throws MalformedURLException {
    url = new URL("http://"+HOST+":"+PORT+"/participantEvent/accomplished/activity/5f5fade308a0fc339325a8c8");
    Assert.assertEquals(resources.getAccomplishedParticipantEventAddressByActivity(ACTIVITY_ID), url);
  }

  @Test
  public void getReopenedParticipantEventAddressByActivity_method_should_return_expected_url() throws MalformedURLException {
    url = new URL("http://"+HOST+":"+PORT+"/participantEvent/reopened/activity/5f5fade308a0fc339325a8c8");
    Assert.assertEquals(resources.getReopenedParticipantEventAddressByActivity(ACTIVITY_ID), url);
  }

}