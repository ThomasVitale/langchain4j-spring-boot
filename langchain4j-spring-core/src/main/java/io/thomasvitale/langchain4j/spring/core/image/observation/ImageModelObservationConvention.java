package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ImageModelObservationContext}.
 */
public interface ImageModelObservationConvention extends ObservationConvention<ImageModelObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ImageModelObservationContext;
    }

}
