package br.org.otus.rest;

import org.ccem.otus.model.survey.activity.SurveyActivity;

import com.google.gson.Gson;

public class Response {

	private Object data;

	public Object getData() {
		return data;
	}

	public Response setData(Object data) {
		this.data = data;
		return this;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String toSurveyJson() {
		return SurveyActivity.getGsonBuilder().create().toJson(this);
	}

	public Response buildSuccess(Object data) {
		this.data = data;
		return this;
	}

	public Response buildSuccess() {
		this.data = Boolean.TRUE;
		return this;
	}

}
