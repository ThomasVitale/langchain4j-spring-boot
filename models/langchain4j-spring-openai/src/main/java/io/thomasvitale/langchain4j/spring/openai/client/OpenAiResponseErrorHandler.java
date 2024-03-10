package io.thomasvitale.langchain4j.spring.openai.client;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Handles errors from HTTP interactions with the OpenAI API.
 * <p>
 * Based on the Spring AI implementation.
 */
public class OpenAiResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            throw new OpenAiResponseException(String.format("%s - %s", response.getStatusCode().value(),
                    objectMapper.readValue(response.getBody(), ResponseError.class)));
        }
    }

}
