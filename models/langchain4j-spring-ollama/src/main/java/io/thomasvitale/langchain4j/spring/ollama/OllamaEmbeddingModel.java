package io.thomasvitale.langchain4j.spring.ollama;

import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.embedding.observation.DefaultEmbeddingModelObservationConvention;
import io.thomasvitale.langchain4j.spring.core.embedding.observation.EmbeddingModelObservationContext;
import io.thomasvitale.langchain4j.spring.core.embedding.observation.EmbeddingModelObservationConvention;
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

    private final ObservationRegistry observationRegistry;

    private final EmbeddingModelObservationConvention observationConvention = new DefaultEmbeddingModelObservationConvention();

    private OllamaEmbeddingModel(OllamaClient ollamaClient, String model, Options options, ObservationRegistry observationRegistry) {
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.hasText(model, "model cannot be null or empty");
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");

        this.ollamaClient = ollamaClient;
        this.model = model;
        this.options = options;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<Embedding> embeddings = new ArrayList<>();

        EmbeddingModelObservationContext observationContext = new EmbeddingModelObservationContext("ollama");
        observationContext.setModel(model);

        Response<List<Embedding>> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            textSegments.forEach(textSegment -> {
                EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                        .model(model)
                        .prompt(textSegment.text())
                        .options(options)
                        .build();

                EmbeddingResponse embeddingResponse = ollamaClient.embeddings(embeddingRequest);

                if (embeddingResponse == null) {
                    throw new IllegalStateException("Embedding response is empty");
                }

                embeddings.add(Embedding.from(embeddingResponse.embedding()));
            });

            return Response.from(embeddings);
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OllamaClient ollamaClient;
        private String model = DEFAULT_MODEL;
        private Options options = Options.builder().build();
        private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

        private Builder() {}

        public Builder client(OllamaClient ollamaClient) {
            this.ollamaClient = ollamaClient;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder options(Options options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public OllamaEmbeddingModel build() {
            return new OllamaEmbeddingModel(ollamaClient, model, options, observationRegistry);
        }
    }

}
