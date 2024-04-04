package io.thomasvitale.langchain4j.spring.core.embedding.observation;

import dev.langchain4j.model.output.TokenUsage;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * Observation context for embedding model interactions.
 */
public class EmbeddingObservationContext extends ModelObservationContext {

    @Nullable
    private TokenUsage tokenUsage;

    public EmbeddingObservationContext(String provider) {
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
