package io.thomasvitale.langchain4j.spring.core.chat.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ChatModelObservationContext}.
 */
public interface ChatModelObservationConvention extends ObservationConvention<ChatModelObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ChatModelObservationContext;
    }

}
