package br.org.otus.gateway.request;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonPOSTUtility extends JsonWritingRequestUtility {

  public JsonPOSTUtility(URL requestURL, String body) throws IOException {
    super(RequestTypeOptions.POST, requestURL, APPLICATION_JSON_CONTENT_TYPE);
    request.write(body.getBytes(StandardCharsets.UTF_8));
  }

}