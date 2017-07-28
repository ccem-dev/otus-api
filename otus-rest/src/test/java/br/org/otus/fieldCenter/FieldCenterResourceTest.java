package br.org.otus.fieldCenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.ccem.otus.model.FieldCenter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.otus.fieldCenter.api.FieldCenterFacade;
import br.org.otus.rest.Response;

@RunWith(PowerMockRunner.class)
public class FieldCenterResourceTest {	
	@InjectMocks
	FieldCenterResource fieldCenterResource;
	@Mock
	FieldCenterFacade fieldCenterFacade;
	
	private FieldCenter fieldCenter;
	private String responseExpected;
	private ArrayList<FieldCenter> fieldCenters;
	private String responseListExpected;
	
	@Before
	public void setUp(){
		fieldCenter = new FieldCenter();
		fieldCenter.setAcronym("SP");
		fieldCenter.setName("Sao Paulo");
		fieldCenter.setCode(6);		
		responseExpected = new Response().buildSuccess().toJson();
	}

	@Test
	public void method_create_should_return_responseJson_and_verify_fieldCenterFacadeCreateMethod() {
		assertEquals(responseExpected,fieldCenterResource.create(fieldCenter));
		verify(fieldCenterFacade).create(fieldCenter);
	}
	@Test
	public void method_list_should_return_resposeListJson() {
		fieldCenters = new ArrayList<FieldCenter>();
		fieldCenters.add(fieldCenter);
		responseListExpected = new Response().setData(fieldCenters).toJson();		
		when(fieldCenterFacade.list()).thenReturn(fieldCenters);
		assertEquals(responseListExpected, fieldCenterResource.list());		
	}
	@Test
	public void method_update_should_return_responseJson_and_verify_fieldCenterFacadeUpdateMethod() {
		assertEquals(responseExpected, fieldCenterResource.update(fieldCenter));
		verify(fieldCenterFacade).update(fieldCenter);		
	}
}
