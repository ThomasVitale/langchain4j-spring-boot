package io.thomasvitale.langchain4j.spring.ollama.api;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.util.Assert;

/**
 * Generate a completion for a given prompt with a provided model (POST /api/generate).
 *
 * @param model the model name
 * @param prompt the prompt to generate a response for
 * @param system system message to use (overrides what is defined in the Modelfile)
 * @param template the prompt template to use (overrides what is defined in the Modelfile)
 * @param context the context parameter returned from a previous request to '/generate',
 *                this can be used to keep a short conversational memory
 * @param stream if false the response will be returned as a single response object,
 *               rather than a stream of objects
 * @param raw if true no formatting will be applied to the prompt. You may choose to use
 *            the raw parameter if you are specifying a full templated prompt in your
 *            request to the API
 * @param format the format to return a response in. Currently, the only accepted value is 'json'
 * @param keepAlive controls how long the model will stay loaded into memory following
 *                  the request (default: 5m)
 * @param images a list of base64-encoded images (for multimodal models such as llava)
 * @param options additional model parameters listed in the documentation for
 *                the Modelfile such as temperature
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GenerateRequest(
        String model,
        String prompt,
        String system,
        String template,
        List<Integer> context,
        Boolean stream,
        Boolean raw,
        String format,
        Duration keepAlive,
        List<String> images,
        Map<String, Object> options
) {

    public GenerateRequest {
        Assert.hasText(model, "model must not be null or empty");
        Assert.hasText(prompt, "prompt must not be null or empty");
    }

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
        private List<String> images;
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

        public Builder system(String system) {
            this.system = system;
            return this;
        }

        public Builder template(String template) {
            this.template = template;
            return this;
        }

        public Builder context(List<Integer> context) {
            this.context = context;
            return this;
        }

        public Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        public Builder raw(Boolean raw) {
            this.raw = raw;
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

        public Builder images(List<String> images) {
            this.images = images;
            return this;
        }

        public Builder options(Map<String, Object> options) {
            this.options = options;
            return this;
        }

        public GenerateRequest build() {
            return new GenerateRequest(model, prompt, system, template, context, stream, raw, format, keepAlive, images,
                    options);
        }
    }

}
