package io.thomasvitale.langchain4j.spring.core.embedding.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link EmbeddingModelObservationConvention} implementation.
 */
public final class DefaultEmbeddingModelObservationConvention implements EmbeddingModelObservationConvention {

    static final String OBSERVATION_NAME = "langchain4j.embedding";

    @Override
    public String getName() {
        return OBSERVATION_NAME;
    }

    @Override
    public String getContextualName(EmbeddingModelObservationContext context) {
        return "langchain4j embedding %s".formatted(context.getProvider());
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(EmbeddingModelObservationContext context) {
        return KeyValues.of("embedding.provider", context.getProvider());
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(EmbeddingModelObservationContext context) {
        var keyValues = KeyValues.of("embedding.model", getModel(context));
        keyValues = addTokenUsage(keyValues, context);
        return keyValues;
    }

    private String getModel(EmbeddingModelObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private KeyValues addTokenUsage(KeyValues keyValues, EmbeddingModelObservationContext context) {
        if (context.getTokenUsage() != null) {
            if (context.getTokenUsage().inputTokenCount() != null) {
                keyValues = keyValues.and("embedding.usage.tokens.input", String.valueOf(context.getTokenUsage().inputTokenCount()));
            }
            if (context.getTokenUsage().outputTokenCount() != null) {
                keyValues = keyValues.and("embedding.usage.tokens.output", String.valueOf(context.getTokenUsage().outputTokenCount()));
            }
            if (context.getTokenUsage().totalTokenCount() != null) {
                keyValues = keyValues.and("embedding.usage.tokens.total", String.valueOf(context.getTokenUsage().totalTokenCount()));
            }
        }
        return keyValues;
    }

}
