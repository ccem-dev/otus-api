package br.org.otus.laboratory.project.api;

import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import br.org.otus.laboratory.configuration.LaboratoryConfigurationService;
import br.org.otus.laboratory.configuration.collect.aliquot.AliquoteDescriptor;
import br.org.otus.laboratory.project.business.LaboratoryProjectService;
import br.org.otus.response.builders.Security;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import br.org.otus.laboratory.project.aliquot.WorkAliquot;
import br.org.otus.laboratory.project.exam.ExamLot;
import br.org.otus.laboratory.project.exam.businnes.ExamLotService;
import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;

public class ExamLotFacade {

    @Inject
    private ExamLotService examLotService;

    @Inject
    private LaboratoryConfigurationService laboratoryConfigurationService;

    @Inject
    private LaboratoryProjectService laboratoryProjectService;

    public ExamLot create(ExamLot examLot, String userEmail) {
        try {
            return examLotService.create(examLot, userEmail);
        } catch (ValidationException e) {
            e.printStackTrace();
            throw new HttpResponseException(
                    Security.Validation.build(e.getCause().getMessage(), e.getData()));
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            throw new HttpResponseException(
                    ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
        }
    }

    public ExamLot update(ExamLot examLot) {
        try {
            return examLotService.update(examLot);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        } catch (ValidationException e) {
            e.printStackTrace();
            throw new HttpResponseException(
                    ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
        }
    }

    public void delete(String id) {
        try {
            examLotService.delete(id);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }
    }

    public List<ExamLot> getLots() {
        return examLotService.list();
    }

    public List<WorkAliquot> getAliquots() {
        try {
            return examLotService.getAliquots();
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }
    }

    public LinkedHashSet<AliquoteDescriptor> getAvailableExams(String center) {
        try {
            return laboratoryProjectService.getAvailableExams(center);
        } catch (DataNotFoundException e) {
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }
    }

}
