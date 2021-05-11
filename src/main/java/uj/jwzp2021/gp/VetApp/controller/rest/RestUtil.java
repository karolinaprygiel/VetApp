package uj.jwzp2021.gp.VetApp.controller.rest;

import java.util.Collections;
import java.util.Map;

public class RestUtil {
  public static Map<String, String> response(String responseValue) {
    return Collections.singletonMap("response", responseValue);
  }

  public static Map<String, String> response(String key, String value) {
    return Collections.singletonMap(key, value);
  }
}
