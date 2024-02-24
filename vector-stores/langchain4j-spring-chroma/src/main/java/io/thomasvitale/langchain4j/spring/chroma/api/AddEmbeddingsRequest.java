package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Request to add embeddings to a Chroma collection.
 *
 * @param ids The ids of the embeddings to add.
 * @param embeddings The embeddings to add if you don't want Chroma to calculate them.
 *                   Optional if documents are provided.
 * @param metadata The metadata to associate with the embeddings, enabling filtering at query time.
 * @param documents The documents to associate with the embeddings.
 *                  Optional if embeddings are provided.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddEmbeddingsRequest(
        List<String> ids,
        List<float[]> embeddings,
        @JsonProperty("metadatas")
        List<Map<String, String>> metadata, // TODO: this should be Map<String, Object> but the LangChain4j APIs don't support that, investigate
        List<String> documents
) {

    public AddEmbeddingsRequest {
        Assert.notEmpty(ids, "ids must not be null or empty");
        if (CollectionUtils.isEmpty(documents)) {
            Assert.notEmpty(embeddings, "embeddings must not be null or empty");
        }
        if (CollectionUtils.isEmpty(embeddings)) {
            Assert.notEmpty(documents, "documents must not be null or empty");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> ids;
        private List<float[]> embeddings;
        private List<Map<String, String>> metadata;
        private List<String> documents;

        private Builder() {}

        public Builder ids(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder embeddings(List<float[]> embeddings) {
            this.embeddings = embeddings;
            return this;
        }

        public Builder metadata(List<Map<String, String>> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder documents(List<String> documents) {
            this.documents = documents;
            return this;
        }

        public AddEmbeddingsRequest build() {
            return new AddEmbeddingsRequest(ids, embeddings, metadata, documents);
        }
    }

}
