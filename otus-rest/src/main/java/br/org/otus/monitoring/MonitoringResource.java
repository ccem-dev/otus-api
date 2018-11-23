package br.org.otus.monitoring;

import br.org.otus.rest.Response;
import br.org.otus.security.Secured;
import org.ccem.otus.model.monitoring.ActivitiesProgressReport;
import org.ccem.otus.model.monitoring.ParticipantActivityReportDto;
import org.ccem.otus.model.survey.activity.configuration.ActivityInapplicability;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/monitoring")
public class MonitoringResource {

  @Inject
  private MonitoringFacade monitoringFacade;

  @GET
  @Secured
  @Path("/activities")
  @Produces(MediaType.APPLICATION_JSON)
  public String listActivities() {
    return new Response().buildSuccess(monitoringFacade.listActivities()).toJson();
  }

  @GET
  @Secured
  @Path("/activities/{acronym}")
  @Produces(MediaType.APPLICATION_JSON)
  public String get(@PathParam("acronym") String acronym) {
    return new Response().buildSuccess(monitoringFacade.get(acronym)).toJson();
  }

  @GET
  @Secured
  @Path("/centers")
  @Produces(MediaType.APPLICATION_JSON)
  public String getMonitoring() {
    return new Response().buildSuccess(monitoringFacade.getMonitoringCenters()).toJson();
  }

  @GET
  @Secured
  @Path("/activities/progress/")
  @Produces(MediaType.APPLICATION_JSON)
  public String getProjectStatus() {
    return new Response().buildSuccess(monitoringFacade.getActivitiesProgress()).toJson();
  }

  @GET
  @Secured
  @Path("/activities/progress/{center}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getActivitiesProgress(@PathParam("center") String center) {
    return new Response().buildSuccess(monitoringFacade.getActivitiesProgress(center)).toJson(ActivitiesProgressReport.getGsonBuilder());
  }

  @GET
  @Secured
  @Path("/activities/progress/participant/{rn}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getParticipantActivitiesProgress(@PathParam("rn") Long rn) {
    return new Response().buildSuccess(monitoringFacade.getParticipantActivitiesProgress(rn)).toJson(ParticipantActivityReportDto.getGsonBuilder());
  }

  @PUT
  @Secured
  @Path("/activities/progress/not-apply")
  @Consumes (MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String defineActivityInapplicability(String activityApplicability) {
    ActivityInapplicability activityInapplicability = ActivityInapplicability.deserialize(activityApplicability);
    monitoringFacade.setActivityApplicability(activityInapplicability);
    return new Response().buildSuccess().toJson();
  }

  @DELETE
  @Secured
  @Path("/activities/progress/not-apply/{rn}/{acronym}")
  @Produces(MediaType.APPLICATION_JSON)
  public String deleteActivityInapplicability(@PathParam("rn") Long rn,@PathParam("acronym") String acronym) {
    monitoringFacade.deleteActivityApplicability(rn, acronym);
    return new Response().buildSuccess().toJson();
  }


}
