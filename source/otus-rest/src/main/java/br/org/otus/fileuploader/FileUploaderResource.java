package br.org.otus.fileuploader;

import br.org.otus.fileuploader.api.FileUploaderFacade;
import br.org.otus.security.user.Secured;
import org.ccem.otus.model.FileUploaderPOJO;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;

@Path("/upload")
public class FileUploaderResource {

  @Inject
  private FileUploaderFacade facade;

  @POST
  @Secured
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public String post(@Context HttpServletRequest request, @MultipartForm FileUploaderPOJO form) throws IOException {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    return new br.org.otus.rest.Response().buildSuccess(facade.upload(form, token)).toJson();
  }

  @GET
  @Secured
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getById(@PathParam("id") String oid) {
    ResponseBuilder builder = Response.ok(facade.getById(oid));
    builder.header("Content-Disposition", "attachment; filename=" + "anything");
    return builder.build();
  }

  @DELETE
  @Secured
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response delete(@PathParam("id") String oid) {
    facade.delete(oid);
    return Response.ok().build();
  }

}
