package io.thomasvitale.langchain4j.spring.chroma.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of the possible fields included in a response from Chroma when performing
 * operations on Collections. The 'ids' field is always included.
 */
public enum Include {

    @JsonProperty("metadatas")
    METADATAS,

    @JsonProperty("documents")
    DOCUMENTS,

    @JsonProperty("embeddings")
    EMBEDDINGS,

    @JsonProperty("distances")
    DISTANCES;

}
