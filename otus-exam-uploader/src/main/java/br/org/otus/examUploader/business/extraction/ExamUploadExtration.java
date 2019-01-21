package br.org.otus.examUploader.business.extraction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import br.org.otus.api.Extractable;
import br.org.otus.examUploader.business.extraction.factories.ExamUploadExtractionHeadersFactory;
import br.org.otus.examUploader.business.extraction.factories.ExamUploadExtractionRecordsFactory;

public class ExamUploadExtration implements Extractable {

  private ExamUploadExtractionHeadersFactory headersFactory;
  private ExamUploadExtractionRecordsFactory recordsFactory;

  public ExamUploadExtration(LinkedHashSet<String> resultHeaders) {
    this.headersFactory = new ExamUploadExtractionHeadersFactory(resultHeaders);
  }

  @Override
  public LinkedHashSet<String> getHeaders() {
    return this.headersFactory.getHeaders();
  }

  @Override
  public List<List<Object>> getValues() throws DataNotFoundException {
    List<List<Object>> values = new ArrayList<>();

    return values;
  }

}
