package br.org.otus.laboratory.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import com.mongodb.client.AggregateIterable;

import br.org.otus.laboratory.configuration.aliquot.AliquotExamCorrelation;

public interface LaboratoryConfigurationDao {

  LaboratoryConfiguration find();

  Boolean existsLaboratoryConfiguration();

  AliquotExamCorrelation getAliquotExamCorrelation() throws DataNotFoundException;

  List<String> getAliquotsExams(List<String> aliquots);

  void persist(LaboratoryConfiguration laboratoryConfig);

  String createNewLotCodeForTransportation(Integer code);

  String createNewLotCodeForExam(Integer code);

  Integer getLastInsertion(String lot);

  void restoreLotConfiguration(String config, Integer code);

  Integer updateLastTubeInsertion(int newTubesQuantities);

  ArrayList listCenterAliquots(String center) throws DataNotFoundException;

  List<String> getExamName(List<String> centerAliquots);

  AggregateIterable<Document> aggregate(List<Bson> query);

}
