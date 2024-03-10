package io.thomasvitale.langchain4j.spring.ollama.api;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.util.Assert;

/**
 * Generate the next message in a chat with a provided model (POST /api/chat).
 *
 * @param model the model name
 * @param messages the messages of the chat, this can be used to keep a chat memory
 * @param stream if false the response will be returned as a single response object,
 *               rather than a stream of objects
 * @param format the format to return a response in. Currently, the only
 *               accepted value is 'json'
 * @param keepAlive controls how long the model will stay loaded into memory
 *                  following the request (default: 5m)
 * @param options additional model parameters listed in the documentation for
 *                the Modelfile such as temperature
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatRequest(
        String model,
        List<Message> messages,
        Boolean stream,
        String format,
        Duration keepAlive,
        Map<String, Object> options
) {

    public ChatRequest {
        Assert.hasText(model, "model must not be null or empty");
        Assert.notEmpty(messages, "messages must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String model;
        private List<Message> messages;
        private boolean stream = false;
        private String format;
        private Duration keepAlive;
        private Map<String, Object> options;

        private Builder() {}

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Builder stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
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

        public ChatRequest build() {
            return new ChatRequest(model, messages, stream, format, keepAlive, options);
        }
    }

}
