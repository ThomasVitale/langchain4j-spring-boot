package io.thomasvitale.langchain4j.spring.ollama.client;

import io.thomasvitale.langchain4j.spring.core.http.HttpClientConfig;
import io.thomasvitale.langchain4j.spring.core.http.HttpLoggingInterceptor;
import io.thomasvitale.langchain4j.spring.core.http.HttpResponseErrorHandler;
import io.thomasvitale.langchain4j.spring.ollama.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.List;
import java.util.function.Consumer;

/**
 * Client for the Ollama API.
 * <p>
 * Based on the Spring AI implementation.
 * <p>
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 * <p>
 * @author Thomas Vitale
 */
public class OllamaClient {

    private static final Logger logger = LoggerFactory.getLogger(OllamaClient.class);

    private final static String DEFAULT_BASE_URL = "http://localhost:11434";

    private final HttpResponseErrorHandler responseErrorHandler;

    private final RestClient restClient;

    public OllamaClient(String baseUrl, RestClient.Builder restClientBuilder, HttpClientConfig clientConfig) {
        this.responseErrorHandler = new HttpResponseErrorHandler();

        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(clientConfig.getConnectTimeout()).build();

        JdkClientHttpRequestFactory jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkClientHttpRequestFactory.setReadTimeout(clientConfig.getReadTimeout());

        ClientHttpRequestFactory requestFactory;
        if (clientConfig.isLogRequests()) {
            requestFactory = new BufferingClientHttpRequestFactory(jdkClientHttpRequestFactory);
        }
        else {
            requestFactory = jdkClientHttpRequestFactory;
        }

        Consumer<HttpHeaders> defaultHeaders = headers -> {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        };

        this.restClient = restClientBuilder.requestFactory(requestFactory)
            .baseUrl(baseUrl)
            .defaultHeaders(defaultHeaders)
            .requestInterceptors(interceptors -> {
                if (clientConfig.isLogRequests() || clientConfig.isLogResponses()) {
                    interceptors
                        .add(new HttpLoggingInterceptor(clientConfig.isLogRequests(), clientConfig.isLogResponses()));
                }
            })
            .build();
    }

    public GenerateResponse generate(GenerateRequest completionRequest) {
        Assert.notNull(completionRequest, "completionRequest must not be null");
        Assert.isTrue(!completionRequest.stream(), "Stream mode must be disabled.");

        logger.debug("Sending completion request: {}", completionRequest);

        return this.restClient.post()
            .uri("/api/generate")
            .body(completionRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(GenerateResponse.class);
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        Assert.notNull(chatRequest, "chatRequest must not be null");
        Assert.isTrue(!chatRequest.stream(), "Stream mode must be disabled.");

        logger.debug("Sending chat request: {}", chatRequest);

        return this.restClient.post()
            .uri("/api/chat")
            .body(chatRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(ChatResponse.class);
    }

    public EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest) {
        Assert.notNull(embeddingRequest, "embeddingRequest must not be null");

        logger.debug("Sending embedding request: {}", embeddingRequest);

        return this.restClient.post()
            .uri("/api/embeddings")
            .body(embeddingRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(EmbeddingResponse.class);
    }

}
