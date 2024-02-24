package io.thomasvitale.langchain4j.spring.openai.api.moderation;

/**
 * Enum representing the available OpenAI moderation models.
 *
 * @author Thomas Vitale
 */
public enum ModerationModels {

    TEXT_MODERATION_STABLE("text-moderation-stable"),
    TEXT_MODERATION_LATEST("text-moderation-latest");

    private final String name;

    ModerationModels(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
