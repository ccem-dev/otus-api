package br.org.otus.examUploader;

import br.org.otus.examUploader.utils.ObjectIdAdapter;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.ccem.otus.survey.template.utils.adapters.ImmutableDateAdapter;
import org.ccem.otus.survey.template.utils.date.ImmutableDate;

import java.util.List;

public class Exam {

  private ObjectId _id;
  private ObjectId examSendingLotId;
  private String objectType;
  private String name;
  private List<ExamResult> examResults;
  private List<Observation> observations;

  public ObjectId getId() {
    return _id;
  }

  public void setExamSendingLotId(ObjectId examLotId) {
    this.examSendingLotId = examLotId;
  }

  public List<ExamResult> getExamResults() {
    return examResults;
  }

  public static String serialize(Exam exam) {
    return getGsonBuilder().create().toJson(exam);
  }

  public static Exam deserialize(String examResultLotJson) {
    return Exam.getGsonBuilder().create().fromJson(examResultLotJson, Exam.class);
  }

  private static GsonBuilder getGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdAdapter());
    builder.registerTypeAdapter(ImmutableDate.class, new ImmutableDateAdapter());

    return builder;
  }

}
