package org.example.hydrator.plugin.utils;

import com.sun.tools.javac.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtils {

  public static String bytesToString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for (byte b : bytes) {
      sb.append(String.format("0x%02X ", b));
    }
    sb.append("]");
    return sb.toString();
  }

  public static Map<String, String> createKeyValuePairs(String parameterString) {
    HashMap<String, String> hashMap = new HashMap<>();
    String[] hpeMappings = parameterString.split(",");
    for (String string : hpeMappings) {
      String[] mapping = string.split(":");
      hashMap.put(mapping[0].trim(), mapping[1].trim());
    }
    return hashMap;
  }
}
