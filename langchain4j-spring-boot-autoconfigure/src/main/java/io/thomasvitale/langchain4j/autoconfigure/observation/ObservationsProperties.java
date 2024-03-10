package io.thomasvitale.langchain4j.autoconfigure.observation;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for LangChain4j observations.
 */
@ConfigurationProperties(ObservationsProperties.CONFIG_PREFIX)
public class ObservationsProperties {

    public static final String CONFIG_PREFIX = "langchain4j.observations";

    /**
     * Whether to include the prompt messages in the observations.
     */
    private boolean includePromptMessages = false;

    public boolean isIncludePromptMessages() {
        return includePromptMessages;
    }

    public void setIncludePromptMessages(boolean includePromptMessages) {
        this.includePromptMessages = includePromptMessages;
    }
}
