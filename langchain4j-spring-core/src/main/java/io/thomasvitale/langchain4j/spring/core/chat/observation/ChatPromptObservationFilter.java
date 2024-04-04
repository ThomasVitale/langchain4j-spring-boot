package io.thomasvitale.langchain4j.spring.core.chat.observation;

import java.util.List;

import dev.langchain4j.data.message.ChatMessage;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

import org.springframework.util.CollectionUtils;

/**
 * An {@link ObservationFilter} to populate chat prompt specific information.
 */
public class ChatPromptObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof ChatObservationContext chatModelObservationContext)) {
            return context;
        }

        if (CollectionUtils.isEmpty(chatModelObservationContext.getMessages())) {
            return chatModelObservationContext;
        }

        chatModelObservationContext.addHighCardinalityKeyValue(
            ChatObservation.ChatHighCardinalityTags.PROMPT.withValue(buildPrompt(chatModelObservationContext.getMessages()))
        );

        return chatModelObservationContext;
    }

    private String buildPrompt(List<ChatMessage> messages) {
        return messages.stream()
                .map(ChatMessage::toString)
                .reduce("", (a, b) -> a + " " + b);
    }

}
