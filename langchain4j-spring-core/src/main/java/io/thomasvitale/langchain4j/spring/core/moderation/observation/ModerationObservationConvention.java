package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ModerationObservationContext}.
 */
public interface ModerationObservationConvention extends ObservationConvention<ModerationObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ModerationObservationContext;
    }

}
