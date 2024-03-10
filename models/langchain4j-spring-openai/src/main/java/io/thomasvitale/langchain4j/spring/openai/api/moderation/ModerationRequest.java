package io.thomasvitale.langchain4j.spring.openai.api.moderation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.util.Assert;

/**
 * Classifies if text violates OpenAI's Content Policy (POST /v1/moderations).
 *
 * @param input The input text to classify.
 * @param model Two content moderations models are available: 'text-moderation-stable' and
 *              'text-moderation-latest'. The default is 'text-moderation-latest' which will be
 *              automatically upgraded over time. This ensures you are always using
 *              our most accurate model. If you use 'text-moderation-stable', we will provide
 *              advanced notice before updating the model. Accuracy of 'text-moderation-stable'
 *              may be slightly lower than for 'text-moderation-latest'.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModerationRequest(
        List<String> input,
        String model
) {

    public ModerationRequest {
        Assert.notEmpty(input, "Input must not be null or empty");
        Assert.hasText(model, "Model must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> input;
        private String model = ModerationModels.TEXT_MODERATION_LATEST.toString();

        private Builder() {}

        public Builder input(List<String> input) {
            this.input = input;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public ModerationRequest build() {
            return new ModerationRequest(input, model);
        }
    }

}
