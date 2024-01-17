package io.thomasvitale.langchain4j.autoconfigure.openai;

import dev.langchain4j.model.openai.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass({OpenAiChatModel.class})
@EnableConfigurationProperties({OpenAiProperties.class, OpenAiChatProperties.class,
        OpenAiEmbeddingProperties.class, OpenAiModerationProperties.class, OpenAiImageProperties.class})
public class OpenAiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    OpenAiChatModel openAiChatModel(OpenAiProperties openAiProperties, OpenAiChatProperties openAiChatProperties) {
        return OpenAiChatModel.builder()
                .apiKey(openAiProperties.getApiKey())
                .baseUrl(openAiProperties.getBaseUrl())
                .organizationId(openAiProperties.getOrganizationId())
                .modelName(openAiChatProperties.getModel())
                .temperature(openAiChatProperties.getTemperature())
                .topP(openAiChatProperties.getTopP())
                .maxTokens(openAiChatProperties.getMaxTokens())
                .presencePenalty(openAiChatProperties.getPresencePenalty())
                .frequencyPenalty(openAiChatProperties.getFrequencyPenalty())
                .timeout(openAiProperties.getTimeout())
                .maxRetries(openAiProperties.getMaxRetries())
                .user(openAiProperties.getUser())
                .logRequests(openAiProperties.getLogRequests())
                .logResponses(openAiProperties.getLogResponses())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiStreamingChatModel openAiStreamingChatModel(OpenAiProperties openAiProperties, OpenAiChatProperties openAiChatProperties) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(openAiProperties.getApiKey())
                .baseUrl(openAiProperties.getBaseUrl())
                .organizationId(openAiProperties.getOrganizationId())
                .modelName(openAiChatProperties.getModel())
                .temperature(openAiChatProperties.getTemperature())
                .topP(openAiChatProperties.getTopP())
                .maxTokens(openAiChatProperties.getMaxTokens())
                .presencePenalty(openAiChatProperties.getPresencePenalty())
                .frequencyPenalty(openAiChatProperties.getFrequencyPenalty())
                .timeout(openAiProperties.getTimeout())
                .user(openAiProperties.getUser())
                .logRequests(openAiProperties.getLogRequests())
                .logResponses(openAiProperties.getLogResponses())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiEmbeddingModel openAiEmbeddingModel(OpenAiProperties openAiProperties, OpenAiEmbeddingProperties openAiEmbeddingProperties) {
        return OpenAiEmbeddingModel.builder()
                .baseUrl(openAiProperties.getBaseUrl())
                .apiKey(openAiProperties.getApiKey())
                .organizationId(openAiProperties.getOrganizationId())
                .modelName(openAiEmbeddingProperties.getModel())
                .timeout(openAiProperties.getTimeout())
                .maxRetries(openAiProperties.getMaxRetries())
                .user(openAiProperties.getUser())
                .logRequests(openAiProperties.getLogRequests())
                .logResponses(openAiProperties.getLogResponses())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiImageModel openAiImageModel(OpenAiProperties openAiProperties, OpenAiImageProperties openAiImageProperties) {
        return OpenAiImageModel.builder()
                .baseUrl(openAiProperties.getBaseUrl())
                .apiKey(openAiProperties.getApiKey())
                .organizationId(openAiProperties.getOrganizationId())
                .modelName(openAiImageProperties.getModel())
                .size(openAiImageProperties.getSize())
                .quality(openAiImageProperties.getQuality())
                .style(openAiImageProperties.getStyle())
                .user(openAiProperties.getUser())
                .responseFormat(openAiImageProperties.getResponseFormat())
                .timeout(openAiProperties.getTimeout())
                .maxRetries(openAiProperties.getMaxRetries())
                .logRequests(openAiProperties.getLogRequests())
                .logResponses(openAiProperties.getLogResponses())
                .withPersisting(openAiImageProperties.getPersist())
                .persistTo(openAiImageProperties.getPersistDirectory())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiModerationModel openAiModerationModel(OpenAiProperties openAiProperties, OpenAiModerationProperties openAiModerationProperties) {
        return OpenAiModerationModel.builder()
                .baseUrl(openAiProperties.getBaseUrl())
                .apiKey(openAiProperties.getApiKey())
                .organizationId(openAiProperties.getOrganizationId())
                .modelName(openAiModerationProperties.getModel())
                .timeout(openAiProperties.getTimeout())
                .maxRetries(openAiProperties.getMaxRetries())
                .logRequests(openAiProperties.getLogRequests())
                .logResponses(openAiProperties.getLogResponses())
                .build();
    }

}
