package br.org.otus.communication;

import com.google.gson.GsonBuilder;
import org.ccem.otus.exceptions.Dto;

public class IssueMessageDTO implements Dto {
  public String _id;
  private String objectType;
  private String sender;
  private String group;
  private String title;
  private String text;
  private String creationDate;
  private String status;

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSender() {
    return sender;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public static String serialize(IssueMessageDTO issueMessageDTO) {
    return IssueMessageDTO.getGsonBuilder().create().toJson(issueMessageDTO);
  }

  public static IssueMessageDTO deserialize(String issueMessageDTO) {
    return IssueMessageDTO.getGsonBuilder().create().fromJson(issueMessageDTO, IssueMessageDTO.class);
  }

  public static GsonBuilder getGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    return builder;
  }

  @Override
  public Boolean isValid() {
    return !objectType.isEmpty();
  }
}
