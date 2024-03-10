package io.thomasvitale.langchain4j.spring.core.json;

/**
 * Thrown when an error occur while parsing a Java object into JSON.
 */
public class JsonSerializationException extends IllegalStateException {

    public JsonSerializationException(String message) {
        super(message);
    }

    public JsonSerializationException(Exception exception) {
        super(exception);
    }

}
