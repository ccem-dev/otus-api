package persistence;

import model.ProjectConfiguration;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

public interface ProjectConfigurationDao {

    void enableParticipantRegistration(boolean permission) throws DataNotFoundException;

    ProjectConfiguration getProjectConfiguration() throws DataNotFoundException;
}