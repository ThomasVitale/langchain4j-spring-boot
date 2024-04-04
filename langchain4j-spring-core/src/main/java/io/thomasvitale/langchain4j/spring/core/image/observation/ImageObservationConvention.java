package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ImageObservationContext}.
 */
public interface ImageObservationConvention extends ObservationConvention<ImageObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ImageObservationContext;
    }

}
