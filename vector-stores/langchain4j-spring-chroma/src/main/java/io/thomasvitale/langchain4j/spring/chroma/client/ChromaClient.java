package io.thomasvitale.langchain4j.spring.chroma.client;

import java.net.http.HttpClient;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.chroma.api.AddEmbeddingsRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.Collection;
import io.thomasvitale.langchain4j.spring.chroma.api.CreateCollectionRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.DeleteEmbeddingsRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.GetEmbeddingsRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.GetEmbeddingsResponse;
import io.thomasvitale.langchain4j.spring.chroma.api.QueryRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.QueryResponse;
import io.thomasvitale.langchain4j.spring.core.http.HttpResponseErrorHandler;

/**
 * Client for the Chroma API.
 * <p>
 * Based on the Spring AI implementation.
 *
 * @see <a href="https://docs.trychroma.com/api">Chroma API</a>
 * <p>
 * @author Thomas Vitale
 */
public class ChromaClient {

    private static final Logger logger = LoggerFactory.getLogger(ChromaClient.class);

    private final HttpResponseErrorHandler responseErrorHandler;

    private final RestClient restClient;

    public ChromaClient(ChromaClientConfig clientConfig, RestClient.Builder restClientBuilder) {
        Assert.notNull(clientConfig, "clientOptions must not be null");
        Assert.notNull(restClientBuilder, "restClientBuilder must not be null");

        this.responseErrorHandler = new HttpResponseErrorHandler();
        this.restClient = buildRestClient(clientConfig, restClientBuilder);
    }

    private RestClient buildRestClient(ChromaClientConfig clientConfig, RestClient.Builder restClientBuilder) {
        var httpClient = HttpClient.newBuilder()
            .connectTimeout(clientConfig.connectTimeout())
            // Chroma uses Python FastAPI which causes errors when called from JDK
            // HttpClient, using HTTP2 by default, but failing to fall back to HTTP 1.1
            // if not supported. This is a workaround to force HTTP 1.1
            // when not using HTTPS.
            .version(clientConfig.url().getScheme().equals("http") ? HttpClient.Version.HTTP_1_1
                    : HttpClient.Version.HTTP_2)
            .build();

        var clientHttpRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setReadTimeout(clientConfig.readTimeout());

        Consumer<HttpHeaders> defaultHeaders = headers -> {
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            if (StringUtils.hasText(clientConfig.apiToken())) {
                headers.setBearerAuth(clientConfig.apiToken());
            }
        };

        return restClientBuilder.requestFactory(clientHttpRequestFactory)
            .baseUrl(clientConfig.url().toString())
            .defaultHeaders(defaultHeaders)
            .requestInterceptors(interceptors -> {
                if (StringUtils.hasText(clientConfig.username()) && StringUtils.hasText(clientConfig.password())) {
                    interceptors
                        .add(new BasicAuthenticationInterceptor(clientConfig.username(), clientConfig.password()));
                }
            })
            .build();
    }

    // Chroma Client API (https://docs.trychroma.com/js_reference/Client)

