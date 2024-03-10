package io.thomasvitale.langchain4j.spring.openai.api.embedding;

/**
 * Enum representing the available OpenAI embedding models.
 */
public enum EmbeddingModels {

    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large");

    private final String name;

    EmbeddingModels(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
