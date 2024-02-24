package io.thomasvitale.langchain4j.spring.ollama.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Map;

/**
 * Generate embeddings from a prompt with a provided model (POST /api/embeddings).
 *
 * @param model name of model to generate embeddings from
 * @param prompt text to generate embeddings for
 * @param keepAlive controls how long the model will stay loaded into memory
 *                  following the request (default: 5m)
 * @param options (optional) additional model parameters listed in the documentation for
 *                the Modelfile such as temperature
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EmbeddingRequest(
        String model,
        String prompt,
        Duration keepAlive,
        Map<String, Object> options
) {

    public EmbeddingRequest {
        Assert.hasText(model, "model must not be null or empty");
        Assert.hasText(prompt, "prompt must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String model;
        private String prompt;
        private Duration keepAlive;
        private Map<String, Object> options;

        private Builder() {}

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder keepAlive(Duration keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder options(Map<String, Object> options) {
            this.options = options;
            return this;
        }

        public Builder options(Options options) {
            this.options = options.toMap();
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(model, prompt, keepAlive, options);
        }
    }

}
