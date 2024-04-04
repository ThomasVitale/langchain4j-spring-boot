package io.thomasvitale.langchain4j.spring.openai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.embedding.observation.DefaultEmbeddingObservationConvention;
import io.thomasvitale.langchain4j.spring.core.embedding.observation.EmbeddingObservationContext;
import io.thomasvitale.langchain4j.spring.core.embedding.observation.EmbeddingObservationConvention;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingRequest;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

/**
 * Model for embedding documents using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class OpenAiEmbeddingModel implements EmbeddingModel {

    private final OpenAiClient openAiClient;

    private final OpenAiEmbeddingOptions options;

    private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

    private EmbeddingObservationConvention observationConvention = new DefaultEmbeddingObservationConvention();

    private OpenAiEmbeddingModel(OpenAiClient openAiClient, OpenAiEmbeddingOptions options) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
    }

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        List<Embedding> embeddings = new ArrayList<>();
        AtomicInteger promptTokens = new AtomicInteger();

        EmbeddingObservationContext observationContext = new EmbeddingObservationContext("openai");
        observationContext.setModel(options.getModel());

        Response<List<Embedding>> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            textSegments.forEach(textSegment -> {
                EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                        .input(List.of(textSegment.text()))
                        .model(options.getModel())
                        .encodingFormat(options.getEncodingFormat())
                        .dimensions(options.getDimensions())
                        .user(options.getUser())
                        .build();

                EmbeddingResponse embeddingResponse = openAiClient.embeddings(embeddingRequest);

                if (embeddingResponse == null) {
                    throw new IllegalStateException("Embedding response is empty");
                }

                promptTokens.addAndGet(embeddingResponse.usage().promptTokens());

                embeddings.addAll(embeddingResponse.data().stream()
                        .map(OpenAiAdapters::toEmbedding)
                        .toList());
            });

            TokenUsage tokenUsage = new TokenUsage(promptTokens.get());

            observationContext.setTokenUsage(tokenUsage);

            return Response.from(embeddings, tokenUsage);
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    public void setObservationRegistry(ObservationRegistry observationRegistry) {
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");
        this.observationRegistry = observationRegistry;
    }

    public void setObservationConvention(EmbeddingObservationConvention observationConvention) {
        Assert.notNull(observationConvention, "observationConvention cannot be null");
        this.observationConvention = observationConvention;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder().build();
        private ObservationRegistry observationRegistry;
        private EmbeddingObservationConvention observationConvention;

        private Builder() {
        }

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiEmbeddingOptions options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public Builder observationConvention(EmbeddingObservationConvention observationConvention) {
            this.observationConvention = observationConvention;
            return this;
        }

        public OpenAiEmbeddingModel build() {
            var embeddingModel = new OpenAiEmbeddingModel(openAiClient, options);
            if (observationConvention != null) {
                embeddingModel.setObservationConvention(observationConvention);
            }
            if (observationRegistry != null) {
                embeddingModel.setObservationRegistry(observationRegistry);
            }
            return embeddingModel;
        }
    }

}
