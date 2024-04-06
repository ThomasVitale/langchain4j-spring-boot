package io.thomasvitale.langchain4j.spring.openai.api.chat;

/**
 * Enum representing the available OpenAI chat models.
 */
public enum ChatModels {

    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),

    GPT_4("gpt-4"),
    GPT_4_32K("gpt-4-32k"),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview"),
    GPT_4_1106_PREVIEW("gpt-4-1106-preview");

    private final String name;

    ChatModels(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
