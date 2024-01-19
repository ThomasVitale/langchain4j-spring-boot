package io.thomasvitale.langchain4j.autoconfigure.openai;

/**
 * Exception thrown if an invalid property is found when processing
 * the OpenAI configuration data.
 *
 * @author Thomas Vitale
 */
public class OpenAiConfigurationException extends RuntimeException {

    public OpenAiConfigurationException(String message) {
        super(message);
    }

}
