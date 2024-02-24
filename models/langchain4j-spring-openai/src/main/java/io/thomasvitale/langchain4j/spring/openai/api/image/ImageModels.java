package io.thomasvitale.langchain4j.spring.openai.api.image;

/**
 * Enum representing the available OpenAI image models.
 *
 * @author Thomas Vitale
 */
public enum ImageModels {

    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3");

    private final String name;

    ImageModels(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
