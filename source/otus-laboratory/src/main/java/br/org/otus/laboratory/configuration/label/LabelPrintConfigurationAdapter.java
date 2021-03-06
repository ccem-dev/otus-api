package br.org.otus.laboratory.configuration.label;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class LabelPrintConfigurationAdapter implements JsonDeserializer<LabelPrintConfiguration>, JsonSerializer<LabelPrintConfiguration> {

  @Override
  public JsonElement serialize(LabelPrintConfiguration printConfiguration, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    Set<Entry<String, List<LabelReference>>> orderSet = printConfiguration.getOrders().entrySet();

    for (Entry<String, List<LabelReference>> labelReferenceEntry : orderSet) {
      JsonArray orderArray = new JsonArray();

      labelReferenceEntry.getValue().forEach(new Consumer<LabelReference>() {
        @Override
        public void accept(LabelReference labelReference) {
          orderArray.add(context.serialize(labelReference, LabelReference.class));
        }
      });

      jsonObject.add(labelReferenceEntry.getKey(), orderArray);
    }

    return jsonObject;
  }

  @Override
  public LabelPrintConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Map<String, List<LabelReference>> orders = new HashMap<>();
    Set<Entry<String, JsonElement>> serializedOrders = json.getAsJsonObject().entrySet();

    for (Entry<String, JsonElement> serializedOrder : serializedOrders) {
      String orderName = serializedOrder.getKey();
      List<LabelReference> references = new ArrayList<>();

      JsonArray labelReferenceList = serializedOrder.getValue().getAsJsonArray();

      for (JsonElement serializedLabelReference : labelReferenceList) {
        JsonObject labelReferenceJson = serializedLabelReference.getAsJsonObject();
        String groupName = labelReferenceJson.get("groupName").getAsString();
        String type = labelReferenceJson.get("type").getAsString();
        String moment = labelReferenceJson.get("moment").getAsString();

        LabelReference labelReference = new LabelReference(groupName, type, moment);
        references.add(labelReference);
      }

      orders.put(orderName, references);
    }

    return new LabelPrintConfiguration(orders);
  }

}
