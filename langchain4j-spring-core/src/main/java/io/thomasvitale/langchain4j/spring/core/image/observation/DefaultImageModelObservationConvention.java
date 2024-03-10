package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ImageModelObservationConvention} implementation.
 */
public final class DefaultImageModelObservationConvention implements ImageModelObservationConvention {

    static final String OBSERVATION_NAME = "langchain4j.image";

    @Override
    public String getName() {
        return OBSERVATION_NAME;
    }

    @Override
    public String getContextualName(ImageModelObservationContext context) {
        return "langchain4j image %s".formatted(context.getProvider());
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ImageModelObservationContext context) {
        return KeyValues.of("image.provider", context.getProvider());
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(ImageModelObservationContext context) {
        var keyValues = KeyValues.of("image.model", getModel(context));
        keyValues = addImageNumber(keyValues, context);
        return keyValues;
    }

    private String getModel(ImageModelObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private KeyValues addImageNumber(KeyValues keyValues, ImageModelObservationContext context) {
        if (context.getNumber() != null) {
            keyValues = keyValues.and("image.number", String.valueOf(context.getNumber()));
        }
        return keyValues;
    }

}
