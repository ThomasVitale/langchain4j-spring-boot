package io.thomasvitale.langchain4j.spring.core.chat.messages;

import org.junit.jupiter.api.Test;

import io.thomasvitale.langchain4j.spring.core.chat.messages.jackson.JacksonChatMessageJsonCodec;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SpringChatMessageJsonCodecFactory}.
 */
class SpringChatMessageJsonCodecFactoryTests {

    @Test
    void factoryProducesJacksonImplementation() {
        var chatMessageJsonCodecFactory = new SpringChatMessageJsonCodecFactory();
        var chatMessageJsonCodec = chatMessageJsonCodecFactory.create();
        assertThat(chatMessageJsonCodec).isInstanceOf(JacksonChatMessageJsonCodec.class);
    }

}
