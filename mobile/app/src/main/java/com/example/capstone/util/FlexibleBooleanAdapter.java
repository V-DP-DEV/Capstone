package com.example.capstone.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FlexibleBooleanAdapter extends TypeAdapter<Boolean> {

  @Override
  public Boolean read(JsonReader in) throws IOException {
    // Check the JSON value's type before attempting to read it.
    JsonToken token = in.peek();

    switch (token) {
      case BOOLEAN:
        // Handle standard JSON booleans: true or false.
        return in.nextBoolean();

      case NUMBER:
        // Handle numeric booleans from PHP/MySQL: 1 = true, 0 = false.
        // This only applies when Gson is reading a Boolean field.
        return in.nextInt() != 0;

      case NULL:
        // Preserve null values instead of converting them to false.
        in.nextNull();
        return null;

      case STRING:
        // Handle boolean values returned as strings, such as "1" or "true".
        String value = in.nextString();

        // Convert "1" or "true" (case-insensitive) to true.
        if ("1".equals(value) || "true".equalsIgnoreCase(value)) {
          return true;
        }

        // Convert "0" or "false" (case-insensitive) to false.
        if ("0".equals(value) || "false".equalsIgnoreCase(value)) {
          return false;
        }

        // Reject strings that cannot be interpreted as booleans.
        // For example, "NOT_REQUESTED" is an enum value, not a boolean.
        throw new IOException("Invalid boolean: " + value);

      default:
        // Reject unsupported JSON types, such as objects or arrays.
        throw new IOException("Expected boolean or 0/1, got " + token);
    }
  }

  @Override
  public void write(JsonWriter out, Boolean value) throws IOException {
    // Write null as JSON null.
    if (value == null) {
      out.nullValue();
    } else {
      // Write a proper JSON boolean: true or false.
      out.value(value);
    }
  }
}