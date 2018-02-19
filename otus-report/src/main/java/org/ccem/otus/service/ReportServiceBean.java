package org.ccem.otus.service;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.model.*;
import org.ccem.otus.model.dataSources.ParticipantDataSource;
import org.ccem.otus.model.dataSources.ReportDataSource;
import org.ccem.otus.persistence.ParticipantDataSourceDao;
import org.ccem.otus.persistence.ReportDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ReportServiceBean implements ReportService {

    @Inject
    private ReportDao reportDao;

    @Inject
    private ParticipantDataSourceDao participantDataSourceDao;

    @Override
    public ReportTemplate findReport(RequestParameters requestParameters) throws DataNotFoundException{
        ReportTemplate report = reportDao.findReport(requestParameters.getReportId());
        for (ReportDataSource dataSource:report.getDataSources()) {
            if(dataSource instanceof ParticipantDataSource){
                dataSource.addResult(participantDataSourceDao.getResult(requestParameters.getRecruitmentNumber(),(ParticipantDataSource) dataSource));
            }
        }
        return report;
    }
}
