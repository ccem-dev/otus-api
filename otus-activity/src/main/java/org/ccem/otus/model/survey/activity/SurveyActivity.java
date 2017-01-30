package org.ccem.otus.model.survey.activity;

import java.util.List;

import org.bson.types.ObjectId;
import org.ccem.otus.model.Participant;
import org.ccem.otus.model.survey.activity.filling.AnswerFill;
import org.ccem.otus.model.survey.activity.filling.FillContainer;
import org.ccem.otus.model.survey.activity.interview.Interview;
import org.ccem.otus.model.survey.activity.mode.ActivityMode;
import org.ccem.otus.model.survey.activity.status.ActivityStatus;
import org.ccem.otus.survey.form.SurveyForm;
import org.ccem.otus.utils.AnswerAdapter;
import org.ccem.otus.utils.ObjectIdAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class SurveyActivity {

	private String objectType;
	@SerializedName("_id")
	private ObjectId activityID;
	private SurveyForm surveyForm;
	private ActivityMode mode;
	private Participant participantData;
	private List<Interview> interviews;
	private FillContainer fillContainer;
	private List<ActivityStatus> statusHistory;
	private Boolean isDiscarded;

	public SurveyActivity() {
		this.isDiscarded = Boolean.FALSE;
	}

	public void setIsDiscarded(Boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}

	public Boolean isDiscarded() {
		return isDiscarded;
	}

	public ObjectId getActivityID() {
		return activityID;
	}

	public String getObjectType() {
		return objectType;
	}

	public SurveyForm getSurveyForm() {
		return surveyForm;
	}

	public ActivityMode getMode() {
		return mode;
	}

	public List<Interview> getInterviews() {
		return interviews;
	}

	public FillContainer getFillContainer() {
		return fillContainer;
	}

	public List<ActivityStatus> getStatusHistory() {
		return statusHistory;
	}

	public Participant getParticipantData() {
		return participantData;
	}

	public static String serialize(SurveyActivity surveyActivity) {
		return getGsonBuilder().create().toJson(surveyActivity);
	}

	public static SurveyActivity deserialize(String surveyActivity) {
		return getGsonBuilder().create().fromJson(surveyActivity, SurveyActivity.class);
	}

	/**
	 * @return a GsonBuilder instance with SurveyItemAdapter, DateAdapter and
	 *         OptionsAdapter, AnswerAdapter, ObjectIdAdapter registered. Also
	 *         disableHtmlEscaping and serializeNulls options.
	 */
	public static GsonBuilder getGsonBuilder() {
		GsonBuilder builder = SurveyForm.getGsonBuilder();

		builder.registerTypeAdapter(AnswerFill.class, new AnswerAdapter());
		builder.registerTypeAdapter(ObjectId.class, new ObjectIdAdapter());
		builder.serializeNulls();

		return builder;
	}

}
