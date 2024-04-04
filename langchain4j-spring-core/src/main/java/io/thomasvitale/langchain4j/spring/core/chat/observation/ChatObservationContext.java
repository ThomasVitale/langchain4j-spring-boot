package io.thomasvitale.langchain4j.spring.core.chat.observation;

import java.util.List;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.model.observation.ModelObservationContext;

/**
 * Observation context for chat model interactions.
 */
public class ChatObservationContext extends ModelObservationContext {

    @Nullable
    private Double temperature;
    @Nullable
    private FinishReason finishReason;
    @Nullable
    private List<ChatMessage> messages;
    @Nullable
    private TokenUsage tokenUsage;

    public ChatObservationContext(String provider) {
        super(provider);
    }

    @Nullable
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        Assert.notNull(temperature, "temperature cannot be null");
        this.temperature = temperature;
    }

    @Nullable
    public FinishReason getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(FinishReason finishReason) {
        Assert.notNull(finishReason, "finishReason cannot be null");
        this.finishReason = finishReason;
    }

    @Nullable
    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        Assert.notEmpty(messages, "messages cannot be empty");
        this.messages = messages;
    }

    @Nullable
    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(TokenUsage tokenUsage) {
        Assert.notNull(tokenUsage, "tokenUsage cannot be null");
        this.tokenUsage = tokenUsage;
    }

}
