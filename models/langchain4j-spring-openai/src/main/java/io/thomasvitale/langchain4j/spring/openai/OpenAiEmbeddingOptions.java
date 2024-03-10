package io.thomasvitale.langchain4j.spring.openai;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingModels;

/**
 * Options for customizing interactions with the OpenAI Embedding API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpenAiEmbeddingOptions {

    /**
     * ID of the model to use.
     */
    private String model = EmbeddingModels.TEXT_EMBEDDING_ADA_002.toString();
    /**
     * The format to return the embeddings in. Can be either 'float' or 'base64'.
     */
    private String encodingFormat = "float";
    /**
     * The number of dimensions the resulting output embeddings should have. Only supported in text-embedding-3 and later models.
     */
    private Integer dimensions;
    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     */
    private String user;

    // Builders

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected final OpenAiEmbeddingOptions options = new OpenAiEmbeddingOptions();

        private Builder() {
        }

        public Builder model(String model) {
            options.model = model;
            return this;
        }

        public Builder encodingFormat(String encodingFormat) {
            options.encodingFormat = encodingFormat;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            options.dimensions = dimensions;
            return this;
        }

        public Builder user(String user) {
            options.user = user;
            return this;
        }

        public OpenAiEmbeddingOptions build() {
            return options;
        }
    }

    // Getters and Setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEncodingFormat() {
        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    public Integer getDimensions() {
        return dimensions;
    }

    public void setDimensions(Integer dimensions) {
        this.dimensions = dimensions;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
