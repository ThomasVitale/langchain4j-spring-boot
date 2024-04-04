package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.image.observation.DefaultImageObservationConvention;
import io.thomasvitale.langchain4j.spring.core.image.observation.ImageObservationContext;
import io.thomasvitale.langchain4j.spring.core.image.observation.ImageObservationConvention;
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

    private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

    private ImageObservationConvention observationConvention = new DefaultImageObservationConvention();

    private OpenAiImageModel(OpenAiClient openAiClient, OpenAiImageOptions options) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
    }

    @Override
    public Response<Image> generate(String prompt) {
        ImageGenerationRequest request = imageGenerationRequestBuilder(prompt).build();

        ImageObservationContext observationContext = new ImageObservationContext("openai");
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

        ImageObservationContext observationContext = new ImageObservationContext("openai");
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

    public void setObservationRegistry(ObservationRegistry observationRegistry) {
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");
        this.observationRegistry = observationRegistry;
    }

    public void setObservationConvention(ImageObservationConvention observationConvention) {
        Assert.notNull(observationConvention, "observationConvention cannot be null");
        this.observationConvention = observationConvention;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiImageOptions options = new OpenAiImageOptions();
        private ObservationRegistry observationRegistry;
        private ImageObservationConvention observationConvention;

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

        public Builder observationConvention(ImageObservationConvention observationConvention) {
            this.observationConvention = observationConvention;
            return this;
        }

        public OpenAiImageModel build() {
            var imageModel = new OpenAiImageModel(openAiClient, options);
            if (observationConvention != null) {
                imageModel.setObservationConvention(observationConvention);
            }
            if (observationRegistry != null) {
                imageModel.setObservationRegistry(observationRegistry);
            }
            return imageModel;
        }
    }

}
