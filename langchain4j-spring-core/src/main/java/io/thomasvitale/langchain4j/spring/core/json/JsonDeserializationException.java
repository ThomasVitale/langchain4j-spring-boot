package io.thomasvitale.langchain4j.spring.core.json;

/**
 * Thrown when an error occur while parsing JSON into a Java object.
 */
public class JsonDeserializationException extends IllegalStateException {

    public JsonDeserializationException(String message) {
        super(message);
    }

    public JsonDeserializationException(Exception exception) {
        super(exception);
    }

}
