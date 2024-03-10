package io.thomasvitale.langchain4j.spring.core.chat.observation;

import io.micrometer.common.KeyValues;

import org.springframework.util.StringUtils;

/**
 * Default {@link ChatModelObservationConvention} implementation.
 */
public final class DefaultChatModelObservationConvention implements ChatModelObservationConvention {

    static final String OBSERVATION_NAME = "langchain4j.chat";

    @Override
    public String getName() {
        return OBSERVATION_NAME;
    }

    @Override
    public String getContextualName(ChatModelObservationContext context) {
        return "langchain4j chat %s".formatted(context.getProvider());
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ChatModelObservationContext context) {
        return KeyValues.of("chat.provider", context.getProvider());
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(ChatModelObservationContext context) {
        var keyValues = KeyValues.of("chat.model", getModel(context))
                .and("chat.temperature", getTemperature(context));

        keyValues = addTokenUsage(keyValues, context);
        keyValues = addFinishReason(keyValues, context);

        return keyValues;
    }

    private String getModel(ChatModelObservationContext context) {
        return StringUtils.hasText(context.getModel()) ? context.getModel() : "unknown";
    }

    private String getTemperature(ChatModelObservationContext context) {
        return String.valueOf(context.getTemperature());
    }

    private KeyValues addTokenUsage(KeyValues keyValues, ChatModelObservationContext context) {
        if (context.getTokenUsage() != null) {
            if (context.getTokenUsage().inputTokenCount() != null) {
                keyValues = keyValues.and("chat.usage.tokens.input", String.valueOf(context.getTokenUsage().inputTokenCount()));
            }
            if (context.getTokenUsage().outputTokenCount() != null) {
                keyValues = keyValues.and("chat.usage.tokens.output", String.valueOf(context.getTokenUsage().outputTokenCount()));
            }
            if (context.getTokenUsage().totalTokenCount() != null) {
                keyValues = keyValues.and("chat.usage.tokens.total", String.valueOf(context.getTokenUsage().totalTokenCount()));
            }
        }
        return keyValues;
    }

    private KeyValues addFinishReason(KeyValues keyValues, ChatModelObservationContext context) {
        if (context.getFinishReason() != null) {
            keyValues = keyValues.and("chat.finish.reason", String.valueOf(context.getFinishReason()).toLowerCase());
        }
        return keyValues;
    }

}
