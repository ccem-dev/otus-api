package br.org.otus.security;

import br.org.mongodb.MongoGenericDao;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;

import java.util.Date;

public class PasswordResetControlDaoBean extends MongoGenericDao<Document> implements PasswordResetControlDao {

  private static final String COLLECTION_NAME = "password_reset_control";

  public PasswordResetControlDaoBean() {
    super(COLLECTION_NAME, Document.class);
  }

  @Override
  public void registerToken(String token, String email) {
    Document insertion = new Document();
    insertion.append("token", token);
    insertion.append("email", email);
    insertion.append("creationDate", new Date());
    collection.insertOne(insertion);
  }

  @Override
  public void removeRegister(String token) {
    Document query = new Document();
    query.append("token", token);
    collection.deleteOne(query);
  }

  @Override
  public void removeAllRegisters(String email) {
    Document query = new Document();
    query.append("email", email);
    collection.deleteMany(query);
  }

  @Override
  public boolean tokenExists(String token) {
    Document query = new Document();
    query.append("token", token);
    FindIterable<Document> found = collection.find(query);
    return found.first() != null;
  }

  @Override
  public boolean hasRegister(String email) {
    Document query = new Document();
    query.append("email", email);
    FindIterable<Document> found = collection.find(query);
    return found.first() != null;
  }

  @Override
  public String getRequestEmail(String token) throws DataNotFoundException {
    Document query = new Document();
    query.append("token", token);
    FindIterable<Document> request = collection.find(query);
    Document first = request.first();
    if (first == null) {
      throw new DataNotFoundException("Invalid token");
    }
    return first.get("email").toString();
  }
}
