package io.thomasvitale.langchain4j.spring.ollama;

import java.util.List;
import java.util.stream.Collectors;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.ollama.api.ChatRequest;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

/**
 * Model for chat-based language generation using Ollama.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class OllamaChatModel implements ChatLanguageModel {

    public static final String DEFAULT_MODEL = "llama2";

    private final OllamaClient ollamaClient;

    private final String model;

    @Nullable
    private final String format;

    private final Options options;

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
            .withMessages(messages.stream()
                .filter(OllamaChatModel::isMessageWithSupportedType)
                .map(OllamaAdapters::toOllamaMessage)
                .collect(Collectors.toList()))
            .withModel(model)
            .withFormat(format)
            .withOptions(options.toMap())
            .withStream(false)
            .build();

        ChatResponse chatResponse = ollamaClient.chat(chatRequest);

        AiMessage aiMessage = AiMessage.from(chatResponse.message().content());
        return Response.from(aiMessage, OllamaAdapters.toTokenUsage(chatResponse));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private OllamaClient ollamaClient;

        private String model = DEFAULT_MODEL;

        @Nullable
        private String format;

        private Options options = Options.create();

        private Builder() {
        }

        public Builder client(OllamaClient ollamaClient) {
            Assert.notNull(ollamaClient, "ollamaClient cannot be null");
            this.ollamaClient = ollamaClient;
            return this;
        }

        public Builder model(String model) {
            Assert.hasText(model, "model cannot be empty");
            this.model = model;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder options(Options options) {
            Assert.notNull(ollamaClient, "options cannot be null");
            this.options = options;
            return this;
        }

        public OllamaChatModel build() {
            return new OllamaChatModel(ollamaClient, model, format, options);
        }

    }

    private static boolean isMessageWithSupportedType(ChatMessage chatMessage) {
        return chatMessage.type() == ChatMessageType.USER || chatMessage.type() == ChatMessageType.AI
                || chatMessage.type() == ChatMessageType.SYSTEM;
    }

}
