package io.thomasvitale.langchain4j.spring.openai.client;

import java.net.http.HttpClient;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.core.http.HttpLoggingInterceptor;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionRequest;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionResponse;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingRequest;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationRequest;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationResponse;
import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationRequest;
import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationResponse;

/**
 * Client for the OpenAI API.
 * <p>
 * Based on the Spring AI implementation.
 * <p>
 *
 * @see <a href="https://platform.openai.com/docs/api-reference/chat">OpenAI API</a>
 * <p>
 * @author Thomas Vitale
 */
public class OpenAiClient {

    private final Logger logger = LoggerFactory.getLogger(OpenAiClient.class);

    private final ResponseErrorHandler responseErrorHandler;

    private final RestClient restClient;

    public OpenAiClient(OpenAiClientConfig clientConfig, RestClient.Builder restClientBuilder) {
        Assert.notNull(clientConfig, "clientOptions must not be null");
        Assert.notNull(restClientBuilder, "restClientBuilder must not be null");

        this.responseErrorHandler = new OpenAiResponseErrorHandler();
        this.restClient = buildRestClient(clientConfig, restClientBuilder);
    }

    private RestClient buildRestClient(OpenAiClientConfig clientConfig, RestClient.Builder restClientBuilder) {
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
            if (StringUtils.hasText(clientConfig.apiKey())) {
                headers.setBearerAuth(clientConfig.apiKey());
            }
        };

        return restClientBuilder.requestFactory(requestFactory)
                .baseUrl(clientConfig.baseUrl().toString())
                .defaultHeaders(defaultHeaders)
                .defaultStatusHandler(responseErrorHandler)
                .requestInterceptors(interceptors -> {
                    if (clientConfig.logRequests() || clientConfig.logResponses()) {
                        interceptors
                                .add(new HttpLoggingInterceptor(clientConfig.logRequests(), clientConfig.logResponses()));
                    }
                })
                .build();
    }

    /**
     * Creates a model response for the given chat conversation.
     */
    @Nullable
    public ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletionRequest) {
        Assert.notNull(chatCompletionRequest, "chatCompletionRequest cannot be null");
        Assert.isTrue(!chatCompletionRequest.stream(), "Stream mode must be disabled");

        logger.debug("Sending chat completion request: {}", chatCompletionRequest);

        return this.restClient.post()
                .uri("/v1/chat/completions")
                .body(chatCompletionRequest)
                .retrieve()
                .body(ChatCompletionResponse.class);
    }

    /**
     * Creates an embedding vector representing the input text.
     */
    @Nullable
    public EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest) {
        Assert.notNull(embeddingRequest, "embeddingRequest cannot be null");

        logger.debug("Sending embedding request: {}", embeddingRequest);

        return this.restClient.post()
                .uri("/v1/embeddings")
                .body(embeddingRequest)
                .retrieve()
                .body(EmbeddingResponse.class);
    }

    /**
     * Creates an image given a prompt.
     */
    @Nullable
    public ImageGenerationResponse imageGeneration(ImageGenerationRequest imageGenerationRequest) {
        Assert.notNull(imageGenerationRequest, "imageGenerationRequest cannot be null.");

        logger.debug("Sending image generation request: {}", imageGenerationRequest);

        return this.restClient.post()
                .uri("v1/images/generations")
                .body(imageGenerationRequest)
                .retrieve()
                .body(ImageGenerationResponse.class);
    }

    /**
     * Classifies if text violates OpenAI's Content Policy
     */
    @Nullable
    public ModerationResponse moderation(ModerationRequest moderationRequest) {
        Assert.notNull(moderationRequest, "moderationRequest cannot be null.");

        logger.debug("Sending moderation request: {}", moderationRequest);

        return this.restClient.post()
                .uri("v1/moderations")
                .body(moderationRequest)
                .retrieve()
                .body(ModerationResponse.class);
    }

}
