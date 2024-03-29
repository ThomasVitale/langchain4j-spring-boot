package io.thomasvitale.langchain4j.spring.ollama.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents the response from the Ollama API for embedding requests to /api/embeddings.
 *
 * @param embedding the embedding representation generated by the model
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmbeddingResponse(
        List<Float> embedding
) {}
