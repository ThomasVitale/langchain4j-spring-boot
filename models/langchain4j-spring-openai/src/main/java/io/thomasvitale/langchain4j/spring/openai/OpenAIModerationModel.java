package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.output.Response;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.util.Assert;

import io.thomasvitale.langchain4j.spring.core.moderation.observation.DefaultModerationObservationConvention;
import io.thomasvitale.langchain4j.spring.core.moderation.observation.ModerationObservationContext;
import io.thomasvitale.langchain4j.spring.core.moderation.observation.ModerationObservationConvention;
import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationRequest;
import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

import static java.util.stream.Collectors.toList;

/**
 * Model for moderating content using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class OpenAIModerationModel implements ModerationModel {

    private final OpenAiClient openAiClient;

    private final OpenAiModerationOptions options;

    private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

    private ModerationObservationConvention observationConvention = new DefaultModerationObservationConvention();

    private OpenAIModerationModel(OpenAiClient openAiClient, OpenAiModerationOptions options) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
    }

    @Override
    public Response<Moderation> moderate(String text) {
        return moderateInternal(List.of(text));
    }

    @Override
    public Response<Moderation> moderate(List<ChatMessage> messages) {
        List<String> inputs = messages.stream()
                .map(ChatMessage::text)
                .collect(toList());

        return moderateInternal(inputs);
    }

    private Response<Moderation> moderateInternal(List<String> inputs) {
        ModerationRequest request = ModerationRequest.builder()
                .model(options.getModel())
                .input(inputs)
                .build();

        ModerationObservationContext observationContext = new ModerationObservationContext("openai");
        observationContext.setModel(options.getModel());

        Response<Moderation> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            ModerationResponse response = openAiClient.moderation(request);

            if (response == null) {
                return null;
            }

            int i = 0;
            for (ModerationResponse.ModerationData moderationData : response.results()) {
                if (moderationData.flagged()) {
                    return Response.from(Moderation.flagged(inputs.get(i)));
                }
                i++;
            }

            return Response.from(Moderation.notFlagged());
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    public void setObservationRegistry(ObservationRegistry observationRegistry) {
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");
        this.observationRegistry = observationRegistry;
    }

    public void setObservationConvention(ModerationObservationConvention observationConvention) {
        Assert.notNull(observationConvention, "observationConvention cannot be null");
        this.observationConvention = observationConvention;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiModerationOptions options = new OpenAiModerationOptions();
        private ObservationRegistry observationRegistry;
        private ModerationObservationConvention observationConvention;

        private Builder() {}

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiModerationOptions options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public Builder observationConvention(ModerationObservationConvention observationConvention) {
            this.observationConvention = observationConvention;
            return this;
        }

        public OpenAIModerationModel build() {
            var moderationModel = new OpenAIModerationModel(openAiClient, options);
            if (observationConvention != null) {
                moderationModel.setObservationConvention(observationConvention);
            }
            if (observationRegistry != null) {
                moderationModel.setObservationRegistry(observationRegistry);
            }
            return moderationModel;
        }
    }

}
