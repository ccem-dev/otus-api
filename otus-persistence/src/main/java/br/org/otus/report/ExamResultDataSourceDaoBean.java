package br.org.otus.report;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.bson.Document;
import org.ccem.otus.model.dataSources.exam.ExamDataSourceResult;
import org.ccem.otus.model.dataSources.exam.ExamResultDataSource;
import org.ccem.otus.model.dataSources.exam.ExamSendingLotDataSourceResult;
import org.ccem.otus.persistence.ExamResultDataSourceDao;
import org.json.JSONObject;

import com.mongodb.client.AggregateIterable;

import br.org.mongodb.MongoGenericDao;

public class ExamResultDataSourceDaoBean extends MongoGenericDao<Document> implements ExamResultDataSourceDao {

	private static final String COLLECTION_NAME = "exam_result";
	private List<ExamDataSourceResult> result;

	@Inject
	private ExamSendingLotDataSourceDaoBean examSendingLotDataSource;

	public ExamResultDataSourceDaoBean() {
		super(COLLECTION_NAME, Document.class);
	}

	@Override
	public List<ExamDataSourceResult> getResult(Long recruitmentNumber, ExamResultDataSource examDataSource) {
		this.result = new ArrayList<>();
		ExamSendingLotDataSourceResult examSendingLot = null;

		ArrayList<Document> queryToLot = examDataSource.builtQuery(recruitmentNumber);
		AggregateIterable<?> outputLot = examSendingLotDataSource.getCollection().aggregate(queryToLot);

		for (Object anOutput : outputLot) {
			Document next = (Document) anOutput;
			examSendingLot = ExamSendingLotDataSourceResult.deserialize(next.toJson());
		}

		if (examSendingLot != null) {
			ArrayList<Document> queryToResults = examDataSource.builtQueryToExamResults(examSendingLot.getObjectId());
			AggregateIterable<?> outputResults = collection.aggregate(queryToResults);

			for (Object anOutput : outputResults) {
				Document next = (Document) anOutput;
				this.result.add(ExamDataSourceResult.deserialize(new JSONObject(next).toString()));
			}
		}

		return this.result;
	}

}
