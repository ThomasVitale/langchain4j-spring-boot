package io.thomasvitale.langchain4j.spring.ollama;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.ollama.api.EmbeddingRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

/**
 * Model for embedding documents using Ollama.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class OllamaEmbeddingModel implements EmbeddingModel {

    public static final String DEFAULT_MODEL = "llama2";

    private final OllamaClient ollamaClient;

    private final String model;

    private final Options options;

    private OllamaEmbeddingModel(OllamaClient ollamaClient, String model, Options options) {
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.hasText(model, "model cannot be null or empty");
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");

        this.ollamaClient = ollamaClient;
        this.model = model;
        this.options = options;
    }

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<Embedding> embeddings = new ArrayList<>();

        textSegments.forEach(textSegment -> {
            EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                .withModel(model)
                .withPrompt(textSegment.text())
                .withOptions(options)
                .build();

            EmbeddingResponse embeddingResponse = ollamaClient.embeddings(embeddingRequest);

            embeddings.add(Embedding.from(embeddingResponse.embedding()));
        });

        return Response.from(embeddings);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private OllamaClient ollamaClient;

        private String model = DEFAULT_MODEL;

        private Options options = Options.create();

        private Builder() {
        }

        public Builder client(OllamaClient ollamaClient) {
            Assert.notNull(ollamaClient, "ollamaClient cannot be null");
            this.ollamaClient = ollamaClient;
            return this;
        }

        public Builder model(String model) {
            Assert.hasText(model, "model cannot be empty");
            this.model = model;
            return this;
        }

        public Builder options(Options options) {
            Assert.notNull(options, "options cannot be null");
            this.options = options;
            return this;
        }

        public OllamaEmbeddingModel build() {
            return new OllamaEmbeddingModel(ollamaClient, model, options);
        }

    }

}
