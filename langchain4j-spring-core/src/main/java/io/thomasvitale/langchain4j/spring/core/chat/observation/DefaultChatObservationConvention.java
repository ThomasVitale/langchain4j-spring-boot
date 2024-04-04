package io.thomasvitale.langchain4j.spring.core.chat.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ChatObservationConvention} implementation.
 */
public class DefaultChatObservationConvention implements ChatObservationConvention {

    @Override
    public String getName() {
        return ChatObservation.CHAT_OBSERVATION.getName();
    }

    @Override
    public String getContextualName(ChatObservationContext context) {
        return ChatObservation.CHAT_OBSERVATION.getContextualName();
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ChatObservationContext context) {
        return KeyValues.of(
                ChatObservation.ChatLowCardinalityTags.MODEL_PROVIDER.withValue(context.getProvider()),
                ChatObservation.ChatLowCardinalityTags.MODEL_NAME.withValue(getModel(context))
        );
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(ChatObservationContext context) {
        var keyValues = KeyValues.of(
                ChatObservation.ChatHighCardinalityTags.MODEL_TEMPERATURE.withValue(getTemperature(context))
        );

        keyValues = addTokenUsage(keyValues, context);
        keyValues = addFinishReason(keyValues, context);

        return keyValues;
    }

    private String getModel(ChatObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private String getTemperature(ChatObservationContext context) {
        return String.valueOf(context.getTemperature());
    }

    private KeyValues addTokenUsage(KeyValues keyValues, ChatObservationContext context) {
        if (context.getTokenUsage() != null) {
            if (context.getTokenUsage().inputTokenCount() != null) {
                keyValues = keyValues.and(ChatObservation.ChatHighCardinalityTags.USAGE_TOKENS_INPUT.withValue(String.valueOf(context.getTokenUsage().inputTokenCount())));
            }
            if (context.getTokenUsage().outputTokenCount() != null) {
                keyValues = keyValues.and(ChatObservation.ChatHighCardinalityTags.USAGE_TOKENS_OUTPUT.withValue(String.valueOf(context.getTokenUsage().outputTokenCount())));
            }
            if (context.getTokenUsage().totalTokenCount() != null) {
                keyValues = keyValues.and(ChatObservation.ChatHighCardinalityTags.USAGE_TOKENS_TOTAL.withValue(String.valueOf(context.getTokenUsage().totalTokenCount())));
            }
        }
        return keyValues;
    }

    private KeyValues addFinishReason(KeyValues keyValues, ChatObservationContext context) {
        if (context.getFinishReason() != null) {
            keyValues = keyValues.and(ChatObservation.ChatHighCardinalityTags.FINISH_REASON.withValue(String.valueOf(context.getFinishReason()).toLowerCase()));
        }
        return keyValues;
    }

}
