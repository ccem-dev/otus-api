package br.org.otus.laboratory.project.transportation.persistence;

import br.org.otus.laboratory.project.transportation.TransportMaterialCorrelation;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface TransportMaterialCorrelationDao {
  void persist(TransportMaterialCorrelation transportMaterialCorrelation);

  TransportMaterialCorrelation get(ObjectId lotId);

  void update(ObjectId lotId, ArrayList<String> newAliquotCodeList);
}
