package br.org.otus.api;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

public interface ExtractionService {

  byte[] createExtraction(Extractable extractionInterface) throws DataNotFoundException;

  byte[] getAttachmentsReport(String acronym, Integer version) throws DataNotFoundException;
}
