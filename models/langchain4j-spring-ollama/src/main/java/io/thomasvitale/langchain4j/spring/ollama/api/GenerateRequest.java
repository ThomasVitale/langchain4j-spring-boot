package io.thomasvitale.langchain4j.spring.ollama.api;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Generate a completion for a given prompt with a provided model (POST /api/generate).
 *
 * @param model (required) the model name
 * @param prompt (required) the prompt to generate a response for
 * @param system (optional) system message to use (overrides what is defined in the
 * Modelfile)
 * @param template (optional) the prompt template to use (overrides what is defined in the
 * Modelfile)
 * @param context (optional) the context parameter returned from a previous request to
 * <code>/generate</code>, this can be used to keep a short conversational memory
 * @param stream (optional) if false the response will be returned as a single response
 * object, rather than a stream of objects
 * @param raw (optional) if true no formatting will be applied to the prompt. You may
 * choose to use the raw parameter if you are specifying a full templated prompt in your
 * request to the API
 * @param format (optional) the format to return a response in. Currently, the only
 * accepted value is <code>json</code>
 * @param keepAlive (optional) controls how long the model will stay loaded into memory
 * following the request (default: 5m)
 * @param images (optional) a list of base64-encoded images (for multimodal models such as
 * llava)
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
public record GenerateRequest(
// @formatter:off
        String model,
        String prompt,
        String system,
        String template,
        List<Integer> context,
        Boolean stream,
        Boolean raw,
        String format,
        Duration keepAlive,
        List<byte[]> images,
        Map<String, Object> options
// @formatter:on
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String model;

        private String prompt;

        private String system;

        private String template;

        private List<Integer> context;

        private Boolean stream;

        private Boolean raw;

        private String format;

        private Duration keepAlive;

        private List<byte[]> images;

        private Map<String, Object> options;

        private Builder() {
        }

        public Builder withModel(String model) {
            this.model = model;
            return this;
        }

        public Builder withPrompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder withSystem(String system) {
            this.system = system;
            return this;
        }

        public Builder withTemplate(String template) {
            this.template = template;
            return this;
        }

        public Builder withContext(List<Integer> context) {
            this.context = context;
            return this;
        }

        public Builder withStream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder withRaw(Boolean raw) {
            this.raw = raw;
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

        public Builder withImages(List<byte[]> images) {
            this.images = images;
            return this;
        }

        public Builder withOptions(Map<String, Object> options) {
            this.options = options;
            return this;
        }

        public GenerateRequest build() {
            return new GenerateRequest(model, prompt, system, template, context, stream, raw, format, keepAlive, images,
                    options);
        }

    }
}
