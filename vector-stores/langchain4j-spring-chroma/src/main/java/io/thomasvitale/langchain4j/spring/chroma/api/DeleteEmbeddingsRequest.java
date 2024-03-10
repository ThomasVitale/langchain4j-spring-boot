package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.util.Assert;

/**
 * Request to delete embeddings from a Chroma collection.
 *
 * @param ids The ids of the embeddings to delete.
 * @param where Optional where clause to filter the embeddings to delete based on metadata values.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeleteEmbeddingsRequest(
        List<String> ids,
        Map<String, Object> where
) {
    public DeleteEmbeddingsRequest {
        Assert.notEmpty(ids, "Ids must not be empty");
    }

    // TODO: Add support for whereDocument
    public DeleteEmbeddingsRequest(List<String> ids) {
        this(ids, Map.of());
    }
}
