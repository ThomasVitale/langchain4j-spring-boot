package io.thomasvitale.langchain4j.spring.ollama;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.output.TokenUsage;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import io.thomasvitale.langchain4j.spring.ollama.api.ChatResponse;
import io.thomasvitale.langchain4j.spring.ollama.api.Message;

import static dev.langchain4j.data.message.ContentType.IMAGE;

/**
 * Utility class to convert between Ollama and Langchain4j types.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public final class OllamaAdapters {

    private static final List<String> SUPPORTED_URL_SCHEMES = Arrays.asList("http", "https", "file");

    public static Message.Role toOllamaRole(ChatMessage chatMessage) {
        return switch (chatMessage.type()) {
            case SYSTEM -> Message.Role.SYSTEM;
            case USER -> Message.Role.USER;
            case AI -> Message.Role.ASSISTANT;
            default -> throw new IllegalArgumentException("Unsupported message type: " + chatMessage.type());
        };
    }

    public static Message toOllamaMessage(ChatMessage chatMessage) {
        if (chatMessage instanceof SystemMessage systemMessage) {
            return Message.builder().role(toOllamaRole(chatMessage)).content(systemMessage.text()).build();
        } else if (chatMessage instanceof AiMessage aiMessage) {
            return Message.builder().role(toOllamaRole(chatMessage)).content(aiMessage.text()).build();
        } else if (chatMessage instanceof UserMessage userMessage) {
            if (userMessage.contents().stream().anyMatch(content -> IMAGE.equals(content.type()))) {
                return toMessageWithImage(userMessage);
            } else {
                return toMessageWithText(userMessage);
            }
        } else {
            throw new IllegalArgumentException("Unsupported message class: " + chatMessage.getClass().getSimpleName());
        }
    }

    public static TokenUsage toTokenUsage(ChatResponse chatResponse) {
        return new TokenUsage(chatResponse.promptEvalCount(), chatResponse.evalCount());
    }

    private static Message toMessageWithImage(UserMessage userMessage) {
        Map<ContentType, List<Content>> contentsByGroup = userMessage.contents()
            .stream()
            .collect(Collectors.groupingBy(Content::type));

        if (contentsByGroup.get(ContentType.TEXT).size() != 1) {
            throw new IllegalArgumentException("Expecting single text content, but got: " + userMessage.contents());
        }

        String textContents = ((TextContent) contentsByGroup.get(ContentType.TEXT).get(0)).text();

        List<ImageContent> imageContents = contentsByGroup.get(ContentType.IMAGE)
            .stream()
            .map(content -> (ImageContent) content)
            .collect(Collectors.toList());

        return Message.builder()
            .role(toOllamaRole(userMessage))
            .content(textContents)
            .images(toBase64EncodedImages(imageContents))
            .build();
    }

    private static Message toMessageWithText(UserMessage userMessage) {
        return Message.builder()
            .role(toOllamaRole(userMessage))
            .content(userMessage.contents()
                .stream()
                .map(content -> (TextContent) content)
                .map(TextContent::text)
                .collect(Collectors.joining("\n")))
            .build();
    }

    private static List<String> toBase64EncodedImages(List<ImageContent> imageContents) {
        return imageContents.stream()
            .map(ImageContent::image)
            .map(OllamaAdapters::toBase64EncodedImage)
            .collect(Collectors.toList());
    }

    private static String toBase64EncodedImage(Image image) {
        if (StringUtils.hasText(image.base64Data())) {
            return image.base64Data();
        } else {
            if (SUPPORTED_URL_SCHEMES.contains(image.url().getScheme())) {
                return image.url().getScheme().startsWith("http") ? encodeImageFromHttp(image)
                        : encodeImageFromFile(image);
            } else {
                throw new IllegalArgumentException(
                        "The Ollama integration supports only http/https and file URLs. Unsupported URL scheme: "
                                + image.url().getScheme());
            }
        }
    }

    private static String encodeImageFromHttp(Image image) {
        byte[] imageBytes = Utils.readBytes(image.url().toString());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static String encodeImageFromFile(Image image) {
        byte[] imageFileBytes;
        try {
            var imageFile = ResourceUtils.getFile(image.url());
            imageFileBytes = FileCopyUtils.copyToByteArray(imageFile);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read the image with path: %s".formatted(image.url()), ex);
        }
        return Base64.getEncoder().encodeToString(imageFileBytes);
    }

}
