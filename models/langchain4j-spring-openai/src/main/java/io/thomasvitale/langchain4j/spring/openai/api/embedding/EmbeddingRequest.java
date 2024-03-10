package io.thomasvitale.langchain4j.spring.openai.api.embedding;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.util.Assert;

/**
 * Creates an embedding vector representing the input text (POST /v1/embeddings).
 *
 * @param input Input text to embed, encoded as a string or array of tokens. To embed multiple inputs
 *              in a single request, pass an array of strings or array of token arrays. The input
 *              must not exceed the max input tokens for the model (8192 tokens for text-embedding-ada-002),
 *              cannot be an empty string, and any array must be 2048 dimensions or less.
 * @param model ID of the model to use.
 * @param encodingFormat The format to return the embeddings in. Can be either float or base64.
 * @param dimensions The number of dimensions the resulting output embeddings should have. Only supported
 *                   in text-embedding-3 and later models.
 * @param user A unique identifier representing your end-user, which can help OpenAI to monitor
 *             and detect abuse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EmbeddingRequest(
        List<String> input,
        String model,
        String encodingFormat,
        Integer dimensions,
        String user
){

    public EmbeddingRequest {
        Assert.notEmpty(input, "Input must not be null or empty");
        Assert.isTrue(input.size() <= 2048, "Input must be 2048 dimensions or less");
        Assert.hasText(model, "Model must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> input;
        private String model = EmbeddingModels.TEXT_EMBEDDING_ADA_002.toString();
        private String encodingFormat = "float";
        private Integer dimensions;
        private String user;

        private Builder() {}

        public Builder input(List<String> input) {
            this.input = input;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(input, model, encodingFormat, dimensions, user);
        }
    }

}
