package org.ccem.otus.participant.persistence;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.participant.model.Participant;

import java.util.ArrayList;

public interface ParticipantDao {

  void persist(Participant participant);

  ArrayList<Participant> find();

  Participant findByRecruitmentNumber(Long rn) throws DataNotFoundException;

  Participant findLastInsertion (String fieldCenter) throws DataNotFoundException;

  ArrayList<Participant> findByFieldCenter(FieldCenter fieldCenter);

  Long getPartipantsActives(String acronymCenter) throws DataNotFoundException;

  boolean exists(Long rn);

  Participant getLastInsertion (String fieldCenter) throws DataNotFoundException;
}
