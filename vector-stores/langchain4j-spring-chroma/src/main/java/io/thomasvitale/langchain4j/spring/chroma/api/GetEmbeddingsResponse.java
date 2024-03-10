package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response when retrieving embeddings from a Chroma collection .
 *
 * @param ids A list of document ids for each returned document.
 * @param embeddings A list of document embeddings for each returned document.
 * @param documents A list of document contents for each returned document.
 * @param metadata A list of document metadata for each returned document.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetEmbeddingsResponse(
        List<List<String>> ids,
        List<List<List<Float>>> embeddings,
        List<List<String>> documents,
        @JsonProperty("metadatas")
        List<List<Map<String, Object>>> metadata
) {}
