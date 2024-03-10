package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.image.observation.DefaultImageModelObservationConvention;
import io.thomasvitale.langchain4j.spring.core.image.observation.ImageModelObservationContext;
import io.thomasvitale.langchain4j.spring.core.image.observation.ImageModelObservationConvention;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationRequest;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

/**
 * Model for generating images using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class OpenAiImageModel implements ImageModel {

    private final OpenAiClient openAiClient;

    private final OpenAiImageOptions options;

    private final ObservationRegistry observationRegistry;

    private final ImageModelObservationConvention observationConvention = new DefaultImageModelObservationConvention();

    private OpenAiImageModel(OpenAiClient openAiClient, OpenAiImageOptions options, ObservationRegistry observationRegistry) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public Response<Image> generate(String prompt) {
        ImageGenerationRequest request = imageGenerationRequestBuilder(prompt).build();

        ImageModelObservationContext observationContext = new ImageModelObservationContext("openai");
        observationContext.setModel(options.getModel());
        observationContext.setNumber(1);

        Response<Image> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            ImageGenerationResponse response = openAiClient.imageGeneration(request);

            if (response == null) {
                return null;
            }

            return Response.from(OpenAiAdapters.toImage(response.data().get(0)));
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    @Override
    public Response<List<Image>> generate(String prompt, int n) {
        ImageGenerationRequest request = imageGenerationRequestBuilder(prompt).n(n).build();

        ImageModelObservationContext observationContext = new ImageModelObservationContext("openai");
        observationContext.setModel(options.getModel());
        observationContext.setNumber(n);

        Response<List<Image>> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            ImageGenerationResponse response = openAiClient.imageGeneration(request);

            if (response == null) {
                return null;
            }

            return Response.from(response.data().stream().map(OpenAiAdapters::toImage).toList());
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
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
        private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;;

        private Builder() {}

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiImageOptions options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public OpenAiImageModel build() {
            return new OpenAiImageModel(openAiClient, options, observationRegistry);
        }
    }

}
