package de.tum.in.eist;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Helps with parsing JSON.
 */
public class JsonHelper {

  public static JsonObject parse(String json) {
    return new JsonParser().parse(json).getAsJsonObject();
  }

}
