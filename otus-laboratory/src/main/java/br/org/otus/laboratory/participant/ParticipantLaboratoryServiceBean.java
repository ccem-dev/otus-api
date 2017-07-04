package br.org.otus.laboratory.participant;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.participant.model.Participant;
import org.ccem.otus.participant.persistence.ParticipantDao;

import br.org.otus.laboratory.collect.aliquot.Aliquot;
import br.org.otus.laboratory.collect.group.CollectGroupDescriptor;
import br.org.otus.laboratory.collect.group.CollectGroupRaffle;
import br.org.otus.laboratory.collect.tube.Tube;
import br.org.otus.laboratory.collect.tube.TubeService;
import br.org.otus.laboratory.collect.tube.generator.TubeSeed;
import br.org.otus.laboratory.dto.UpdateTubeAliquotsDTO;
import br.org.otus.laboratory.dto.UpdateAliquotsDTO;

@Stateless
public class ParticipantLaboratoryServiceBean implements ParticipantLaboratoryService {

	@Inject
	private ParticipantLaboratoryDao participantLaboratoryDao;
	@Inject
	private ParticipantDao participantDao;
	@Inject
	private TubeService tubeService;
	@Inject
	private CollectGroupRaffle groupRaffle;

	@Override
	public boolean hasLaboratory(Long recruitmentNumber) {
		try {
			participantLaboratoryDao.findByRecruitmentNumber(recruitmentNumber);
			return true;
		} catch (DataNotFoundException e) {
			return false;
		}
	}

	@Override
	public ParticipantLaboratory getLaboratory(Long recruitmentNumber) throws DataNotFoundException {
		return participantLaboratoryDao.findByRecruitmentNumber(recruitmentNumber);
	}

	@Override
	public ParticipantLaboratory create(Long recruitmentNumber) throws DataNotFoundException {
		Participant participant = participantDao.findByRecruitmentNumber(recruitmentNumber);
		CollectGroupDescriptor collectGroup = groupRaffle.perform(participant);
		List<Tube> tubes = tubeService.generateTubes(TubeSeed.generate(participant, collectGroup));
		ParticipantLaboratory laboratory = new ParticipantLaboratory(recruitmentNumber, collectGroup.getName(), tubes);
		participantLaboratoryDao.persist(laboratory);
		return laboratory;
	}

	@Override
	public ParticipantLaboratory update(ParticipantLaboratory partipantLaboratory) throws DataNotFoundException {
		return participantLaboratoryDao.updateLaboratoryData(partipantLaboratory);
	}

	@Override
	public boolean isAliquoted(long rn, String aliquotCode) {
		try {
			participantLaboratoryDao.findDocumentWithAliquotCodeNotInRecruimentNumber(rn, aliquotCode);
			return true;

		} catch (DataNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ParticipantLaboratory updateAliquots(UpdateAliquotsDTO updateAliquots) {
		for (UpdateTubeAliquotsDTO aliquotDTO : updateAliquots.getUpdateAliquots()) {

			verifyConflicts(updateAliquots, aliquotDTO);

		}

		// Verificar se a alíquota já foi aliquotada em outro participante.
		// Verificar se a alíquota já foi utilizado dentro do mesmo
		// participante.
		return null;
	}

	private List<Object> verifyConflicts(UpdateAliquotsDTO updateAliquots, UpdateTubeAliquotsDTO aliquotDTO) {
		ArrayList<Object> conflicts = new ArrayList<>();
		List<Aliquot> duplicates = aliquotDTO.getDuplicatesAliquots();

		if (!duplicates.isEmpty()) {
			conflicts.addAll(duplicates);
		}

		for (Aliquot aliquot : aliquotDTO.getNewAliquots()) {
			if (this.isAliquoted(updateAliquots.getRecruitmentNumber(), aliquot.getCode())) {
				conflicts.add(aliquot);
			}
		}
		return conflicts;
	}

}
