package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ModerationObservationConvention} implementation.
 */
public final class DefaultModerationObservationConvention implements ModerationObservationConvention {

    @Override
    public String getName() {
        return ModerationObservation.IMAGE_OBSERVATION.getName();
    }

    @Override
    public String getContextualName(ModerationObservationContext context) {
        return ModerationObservation.IMAGE_OBSERVATION.getContextualName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ModerationObservationContext context) {
        return KeyValues.of(
                ModerationObservation.ModerationLowCardinalityTags.MODEL_PROVIDER.withValue(context.getProvider()),
                ModerationObservation.ModerationLowCardinalityTags.MODEL_NAME.withValue(getModel(context))
        );
    }

    private String getModel(ModerationObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

}
