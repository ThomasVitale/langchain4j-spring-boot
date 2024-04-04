package io.thomasvitale.langchain4j.spring.core.embedding.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link EmbeddingObservationConvention} implementation.
 */
public final class DefaultEmbeddingObservationConvention implements EmbeddingObservationConvention {

    @Override
    public String getName() {
        return EmbeddingObservation.EMBEDDING_OBSERVATION.getName();
    }

    @Override
    public String getContextualName(EmbeddingObservationContext context) {
        return EmbeddingObservation.EMBEDDING_OBSERVATION.getContextualName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(EmbeddingObservationContext context) {
        return KeyValues.of(
                EmbeddingObservation.EmbeddingLowCardinalityTags.MODEL_PROVIDER.withValue(context.getProvider()),
                EmbeddingObservation.EmbeddingLowCardinalityTags.MODEL_NAME.withValue(getModel(context))
        );
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(EmbeddingObservationContext context) {
        return addTokenUsage(KeyValues.empty(), context);
    }

    private String getModel(EmbeddingObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private KeyValues addTokenUsage(KeyValues keyValues, EmbeddingObservationContext context) {
        if (context.getTokenUsage() != null) {
            if (context.getTokenUsage().inputTokenCount() != null) {
                keyValues = keyValues.and(EmbeddingObservation.EmbeddingHighCardinalityTags.USAGE_TOKENS_INPUT.withValue(String.valueOf(context.getTokenUsage().inputTokenCount())));
            }
            if (context.getTokenUsage().outputTokenCount() != null) {
                keyValues = keyValues.and(EmbeddingObservation.EmbeddingHighCardinalityTags.USAGE_TOKENS_OUTPUT.withValue(String.valueOf(context.getTokenUsage().outputTokenCount())));
            }
            if (context.getTokenUsage().totalTokenCount() != null) {
                keyValues = keyValues.and(EmbeddingObservation.EmbeddingHighCardinalityTags.USAGE_TOKENS_TOTAL.withValue(String.valueOf(context.getTokenUsage().totalTokenCount())));
            }
        }
        return keyValues;
    }

}
