package br.org.mongodb;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class MongoGenericDao {

	@Inject
	protected MongoDatabase db;
	private String collectionName;
	protected MongoCollection<Document> collection;

	@PostConstruct
	private void setUp() {
		collection = db.getCollection(collectionName);
	}

	public MongoGenericDao(String collectionName) {
		this.collectionName = collectionName;
	}

	public void persist(String json) {
		collection.insertOne(Document.parse(json));
		
	}
	
	public FindIterable<Document> list() {
		return collection.find();
	}

	public long count() {
		return collection.count();
	}

}
