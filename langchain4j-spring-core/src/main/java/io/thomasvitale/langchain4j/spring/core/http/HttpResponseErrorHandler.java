package io.thomasvitale.langchain4j.spring.core.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Handles errors from HTTP interactions.
 * <p>
 * Based on the Spring AI implementation.
 */
public class HttpResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            int statusCode = response.getStatusCode().value();
            String statusText = response.getStatusText();
            String message = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            logger.warn(String.format("[%s] %s - %s", statusCode, statusText, message));
            throw new RuntimeException(String.format("[%s] %s - %s", statusCode, statusText, message));
        }
    }

}
