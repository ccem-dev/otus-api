package org.ccem.otus.service.extraction;

import br.org.otus.api.Extractable;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.survey.activity.SurveyActivity;
import org.ccem.otus.service.DataSourceService;
import org.ccem.otus.service.DataSourceServiceBean;
import org.ccem.otus.service.extraction.factories.SurveyActivityExtractionHeadersFactory;
import org.ccem.otus.service.extraction.factories.SurveyActivityExtractionRecordsFactory;
import org.ccem.otus.survey.form.SurveyForm;

import java.util.*;

public class SurveyActivityExtraction implements Extractable {

	private List<SurveyActivity> surveyActivities;
	private SurveyForm surveyForm;
	private SurveyActivityExtractionHeadersFactory headersFactory;
	private SurveyActivityExtractionRecordsFactory recordsFactory;
	private DataSourceService dataSourceService;

	public SurveyActivityExtraction(SurveyForm surveyForm, List<SurveyActivity> surveyActivities, DataSourceService dataSourceService) {
		this.surveyActivities = surveyActivities;
		this.surveyForm = surveyForm;
		this.headersFactory = new SurveyActivityExtractionHeadersFactory(this.surveyForm);
		this.dataSourceService = dataSourceService;
	}

	@Override
	public LinkedHashSet<String> getHeaders() {
		return this.headersFactory.getHeaders();
	}

	@Override
	public List<List<Object>> getValues() throws DataNotFoundException {
		List<List<Object>> values = new ArrayList<>();

		for (SurveyActivity surveyActivity : this.surveyActivities) {
			this.recordsFactory = new SurveyActivityExtractionRecordsFactory(this.surveyForm, this.headersFactory.getHeaders(), dataSourceService);
			List<Object> resultInformation = new ArrayList<>();
			this.recordsFactory.getSurveyBasicInfo(surveyActivity);
			this.recordsFactory.getSurveyQuestionInfo(surveyActivity);
			resultInformation.addAll(new ArrayList<>(this.recordsFactory.getSurveyInformation().values()));
			values.add(resultInformation);			
		}
		return values;
	}

}
