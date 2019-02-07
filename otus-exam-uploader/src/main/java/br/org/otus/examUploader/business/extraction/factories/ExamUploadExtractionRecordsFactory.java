package br.org.otus.examUploader.business.extraction.factories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.org.otus.examUploader.business.extraction.model.ParticipantExamUploadResultExtraction;

public class ExamUploadExtractionRecordsFactory {

  private LinkedList<ParticipantExamUploadResultExtraction> inputRecords;
  private List<List<Object>> outputRecords;

  public ExamUploadExtractionRecordsFactory(LinkedList<ParticipantExamUploadResultExtraction> records) {
    this.inputRecords = records;
    this.outputRecords = new LinkedList<>();
  }

  public List<List<Object>> getRecords() {
    return this.outputRecords;
  }

  public void buildResultInformation() {
    inputRecords.forEach(result -> {
      this.outputRecords.add(new ArrayList<>(this.createRecordsAnswers(result)));
    });
  }

  private List<String> createRecordsAnswers(ParticipantExamUploadResultExtraction result) {
    List<String> answers = new LinkedList<String>();
    answers.add(result.getRecruitmentNumber().toString());
    answers.add(result.getAliquotCode());
    answers.add(result.getResultName());
    answers.add(result.getValue());
    answers.add(result.getReleaseDate());
    String observations = "";
    result.getObservations().forEach(observation -> {
      if (observations.isEmpty()) {
        observations.concat(observation.getValue());
      } else {
        observations.concat(", " + observation.getValue());
      }
    });
    answers.add(observations.toString());

    return answers;
  }

}
