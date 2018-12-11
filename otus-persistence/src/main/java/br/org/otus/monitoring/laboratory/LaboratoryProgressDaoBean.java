package br.org.otus.monitoring.laboratory;

import br.org.otus.examUploader.persistence.ExamResultDao;
import br.org.otus.laboratory.participant.aliquot.persistence.AliquotDao;
import br.org.otus.monitoring.builder.LaboratoryProgressQueryBuilder;
import org.bson.Document;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.monitoring.laboratory.LaboratoryProgressDTO;
import org.ccem.otus.persistence.laboratory.LaboratoryProgressDao;

import javax.inject.Inject;

public class LaboratoryProgressDaoBean implements LaboratoryProgressDao {

    @Inject
    private ExamResultDao examResultDao;
    @Inject
    private AliquotDao aliquotDao;

    @Override
    public LaboratoryProgressDTO getOrphanExams() throws DataNotFoundException {
        LaboratoryProgressQueryBuilder laboratoryProgressQueryBuilder = new LaboratoryProgressQueryBuilder();
        Document first = examResultDao.aggregate(laboratoryProgressQueryBuilder.getOrphansQuery()).first();
        validateFirst(first);
        return LaboratoryProgressDTO.deserialize(first.toJson());
    }

    @Override
    public LaboratoryProgressDTO getQuantitativeByTypeOfAliquots() throws DataNotFoundException {
        LaboratoryProgressQueryBuilder laboratoryProgressQueryBuilder = new LaboratoryProgressQueryBuilder();
        Document first = aliquotDao.aggregate(laboratoryProgressQueryBuilder.getQuantitativeQuery()).first();
        validateFirst(first);
        return LaboratoryProgressDTO.deserialize(first.toJson());
    }

    @Override
    public LaboratoryProgressDTO getDataOfPendingResultsByAliquot() throws DataNotFoundException {
        LaboratoryProgressQueryBuilder laboratoryProgressQueryBuilder = new LaboratoryProgressQueryBuilder();
        Document first = aliquotDao.aggregate(laboratoryProgressQueryBuilder.getPendingResultsQuery()).first();
        validateFirst(first);
        return LaboratoryProgressDTO.deserialize(first.toJson());
    }

    @Override
    public LaboratoryProgressDTO getDataOfStorageByAliquot() throws DataNotFoundException {
        LaboratoryProgressQueryBuilder laboratoryProgressQueryBuilder = new LaboratoryProgressQueryBuilder();
        Document first = aliquotDao.aggregate(laboratoryProgressQueryBuilder.getStorageByAliquotQuery()).first();
        validateFirst(first);
        return LaboratoryProgressDTO.deserialize(first.toJson());
    }

    @Override
    public LaboratoryProgressDTO getDataOfResultsByExam() throws DataNotFoundException {
        LaboratoryProgressQueryBuilder laboratoryProgressQueryBuilder = new LaboratoryProgressQueryBuilder();
        Document first = examResultDao.aggregate(laboratoryProgressQueryBuilder.getResultsByExamQuery()).first();
        validateFirst(first);
        return LaboratoryProgressDTO.deserialize(first.toJson());
    }

    private void validateFirst(Document first) throws DataNotFoundException {
        if (first == null){
            throw new DataNotFoundException(new Throwable("There are no result"));
        }
    }
}
