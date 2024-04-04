package io.thomasvitale.langchain4j.spring.core.image.observation;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * Observation context for image model interactions.
 */
public class ImageObservationContext extends ModelObservationContext {

    @Nullable
    private Integer number;

    public ImageObservationContext(String provider) {
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
