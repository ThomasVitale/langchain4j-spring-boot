package io.thomasvitale.langchain4j.spring.openai.api.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.util.Assert;

/**
 * Creates an image given a prompt (POST /v1/images/generations).
 *
 * @param prompt A text description of the desired image(s). The maximum length is 1000 characters
 *               for 'dall-e-2' and 4000 characters for 'dall-e-3'.
 * @param model The model to use for image generation.
 * @param n The number of images to generate. Must be between 1 and 10. For 'dall-e-3',
 *          only 'n=1' is supported.
 * @param quality The quality of the image that will be generated. 'hd' creates images with finer details
 *                and greater consistency across the image. This param is only supported for 'dall-e-3'.
 * @param responseFormat The format in which the generated images are returned.
 *                       Must be one of 'url' or 'b64_json'.
 * @param size The size of the generated images. Must be one of '256x256', '512x512', or '1024x1024'
 *             for 'dall-e-2'. Must be one of '1024x1024', '1792x1024', or '1024x1792' for 'dall-e-3' models.
 * @param style The style of the generated images. Must be one of 'vivid' or 'natural'. Vivid causes the model
 *              to lean towards generating hyper-real and dramatic images. Natural causes the model
 *              to produce more natural, less hyper-real looking images.
 *              This param is only supported for 'dall-e-3'.
 * @param user A unique identifier representing your end-user, which can help OpenAI to monitor
 *             and detect abuse.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ImageGenerationRequest(
        String prompt,
        String model,
        Integer n,
        String quality,
        String responseFormat,
        String size,
        String style,
        String user
) {

    public ImageGenerationRequest {
        Assert.hasText(prompt, "prompt must not be null or empty");
        Assert.hasText(model, "model must not be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String prompt;
        private String model = ImageModels.DALL_E_2.toString();
        private Integer n = 1;
        private String quality;
        private String responseFormat;
        private String size;
        private String style;
        private String user;

        private Builder() {}

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder n(Integer n) {
            this.n = n;
            return this;
        }

        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public Builder responseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public ImageGenerationRequest build() {
            return new ImageGenerationRequest(prompt, model, n, quality, responseFormat, size, style, user);
        }
    }

}
