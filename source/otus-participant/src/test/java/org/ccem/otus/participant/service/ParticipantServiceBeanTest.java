package org.ccem.otus.participant.service;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.participant.model.Participant;
import org.ccem.otus.participant.persistence.ParticipantDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import service.ProjectConfigurationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantServiceBeanTest {
  private static final long RECRUITMENT_NUMBER = 3051442;
  private static final String PARTICIPANT_NAME = "Jose Otus";
  private static final String ACRONYM = "RS";
  private static final String EMAIL = "email@test.com";
  private static final String PASSWORD = "password";
  private static final String ID = "58c83f502226685b94f8973a";

  @InjectMocks
  ParticipantServiceBean participantServiceBean;
  @Mock
  ParticipantDao participantDao;
  @Mock
  ProjectConfigurationService projectConfigurationService;
  @Mock
  RecruitmentNumberService recruitmentNumberService;
  private Set<Participant> setParticipants;
  private Participant participant;
  private FieldCenter fieldCenter;
  private ArrayList<Participant> listParticipants;
  private ObjectId objectId;

  @Before
  public void setUp() {
    fieldCenter = new FieldCenter();
    fieldCenter.setAcronym(ACRONYM);

    setParticipants = Mockito.spy(new HashSet<>());

    participant = new Participant(RECRUITMENT_NUMBER);
    participant.setName(PARTICIPANT_NAME);
    participant.setFieldCenter(fieldCenter);

    listParticipants = new ArrayList<>();
    listParticipants.add(participant);
    listParticipants.add(participant);
    objectId = new ObjectId(ID);
  }

  @Test
  public void method_create_with_SetParticipant_should_evocate_forEach_of_participants_list()
    throws ValidationException {
    participantServiceBean.create(setParticipants);
    verify(setParticipants, times(1)).forEach(anyObject());
  }

  @Test
  public void method_create_with_Participant_shoud_evocate_persist_of_ParticipantDao() throws ValidationException, DataNotFoundException {
    participantServiceBean.create(participant);
    verify(participantDao, times(1)).persist(participant);
  }

  @Test
  public void method_list_should_execute_find_of_participantDao() {
    fieldCenter = null;
    when(participantDao.find()).thenReturn(listParticipants);
    assertEquals(PARTICIPANT_NAME, participantServiceBean.list(fieldCenter).get(0).getName());
    verify(participantDao, times(0)).findByFieldCenter(fieldCenter);
  }

  @Test
  public void method_create_participant_should_insert_successfully_when_a_participant_with_given_rn_doesnt_exist() throws ValidationException {
    setParticipants.add(participant);
    when(participantDao.exists(RECRUITMENT_NUMBER)).thenReturn(false);
    participantServiceBean.create(setParticipants);
  }

  @Test
  public void method_create_participants_validate_recruitmentNumber_should_return_exception() {
    setParticipants.add(participant);
    when(participantDao.exists(RECRUITMENT_NUMBER)).thenReturn(true);
    participantServiceBean.create(setParticipants);
  }

  @Test(expected = ValidationException.class)
  public void method_create_a_participant_validate_recruitmentNumber_should_return_exception() throws ValidationException, DataNotFoundException {
    when(participantDao.exists(RECRUITMENT_NUMBER)).thenReturn(true);
    assertNull(participantServiceBean.create(participant));
  }

  @Test
  public void method_list_should_execute_findByFieldCenter_of_participantDao() {
    when(participantDao.findByFieldCenter(fieldCenter)).thenReturn(listParticipants);
    assertEquals(participant.getName(), participantServiceBean.list(fieldCenter).get(0).getName());
    assertEquals(ACRONYM, participantServiceBean.list(fieldCenter).get(0).getFieldCenter().getAcronym());
    verify(participantDao, times(0)).find();
  }

  @Test
  public void method_getByRecruitmentNumber_should_return_participant() throws DataNotFoundException {
    when(participantDao.findByRecruitmentNumber(RECRUITMENT_NUMBER)).thenReturn(participant);
    assertEquals(PARTICIPANT_NAME, participantServiceBean.getByRecruitmentNumber(RECRUITMENT_NUMBER).getName());
  }

  @Test
  public void create_should_call_ask_for_recruitment_number_when_the_auto_generation_is_on() throws DataNotFoundException, ValidationException {
    when(projectConfigurationService.isRnProvided()).thenReturn(true);
    when(recruitmentNumberService.get(ACRONYM)).thenReturn(RECRUITMENT_NUMBER);

    Participant result = participantServiceBean.create(this.participant);

    verify(recruitmentNumberService).get(this.participant.getFieldCenter().getAcronym());
    assertEquals(result.getRecruitmentNumber(), (Long) RECRUITMENT_NUMBER);
  }

  @Test(expected = ValidationException.class)
  public void create_should_throw_error_when_rn_is_null_and_not_provided() throws DataNotFoundException, ValidationException {
    when(projectConfigurationService.isRnProvided()).thenReturn(false);
    participant.setRecruitmentNumber(null);
    doThrow(new ValidationException()).when(recruitmentNumberService).validate(this.participant.getFieldCenter(), null);
    participantServiceBean.create(this.participant);
  }

  @Test(expected = ValidationException.class)
  public void validate_should_throw_error_when_rn_is_null_and_not_provided() throws DataNotFoundException, ValidationException {
    when(projectConfigurationService.isRnProvided()).thenReturn(false);
    participant.setRecruitmentNumber(null);
    doThrow(new ValidationException()).when(recruitmentNumberService).validate(this.participant.getFieldCenter(), null);
    participantServiceBean.create(this.participant);
  }

  @Test
  public void should_call_dao_for_the_list_of_rns() throws DataNotFoundException, ValidationException {
    participantServiceBean.listCenterRecruitmentNumbers(ACRONYM);
    Mockito.verify(participantDao, Mockito.times(1)).getRecruitmentNumbersByFieldCenter(ACRONYM);
  }

  @Test
  public void getParticipantFieldCenterByRecruitmentNumber_method_should_call_method_getParticipantFieldCenterByRecruitmentNumber() throws DataNotFoundException {
    participantServiceBean.getParticipantFieldCenterByRecruitmentNumber(RECRUITMENT_NUMBER);
    Mockito.verify(participantDao, Mockito.times(1)).getParticipantFieldCenterByRecruitmentNumber(RECRUITMENT_NUMBER);
  }

  @Test
  public void method_registerPassword_should_call_participantDao_registerPassword() throws DataNotFoundException {
    participantServiceBean.registerPassword(EMAIL, PASSWORD);
    verify(participantDao, times(1)).registerPassword(EMAIL, PASSWORD);
  }

  @Test
  public void updateEmail_method_should_evoke_call_methods() throws DataNotFoundException, AlreadyExistException {
    participantServiceBean.updateEmail(objectId,EMAIL);
    verify(participantDao, times(1)).updateEmail(objectId, EMAIL);
  }

  @Test
  public void deleteEmail_method_should_evoke_call_methods() throws DataNotFoundException {
    participantServiceBean.deleteEmail(objectId);
    verify(participantDao, times(1)).deleteEmail(objectId);
  }

  @Test
  public void getEmail_method_should_evoke_call_methods() throws ValidationException, DataNotFoundException {
    participantServiceBean.getEmail(ID);
    verify(participantDao, times(1)).getEmail(objectId);
  }
}
