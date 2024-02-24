package io.thomasvitale.langchain4j.spring.ollama.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Message in a chat request.
 *
 * @param role the role of the message, either system, user or assistant
 * @param content the content of the message
 * @param images a list of images to include in the message (for multimodal models such as llava)
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Message(
        Role role,
        String content,
        List<String> images
) {

    public Message {
        Assert.notNull(role, "role must not be null");
        Assert.hasText(content, "content must not be null or empty");
    }

    public enum Role {
        @JsonProperty("system") SYSTEM,
        @JsonProperty("user") USER,
        @JsonProperty("assistant") ASSISTANT;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Role role;
        private String content;
        private List<String> images;

        private Builder() {}

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder images(List<String> images) {
            this.images = images;
            return this;
        }

        public Message build() {
            return new Message(role, content, images);
        }
    }

}
