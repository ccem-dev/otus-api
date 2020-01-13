package br.org.otus.model.pendency;

import br.org.otus.utils.ObjectIdAdapter;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;

import java.util.Objects;

public class ActivityInfo {

  private ObjectId id;
  private String acronym;
  private int recruitmentNumber;

  public ObjectId getId() {
    return id;
  }

  public String getAcronym() {
    return acronym;
  }

  public int getRecruitmentNumber() {
    return recruitmentNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return Objects.equals(id, ((ActivityInfo) o).getId());
  }

  public static String serialize(ActivityInfo activityInfo) {
    return getGsonBuilder().create().toJson(activityInfo);
  }

  public static ActivityInfo deserialize(String activityInfoJson) {
    return ActivityInfo.getGsonBuilder().create().fromJson(activityInfoJson, ActivityInfo.class);
  }

  public static GsonBuilder getGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdAdapter());
    return builder;
  }

}