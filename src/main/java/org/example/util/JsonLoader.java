package org.example.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.JavaType;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.type.TypeFactory;


public class JsonLoader {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public static <T> T load(File file, Class<T> clazz) throws IOException {
        return MAPPER.readValue(file, clazz);
    }

    public static void save(File file, Object object) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, object);
    }

    public static String toJson(Object object) {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz)  {
        return MAPPER.readValue(json, clazz);
    }

    public static <T> T loadList(File file, TypeReference<T> typeReference) throws IOException {
        return MAPPER.readValue(file, typeReference);
    }
}