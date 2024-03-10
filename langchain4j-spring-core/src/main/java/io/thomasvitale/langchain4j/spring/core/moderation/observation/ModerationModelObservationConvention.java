package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ModerationModelObservationContext}.
 */
public interface ModerationModelObservationConvention extends ObservationConvention<ModerationModelObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ModerationModelObservationContext;
    }

}
