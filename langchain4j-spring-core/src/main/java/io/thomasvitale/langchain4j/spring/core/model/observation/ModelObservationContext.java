package io.thomasvitale.langchain4j.spring.core.model.observation;

import io.micrometer.observation.Observation;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Abstract {@link Observation.Context} to use for observing LangChain4j model interactions.
 */
public abstract class ModelObservationContext extends Observation.Context {

    private final String provider;
    @Nullable
    private String model;

    public ModelObservationContext(String provider) {
        Assert.hasText(provider, "provider cannot be null or empty");
        this.provider = provider.toLowerCase();
    }

    public String getProvider() {
        return provider;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        Assert.hasText(model, "model cannot be null or empty");
        this.model = model;
    }
}
