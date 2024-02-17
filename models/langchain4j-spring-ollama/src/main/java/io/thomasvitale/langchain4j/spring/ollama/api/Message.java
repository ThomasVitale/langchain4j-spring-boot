package io.thomasvitale.langchain4j.spring.ollama.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Message in a chat request.
 *
 * @param role the role of the message, either system, user or assistant
 * @param content the content of the message
 * @param images (optional) a list of images to include in the message (for multimodal
 * models such as llava)
 * <p>
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Message(
// @formatter:off
        Role role,
        String content,
        List<String> images
// @formatter:on
) {
    public enum Role {

    // @formatter:off
        @JsonProperty("system") SYSTEM,
        @JsonProperty("user") USER,
        @JsonProperty("assistant") ASSISTANT;
    // @formatter:on

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Role role;

        private String content;

        private List<String> images;

        private Builder() {
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withImages(List<String> images) {
            this.images = images;
            return this;
        }

        public Message build() {
            return new Message(role, content, images);
        }

    }
}
