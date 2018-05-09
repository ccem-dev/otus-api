package org.ccem.otus.persistence;

import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.survey.form.SurveyForm;

import java.util.List;
import java.util.Set;

public interface SurveyDao {
    List<SurveyForm> findUndiscarded();

    List<SurveyForm> findByAcronym(String acronym);

    List<SurveyForm> findByCustomId(Set<String> ids, String surveyAcronym);

    ObjectId persist(SurveyForm survey);

    boolean updateLastVersionSurveyType(String acronym, String surveyFormType);

    boolean deleteLastVersionByAcronym(String acronym) throws DataNotFoundException;

    boolean discardSurvey(ObjectId id) throws DataNotFoundException;

    SurveyForm getLastVersionByAcronym(String acronym) throws DataNotFoundException;

    List<Integer> getSurveyVersions(String acronym);
}
