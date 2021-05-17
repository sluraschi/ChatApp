package com.asapp.backend.challenge.utils;

import com.asapp.backend.challenge.resources.GetMessageResource;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.resources.UserResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JSONUtil {

    ObjectMapper objectMapper;

    public JSONUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String dataToJson(Object data, Class<?> view) {
        try {
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
            objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'z'");
            objectMapper.setDateFormat(df);
            StringWriter sw = new StringWriter();
            objectMapper.writerWithView(view).writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON");
        }
    }

    public Object jsonToData(String json, Class<?> view, Class<?> objectClass) throws IOException {
        try {
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
            objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

            return objectMapper.readerWithView(view).forType(objectClass).readValue(json);
        } catch (IOException e) {
            throw new IOException("IOEXception while mapping json to object");
        }
    }
}
