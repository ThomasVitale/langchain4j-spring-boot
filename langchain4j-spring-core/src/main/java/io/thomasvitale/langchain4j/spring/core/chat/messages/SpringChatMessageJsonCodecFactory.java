package io.thomasvitale.langchain4j.spring.core.chat.messages;

import dev.langchain4j.data.message.ChatMessageJsonCodec;
import dev.langchain4j.spi.data.message.ChatMessageJsonCodecFactory;

import io.thomasvitale.langchain4j.spring.core.chat.messages.jackson.JacksonChatMessageJsonCodec;

/**
 * A factory for creating a {@link ChatMessageJsonCodec} instance.
 *
 * @author Thomas Vitale
 */
public class SpringChatMessageJsonCodecFactory implements ChatMessageJsonCodecFactory {

    @Override
    public ChatMessageJsonCodec create() {
        return new JacksonChatMessageJsonCodec();
    }

}
