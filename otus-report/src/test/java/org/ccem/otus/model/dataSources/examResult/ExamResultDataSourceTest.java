package org.ccem.otus.model.dataSources.examResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class ExamResultDataSourceTest {

	private static final Long RECRUITMENT_NUMBER = 1063154L;
	private static final String FIELD_CENTER_ACRONYM = "acronym";

	private static final String EXPECTED_RESULT = "Document{{$match=Document{{recruitmentNumber=1063154, examName=TRIGLICÉRIDES - SANGUE, realizationDate=2018-03-21T17:42:17.205Z, fieldCenter.acronym=RS}}}}";
	private static final String FILTER_EXAM_NAME = "examName";
	private static final CharSequence FILTER_REALIZATION_DATE = "realizationDate";
	private static final CharSequence FILTER_FIELD_CENTER_ACRONYM = "fieldCenter.acronym";

	private static final String VALUE_RS = "RS";

	private ExamResultDataSource examResultDataSource;
	private ExamResultDataSourceFilters filters;

	@Before
	public void setUp() {
		examResultDataSource = new ExamResultDataSource();
		filters = new ExamResultDataSourceFilters();
		ExamResultDataSourceFieldCenterFilter fieldCenterFilter = new ExamResultDataSourceFieldCenterFilter();
		Whitebox.setInternalState(fieldCenterFilter, FIELD_CENTER_ACRONYM, VALUE_RS);

		Whitebox.setInternalState(filters, "examName", "TRIGLICÉRIDES - SANGUE");
		Whitebox.setInternalState(filters, "realizationDate", "2018-03-21T17:42:17.205Z");
		Whitebox.setInternalState(filters, "fieldCenter", fieldCenterFilter);
	}

	@Test
	public void method_builtQuery_should_return_query() {
		Whitebox.setInternalState(examResultDataSource, "filters", filters);
		ArrayList<Document> query = examResultDataSource.builtQuery(RECRUITMENT_NUMBER);

		Assert.assertNotNull(query);
	}

	@Test
	public void method_builtQuery_should_return_query_expected() throws Exception {
		Whitebox.setInternalState(examResultDataSource, "filters", filters);
		ArrayList<Document> query = examResultDataSource.builtQuery(RECRUITMENT_NUMBER);

		assertEquals(EXPECTED_RESULT, query.get(0).toString());
	}

	@Test
	public void query_should_contains_filters() {
		Whitebox.setInternalState(examResultDataSource, "filters", filters);
		ArrayList<Document> query = examResultDataSource.builtQuery(RECRUITMENT_NUMBER);

		assertTrue(query.get(0).toJson().contains(FILTER_EXAM_NAME));
		assertTrue(query.get(0).toJson().contains(FILTER_REALIZATION_DATE));
		assertTrue(query.get(0).toJson().contains(FILTER_FIELD_CENTER_ACRONYM));
	}
}
