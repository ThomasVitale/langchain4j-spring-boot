package io.thomasvitale.langchain4j.spring.core.embedding.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link EmbeddingModelObservationContext}.
 */
public interface EmbeddingModelObservationConvention extends ObservationConvention<EmbeddingModelObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof EmbeddingModelObservationContext;
    }

}
