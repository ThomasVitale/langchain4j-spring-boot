package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to add embeddings to a Chroma collection.
 *
 * @param ids The ids of the embeddings to add.
 * @param embeddings The embeddings to add if you don't want Chroma to calculate them.
 * Optional if documents are provided.
 * @param metadata (optional) The metadata to associate with the embeddings, enabling
 * filtering at query time.
 * @param documents The documents to associate with the embeddings. Optional if embeddings
 * are provided.
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddEmbeddingsRequest(
//@formatter:off
        List<String> ids,
        List<float[]> embeddings,
        @JsonProperty("metadatas")
        List<Map<String, String>> metadata, // TODO: this should be Map<String, Object> but the LangChain4j APIs don't support that, investigate
        List<String> documents
//@formatter:on
) {

    /**
     * A convenience constructor to add a single embedding to the store.
     */
    public AddEmbeddingsRequest(String id, float[] embedding, Map<String, String> metadata, String document) {
        this(List.of(id), List.of(embedding), List.of(metadata), List.of(document));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<String> ids;

        private List<float[]> embeddings;

        private List<Map<String, String>> metadata;

        private List<String> documents;

        private Builder() {
        }

        public Builder withIds(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withEmbeddings(List<float[]> embeddings) {
            this.embeddings = embeddings;
            return this;
        }

        public Builder withMetadata(List<Map<String, String>> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder withDocuments(List<String> documents) {
            this.documents = documents;
            return this;
        }

        public AddEmbeddingsRequest build() {
            return new AddEmbeddingsRequest(ids, embeddings, metadata, documents);
        }

    }
}
