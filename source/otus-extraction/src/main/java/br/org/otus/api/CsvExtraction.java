package br.org.otus.api;

import com.google.gson.GsonBuilder;
import org.bson.Document;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import java.util.List;
import java.util.zip.DataFormatException;

public class CsvExtraction implements Extractable {

  private static final String HEADER_KEY_NAME = "header";
  private static final String VALUES_KEY_NAME = "values";

  private List<String> header;
  private List<List<Object>> values;

  public CsvExtraction(String content) throws DataFormatException {
    try{
      Document doc = new GsonBuilder().create().fromJson(content, Document.class);
      this.header = (List<String>) doc.get(HEADER_KEY_NAME);
      this.values = (List<List<Object>>)doc.get(VALUES_KEY_NAME);
    }
    catch(Exception e){
      throw new DataFormatException("Invalid csv content: " + e.getMessage());
    }
  }

  @Override
  public List<String> getHeaders() {
    return header;
  }

  @Override
  public List<List<Object>> getValues() throws DataNotFoundException {
    return this.values;
  }
}
