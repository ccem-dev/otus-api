package br.org.otus.examUploader;

import br.org.otus.examUploader.utils.LabObjectIdAdapter;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.ccem.otus.model.FieldCenter;

public class ExamResult {

    private ObjectId _id;
    private ObjectId examId;

    private String aliquotCode;
    private String examName;
    private String resultName;
    private double value;
    private String releaseDate;

    //TODO 09/01/18: split this into metadata?
    private String observations;

    private FieldCenter fieldCenter;
    private Long recruitmentNumber; //TODO 09/01/18: is this necessary?

    public void setExamId(ObjectId examId) {
        this.examId = examId;
    }

    public void setFieldCenter(FieldCenter fieldCenter) {
        this.fieldCenter = fieldCenter;
    }

    public String getAliquotCode() {
        return aliquotCode;
    }

    public Long getRecruitmentNumber() {
        return recruitmentNumber;
    }

    public void setRecruitmentNumber(Long recruitmentNumber) {
        this.recruitmentNumber = recruitmentNumber;
    }

    public static String serialize(ExamResult examResult) {
        return getGsonBuilder().create().toJson(examResult);
    }

    public static ExamResult deserialize(String examResultJson) {
        return ExamResult.getGsonBuilder().create().fromJson(examResultJson, ExamResult.class);
    }

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ObjectId.class, new LabObjectIdAdapter());
        return builder;
    }
}
