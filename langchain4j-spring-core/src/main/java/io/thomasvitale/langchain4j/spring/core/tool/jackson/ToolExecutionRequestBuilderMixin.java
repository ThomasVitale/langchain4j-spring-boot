package io.thomasvitale.langchain4j.spring.core.tool.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import dev.langchain4j.agent.tool.ToolExecutionRequest;

/**
 * Mixin used to configure {@link ToolExecutionRequest.Builder} for
 * serialization/deserialization.
 *
 * @author Thomas Vitale
 */
@JsonPOJOBuilder(withPrefix = "")
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ToolExecutionRequestBuilderMixin {

}
