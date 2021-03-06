package org.ccem.otus.participant.model.noteAboutParticipant;

import org.bson.types.ObjectId;
import org.ccem.otus.model.SerializableModelWithID;

public class NoteAboutParticipant extends SerializableModelWithID {

  private ObjectId _id;
  private Long recruitmentNumber;
  private String creationDate;
  private String lastUpdate;
  private Boolean edited;
  private Boolean starred;
  private String comment;
  private ObjectId userId;

  public NoteAboutParticipant() {
    edited = false;
    starred = false;
  }

  public ObjectId getId() {
    return _id;
  }

  public Long getRecruitmentNumber() {
    return recruitmentNumber;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public Boolean getEdited() {
    return edited;
  }

  public Boolean getStarred() { return starred; }

  public String getComment() {
    return comment;
  }

  public ObjectId getUserId() {
    return userId;
  }

  public void setId(ObjectId _id) {
    this._id = _id;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public void setEdited(Boolean edited) {
    this.edited = edited;
  }

  public void setStarred(Boolean starred) {
    this.starred = starred;
  }

  public void setUserId(ObjectId userId) {
    this.userId = userId;
  }

}
