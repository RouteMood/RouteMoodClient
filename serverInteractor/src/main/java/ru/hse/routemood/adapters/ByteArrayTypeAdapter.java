package ru.hse.routemood.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Base64;

public class ByteArrayTypeAdapter extends TypeAdapter<byte[]> {
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        out.value(Base64.getEncoder().encodeToString(value));
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        String base64 = in.nextString();
        return Base64.getDecoder().decode(base64);
    }
}