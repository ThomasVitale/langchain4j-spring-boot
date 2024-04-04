package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ImageObservationConvention} implementation.
 */
public final class DefaultImageObservationConvention implements ImageObservationConvention {

    @Override
    public String getName() {
        return ImageObservation.IMAGE_OBSERVATION.getName();
    }

    @Override
    public String getContextualName(ImageObservationContext context) {
        return ImageObservation.IMAGE_OBSERVATION.getContextualName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ImageObservationContext context) {
        return KeyValues.of(
                ImageObservation.ImageLowCardinalityTags.MODEL_PROVIDER.withValue(context.getProvider()),
                ImageObservation.ImageLowCardinalityTags.MODEL_NAME.withValue(getModel(context))
        );
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(ImageObservationContext context) {
        var keyValues = KeyValues.empty();
        keyValues = addImageNumber(keyValues, context);
        return keyValues;
    }

    private String getModel(ImageObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private KeyValues addImageNumber(KeyValues keyValues, ImageObservationContext context) {
        if (context.getNumber() != null) {
            keyValues = keyValues.and(ImageObservation.ImageHighCardinalityTags.IMAGE_NUMBER.withValue(String.valueOf(context.getNumber())));
        }
        return keyValues;
    }

}
