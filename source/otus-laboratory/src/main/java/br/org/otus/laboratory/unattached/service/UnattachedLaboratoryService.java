package br.org.otus.laboratory.unattached.service;

import br.org.otus.laboratory.configuration.collect.group.CollectGroupDescriptor;
import br.org.otus.laboratory.unattached.DTOs.ListUnattachedLaboratoryDTO;
import br.org.otus.laboratory.unattached.model.UnattachedLaboratory;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;

public interface UnattachedLaboratoryService {
  void create(String userEmail, Integer unattachedLaboratoryLastInsertion, CollectGroupDescriptor collectGroupDescriptor, FieldCenter fieldCenterAcronym) throws DataNotFoundException;

  ListUnattachedLaboratoryDTO find(String fieldCenterAcronym, String collectGroupDescriptorName, int page, int quantityByPage) throws DataNotFoundException;

  void attache(Long recruitmentNumber, String email, int laboratoryIdentification, String participantCollectGroupName, String participantFieldCenterAcronym) throws DataNotFoundException, ValidationException;

  void discard(String userEmail, String laboratoryOid) throws DataNotFoundException;

  UnattachedLaboratory findById(String laboratoryOid) throws DataNotFoundException;

  UnattachedLaboratory findByIdentification(int laboratoryIdentification) throws DataNotFoundException;
}
