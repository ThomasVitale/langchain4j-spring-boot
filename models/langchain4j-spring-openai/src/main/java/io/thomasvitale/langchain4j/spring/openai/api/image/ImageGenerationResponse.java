package io.thomasvitale.langchain4j.spring.openai.api.image;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A collection of images generated from a prompt.
 *
 * @param created Creation date.
 * @param data List of image objects.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageGenerationResponse(
        Long created,
        List<ImageData> data
) {

    /**
     * Represents the url or the content of an image generated by the OpenAI API.
     *
     * @param b64Json The base64-encoded JSON of the generated image, if 'response_format' is 'b64_json'.
     * @param url The URL of the generated image, if 'response_format' is 'url' (default).
     * @param revisedPrompt The prompt that was used to generate the image, if there was any revision to the prompt.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ImageData(
            String b64Json,
            String url,
            String revisedPrompt
    ) {}

}