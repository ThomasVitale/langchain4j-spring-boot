package io.thomasvitale.langchain4j.autoconfigure.models.openai;

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
 *
 * @author Thomas Vitale
 */
@AutoConfiguration(after = RestClientAutoConfiguration.class)
@ConditionalOnClass({ OpenAiChatModel.class })
@ConditionalOnProperty(prefix = OpenAiProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties({ OpenAiProperties.class, OpenAiChatProperties.class, OpenAiEmbeddingProperties.class,
        OpenAiModerationProperties.class, OpenAiImageProperties.class })
public class OpenAiAutoConfiguration {

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

        return new OpenAiClient(openAiClientConfig, restClientBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiChatModel openAiChatModel(OpenAiClient openAiClient, OpenAiChatProperties openAiChatProperties) {
        return OpenAiChatModel.builder()
                .client(openAiClient)
                .options(openAiChatProperties.getOptions())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiEmbeddingModel openAiEmbeddingModel(OpenAiClient openAiClient, OpenAiEmbeddingProperties openAiEmbeddingProperties) {
        return OpenAiEmbeddingModel.builder()
                .client(openAiClient)
                .options(openAiEmbeddingProperties.getOptions())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAiImageModel openAiImageModel(OpenAiClient openAiClient, OpenAiImageProperties openAiImageProperties) {
        return OpenAiImageModel.builder()
                .client(openAiClient)
                .options(openAiImageProperties.getOptions())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OpenAIModerationModel openAiModerationModel(OpenAiClient openAiClient, OpenAiModerationProperties openAiModerationProperties) {
        return OpenAIModerationModel.builder()
                .client(openAiClient)
                .options(openAiModerationProperties.getOptions())
                .build();
    }

}
