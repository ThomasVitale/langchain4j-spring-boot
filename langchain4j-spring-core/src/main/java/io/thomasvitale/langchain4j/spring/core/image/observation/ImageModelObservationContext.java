package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.observation.Observation;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * An {@link Observation.Context} used during interactions with an image model.
 */
public class ImageModelObservationContext extends ModelObservationContext {

    @Nullable
    private Integer number;

    public ImageModelObservationContext(String provider) {
        super(provider);
    }

    @Nullable
    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        Assert.isTrue(number > 0, "number must be greater than 0");
        this.number = number;
    }
}
