package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A request to query a Chroma collection for similar embeddings.
 *
 * @param queryEmbeddings Optional query embeddings to use for the search.
 * @param nResults Optional number of results to return.
 * @param where Optional where clause to filter the results based on metadata values.
 * @param include Optional list of fields to include in the response.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record QueryRequest(
        List<List<Float>> queryEmbeddings,
        int nResults,
        Map<String, Object> where,
        List<Include> include
) {

    private static final List<Include> DEFAULT_INCLUDES = List.of(Include.METADATAS, Include.DOCUMENTS,
            Include.DISTANCES);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<List<Float>> queryEmbeddings;
        private int nResults = 10;
        private Map<String, Object> where;
        private List<Include> include = DEFAULT_INCLUDES;

        private Builder() {}

        public Builder queryEmbeddings(List<List<Float>> queryEmbeddings) {
            this.queryEmbeddings = queryEmbeddings;
            return this;
        }

        public Builder nResults(int nResults) {
            this.nResults = nResults;
            return this;
        }

        public Builder where(Map<String, Object> where) {
            this.where = where;
            return this;
        }

        public Builder include(List<Include> include) {
            this.include = include;
            return this;
        }

        public QueryRequest build() {
            return new QueryRequest(queryEmbeddings, nResults, where, include);
        }
    }

}
