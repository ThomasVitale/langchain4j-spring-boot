package io.thomasvitale.langchain4j.spring.core.embedding.observation;

import dev.langchain4j.model.output.TokenUsage;

import io.micrometer.observation.Observation;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * An {@link Observation.Context} used during interactions with an embedding model.
 */
public class EmbeddingModelObservationContext extends ModelObservationContext {

    @Nullable
    private TokenUsage tokenUsage;

    public EmbeddingModelObservationContext(String provider) {
        super(provider);
    }

    @Nullable
    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(TokenUsage tokenUsage) {
        Assert.notNull(tokenUsage, "tokenUsage cannot be null");
        this.tokenUsage = tokenUsage;
    }
}
