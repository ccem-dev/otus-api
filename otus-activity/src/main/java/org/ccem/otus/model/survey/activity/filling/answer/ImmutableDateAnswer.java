package org.ccem.otus.model.survey.activity.filling.answer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ccem.otus.model.survey.activity.filling.AnswerFill;
import org.ccem.otus.survey.template.utils.date.ImmutableDate;

public class ImmutableDateAnswer extends AnswerFill {

	private ImmutableDate value;

	public ImmutableDate getValue() {
		return value;
	}

	@Override
	public Map<String, Object> getAnswerExtract(String questionID) {
		Map<String, Object> extraction = new LinkedHashMap<String, Object>();
		if (this.value!= null) extraction.put(questionID, this.value.getValue());
		else extraction.put(questionID, ""); // TODO: 04/10/17 check if this cannot be solved using the ImmutableDate toString method
		return extraction;
	}

}
