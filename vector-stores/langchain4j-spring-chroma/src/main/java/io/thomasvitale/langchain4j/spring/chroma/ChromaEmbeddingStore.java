package io.thomasvitale.langchain4j.spring.chroma;

import java.util.List;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import io.thomasvitale.langchain4j.spring.chroma.api.AddEmbeddingsRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.CreateCollectionRequest;
import io.thomasvitale.langchain4j.spring.chroma.api.Include;
import io.thomasvitale.langchain4j.spring.chroma.api.QueryRequest;
import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClient;

import static dev.langchain4j.internal.Utils.randomUUID;
import static java.util.stream.Collectors.toList;

/**
 * Represents a store for embeddings using the Chroma vector store.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class ChromaEmbeddingStore implements EmbeddingStore<TextSegment>, InitializingBean {

    public static final String DEFAULT_COLLECTION_NAME = "LangChain4jCollection";

    private final ChromaClient chromaClient;

    private final String collectionName;

    private String collectionId;

    private ChromaEmbeddingStore(ChromaClient chromaClient, @Nullable String collectionName) {
        Assert.notNull(chromaClient, "chromaClient cannot be null");
        this.chromaClient = chromaClient;
        this.collectionName = StringUtils.hasText(collectionName) ? collectionName : DEFAULT_COLLECTION_NAME;
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

    private void sendAddEmbeddingsRequest(List<String> ids, List<Embedding> embeddings,
            @Nullable List<TextSegment> textSegments) {
        var addEmbeddingsRequest = AddEmbeddingsRequest.builder()
            .ids(ids)
            .embeddings(embeddings.stream().map(Embedding::vector).toList())
            .metadata(CollectionUtils.isEmpty(textSegments) ? null
                    : textSegments.stream().map(TextSegment::metadata).map(Metadata::asMap).toList())
            .documents(CollectionUtils.isEmpty(textSegments) ? null
                    : textSegments.stream().map(TextSegment::text).toList())
            .build();

        chromaClient.upsertEmbeddings(collectionId, addEmbeddingsRequest);
    }

    // TODO: Add filter expressions after LangChain4j supports them.
    @Override
    public List<EmbeddingMatch<TextSegment>> findRelevant(Embedding referenceEmbedding, int maxResults,
            double minScore) {
        Assert.notNull(referenceEmbedding, "referenceEmbedding cannot be null");
        Assert.isTrue(maxResults >= 1, "maxResults must be greater than or equal to 1");
        Assert.isTrue(minScore >= 0 && minScore <= 1, "minScore must be between 0 and 1 (inclusive)");

        var queryRequest = QueryRequest.builder()
            .queryEmbeddings(List.of(referenceEmbedding.vectorAsList()))
            .nResults(maxResults)
            .include(List.of(Include.METADATAS, Include.DOCUMENTS, Include.DISTANCES, Include.EMBEDDINGS))
            .build();

        var queryResponse = chromaClient.queryCollection(collectionId, queryRequest);

        List<EmbeddingMatch<TextSegment>> matches = ChromaAdapters.toEmbeddingMatches(queryResponse);

        return matches.stream().filter(match -> match.score() >= minScore).collect(toList());
    }

    /**
     * This method is called after all properties are set, and before the class is put
     * into service. Here we create the collection if it does not exist yet and save the
     * resulting collection id.
     */
    @Override
    public void afterPropertiesSet() {
        var collection = chromaClient.getCollection(collectionName);
        if (collection == null) {
            collection = chromaClient.createCollection(new CreateCollectionRequest(collectionName));
        }
        if (collection == null) {
            throw new IllegalStateException("Failed to create Chroma collection: " + collectionName);
        }
        collectionId = collection.id();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ChromaClient chromaClient;
        private String collectionName;

        private Builder() {}

        public Builder client(ChromaClient chromaClient) {
            this.chromaClient = chromaClient;
            return this;
        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public ChromaEmbeddingStore build() {
            return new ChromaEmbeddingStore(chromaClient, collectionName);
        }
    }

}
