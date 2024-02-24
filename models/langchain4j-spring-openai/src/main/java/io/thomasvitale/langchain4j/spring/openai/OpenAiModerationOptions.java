package io.thomasvitale.langchain4j.spring.openai;

import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationModels;

/**
 * Options for customizing interactions with the OpenAI Moderation API.
 *
 * @author Thomas Vitale
 */
public class OpenAiModerationOptions {

    /**
     * ID of the model to use.
     */
    private String model = ModerationModels.TEXT_MODERATION_STABLE.toString();

    // Builders

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OpenAiModerationOptions options = new OpenAiModerationOptions();

        private Builder() {}

        public Builder model(String model) {
            this.options.model = model;
            return this;
        }

        public OpenAiModerationOptions build() {
            return this.options;
        }
    }

    // Getters and Setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
