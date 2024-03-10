package io.thomasvitale.langchain4j.spring.ollama;

import java.util.List;
import java.util.stream.Collectors;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatModelObservationContext;
import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatModelObservationConvention;
import io.thomasvitale.langchain4j.spring.core.chat.observation.DefaultChatModelObservationConvention;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

/**
 * Model for chat-based language generation using Ollama.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class OllamaChatModel implements ChatLanguageModel {

    public static final String DEFAULT_MODEL = "llama2";

    private final OllamaClient ollamaClient;

    private final String model;

    @Nullable
    private final String format;

    private final Options options;

    private final ObservationRegistry observationRegistry;

    private final ChatModelObservationConvention observationConvention = new DefaultChatModelObservationConvention();

    private OllamaChatModel(OllamaClient ollamaClient, String model, @Nullable String format, Options options, ObservationRegistry observationRegistry) {
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.hasText(model, "model cannot be null or empty");
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");

        this.ollamaClient = ollamaClient;
        this.model = model;
        this.format = format;
        this.options = options;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        ChatRequest chatRequest = ChatRequest.builder()
            .messages(messages.stream()
                .filter(OllamaChatModel::isMessageWithSupportedType)
                .map(OllamaAdapters::toOllamaMessage)
                .collect(Collectors.toList()))
            .model(model)
            .format(format)
            .options(options.toMap())
            .stream(false)
            .build();

        ChatModelObservationContext observationContext = new ChatModelObservationContext("ollama");
        observationContext.setModel(model);
        observationContext.setMessages(messages);
        observationContext.setTemperature(options.getTemperature());

        Response<AiMessage> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            ChatResponse chatResponse = ollamaClient.chat(chatRequest);

            if (chatResponse == null) {
                return null;
            }

            TokenUsage tokenUsage = OllamaAdapters.toTokenUsage(chatResponse);

            observationContext.setTokenUsage(tokenUsage);

            AiMessage aiMessage = AiMessage.from(chatResponse.message().content());
            return Response.from(aiMessage, tokenUsage);
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OllamaClient ollamaClient;
        private String model = DEFAULT_MODEL;
        @Nullable
        private String format;
        private Options options = Options.builder().build();
        private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

        private Builder() {}

        public Builder client(OllamaClient ollamaClient) {
            this.ollamaClient = ollamaClient;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder options(Options options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public OllamaChatModel build() {
            return new OllamaChatModel(ollamaClient, model, format, options, observationRegistry);
        }
    }

    private static boolean isMessageWithSupportedType(ChatMessage chatMessage) {
        return chatMessage.type() == ChatMessageType.USER || chatMessage.type() == ChatMessageType.AI
                || chatMessage.type() == ChatMessageType.SYSTEM;
    }

}
