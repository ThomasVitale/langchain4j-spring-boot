package io.thomasvitale.langchain4j.autoconfigure.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OpenAiEmbeddingProperties.CONFIG_PREFIX)
public class OpenAiEmbeddingProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.embedding";

    private String model = "text-embedding-ada-002";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
