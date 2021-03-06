package br.org.otus.laboratory.participant.aliquot.persistence;

import br.org.otus.laboratory.participant.aliquot.Aliquot;
import br.org.otus.laboratory.project.transportation.TransportationLot;
import br.org.otus.laboratory.project.transportation.persistence.TransportationAliquotFiltersDTO;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface AliquotDao {

  List<Aliquot> getAliquots();

  void persist(Aliquot aliquot);

  List<Aliquot> list(Long recruitmentNumber);

  List<Aliquot> getAliquotsByPeriod(TransportationAliquotFiltersDTO transportationAliquotFiltersDTO, String locationPoint, List<String> aliquotsInLocationPoint, List<String> aliquotsNotInOrigin);

  void updateExamLotId(ArrayList<String> codeList, ObjectId loId) throws DataNotFoundException;

  Aliquot getAliquot(TransportationAliquotFiltersDTO workAliquotFiltersDTO) throws DataNotFoundException;

  void addToTransportationLot(ArrayList<String> aliquotCodeList, ObjectId transportationLotId) throws DataNotFoundException;

  void updateTransportationLotId(ArrayList<String> codeList, ObjectId loId) throws DataNotFoundException;

  boolean exists(String code);

  void delete(String code) throws DataNotFoundException;

  Aliquot find(String code) throws DataNotFoundException;

  AggregateIterable<Document> aggregate(List<Bson> query);

  List<Aliquot> getExamLotAliquots(ObjectId lotOId);

  void executeFunction(String function);

  String convertAliquotRole(Aliquot convertedAliquot) throws DataNotFoundException;

  ArrayList<Document> buildTrails(ArrayList<String> aliquotCodeList, ObjectId userId, TransportationLot transportationLot);

  ArrayList<Aliquot> getAliquots(ArrayList<String> aliquotCodeList);

  List<String> getAliquotsByOrigin(String locationPointId);

  HashMap<String, Aliquot> getExamAliquotsHashMap(List<String> aliquotCodes);
}