    /**
     * Create a new Chroma collection with the specified parameters.
     */
    @Nullable
    public Collection createCollection(CreateCollectionRequest createCollectionRequest) {
        Assert.notNull(createCollectionRequest, "createCollectionRequest must not be null");

        logger.debug("Sending create collection request: {}", createCollectionRequest);

        return this.restClient.post()
            .uri("/api/v1/collections")
            .body(createCollectionRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(Collection.class);
    }

    /**
     * Delete a Chroma collection with the specified name.
     */
    public void deleteCollection(String collectionName) {
        Assert.hasText(collectionName, "collectionName must not be empty");

        logger.debug("Sending delete collection request for: {}", collectionName);

        this.restClient.delete()
            .uri("/api/v1/collections/{name}", collectionName)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .toBodilessEntity();
    }

    /**
     * Get a Chroma collection with the specified name.
     */
    @Nullable
    public Collection getCollection(String collectionName) {
        Assert.hasText(collectionName, "collectionName must not be empty");

        logger.debug("Sending get collection request for: {}", collectionName);

        try {
            return this.restClient.get()
                .uri("/api/v1/collections/{name}", collectionName)
                .retrieve()
                .body(Collection.class);
        }
        catch (HttpServerErrorException ex) {
            var errorMessage = ex.getMessage();
            // The Chroma API returns 500 instead of 404 when the collection does not
            // exist, so we need this workaround to handle the error.
            if (StringUtils.hasText(errorMessage)
                    && errorMessage.contains("Collection %s does not exist".formatted(collectionName))) {
                return null;
            }
            throw new RuntimeException(errorMessage, ex);
        }
    }

    /**
     * Get the Chroma heartbeat to check if the server is alive.
     */
    public boolean heartbeat() {
        return this.restClient.get()
            .uri("/api/v1/heartbeat")
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .toBodilessEntity()
            .getStatusCode()
            .is2xxSuccessful();
    }

    /**
     * List all Chroma collections.
     */
    @Nullable
    public List<Collection> listCollections() {
        var typeReference = new ParameterizedTypeReference<List<Collection>>() {
        };

        logger.debug("Sending list collections request");

        return this.restClient.get()
            .uri("/api/v1/collections")
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(typeReference);
    }

    /**
     * Get the version of the Chroma API.
     */
    @Nullable
    public String version() {
        return this.restClient.get()
            .uri("/api/v1/version")
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(String.class);
    }

    // Chroma Collection API (https://docs.trychroma.com/js_reference/Collection)

    /**
     * Add embeddings to a Chroma collection or update them if already present.
     */
    @Nullable
    public Boolean upsertEmbeddings(String collectionName, AddEmbeddingsRequest addEmbeddingsRequest) {
        Assert.hasText(collectionName, "collectionName must not be empty");
        Assert.notNull(addEmbeddingsRequest, "createCollectionRequest must not be null");

        logger.debug("Sending upsert embeddings request: {}", addEmbeddingsRequest);

        return this.restClient.post()
            .uri("/api/v1/collections/{name}/upsert", collectionName)
            .body(addEmbeddingsRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(Boolean.class);
    }

    /**
     * Count the number of embeddings in a Chroma collection.
     */
    @Nullable
    public Long countEmbeddings(String collectionName) {
        Assert.hasText(collectionName, "collectionName must not be empty");

        return this.restClient.get()
            .uri("/api/v1/collections/{name}/count", collectionName)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(Long.class);
    }

    /**
     * Delete embeddings from a Chroma collection.
     */
    @Nullable
    public List<String> deleteEmbeddings(String collectionName, DeleteEmbeddingsRequest deleteEmbeddingsRequest) {
        Assert.hasText(collectionName, "collectionName must not be empty");
        Assert.notNull(deleteEmbeddingsRequest, "deleteEmbeddingsRequest must not be null");

        var typeReference = new ParameterizedTypeReference<List<String>>() {
        };

        logger.debug("Sending delete embeddings request: {}", deleteEmbeddingsRequest);

        return this.restClient.post()
            .uri("/api/v1/collections/{name}/delete", collectionName)
            .body(deleteEmbeddingsRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(typeReference);
    }

    /**
     * Get embeddings from a Chroma collection.
     */
    @Nullable
    public GetEmbeddingsResponse getEmbeddings(String collectionName, GetEmbeddingsRequest getEmbeddingsRequest) {
        Assert.hasText(collectionName, "collectionName must not be empty");
        Assert.notNull(getEmbeddingsRequest, "getEmbeddingsRequest must not be null");

        logger.debug("Sending get embeddings request: {}", getEmbeddingsRequest);

        return this.restClient.post()
            .uri("/api/v1/collections/{name}/get", collectionName)
            .body(getEmbeddingsRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(GetEmbeddingsResponse.class);
    }

    /**
     * Query a Chroma collection for similar embeddings.
     */
    @Nullable
    public QueryResponse queryCollection(String collectionName, QueryRequest queryRequest) {
        Assert.hasText(collectionName, "collectionName must not be empty");
        Assert.notNull(queryRequest, "queryRequest must not be null");

        logger.debug("Sending query collection request: {}", queryRequest);

        return this.restClient.post()
            .uri("/api/v1/collections/{name}/query", collectionName)
            .body(queryRequest)
            .retrieve()
            .onStatus(this.responseErrorHandler)
            .body(QueryResponse.class);
    }

}
