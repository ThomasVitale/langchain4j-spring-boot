package io.thomasvitale.langchain4j.spring.openai.api.embedding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.thomasvitale.langchain4j.spring.openai.api.Usage;

/**
 * A collection of embedding vectors representing the input data.
 *
 * @param object The object type, which is always "list".
 * @param data List of embedding objects.
 * @param model The model used for generating the embeddings.
 * @param usage Usage statistics for the embedding request.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmbeddingResponse(
        String object,
        List<EmbeddingData> data,
        String model,
        Usage usage
) {
    /**
     * An embedding vector representing the input data.
     *
     * @param index The index of the embedding in the list of embeddings.
     * @param embedding The embedding vector, which is a list of floats.
     *                  The length of vector depends on the model.
     * @param object The object type, which is always "embedding".
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EmbeddingData(
            Integer index,
            List<Double> embedding,
            String object
    ) {
        public EmbeddingData(Integer index, List<Double> embedding) {
            this(index, embedding, "embedding");
        }
    }

}
