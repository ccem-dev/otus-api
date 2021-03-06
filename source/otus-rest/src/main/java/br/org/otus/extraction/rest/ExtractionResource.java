package br.org.otus.extraction.rest;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import br.org.otus.extraction.ExtractionFacade;
import br.org.otus.extraction.SecuredExtraction;
import br.org.otus.rest.Response;
import br.org.otus.security.AuthorizationHeaderReader;
import br.org.otus.security.user.Secured;
import br.org.otus.security.context.SecurityContext;
import br.org.otus.user.api.UserFacade;
import br.org.otus.user.dto.ManagementUserDto;

@Path("data-extraction")
public class ExtractionResource {

  @Inject
  private UserFacade userFacade;
  @Inject
  private ExtractionFacade extractionFacade;
  @Inject
  private SecurityContext securityContext;

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/laboratory/exams-values")
  public byte[] extractExamsValues() {
    return extractionFacade.createLaboratoryExamsValuesExtraction();
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/laboratory")
  public byte[] extractLaboratory() {
    return extractionFacade.createLaboratoryExtraction();
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/participant")
  public byte[] extractParticipant() {
    return extractionFacade.createParticipantExtraction();
  }

  @POST
  @Secured
  @Path("/enable")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String enableUsers(ManagementUserDto managementUserDto) {
    userFacade.enableExtraction(managementUserDto);
    return new Response().buildSuccess().toJson();
  }

  @POST
  @Secured
  @Path("/disable")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String disableUsers(ManagementUserDto managementUserDto) {
    userFacade.disableExtraction(managementUserDto);
    return new Response().buildSuccess().toJson();
  }

  @POST
  @Secured
  @Path("/enable-ips")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String enableIps(ManagementUserDto managementUserDto) {
    userFacade.updateExtractionIps(managementUserDto);
    return new Response().buildSuccess().toJson();
  }

  @GET
  @Secured
  @Path("/extraction-token")
  @Produces(MediaType.APPLICATION_JSON)
  public String getToken(@Context HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    String userEmail = securityContext.getSession(AuthorizationHeaderReader.readToken(token)).getAuthenticationData().getUserEmail();
    String extractionToken = userFacade.getExtractionToken(userEmail);
    return new Response().buildSuccess(extractionToken).toJson();
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/participant/participant-contact-attempts")
  public byte[] extractParticipantContactAttempts() {
    return extractionFacade.createParticipantContactAttemptsExtraction();
  }

}
