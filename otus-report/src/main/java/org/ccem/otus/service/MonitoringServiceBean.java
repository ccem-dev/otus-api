package org.ccem.otus.service;

import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.monitoring.MonitoringDataSourceResult;
import org.ccem.otus.persistence.MonitoringDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class MonitoringServiceBean implements MonitoringService{

    @Inject
    private MonitoringDao monitoringDao;

    @Override
    public List<MonitoringDataSourceResult> list() throws ValidationException {
            return monitoringDao.getAll();
    }
}
