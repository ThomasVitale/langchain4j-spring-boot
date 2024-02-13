package io.thomasvitale.langchain4j.spring.core.tool.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.agent.tool.ToolExecutionRequest;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ToolExecutionRequestMixin}.
 *
 * @author Thomas Vitale
 */
class ToolExecutionRequestMixinTests {

    private final ObjectMapper objectMapper = LangChain4jJacksonProvider.getObjectMapper();

    @Test
    void serializeAndDeserializeToolExecutionRequest() throws JsonProcessingException, JSONException {
        var toolExecutionRequest = ToolExecutionRequest.builder()
            .id("QUERY_DB")
            .name("queryDatabase")
            .arguments("{}")
            .build();

        var json = objectMapper.writeValueAsString(toolExecutionRequest);

        JSONAssert.assertEquals("""
                    {
                        "id": "QUERY_DB",
                        "name": "queryDatabase",
                        "arguments": "{}"
                    }
                """, json, JSONCompareMode.STRICT);

        var deserializedToolExecutionRequest = objectMapper.readValue(json, ToolExecutionRequest.class);
        assertThat(deserializedToolExecutionRequest).isEqualTo(toolExecutionRequest);
    }

}
