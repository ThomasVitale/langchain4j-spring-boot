package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationRequest;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

/**
 * Model for generating images using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class OpenAiImageModel implements ImageModel {

    private final OpenAiClient openAiClient;

    private final OpenAiImageOptions options;

    private OpenAiImageModel(OpenAiClient openAiClient, OpenAiImageOptions options) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
    }

    @Override
    public Response<Image> generate(String prompt) {
        ImageGenerationRequest request = imageGenerationRequestBuilder(prompt).build();

        ImageGenerationResponse response = openAiClient.imageGeneration(request);

        if (response == null) {
            throw new IllegalStateException("Image response is empty");
        }

        return Response.from(OpenAiAdapters.toImage(response.data().get(0)));
    }

    @Override
    public Response<List<Image>> generate(String prompt, int n) {
        ImageGenerationRequest request = imageGenerationRequestBuilder(prompt).n(n).build();

        ImageGenerationResponse response = openAiClient.imageGeneration(request);

        if (response == null) {
            throw new IllegalStateException("Image response is empty");
        }

        return Response.from(response.data().stream().map(OpenAiAdapters::toImage).toList());
    }

    private ImageGenerationRequest.Builder imageGenerationRequestBuilder(String prompt) {
        return ImageGenerationRequest.builder()
                .prompt(prompt)
                .model(options.getModel())
                .n(options.getN())
                .quality(options.getQuality())
                .responseFormat(options.getResponseFormat())
                .size(options.getSize())
                .style(options.getStyle())
                .user(options.getUser());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiImageOptions options = new OpenAiImageOptions();

        private Builder() {}

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiImageOptions options) {
            this.options = options;
            return this;
        }

        public OpenAiImageModel build() {
            return new OpenAiImageModel(openAiClient, options);
        }
    }

}
