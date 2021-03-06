package br.org.otus.laboratory.project.exam.examLot.businnes;

import java.util.HashSet;
import java.util.List;

import br.org.otus.laboratory.participant.aliquot.Aliquot;
import br.org.otus.laboratory.project.exam.examLot.persistence.ExamLotAliquotFilterDTO;
import org.bson.Document;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import br.org.otus.laboratory.project.exam.examLot.ExamLot;

public interface ExamLotService {

  ExamLot create(ExamLot examLot, String email) throws ValidationException, DataNotFoundException;

  ExamLot update(ExamLot examLot) throws DataNotFoundException, ValidationException;

  List<ExamLot> list(String centerAcronym);

  void delete(String code) throws DataNotFoundException;

  HashSet<Document> getAliquotsInfosInTransportationLots() throws DataNotFoundException;

  Aliquot validateNewAliquot(ExamLotAliquotFilterDTO examLotAliquotFilterDTO) throws DataNotFoundException, ValidationException;
}
