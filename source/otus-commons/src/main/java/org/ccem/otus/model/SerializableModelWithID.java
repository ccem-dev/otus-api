package org.ccem.otus.model;

import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.ccem.otus.survey.template.utils.adapters.LocalDateTimeAdapter;
import org.ccem.otus.utils.LongAdapter;
import org.ccem.otus.utils.ObjectIdAdapter;
import org.ccem.otus.utils.ObjectIdToStringAdapter;

import java.time.LocalDateTime;

public abstract class SerializableModelWithID {

  public static String serialize(Object object) {
    return getGsonBuilder().create().toJson(object);
  }

  public String toJson(){
    return getGsonBuilder().create().toJson(this);
  }

  protected static Object deserialize(String json, Class clazz){
    return getGsonBuilder().create().fromJson(json, clazz);
  }
  protected static GsonBuilder getGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdAdapter());
    return builder;
  }

  public static GsonBuilder getFrontGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdToStringAdapter());
    return builder;
  }

  // Non static

  public <T> T deserializeNonStatic(String json){
    return (T)gsonBuilder().create().fromJson(json, this.getClass());
  }

  protected GsonBuilder gsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdAdapter());
    registerSpecificTypeAdapter(builder);
    return builder;
  }

  public GsonBuilder frontGsonBuilder() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ObjectId.class, new ObjectIdToStringAdapter());
    registerSpecificTypeAdapter(builder);
    return builder;
  }

  protected void registerSpecificTypeAdapter(GsonBuilder builder){
    // Override by child class
  }

  protected void registerGsonBuilderLongAdapter(GsonBuilder builder){
    builder.registerTypeAdapter(Long.class, new LongAdapter());
  }

  protected void registerGsonBuilderLocalDateTimeAdapter(GsonBuilder builder){
    builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
  }

}
