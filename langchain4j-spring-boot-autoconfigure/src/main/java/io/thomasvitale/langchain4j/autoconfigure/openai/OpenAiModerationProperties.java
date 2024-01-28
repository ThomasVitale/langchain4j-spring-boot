package io.thomasvitale.langchain4j.autoconfigure.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for OpenAI moderation clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OpenAiModerationProperties.CONFIG_PREFIX)
public class OpenAiModerationProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.moderation";

    /**
     * Name of the model to use.
     */
    private String model = "text-moderation-latest";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
