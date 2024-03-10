package io.thomasvitale.langchain4j.spring.ollama.client;

import java.net.http.HttpClient;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.core.http.HttpLoggingInterceptor;
import io.thomasvitale.langchain4j.spring.core.http.HttpResponseErrorHandler;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.EmbeddingRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.GenerateRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.GenerateResponse;

/**
 * Client for the Ollama API.
 * <p>
 * Based on the Spring AI implementation.
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 */
public class OllamaClient {

    private static final Logger logger = LoggerFactory.getLogger(OllamaClient.class);

    private final HttpResponseErrorHandler responseErrorHandler;

    private final RestClient restClient;

    public OllamaClient(OllamaClientConfig clientConfig, RestClient.Builder restClientBuilder) {
        Assert.notNull(clientConfig, "clientOptions must not be null");
        Assert.notNull(restClientBuilder, "restClientBuilder must not be null");

        this.responseErrorHandler = new HttpResponseErrorHandler();
        this.restClient = buildRestClient(clientConfig, restClientBuilder);
    }

    private RestClient buildRestClient(OllamaClientConfig clientConfig, RestClient.Builder restClientBuilder) {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(clientConfig.connectTimeout()).build();

        var jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkClientHttpRequestFactory.setReadTimeout(clientConfig.readTimeout());

        ClientHttpRequestFactory requestFactory;
        if (clientConfig.logRequests()) {
            requestFactory = new BufferingClientHttpRequestFactory(jdkClientHttpRequestFactory);
        }
        else {
            requestFactory = jdkClientHttpRequestFactory;
        }

        Consumer<HttpHeaders> defaultHeaders = headers -> {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        };

        return restClientBuilder.requestFactory(requestFactory)
            .baseUrl(clientConfig.baseUrl().toString())
            .defaultHeaders(defaultHeaders)
                .defaultStatusHandler(this.responseErrorHandler)
            .requestInterceptors(interceptors -> {
                if (clientConfig.logRequests() || clientConfig.logResponses()) {
                    interceptors
                        .add(new HttpLoggingInterceptor(clientConfig.logRequests(), clientConfig.logResponses()));
                }
            })
            .build();
    }

    @Nullable
    public GenerateResponse generate(GenerateRequest completionRequest) {
        Assert.notNull(completionRequest, "completionRequest must not be null");
        Assert.isTrue(!completionRequest.stream(), "Stream mode must be disabled");

        logger.debug("Sending completion request: {}", completionRequest);

        return this.restClient.post()
            .uri("/api/generate")
            .body(completionRequest)
            .retrieve()
            .body(GenerateResponse.class);
    }

    @Nullable
    public ChatResponse chat(ChatRequest chatRequest) {
        Assert.notNull(chatRequest, "chatRequest must not be null");
        Assert.isTrue(!chatRequest.stream(), "Stream mode must be disabled");

        logger.debug("Sending chat request: {}", chatRequest);

        return this.restClient.post()
            .uri("/api/chat")
            .body(chatRequest)
            .retrieve()
            .body(ChatResponse.class);
    }

    @Nullable
    public EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest) {
        Assert.notNull(embeddingRequest, "embeddingRequest must not be null");

        logger.debug("Sending embedding request: {}", embeddingRequest);

        return this.restClient.post()
            .uri("/api/embeddings")
            .body(embeddingRequest)
            .retrieve()
            .body(EmbeddingResponse.class);
    }

}
