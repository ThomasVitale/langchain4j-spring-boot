package io.thomasvitale.langchain4j.spring.weaviate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateAuthClient;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.base.WeaviateErrorMessage;
import io.weaviate.client.v1.auth.exception.AuthException;
import io.weaviate.client.v1.batch.model.ObjectGetResponse;
import io.weaviate.client.v1.batch.model.ObjectsGetResponseAO2Result;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import io.weaviate.client.v1.graphql.model.GraphQLError;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import io.thomasvitale.langchain4j.spring.weaviate.client.WeaviateClientConfig;

import static dev.langchain4j.internal.Utils.randomUUID;

/**
 * Represents a store for embeddings using the Weaviate vector store.
 * <p>
 * Based on the LangChain4j and Spring AI implementations.
 *
 * @author Thomas Vitale
 */
public class WeaviateEmbeddingStore implements EmbeddingStore<TextSegment> {

    public static final String DEFAULT_CONSISTENCY_LEVEL = ConsistencyLevel.ALL;

    public static final String DEFAULT_OBJECT_CLASS_NAME = "LangChain4jClass";

    static final String ADDITIONAL_FIELD_NAME = "_additional";

    static final String ADDITIONAL_ID_FIELD_NAME = "id";

    static final String ADDITIONAL_CERTAINTY_FIELD_NAME = "certainty";

    static final String ADDITIONAL_VECTOR_FIELD_NAME = "vector";

    static final String CONTENT_FIELD_NAME = "text";


    private final WeaviateClient weaviateClient;

    private final String objectClassName;

    private final String consistencyLevel;

    private WeaviateEmbeddingStore(WeaviateClientConfig clientConfig, @Nullable String objectClassName, @Nullable String consistencyLevel) {
        Assert.notNull(clientConfig, "clientConfig cannot be null");

        Config weaviateVectorStoreConfig = new Config(
                clientConfig.url().getScheme(),
                computeFullHostFromUrl(clientConfig.url()),
                Objects.requireNonNullElse(clientConfig.headers(), Map.of()),
                (int) clientConfig.connectTimeout().toSeconds(),
                (int) clientConfig.readTimeout().toSeconds(),
                (int) clientConfig.readTimeout().toSeconds());

        try {
            this.weaviateClient = WeaviateAuthClient
                    .apiKey(weaviateVectorStoreConfig, Objects.requireNonNullElse(clientConfig.apiKey(), ""));
        } catch (AuthException ex) {
            throw new IllegalArgumentException("Failed to create Weaviate client with API Key", ex);
        }

        this.objectClassName = StringUtils.hasText(objectClassName) ? objectClassName : DEFAULT_OBJECT_CLASS_NAME;

        this.consistencyLevel = StringUtils.hasText(consistencyLevel) ? consistencyLevel : ConsistencyLevel.ALL;
    }

    private static String computeFullHostFromUrl(URI url) {
        if (url.getPort() == -1) {
            return url.getHost();
        } else {
            return url.getHost() + ":" + url.getPort();
        }
    }

    @Override
    public String add(Embedding embedding) {
        Assert.notNull(embedding, "embedding cannot be null");
        String id = randomUUID();
        sendAddEmbeddingsRequest(List.of(id), List.of(embedding), null);
        return id;
    }

    @Override
    public void add(String id, Embedding embedding) {
        Assert.hasText(id, "id cannot be null or empty");
        Assert.notNull(embedding, "embedding cannot be null");
        sendAddEmbeddingsRequest(List.of(id), List.of(embedding), null);
    }

    @Override
    public String add(Embedding embedding, @Nullable TextSegment textSegment) {
        Assert.notNull(embedding, "embedding cannot be null");
        String id = randomUUID();
        sendAddEmbeddingsRequest(List.of(id), List.of(embedding), textSegment == null ? null : List.of(textSegment));
        return id;
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings) {
        Assert.notNull(embeddings, "embeddings cannot be null");
        List<String> ids = embeddings.stream().map(embedding -> randomUUID()).toList();
        sendAddEmbeddingsRequest(ids, embeddings, null);
        return ids;
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings, @Nullable List<TextSegment> textSegments) {
        Assert.notNull(embeddings, "embeddings cannot be null");
        List<String> ids = embeddings.stream().map(embedding -> randomUUID()).toList();
        sendAddEmbeddingsRequest(ids, embeddings, textSegments);
        return ids;
    }

