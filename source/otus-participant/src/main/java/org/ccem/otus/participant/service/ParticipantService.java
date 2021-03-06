package org.ccem.otus.participant.service;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.participant.business.extraction.model.ParticipantResultExtraction;
import org.ccem.otus.participant.model.Participant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface ParticipantService {

  Participant create(Participant participant) throws ValidationException, DataNotFoundException;

  Participant update(Participant participant) throws ValidationException, DataNotFoundException;

  Participant getByRecruitmentNumber(Long rn) throws DataNotFoundException;

  Participant getByEmail(String email) throws DataNotFoundException;

  ObjectId findIdByRecruitmentNumber(Long rn) throws DataNotFoundException;

  List<Participant> list(FieldCenter fieldCenter);

  Long getPartipantsActives(String acronymCenter) throws DataNotFoundException;

  void create(Set<Participant> participants);

  ArrayList<Long> listCenterRecruitmentNumbers(String center) throws DataNotFoundException;

  void registerPassword(String token, String Password) throws DataNotFoundException;

  String getParticipantFieldCenterByRecruitmentNumber(Long recruitmentNumber) throws DataNotFoundException;

  Participant getParticipant(ObjectId id) throws DataNotFoundException;

  Boolean updateEmail(ObjectId participantId, String email) throws DataNotFoundException, AlreadyExistException;

  String getEmail(String participantId) throws ValidationException, DataNotFoundException;

  Boolean deleteEmail(ObjectId participantId) throws DataNotFoundException;

  LinkedList<ParticipantResultExtraction> getParticipantExtraction() throws DataNotFoundException;

}
