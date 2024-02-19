package io.thomasvitale.langchain4j.spring.chroma.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Request to create a new Chroma collection.
 *
 * @param name The name of the collection to create.
 * @param metadata Metadata to associate with the collection. For example, you can use it
 * to customize the distance method of the embedding space.
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateCollectionRequest(String name, Map<String, String> metadata) {
    /**
     * A convenience constructor to create a new collection configured to sue cosine
     * similarity as the distance method.
     */
    public CreateCollectionRequest(String name) {
        this(name, Map.of("hnsw:space", "cosine"));
    }
}
