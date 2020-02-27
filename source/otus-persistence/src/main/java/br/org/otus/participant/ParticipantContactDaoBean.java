package br.org.otus.participant;

import br.org.mongodb.MongoGenericDao;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.participant.model.participant_contact.ParticipantContact;
import org.ccem.otus.participant.model.participant_contact.ParticipantContactItem;
import org.ccem.otus.participant.persistence.ParticipantContactDao;
import org.ccem.otus.participant.persistence.dto.ParticipantContactDto;
import org.ccem.otus.participant.persistence.dto.ParticipantContactTypeOptions;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class ParticipantContactDaoBean extends MongoGenericDao<Document> implements ParticipantContactDao {

  private static final String COLLECTION_NAME = "participant_contact";
  private static final String RECRUITMENT_NUMBER_FIELD_NAME = "recruitmentNumber";

  private static final String MAIN_FIELD_PREFIX = "main";
  private static final String SECONDARY_FIELD_PREFIX = "other";
  private static final String EMAIL_FIELD_SUFFIX = "Email";
  private static final String ADDRESS_FIELD_SUFFIX = "Address";
  private static final String PHONE_NUMBER_FIELD_SUFFIX = "PhoneNumber";

  public ParticipantContactDaoBean(){
    super(COLLECTION_NAME, Document.class);
  }

  @Override
  public ObjectId create(ParticipantContact participantContact) {
    Document parsed = Document.parse(ParticipantContact.serialize(participantContact));
    collection.insertOne(parsed);
    return parsed.getObjectId(ID_FIELD_NAME);
  }

  @Override
  public void update(ParticipantContact participantContact) throws DataNotFoundException {
    UpdateResult update = collection.updateOne(
      eq(ID_FIELD_NAME, participantContact.getObjectId()),
      Document.parse(ParticipantContact.serialize(participantContact))
    );
    if(update.getMatchedCount() == 0){
      throw new DataNotFoundException("Participant contact with id { " + participantContact.getObjectId().toString() + " } was not found");
    }
  }

  @Override
  public void updateMainContact(ParticipantContactDto participantContactDto) throws DataNotFoundException {
    String fieldToUpdate = extractMainFieldNameFromDtoType(participantContactDto.getType());
    String newValue = ParticipantContactItem.serialize(participantContactDto.getNewParticipantContactItemValue());
    UpdateResult updateResult = collection.updateOne(
      eq(ID_FIELD_NAME, participantContactDto.getObjectId()),
      new Document("$set", new Document(fieldToUpdate, newValue))
    );
    if(updateResult.getMatchedCount() == 0){
      throw new DataNotFoundException("Participant contact with id { " + participantContactDto.getIdStr() + " } was not found");
    }
  }

  @Override
  public void addSecondaryContact(ParticipantContactDto participantContactDto) throws DataNotFoundException {

  }

  @Override
  public void updateSecondaryContact(ParticipantContactDto participantContactDto) throws DataNotFoundException {

  }

  @Override
  public void swapMainContactWithSecondary(ParticipantContactDto participantContactDto) throws DataNotFoundException {

  }

  @Override
  public void delete(ObjectId participantContactOID) throws DataNotFoundException {
    DeleteResult deleteResult = collection.deleteOne(eq(ID_FIELD_NAME, participantContactOID));
    if(deleteResult.getDeletedCount() == 0){
      throw new DataNotFoundException("Participant contact with id { " + participantContactOID.toString() + " } was not found");
    }
  }

  @Override
  public void deleteSecondaryContact(ParticipantContactDto participantContactDto) throws DataNotFoundException {

  }

  @Override
  public ParticipantContact get(ObjectId participantContactOID) throws DataNotFoundException {
    Document result = collection.find(eq(ID_FIELD_NAME, participantContactOID)).first();
    try{
      return ParticipantContact.deserialize(result.toJson());
    }
    catch (NullPointerException e){
      throw new DataNotFoundException("No participant contact found for OID {" + participantContactOID.toString() + "}");
    }
  }

  @Override
  public ParticipantContact getByRecruitmentNumber(Long recruitmentNumber) throws DataNotFoundException {
    Document result = collection.find(eq(RECRUITMENT_NUMBER_FIELD_NAME, recruitmentNumber)).first();
    try{
      return ParticipantContact.deserialize(result.toJson());
    }
    catch (NullPointerException e){
      throw new DataNotFoundException("No participant contact found with recruitment number {" + recruitmentNumber.toString() + "}");
    }
  }


  private String extractMainFieldNameFromDtoType(String dtoType){
    return MAIN_FIELD_PREFIX + getFieldNameSuffixFromDtoType(dtoType);
  }

  private String extractSecondaryFieldNameFromDtoType(String dtoType){
    String fieldNameSuffix = getFieldNameSuffixFromDtoType(dtoType);
    return SECONDARY_FIELD_PREFIX + fieldNameSuffix + (fieldNameSuffix.equals(ADDRESS_FIELD_SUFFIX) ? "es" : "s");
  }

  private String getFieldNameSuffixFromDtoType(String dtoType){
    HashMap<String, String> map = new HashMap<String, String>(){
      {
        put(ParticipantContactTypeOptions.EMAIL.getValue(), EMAIL_FIELD_SUFFIX);
        put(ParticipantContactTypeOptions.ADDRESS.getValue(), ADDRESS_FIELD_SUFFIX);
        put(ParticipantContactTypeOptions.PHONE.getValue(), PHONE_NUMBER_FIELD_SUFFIX);
      }
    };
    return map.get(dtoType);
  }

}
