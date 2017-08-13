package br.org.otus.laboratory.project.transportation;

import br.org.otus.laboratory.participant.ParticipantLaboratory;
import br.org.otus.laboratory.participant.aliquot.Aliquot;
import br.org.otus.laboratory.project.ImmutableDate;
import br.org.otus.laboratory.project.ImmutableDateAdapter;
import br.org.otus.laboratory.project.LocalDateTimeAdapter;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.List;

public class TransportationLot {

	private String objectType;
	private String code;
	private List<Aliquot> aliquotList;
	private LocalDateTime shipmentDate;
	private LocalDateTime processingDate;
	private String operator;

	public TransportationLot() {
		// TODO: 10/08/17 checkThis
		objectType = "TransportationLot";
	}

	public String getObjectType() {
		return objectType;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public List<Aliquot> getAliquotList() {
		return aliquotList;
	}

	public LocalDateTime getShipmentDate() {
		return shipmentDate;
	}

	public LocalDateTime getProcessingDate() {
		return processingDate;
	}

	public String getOperator() {
		return operator;
	}

	public static String serialize(TransportationLot transportationLot) {
		Gson builder = getGsonBuilder();
		return builder.toJson(transportationLot);
	}

	public static TransportationLot deserialize(String transportationLot) {
		return getGsonBuilder().fromJson(transportationLot, TransportationLot.class);
	}
	
	public static Gson getGsonBuilder() {
		//TODO:  test
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ImmutableDate.class, new ImmutableDateAdapter());
		builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
		builder.serializeNulls();
		
		return builder.create();
	}

}
