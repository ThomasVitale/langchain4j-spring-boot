package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * Observation context for moderation model interactions.
 */
public class ModerationObservationContext extends ModelObservationContext {

    public ModerationObservationContext(String provider) {
        super(provider);
    }

}
