package org.ccem.otus.service;

import com.google.gson.JsonArray;
import org.ccem.otus.exceptions.webservice.common.AlreadyExistException;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;
import org.ccem.otus.model.DataSource;
import org.ccem.otus.model.DataSourceElement;
import org.ccem.otus.persistence.DataSourceDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class DataSourceServiceBean implements DataSourceService {

	@Inject
	private DataSourceDao dataSourceDao;

	public DataSourceServiceBean (){};

	@Override
	public void create(DataSource dataSource, JsonArray duplicatedElements) throws AlreadyExistException, ValidationException {
		if (duplicatedElements.size() > 0){
			throw new ValidationException(new Throwable("There are duplicated elements in datasource {" + duplicatedElements + "}"));
		}else {
			dataSourceDao.persist(dataSource);
		}
	}

	@Override
	public void update(DataSource dataSource, JsonArray duplicatedElements) throws ValidationException, DataNotFoundException {
		DataSource dataSourcePersisted = dataSourceDao.findByID(dataSource.getId());

		if(dataSource.getDataAsSet().containsAll(dataSourcePersisted.getDataAsSet())){
			if (duplicatedElements.size() == 0){
				dataSourceDao.update(dataSource);
			} else {
				throw new ValidationException(new Throwable("There are duplicated elements in datasource {" + duplicatedElements + "}"));
			}
		} else {
			throw new ValidationException(new Throwable("There are missing elements in datasource {" + dataSource.getId() + "}"));
		}
	}

	@Override
	public List<DataSource> list() {
		return dataSourceDao.find();
	}

	@Override
	public DataSource getByID(String id) throws DataNotFoundException {
		return dataSourceDao.findByID(id);
	}

	@Override
	public DataSourceElement getElementDataSource(String value) {
		return dataSourceDao.getElementDataSource(value);
	}

}
