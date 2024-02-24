package io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore;
import io.thomasvitale.langchain4j.spring.weaviate.client.WeaviateClientConfig;

/**
 * Auto-configuration for Weaviate vector store.
 *
 * @author Thomas Vitale
 */
@AutoConfiguration
@ConditionalOnClass(WeaviateEmbeddingStore.class)
@EnableConfigurationProperties({ WeaviateProperties.class })
public class WeaviateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(WeaviateConnectionDetails.class)
    PropertiesWeaviateConnectionDetails propertiesWeaviateConnectionDetails(WeaviateProperties weaviateProperties) {
        return new PropertiesWeaviateConnectionDetails(weaviateProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    WeaviateEmbeddingStore weaviateEmbeddingStore(WeaviateConnectionDetails weaviateConnectionDetails, WeaviateProperties weaviateProperties) {
        var clientConfig = WeaviateClientConfig.builder()
                .scheme(weaviateConnectionDetails.getScheme())
                .host(weaviateConnectionDetails.getHost())
                .connectTimeout(weaviateProperties.getClient().getConnectTimeout())
                .readTimeout(weaviateProperties.getClient().getReadTimeout())
                .sslBundle(weaviateProperties.getClient().getSslBundle())
                .apiKey(weaviateConnectionDetails.getApiKey())
                .headers(weaviateProperties.getClient().getHeaders())
                .build();

        return WeaviateEmbeddingStore.builder()
                .clientConfig(clientConfig)
                .objectClassName(weaviateProperties.getObjectClassName())
                .consistencyLevel(weaviateProperties.getConsistencyLevel())
                .build();
    }

    /**
     * Adapts {@link WeaviateProperties} to {@link WeaviateConnectionDetails}.
     */
    static class PropertiesWeaviateConnectionDetails implements WeaviateConnectionDetails {

        private final WeaviateProperties weaviateProperties;

        PropertiesWeaviateConnectionDetails(WeaviateProperties weaviateProperties) {
            this.weaviateProperties = weaviateProperties;
        }

        @Override
        public String getScheme() {
            return weaviateProperties.getClient().getSchema();
        }

        @Override
        public String getHost() {
            return weaviateProperties.getClient().getHost();
        }

        @Override
        public String getApiKey() {
            return weaviateProperties.getClient().getApiKey();
        }
    }

}
