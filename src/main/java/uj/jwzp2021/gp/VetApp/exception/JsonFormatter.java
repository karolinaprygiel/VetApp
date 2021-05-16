package uj.jwzp2021.gp.VetApp.exception;

import java.util.Collections;
import java.util.Map;

public class JsonFormatter {
  public static Map<String, String> toResponseJson(String responseValue) {
    return Collections.singletonMap("response", responseValue);
  }

  public static Map<String, String> toResponseJson(String key, String value) {
    return Collections.singletonMap(key, value);
  }
}
