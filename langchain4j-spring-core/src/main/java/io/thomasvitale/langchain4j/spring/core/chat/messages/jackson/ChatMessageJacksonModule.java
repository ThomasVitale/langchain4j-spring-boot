package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonModules;

/**
 * Jackson module for the chat message functionality of LangChain4j.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new ChatMessageJacksonModule());
 * </pre>
 *
 * <b>Note: use {@link LangChain4jJacksonModules#getModules()} to get the list of all
 * LangChain4j Jackson modules.</b>
 *
 * @see LangChain4jJacksonModules
 */
public class ChatMessageJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(AiMessage.class, AiMessageMixin.class);
        context.setMixInAnnotations(ChatMessage.class, ChatMessageMixin.class);
        context.setMixInAnnotations(Content.class, ContentMixin.class);
        context.setMixInAnnotations(ImageContent.class, ImageContentMixin.class);
        context.setMixInAnnotations(SystemMessage.class, SystemMessageMixin.class);
        context.setMixInAnnotations(TextContent.class, TextContentMixin.class);
        context.setMixInAnnotations(ToolExecutionResultMessage.class, ToolExecutionResultMessageMixin.class);
        context.setMixInAnnotations(UserMessage.class, UserMessageMixin.class);
    }

}
