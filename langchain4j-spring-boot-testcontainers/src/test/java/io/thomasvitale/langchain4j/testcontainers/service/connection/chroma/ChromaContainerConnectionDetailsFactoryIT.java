package io.thomasvitale.langchain4j.testcontainers.service.connection.chroma;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.chromadb.ChromaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaAutoConfiguration;
import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ChromaContainerConnectionDetailsFactory}.
 */
@SpringJUnitConfig
@Testcontainers
class ChromaContainerConnectionDetailsFactoryIT {

    @Container
    @ServiceConnection
    static final ChromaDBContainer container = new ChromaDBContainer("ghcr.io/chroma-core/chroma:0.5.3");

    @Autowired
    private ChromaConnectionDetails connectionDetails;

    @Test
    void connectionEstablishedWithChromaContainer() {
        assertThat(this.connectionDetails.getUrl().getHost()).isEqualTo(container.getHost());
        assertThat(this.connectionDetails.getUrl().getPort()).isEqualTo(container.getMappedPort(8000));
    }

    @Configuration(proxyBeanMethods = false)
    @ImportAutoConfiguration(ChromaAutoConfiguration.class)
    static class TestConfiguration {

    }

}
