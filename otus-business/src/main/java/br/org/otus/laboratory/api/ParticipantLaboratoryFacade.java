package br.org.otus.laboratory.api;

import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import br.org.otus.laboratory.participant.ParticipantLaboratory;
import br.org.otus.laboratory.participant.ParticipantLaboratoryService;
import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;

public class ParticipantLaboratoryFacade {

	@Inject
	private ParticipantLaboratoryService service;

	public ParticipantLaboratory update(ParticipantLaboratory participantLaboratory) {
		try {
			return service.update(participantLaboratory);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
		}
	}

	public ParticipantLaboratory getLaboratory(Long recruitmentNumber) {
		try {
			return service.getLaboratory(recruitmentNumber);
		} catch (DataNotFoundException e) {
//			e.printStackTrace();
//			throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
			return null;
		}
	}

	public ParticipantLaboratory create(Long recruitmentNumber) {
		try {
			return service.create(recruitmentNumber);
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
		}
	}

	public boolean hasLaboratory(Long recruitmentNumber) {
		return service.hasLaboratory(recruitmentNumber);
	}
	
	

	
}
