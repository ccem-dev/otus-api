package org.ccem.otus.participant.service;

import java.util.List;
import java.util.Set;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.participant.model.Participant;

public interface ParticipantService {

  Participant create(Participant participant) throws ValidationException, DataNotFoundException;

  Participant getByRecruitmentNumber(Long rn) throws DataNotFoundException;

  List<Participant> list(FieldCenter fieldCenter);

  Long getPartipantsActives(String acronymCenter) throws DataNotFoundException;

  void create(Set<Participant> participants);

}
