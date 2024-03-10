package io.thomasvitale.langchain4j.spring.openai.client;

/**
 * Thrown when the OpenAI API returns an error response.
 */
public class OpenAiResponseException extends RuntimeException {

    public OpenAiResponseException(String message) {
        super(message);
    }

    public OpenAiResponseException(String message, Throwable cause) {
        super(message, cause);
    }

}
