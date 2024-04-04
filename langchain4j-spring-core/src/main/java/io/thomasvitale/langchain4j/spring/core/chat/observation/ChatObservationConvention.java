package io.thomasvitale.langchain4j.spring.core.chat.observation;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link ChatObservationContext}.
 */
public interface ChatObservationConvention extends ObservationConvention<ChatObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof ChatObservationContext;
    }

}
