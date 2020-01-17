package br.org.otus.configuration.publish;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.ccem.otus.survey.form.SurveyForm;
import org.ccem.otus.survey.template.SurveyTemplate;

import br.org.otus.rest.Response;
import br.org.otus.security.user.AuthorizationHeaderReader;
import br.org.otus.security.user.Secured;
import br.org.otus.security.context.SecurityContext;
import br.org.otus.survey.api.SurveyFacade;

@Path("configuration/publish/template")
public class TemplateResource {

	@Inject
	private SurveyFacade surveyFacade;

	@Inject
	private SecurityContext securityContext;

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public String post(@Context HttpServletRequest request, String template) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		String userEmail = securityContext.getSession(AuthorizationHeaderReader.readToken(token)).getAuthenticationData().getUserEmail();
		SurveyForm publishedSurveyTemplate = surveyFacade.publishSurveyTemplate(SurveyTemplate.deserialize(template), userEmail);
		return new Response().setData(publishedSurveyTemplate).toJson();
	}
}
