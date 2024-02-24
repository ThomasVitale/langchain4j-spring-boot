package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request to retrieve embeddings from a Chroma collection.
 *
 * @param ids Optional ids of the embeddings to retrieve.
 * @param where Optional where clause to filter the results based on metadata values.
 * @param limit Optional limit on the number of embeddings to retrieve.
 * @param offset Optional offset on the number of embeddings to retrieve.
 * @param include Optional list of fields to include in the response.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetEmbeddingsRequest( // TODO: Add support for whereDocument
        List<String> ids,
        Map<String, Object> where,
        int limit,
        int offset,
        List<Include> include
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

        private Builder() {}

        public Builder ids(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public Builder where(Map<String, Object> where) {
            this.where = where;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder include(List<Include> include) {
            this.include = include;
            return this;
        }

        public GetEmbeddingsRequest build() {
            return new GetEmbeddingsRequest(ids, where, limit, offset, include);
        }
    }

}
