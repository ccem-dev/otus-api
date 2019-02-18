package br.org.otus.fileuploader.api;

import br.org.mongodb.gridfs.FileDownload;
import br.org.mongodb.gridfs.FileStoreBucket;
import br.org.otus.response.builders.ResponseBuild;
import br.org.otus.response.exception.HttpResponseException;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileDownloadService {

  @Inject
  private FileStoreBucket fileStoreBucket;

  public Response list(ArrayList<String> oids) {

    List<ObjectId> objectIds;
    try {
      objectIds = objectIdConverter(oids);
    } catch (ValidationException e) {
      throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
    }

    try {
      List<FileDownload> files = fileStoreBucket.downloadMultiple(objectIds);
      ZipBuilder.Zip zip = ZipBuilder.create(files);
      return zip.buildResponse();
    } catch (DataNotFoundException e) {
      throw new HttpResponseException(ResponseBuild.Security.Validation.build(e.getCause().getMessage(), e.getData()));
    }
  }

  private List<ObjectId> objectIdConverter(List<String> oids) throws ValidationException {

    ArrayList<String> errors = new ArrayList<>();

    List<ObjectId> objectIds = oids.stream().map(s -> {
      try {
        return new ObjectId(s);
      } catch (IllegalArgumentException e) {
        errors.add(s);
        return null;
      }
    }).collect(Collectors.toList());

    if(errors.size() > 0) {
      throw new ValidationException(new Throwable("Invalid Id"), errors);
    }

    return objectIds;
  }

}
