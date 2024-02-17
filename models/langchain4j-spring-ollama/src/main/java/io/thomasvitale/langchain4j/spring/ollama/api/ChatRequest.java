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
 * @param model (required) the model name
 * @param messages the messages of the chat, this can be used to keep a chat memory
 * @param stream if false the response will be returned as a single response object,
 * rather than a stream of objects
 * @param format (optional) the format to return a response in. Currently, the only
 * accepted value is <code>json</code>
 * @param keepAlive (optional) controls how long the model will stay loaded into memory
 * following the request (default: 5m)
 * @param options (optional) additional model parameters listed in the documentation for
 * the Modelfile such as temperature
 * <p>
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatRequest(
// @formatter:off
        String model,
        List<Message> messages,
        Boolean stream,
        String format,
        Duration keepAlive,
        Map<String, Object> options
// @formatter:on
) {

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

        private Builder() {
        }

        public Builder withModel(String model) {
            this.model = model;
            return this;
        }

        public Builder withMessages(List<Message> messages) {
            Assert.notEmpty(messages, "Messages must not be empty");
            this.messages = messages;
            return this;
        }

        public Builder withStream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder withFormat(String format) {
            this.format = format;
            return this;
        }

        public Builder withKeepAlive(Duration keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder withOptions(Map<String, Object> options) {
            this.options = options;
            return this;
        }

        public Builder withOptions(Options options) {
            this.options = options.toMap();
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(model, messages, stream, format, keepAlive, options);
        }

    }
}
