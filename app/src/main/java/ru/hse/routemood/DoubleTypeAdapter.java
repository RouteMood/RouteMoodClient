package ru.hse.routemood;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class DoubleTypeAdapter extends TypeAdapter<Double> {

    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        if (value.isNaN()) {
            out.value("NaN");
        } else if (value == Double.POSITIVE_INFINITY) {
            out.value("Infinity");
        } else if (value == Double.NEGATIVE_INFINITY) {
            out.value("-Infinity");
        } else {
            out.value(value);
        }
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        try {
            if (token == JsonToken.STRING) {
                String str = in.nextString();
                return switch (str) {
                    case "NaN" -> Double.NaN;
                    case "Infinity" -> Double.POSITIVE_INFINITY;
                    case "-Infinity" -> Double.NEGATIVE_INFINITY;
                    default -> throw new JsonParseException("Unknown double value: " + str);
                };
            }
            return in.nextDouble();
        } catch (NumberFormatException e) {
            throw new JsonParseException("Cannot parse double value", e);
        }
    }
}