    private void sendAddEmbeddingsRequest(List<String> ids, List<Embedding> embeddings, @Nullable List<TextSegment> textSegments) {
        List<WeaviateObject> weaviateObjects = new ArrayList<>();
        for (int i = 0; i < embeddings.size(); i++) {
            weaviateObjects.add(toWeaviateObject(ids.get(i), embeddings.get(i),
                    CollectionUtils.isEmpty(textSegments) ? "" : textSegments.get(i).text()));
        }

        Result<ObjectGetResponse[]> response = weaviateClient.batch()
                .objectsBatcher()
                .withObjects(weaviateObjects.toArray(new WeaviateObject[0]))
                .withConsistencyLevel(consistencyLevel)
                .run();

        List<String> errorMessages = new ArrayList<>();

        if (response.hasErrors()) {
            errorMessages.add(response.getError()
                    .getMessages()
                    .stream()
                    .map(WeaviateErrorMessage::getMessage)
                    .collect(Collectors.joining("\n")));
            throw new RuntimeException("Failed to add documents because: \n" + errorMessages);
        }

        if (response.getResult() != null) {
            for (var r : response.getResult()) {
                if (r.getResult() != null && r.getResult().getErrors() != null) {
                    var error = r.getResult().getErrors();
                    errorMessages
                            .add(error.getError().stream()
                                    .map(ObjectsGetResponseAO2Result.ErrorItem::getMessage)
                                    .collect(Collectors.joining("\n")));
                }
            }
        }

        if (!CollectionUtils.isEmpty(errorMessages)) {
            throw new RuntimeException("Failed to add documents because: \n" + errorMessages);
        }
    }

    private WeaviateObject toWeaviateObject(String id, Embedding embedding, String text) {
        Map<String, Object> fields = new HashMap<>();
        fields.put(CONTENT_FIELD_NAME, text);

        return WeaviateObject.builder()
                .className(objectClassName)
                .id(id)
                .vector(embedding.vectorAsList().toArray(new Float[0]))
                .properties(fields)
                .build();
    }

    // TODO: Add filter expressions after LangChain4j supports them.
    @Override
    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResults, double minScore) {
        Assert.notNull(referenceEmbedding, "referenceEmbedding cannot be null");
        Assert.isTrue(maxResults >= 1, "maxResults must be greater than or equal to 1");
        Assert.isTrue(minScore >= 0 && minScore <= 1, "minScore must be between 0 and 1 (inclusive)");

        GetBuilder.GetBuilderBuilder builder = GetBuilder.builder();

        List<Field> searchFields = List.of(
                Field.builder().name(CONTENT_FIELD_NAME).build(),
                Field.builder()
                        .name(ADDITIONAL_FIELD_NAME)
                        .fields(Field.builder().name(ADDITIONAL_ID_FIELD_NAME).build(),
                                Field.builder().name(ADDITIONAL_CERTAINTY_FIELD_NAME).build(),
                                Field.builder().name(ADDITIONAL_VECTOR_FIELD_NAME).build())
                        .build()
        );

        GetBuilder queryBuilder = builder.className(objectClassName)
                .withNearVectorFilter(NearVectorArgument.builder()
                        .vector(referenceEmbedding.vectorAsList().toArray(new Float[0]))
                        .certainty((float) minScore)
                        .build())
                .limit(maxResults)
                .fields(Fields.builder().fields(searchFields.toArray(new Field[0])).build())
                .build();

        String graphQLQuery = queryBuilder.buildQuery();

        Result<GraphQLResponse> result = weaviateClient.graphQL().raw().withQuery(graphQLQuery).run();

        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getError()
                    .getMessages()
                    .stream()
                    .map(WeaviateErrorMessage::getMessage)
                    .collect(Collectors.joining("\n")));
        }

        GraphQLError[] errors = result.getResult().getErrors();
        if (errors != null && errors.length > 0) {
            throw new IllegalArgumentException(
                    Arrays.stream(errors).map(GraphQLError::getMessage).collect(Collectors.joining("\n")));
        }

        GraphQLResponse response = result.getResult();

        @SuppressWarnings("unchecked")
        Optional<Map.Entry<String, Map<?, ?>>> responseData = ((Map<String, Map<?, ?>>) response.getData())
                .entrySet()
                .stream()
                .findFirst();
        if (responseData.isEmpty()) {
            return List.of();
        }

        Optional<?> responseDataItemsPart = responseData.get().getValue().entrySet().stream().findFirst();
        if (responseDataItemsPart.isEmpty()) {
            return List.of();
        }

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> responseItems = ((Map.Entry<String, List<Map<String, ?>>>) responseDataItemsPart.get()).getValue();

        return responseItems.stream().map(WeaviateAdapters::toEmbeddingMatch).toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WeaviateClientConfig clientConfig;
        private String objectClassName;
        private String consistencyLevel;

        private Builder() {}

        public Builder clientConfig(WeaviateClientConfig clientConfig) {
            this.clientConfig = clientConfig;
            return this;
        }

        public Builder objectClassName(String objectClassName) {
            this.objectClassName = objectClassName;
            return this;
        }

        public Builder consistencyLevel(String consistencyLevel) {
            this.consistencyLevel = consistencyLevel;
            return this;
        }

        public WeaviateEmbeddingStore build() {
            return new WeaviateEmbeddingStore(clientConfig, objectClassName, consistencyLevel);
        }
    }

}
