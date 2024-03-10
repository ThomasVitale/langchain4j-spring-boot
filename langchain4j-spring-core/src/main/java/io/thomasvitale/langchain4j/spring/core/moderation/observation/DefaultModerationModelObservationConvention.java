package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ModerationModelObservationConvention} implementation.
 */
public final class DefaultModerationModelObservationConvention implements ModerationModelObservationConvention {

    static final String OBSERVATION_NAME = "langchain4j.moderation";

    @Override
    public String getName() {
        return OBSERVATION_NAME;
    }

    @Override
    public String getContextualName(ModerationModelObservationContext context) {
        return "langchain4j moderation %s".formatted(context.getProvider());
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ModerationModelObservationContext context) {
        return KeyValues.of("moderation.provider", context.getProvider());
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(ModerationModelObservationContext context) {
        return KeyValues.of("moderation.model", getModel(context));
    }

    private String getModel(ModerationModelObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

}
