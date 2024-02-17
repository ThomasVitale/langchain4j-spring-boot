package io.thomasvitale.langchain4j.spring.ollama.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 * Interceptor for logging requests and responses.
 *
 * @author Thomas Vitale
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final boolean logRequests;

    private final boolean logResponses;

    public LoggingInterceptor(boolean logRequests, boolean logResponses) {
        this.logRequests = logRequests;
        this.logResponses = logResponses;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody, ClientHttpRequestExecution execution)
            throws IOException {
        if (logRequests) {
            logRequest(request, requestBody);
        }

        if (logResponses) {
            return logResponse(request, requestBody, execution);
        }
        else {
            return execution.execute(request, requestBody);
        }
    }

    private void logRequest(HttpRequest request, byte[] requestBody) {
        logger.info("Request.\n Method: {}.\n URI: {}.\n Headers: {}.\n Body: {}", request.getMethod(),
                request.getURI(),
                request.getHeaders()
                    .toSingleValueMap()
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ":" + e.getValue())
                    .collect(Collectors.joining(", ")),
                new String(requestBody, StandardCharsets.UTF_8));
    }

    private ClientHttpResponse logResponse(HttpRequest request, byte[] requestBody,
            ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, requestBody);
        String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);

        logger.info("Response.\n Status Code: {}.\n Headers: {}.\n Body: {}", response.getStatusText(),
                response.getHeaders()
                    .toSingleValueMap()
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ":" + e.getValue())
                    .collect(Collectors.joining(", ")),
                responseBody);

        return response;
    }

}
