package io.thomasvitale.langchain4j.spring.core.chat.messages.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.UserMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMessageMixin}.
 *
 * @author Thomas Vitale
 */
class UserMessageMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeUserMessageWithTextContent() throws JsonProcessingException, JSONException {
        var message = UserMessage.from("isabelle", "Simple question");

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "contents": [
                          {
                            "text": "Simple question",
                            "type": "TEXT"
                          }
                        ],
                        "name": "isabelle",
                        "type": "USER"
                      }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, UserMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

    @Test
    void serializeAndDeserializeUserMessageWithImageContent() throws JsonProcessingException, JSONException {
        var message = UserMessage.from("isabelle", ImageContent.from("http://example.net"));

        var json = objectMapper.writeValueAsString(message);

        JSONAssert.assertEquals("""
                    {
                        "contents": [
                          {
                            "detailLevel": "LOW",
                            "image": {
                                "url": "http://example.net"
                            },
                            "type": "IMAGE"
                          }
                        ],
                        "name": "isabelle",
                        "type": "USER"
                      }
                """, json, JSONCompareMode.STRICT);

        var deserializedMessage = objectMapper.readValue(json, UserMessage.class);
        assertThat(deserializedMessage).isEqualTo(message);
    }

}
