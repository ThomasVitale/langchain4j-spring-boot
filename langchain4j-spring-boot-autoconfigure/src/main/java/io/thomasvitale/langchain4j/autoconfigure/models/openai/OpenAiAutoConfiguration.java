package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import java.util.Objects;

import io.micrometer.observation.ObservationRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.openai.OpenAIModerationModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiChatModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiEmbeddingModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiImageModel;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClientConfig;

/**
 * Auto-configuration for OpenAI clients and models.
 */
@AutoConfiguration(after = {RestClientAutoConfiguration.class})
@ConditionalOnClass({ OpenAiChatModel.class })
@ConditionalOnProperty(prefix = OpenAiProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ OpenAiProperties.class, OpenAiChatProperties.class, OpenAiEmbeddingProperties.class,
        OpenAiModerationProperties.class, OpenAiImageProperties.class })
public class OpenAiAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    OpenAiClient openAiClient(OpenAiProperties openAiProperties, RestClient.Builder restClientBuilder) {
        OpenAiClientConfig openAiClientConfig = OpenAiClientConfig.builder()
                .baseUrl(openAiProperties.getClient().getBaseUrl())
                .connectTimeout(openAiProperties.getClient().getConnectTimeout())
                .readTimeout(openAiProperties.getClient().getReadTimeout())
                .sslBundle(openAiProperties.getClient().getSslBundle())
                .apiKey(openAiProperties.getClient().getApiKey())
                .organizationId(openAiProperties.getClient().getOrganizationId())
                .user(openAiProperties.getClient().getUser())
                .logRequests(openAiProperties.getClient().isLogRequests())
                .logResponses(openAiProperties.getClient().isLogResponses())
                .build();

        if (openAiProperties.getClient().isLogRequests()) {
            logger.warn("You have enabled logging of the entire content of each prompt message sent to the model, with the risk of exposing sensitive or private information. Please, be careful!");
        }

        if (openAiProperties.getClient().isLogResponses()) {
            logger.warn("You have enabled logging of the entire response from a model, with the risk of exposing sensitive or private information. Please, be careful!");
        }

        return new OpenAiClient(openAiClientConfig, restClientBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiChatModel openAiChatModel(OpenAiClient openAiClient, OpenAiChatProperties openAiChatProperties, ObjectProvider<ObservationRegistry> observationRegistry) {
        return OpenAiChatModel.builder()
                .client(openAiClient)
                .options(openAiChatProperties.getOptions())
                .observationRegistry(Objects.requireNonNullElse(observationRegistry.getIfUnique(), ObservationRegistry.NOOP))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiEmbeddingModel openAiEmbeddingModel(OpenAiClient openAiClient, OpenAiEmbeddingProperties openAiEmbeddingProperties, ObjectProvider<ObservationRegistry> observationRegistry) {
        return OpenAiEmbeddingModel.builder()
                .client(openAiClient)
                .options(openAiEmbeddingProperties.getOptions())
                .observationRegistry(Objects.requireNonNullElse(observationRegistry.getIfUnique(), ObservationRegistry.NOOP))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiImageModel openAiImageModel(OpenAiClient openAiClient, OpenAiImageProperties openAiImageProperties, ObjectProvider<ObservationRegistry> observationRegistry) {
        return OpenAiImageModel.builder()
                .client(openAiClient)
                .options(openAiImageProperties.getOptions())
                .observationRegistry(Objects.requireNonNullElse(observationRegistry.getIfUnique(), ObservationRegistry.NOOP))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAIModerationModel openAiModerationModel(OpenAiClient openAiClient, OpenAiModerationProperties openAiModerationProperties, ObjectProvider<ObservationRegistry> observationRegistry) {
        return OpenAIModerationModel.builder()
                .client(openAiClient)
                .options(openAiModerationProperties.getOptions())
                .observationRegistry(Objects.requireNonNullElse(observationRegistry.getIfUnique(), ObservationRegistry.NOOP))
                .build();
    }

}
