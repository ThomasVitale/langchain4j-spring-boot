package io.thomasvitale.langchain4j.spring.openai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingRequest;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

/**
 * Model for embedding documents using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class OpenAiEmbeddingModel implements EmbeddingModel {

    private final OpenAiClient openAiClient;

    private final OpenAiEmbeddingOptions options;

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

        return Response.from(embeddings, new TokenUsage(promptTokens.get()));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private OpenAiClient openAiClient;

        private OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder().build();

        private Builder() {
        }

        public Builder client(OpenAiClient openAiClient) {
            Assert.notNull(openAiClient, "openAiClient cannot be null");
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiEmbeddingOptions options) {
            Assert.notNull(options, "options cannot be null");
            this.options = options;
            return this;
        }

        public OpenAiEmbeddingModel build() {
            return new OpenAiEmbeddingModel(openAiClient, options);
        }

    }

}
