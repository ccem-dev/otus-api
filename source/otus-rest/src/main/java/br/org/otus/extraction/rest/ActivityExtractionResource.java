package br.org.otus.extraction.rest;

import br.org.otus.extraction.ActivityExtractionFacade;
import br.org.otus.extraction.SecuredExtraction;
import br.org.otus.rest.Response;
import com.google.gson.internal.LinkedTreeMap;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("data-extraction/activity")
public class ActivityExtractionResource {

  @Inject
  private ActivityExtractionFacade activityExtractionFacade;


  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/{acronym}/versions")
  public String listSurveyVersions(@PathParam("acronym") String acronym) {
    return new Response().buildSuccess(activityExtractionFacade.listSurveyVersions(acronym.toUpperCase())).toJson();
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/{acronym}/{version}/attachments")
  public byte[] extractAnnexesReport(@PathParam("acronym") String acronym, @PathParam("version") Integer version) {
    return activityExtractionFacade.createAttachmentsReportExtraction(acronym.toUpperCase(), version);
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/progress/{center}")
  public byte[] extractActivitiesProgress(@PathParam("center") String center) {
    return activityExtractionFacade.createActivityProgressExtraction(center);
  }

  @POST
  @SecuredExtraction
  @Path("/attachments")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public javax.ws.rs.core.Response fetch(ArrayList<String> oids) {
    javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.ok(activityExtractionFacade.downloadFiles(oids));
    builder.header("Content-Disposition", "attachment; filename=" + "file-extraction.zip");
    return builder.build();
  }


  @PUT
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public String createOrUpdateActivityExtraction(@PathParam("id") String activityId) {
    activityExtractionFacade.createOrUpdateActivityExtraction(activityId);
    return new Response().buildSuccess().toJson();
  }

  @DELETE
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public String deleteActivityExtraction(@PathParam("id") String activityId) {
    activityExtractionFacade.deleteActivityExtraction(activityId);
    return new Response().buildSuccess().toJson();
  }

  @PUT
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/sync/{acronym}/{version}")
  public String syncSurveyExtractions(@PathParam("acronym") String acronym, @PathParam("version") Integer version) {
    activityExtractionFacade.synchronizeSurveyActivityExtractions(acronym, version);
    return new Response().buildSuccess().toJson();
  }

  @PUT
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/sync-force/{acronym}/{version}")
  public String forceSyncSurveyExtractions(@PathParam("acronym") String acronym, @PathParam("version") Integer version) {
    activityExtractionFacade.forceSynchronizeSurveyActivityExtractions(acronym, version);
    return new Response().buildSuccess().toJson();
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/{acronym}/{version}")
  public byte[] getSurveyActivitiesExtractionAsCsv(@PathParam("acronym") String acronym, @PathParam("version") Integer version) {
    return activityExtractionFacade.getSurveyActivitiesExtractionAsCsv(acronym, version);
  }

  @GET
  @SecuredExtraction
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/json/{acronym}/{version}")
  public String getSurveyActivitiesExtractionAsJson(@PathParam("acronym") String acronym, @PathParam("version") Integer version) {
    ArrayList<LinkedTreeMap> json =  activityExtractionFacade.getSurveyActivitiesExtractionAsJson(acronym, version);
    return new Response().buildSuccess(json).toJson();
  }

  @POST
  @SecuredExtraction
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/rscript/csv")
  public byte[] getRscriptSurveyExtractionAsCsv(String rscriptSurveyExtractionJson) {
    return activityExtractionFacade.getRscriptSurveyExtractionAsCsv(rscriptSurveyExtractionJson);
  }

  @POST
  @SecuredExtraction
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/rscript/json")
  public String getRscriptSurveyExtractionAsJson(String rscriptSurveyExtractionJson) {
    return activityExtractionFacade.getRscriptSurveyExtractionAsJson(rscriptSurveyExtractionJson);
  }
}
