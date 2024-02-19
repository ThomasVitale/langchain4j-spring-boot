package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request to retrieve embeddings from a Chroma collection.
 *
 * @param ids (optional) The ids of the embeddings to retrieve.
 * @param where (optional) Where clause to filter the results based on metadata values.
 * @param limit (optional) Limit on the number of embeddings to retrieve.
 * @param offset (optional) Offset on the number of embeddings to retrieve.
 * @param include (optional) List of fields to include in the response.
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetEmbeddingsRequest( // TODO: Add support for whereDocument
//@formatter:off
        List<String> ids,
        Map<String, Object> where,
        int limit,
        int offset,
        List<Include> include
//@formatter:on
) {

    private static final List<Include> DEFAULT_INCLUDES = List.of(Include.METADATAS, Include.DOCUMENTS);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<String> ids;

        private Map<String, Object> where;

        private int limit = 10;

        private int offset = 0;

        private List<Include> include = DEFAULT_INCLUDES;

        private Builder builder() {
            return new Builder();
        }

        public Builder withIds(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder withWhere(Map<String, Object> where) {
            this.where = where;
            return this;
        }

        public Builder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder withInclude(List<Include> include) {
            this.include = include;
            return this;
        }

        public GetEmbeddingsRequest build() {
            return new GetEmbeddingsRequest(ids, where, limit, offset, include);
        }

    }
}
