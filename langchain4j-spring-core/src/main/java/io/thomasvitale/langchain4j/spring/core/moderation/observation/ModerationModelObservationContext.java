package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.observation.Observation;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * An {@link Observation.Context} used during interactions with a moderation model.
 */
public class ModerationModelObservationContext extends ModelObservationContext {

    public ModerationModelObservationContext(String provider) {
        super(provider);
    }

}
