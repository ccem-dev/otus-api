package br.org.otus.survey.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.survey.form.SurveyForm;
import org.ccem.otus.survey.form.SurveyFormType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.otus.survey.SurveyDao;
import br.org.otus.survey.dtos.UpdateSurveyFormTypeDto;

@PrepareForTest(SurveyForm.class)
@RunWith(PowerMockRunner.class)
public class SurveyServiceBeanTest {
	private static final String ACRONYM = "DIEC";
	private static final String ACRONYM_EMPTY = "";
	private static final String SURVEY_SERIALIZE = "{survey:'survey'}";
	private static final String ACRONYM_NULL = "";
	private static final Integer VERSION = 1;

	@InjectMocks
	private SurveyServiceBean service;
	@Mock
	private SurveyForm survey;
	@Mock
	private SurveyDao surveyDao;
	@Mock
	private SurveyValidorService surveyValidorService;
	@Mock
	private UpdateSurveyFormTypeDto updateSurveyFormTypeDtoInvalid;
	@Mock
	private UpdateSurveyFormTypeDto updateSurveyFormTypeDtoValid;

	@Before
	public void setup() {
		PowerMockito.mockStatic(SurveyForm.class);
		PowerMockito.when(SurveyForm.serialize(survey)).thenReturn(SURVEY_SERIALIZE);
		PowerMockito.when(updateSurveyFormTypeDtoInvalid.isValid()).thenReturn(false);
		PowerMockito.when(updateSurveyFormTypeDtoValid.isValid()).thenReturn(true);
		updateSurveyFormTypeDtoValid.acronym = ACRONYM;
		updateSurveyFormTypeDtoValid.newSurveyFormType = SurveyFormType.FORM_INTERVIEW;
		PowerMockito.when(surveyDao.updateSurveyFormType(updateSurveyFormTypeDtoValid.acronym,
				updateSurveyFormTypeDtoValid.newSurveyFormType.toString())).thenReturn(true);
		PowerMockito.when(surveyDao.deleteByAcronym(ACRONYM)).thenReturn(true);
	}

	@Test
	public void saveSurvey_shoud_call_method_validateSurvey() throws AlreadyExistException {
		service.saveSurvey(survey);
		Mockito.verify(surveyValidorService).validateSurvey(surveyDao, survey);
	}

	@Test
	public void saveSurvey_should_call_method_persist() throws AlreadyExistException {
		service.saveSurvey(survey);
		Mockito.verify(surveyDao).persist(SurveyForm.serialize(survey));
	}

	@Test
	public void saveSurvey_should_returns_same_survey() throws AlreadyExistException {
		assertEquals(survey, service.saveSurvey(survey));
	}

	@Test
	public void list_should_call_surveyDao_find() {
		service.list();
		Mockito.verify(surveyDao).find();
	}

	@Test
	public void findByAcronym_should_call_method_findByAcronym_by_surveyDao() {
		service.findByAcronym(ACRONYM);
		Mockito.verify(surveyDao).findByAcronym(ACRONYM);

	}
	
	@Test
	public void findByAcronymWithVersion_should_call_method_findByAcronym_by_surveyDao() throws DataNotFoundException {
		service.findByAcronymWithVersion(ACRONYM, VERSION);
		Mockito.verify(surveyDao).findByAcronymWithVersion(ACRONYM, VERSION);

	}

	@Test(expected = ValidationException.class)
	public void updateSurveyFormType_should_throw_exception_case_updateSurveyFormTypeDto_invalid()
			throws org.ccem.otus.exceptions.webservice.validation.ValidationException {
		service.updateSurveyFormType(updateSurveyFormTypeDtoInvalid);
	}

	@Test
	public void updateSurveyFormType_should_call_and_return_method_updateSurveyFormType_case_updateSurveyFormTypeDto_is_valid()
			throws ValidationException {
		assertTrue(service.updateSurveyFormType(updateSurveyFormTypeDtoValid));
	}

	@Test(expected = ValidationException.class)
	public void deleteByAcronym_should_throw_ValidationException_case_acronym_to_be_empty() throws ValidationException {
		service.deleteByAcronym(ACRONYM_EMPTY);
	}

	@Test(expected = ValidationException.class)
	public void deleteByAcronym_should_throw_ValidationException_case_acronym_to_be_null() throws ValidationException {
		service.deleteByAcronym(ACRONYM_NULL);
	}

	@Test
	public void deleteByAcronym_should_returns_positive_answer_case_acronym_not_be_null_or_empty()
			throws ValidationException {
		assertTrue(surveyDao.deleteByAcronym(ACRONYM));
	}

}
