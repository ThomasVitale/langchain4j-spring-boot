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

import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatObservationContext;
import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatObservationConvention;
import io.thomasvitale.langchain4j.spring.core.chat.observation.DefaultChatObservationConvention;
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

    private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

    private ChatObservationConvention observationConvention = new DefaultChatObservationConvention();

    private OllamaChatModel(OllamaClient ollamaClient, String model, @Nullable String format, Options options) {
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");
        Assert.hasText(model, "model cannot be null or empty");
        Assert.notNull(ollamaClient, "ollamaClient cannot be null");

        this.ollamaClient = ollamaClient;
        this.model = model;
        this.format = format;
        this.options = options;
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

        ChatObservationContext observationContext = new ChatObservationContext("ollama");
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

    public void setObservationRegistry(ObservationRegistry observationRegistry) {
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");
        this.observationRegistry = observationRegistry;
    }

    public void setObservationConvention(ChatObservationConvention observationConvention) {
        Assert.notNull(observationConvention, "observationConvention cannot be null");
        this.observationConvention = observationConvention;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OllamaClient ollamaClient;
        private String model = DEFAULT_MODEL;
        private String format;
        private Options options = Options.builder().build();
        private ObservationRegistry observationRegistry;
        private ChatObservationConvention observationConvention;

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

        public Builder observationConvention(ChatObservationConvention observationConvention) {
            this.observationConvention = observationConvention;
            return this;
        }

        public OllamaChatModel build() {
            var chatModel = new OllamaChatModel(ollamaClient, model, format, options);
            if (observationConvention != null) {
                chatModel.setObservationConvention(observationConvention);
            }
            if (observationRegistry != null) {
                chatModel.setObservationRegistry(observationRegistry);
            }
            return chatModel;
        }
    }

    private static boolean isMessageWithSupportedType(ChatMessage chatMessage) {
        return chatMessage.type() == ChatMessageType.USER || chatMessage.type() == ChatMessageType.AI
                || chatMessage.type() == ChatMessageType.SYSTEM;
    }

}
