package io.thomasvitale.langchain4j.spring.core.json.jackson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.internal.Json;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;
import io.thomasvitale.langchain4j.spring.core.json.JsonSerializationException;

/**
 * A codec for serializing/deserializing objects using Jackson.
 * <p>
 * Adapted from GsonJsonCodec in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
public class JacksonJsonCodec implements Json.JsonCodec {

    private final ObjectMapper objectMapper;

    public JacksonJsonCodec() {
        this.objectMapper = LangChain4jJacksonProvider.getObjectMapper();
    }

    @Override
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException ex) {
            throw new JsonSerializationException(ex);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        }
        catch (JsonProcessingException ex) {
            throw new JsonDeserializationException(ex);
        }
    }

    @Override
    public InputStream toInputStream(Object object, Class<?> type) throws IOException {
        return new ByteArrayInputStream(objectMapper.writeValueAsBytes(object));
    }

}
