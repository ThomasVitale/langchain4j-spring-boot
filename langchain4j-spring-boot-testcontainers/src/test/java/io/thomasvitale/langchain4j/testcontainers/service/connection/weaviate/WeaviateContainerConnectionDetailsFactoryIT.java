package io.thomasvitale.langchain4j.testcontainers.service.connection.weaviate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.weaviate.WeaviateContainer;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate.WeaviateAutoConfiguration;
import io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate.WeaviateConnectionDetails;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WeaviateContainerConnectionDetailsFactory}.
 */
@SpringJUnitConfig
@Testcontainers
class WeaviateContainerConnectionDetailsFactoryIT {

    @Container
    @ServiceConnection
    static final WeaviateContainer container = new WeaviateContainer("semitechnologies/weaviate:1.23.10");

    @Autowired
    private WeaviateConnectionDetails connectionDetails;

    @Test
    void connectionEstablishedWithWeaviateContainer() {
        assertThat(this.connectionDetails.getUrl().getScheme()).isEqualTo("http");
        assertThat(this.connectionDetails.getUrl().getHost()).isEqualTo(container.getHost());
        assertThat(this.connectionDetails.getUrl().getPort()).isEqualTo(container.getFirstMappedPort());
    }

    @Configuration(proxyBeanMethods = false)
    @ImportAutoConfiguration(WeaviateAutoConfiguration.class)
    static class TestConfiguration {

    }

}
