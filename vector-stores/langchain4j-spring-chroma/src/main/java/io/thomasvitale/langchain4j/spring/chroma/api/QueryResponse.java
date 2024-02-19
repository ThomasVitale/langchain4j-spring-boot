package io.thomasvitale.langchain4j.spring.chroma.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Response when querying a Chroma collection for similar embeddings.
 *
 * @param ids A list of document ids for each returned document.
 * @param embeddings A list of document embeddings for each returned document.
 * @param documents A list of document contents for each returned document.
 * @param metadata A list of document metadata for each returned document.
 * @param distances A list of search distances for each returned document.
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record QueryResponse(
//@formatter:off
        List<List<String>> ids,
        List<List<List<Float>>> embeddings,
        List<List<String>> documents,
        @JsonProperty("metadatas")
        List<List<Map<String, String>>> metadata,
        List<List<Double>> distances
//@formatter:on
) {
}
