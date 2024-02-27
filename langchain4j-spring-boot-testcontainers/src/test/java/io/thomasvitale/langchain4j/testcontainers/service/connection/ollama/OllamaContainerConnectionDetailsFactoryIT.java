package io.thomasvitale.langchain4j.testcontainers.service.connection.ollama;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaAutoConfiguration;
import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;
import io.thomasvitale.langchain4j.testcontainers.service.containers.OllamaContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaContainerConnectionDetailsFactory}.
 *
 * @author Thomas Vitale
 */
@SpringJUnitConfig
@Testcontainers
class OllamaContainerConnectionDetailsFactoryIT {

    @Container
    @ServiceConnection
    static final OllamaContainer container = new OllamaContainer("ollama/ollama");

    @Autowired
    private OllamaConnectionDetails connectionDetails;

    @Test
    void connectionEstablishedWithOllamaContainer() {
        assertThat(this.connectionDetails.getUrl().toString())
            .isEqualTo("http://" + container.getHost() + ":" + container.getMappedPort(OllamaContainer.OLLAMA_PORT));
    }

    @Configuration(proxyBeanMethods = false)
    @ImportAutoConfiguration(OllamaAutoConfiguration.class)
    static class TestConfiguration {

    }

}
