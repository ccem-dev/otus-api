package br.org.otus.laboratory.participant;

import br.org.otus.laboratory.participant.aliquot.Aliquot;
import br.org.otus.laboratory.participant.tube.Tube;
import org.bson.Document;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import java.util.ArrayList;

public interface ParticipantLaboratoryDao {

	void persist(ParticipantLaboratory laboratoryParticipant);

	ParticipantLaboratory find();

	ParticipantLaboratory findByRecruitmentNumber(long rn) throws DataNotFoundException;

	ParticipantLaboratory updateLaboratoryData(ParticipantLaboratory labParticipant) throws DataNotFoundException;

	Tube updateTubeCollectionData(long rn, Tube tube) throws DataNotFoundException;

	Document findDocumentByAliquotCode(String aliquotCode) throws DataNotFoundException;

	ArrayList<Aliquot> getFullAliquotsList();

	ArrayList<ParticipantLaboratory> getAllParticipantLaboratory();
}
