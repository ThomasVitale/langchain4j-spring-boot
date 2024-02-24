package io.thomasvitale.langchain4j.spring.chroma.api;

import java.util.Map;

/**
 * Chroma collection of embeddings.
 *
 * @param id The collection id
 * @param name The name of the collection
 * @param metadata Metadata associated to the collection
 *
 * @author Thomas Vitale
 */
public record Collection(
        String id,
        String name,
        Map<String, String> metadata
) {}
