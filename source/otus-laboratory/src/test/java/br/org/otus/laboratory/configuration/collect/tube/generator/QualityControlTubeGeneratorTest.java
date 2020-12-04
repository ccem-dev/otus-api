package br.org.otus.laboratory.configuration.collect.tube.generator;

import br.org.otus.laboratory.configuration.LaboratoryConfiguration;
import br.org.otus.laboratory.configuration.LaboratoryConfigurationService;
import br.org.otus.laboratory.configuration.collect.group.CollectGroupDescriptor;
import br.org.otus.laboratory.configuration.collect.tube.TubeDefinition;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.participant.model.Participant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class QualityControlTubeGeneratorTest {

  @InjectMocks
  private QualityControlTubeGenerator qualityControlTubeGenerator;
  @Mock
  private LaboratoryConfigurationService laboratoryConfigurationService;
  @InjectMocks
  private FieldCenter fieldCenter;
  @InjectMocks
  private LaboratoryConfiguration laboratoryConfiguration;
  private CollectGroupDescriptor collectGroupDescriptor;
  private Set<TubeDefinition> tubeSets;
  private Set<TubeDefinition> expectedTubeSets;
  private List<TubeDefinition> tubeDefinitionsExpected;
  private TubeSeed tubeSeed;
  private String typeExpected;
  private String momentExpected;
  private int tubeCountExpected;

  @Before
  public void setUp() throws DataNotFoundException {
    tubeSets = new HashSet<TubeDefinition>();
    tubeSets.add(new TubeDefinition(1, "FLOURIDE", "POST_OVERLOAD"));
    tubeSets.add(new TubeDefinition(4, "GEL", "FASTING"));
    tubeSets.add(new TubeDefinition(3, "EDTA", "FASTING"));
    tubeSets.add(new TubeDefinition(1, "URINE", "NONE"));
    tubeSets.add(new TubeDefinition(1, "FLOURIDE", "FASTING"));
    tubeSets.add(new TubeDefinition(2, "GEL", "POST_OVERLOAD"));

    collectGroupDescriptor = new CollectGroupDescriptor("DEFAULT", "DEFAULT", tubeSets);
    tubeSeed = TubeSeed.generate(fieldCenter, collectGroupDescriptor);

    when(laboratoryConfigurationService.getTubeSetByGroupName(tubeSeed.getCollectGroupDescriptor().getName()))
      .thenReturn(tubeSets);
  }

  @Test
  public void method_should_call_getTubeDefinitionList() throws DataNotFoundException {
    qualityControlTubeGenerator.getTubeDefinitions(tubeSeed);
    verify(laboratoryConfigurationService).getTubeSetByGroupName("DEFAULT");
  }

  @Test
  public void method_should_getTubeDefinitions() throws DataNotFoundException {
    expectedTubeSets = new HashSet<TubeDefinition>();
    expectedTubeSets.add(new TubeDefinition(4, "GEL", "POST_OVERLOAD"));
    expectedTubeSets.add(new TubeDefinition(3, "EDTA", "FASTING"));

    tubeDefinitionsExpected = expectedTubeSets.stream().map(definition -> definition).collect(Collectors.toList());

    typeExpected = tubeDefinitionsExpected.stream().filter(t -> t.getType().equals("GEL")).findFirst().get()
      .getType();

    momentExpected = tubeDefinitionsExpected.stream().filter(t -> t.getMoment().equals("POST_OVERLOAD")).findFirst()
      .get().getMoment();

    assertEquals(typeExpected, qualityControlTubeGenerator.getTubeDefinitions(tubeSeed).stream()
      .filter(t -> t.getType().equals("GEL")).findFirst().get().getType());

    assertEquals(momentExpected, qualityControlTubeGenerator.getTubeDefinitions(tubeSeed).stream()
      .filter(t -> t.getMoment().equals("POST_OVERLOAD")).findFirst().get().getMoment());
  }

  @Test
  public void method_should_getQuantityTubeDefinition() throws DataNotFoundException {
    int expectatedSizeTubeDefinition = tubeSets.size();
    assertEquals(expectatedSizeTubeDefinition, qualityControlTubeGenerator.getTubeDefinitions(tubeSeed).size());
  }

  @Test
  public void method_should_sumGetTubes() throws DataNotFoundException {
    tubeCountExpected = 0;
    for (TubeDefinition tubeSet : tubeSets) {
      tubeCountExpected += tubeSet.getCount();
    }

    qualityControlTubeGenerator.getTubeDefinitions(tubeSeed);
    assertEquals(tubeCountExpected, (int) tubeSeed.getTubeCount());
  }
}
