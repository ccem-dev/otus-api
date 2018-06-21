package org.ccem.otus.service;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.FieldCenter;
import org.ccem.otus.model.monitoring.MonitoringCenter;
import org.ccem.otus.model.monitoring.MonitoringDataSource;
import org.ccem.otus.model.monitoring.MonitoringDataSourceResult;
import org.ccem.otus.participant.persistence.ParticipantDao;
import org.ccem.otus.persistence.FieldCenterDao;
import org.ccem.otus.persistence.MonitoringDao;

@Stateless
public class MonitoringServiceBean implements MonitoringService {

  @Inject
  private MonitoringDao monitoringDao;

  @Inject
  private FieldCenterDao fieldCenterDao;
  
  @Inject
  private ParticipantDao participantDao;

  @Override
  public ArrayList<MonitoringDataSourceResult> get(String acronym) throws ValidationException {
    MonitoringDataSource monitoringDataSource = new MonitoringDataSource();
    return monitoringDao.get(monitoringDataSource.buildQuery(acronym));
  }

  @Override
  public ArrayList<MonitoringCenter> getMonitoringCenter() throws ValidationException, DataNotFoundException {

    ArrayList<MonitoringCenter> results = new ArrayList<>();
    ArrayList<String> centers = fieldCenterDao.listAcronyms();
    for (String acronymCenter : centers) {
      FieldCenter fieldCenter = fieldCenterDao.fetchByAcronym(acronymCenter);
      Long goal = participantDao.getPartipantsActives(acronymCenter);
      results.add(monitoringDao.getMonitoringCenter(fieldCenter, goal));
    }
    return results;
  }
}
