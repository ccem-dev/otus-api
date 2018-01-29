package br.org.otus.examUploader.api;

import br.org.otus.examUploader.ExamResult;
import br.org.otus.examUploader.ExamResultLot;
import br.org.otus.examUploader.ExamUploadDTO;
import br.org.otus.examUploader.business.ExamUploadService;
import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import javax.inject.Inject;
import java.util.List;

public class ExamUploadFacade {

    @Inject
    private ExamUploadService examUploadService;

    public String create(String examUploadJson, String userEmail){
        ExamUploadDTO examUploadDTO = ExamUploadDTO.deserialize(examUploadJson);
        String lotId = null;
        try {
            lotId = examUploadService.create(examUploadDTO, userEmail);
        } catch (DataNotFoundException e) {
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
        } catch (ValidationException e) {

            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
        }
        return lotId;
    }

    public List<ExamResultLot> list(){
        return examUploadService.list();
    }

    public ExamResultLot getById(String id){
        try {
            return examUploadService.getByID(id);
        } catch (DataNotFoundException e) {
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }
    }

    public void deleteById(String id){
        try {
            examUploadService.delete(id);
        } catch (DataNotFoundException e) {
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }
    }

    public List<ExamResult> listResults(String id){
        try {
            ObjectId objectId = new ObjectId(id);
            return examUploadService.getAllByExamId(objectId);
        } catch (DataNotFoundException e) {
            throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage()));
        }

    }
}