package io.thomasvitale.langchain4j.testcontainers.service.connection.ollama;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaAutoConfiguration;
import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaContainerConnectionDetailsFactory}.
 */
@SpringJUnitConfig
@Testcontainers
class OllamaContainerConnectionDetailsFactoryIT {

    @Nested
    class OllamaContainerWithDefaultImageIT {

        @Container
        @ServiceConnection
        static final OllamaContainer container = new OllamaContainer("ollama/ollama:0.6.4");

        @Autowired
        private OllamaConnectionDetails connectionDetails;

        @Test
        void connectionEstablishedWithOllamaContainer() {
            assertThat(this.connectionDetails.getUrl().toString())
                    .isEqualTo("http://" + container.getHost() + ":" + container.getFirstMappedPort());
        }
    }

    @Nested
    class OllamaContainerWithCustomImageIT {

        @Container
        @ServiceConnection
        static final OllamaContainer container = new OllamaContainer(DockerImageName
                .parse("ghcr.io/thomasvitale/ollama-orca-mini")
                .asCompatibleSubstituteFor("ollama/ollama"));

        @Autowired
        private OllamaConnectionDetails connectionDetails;

        @Test
        void connectionEstablishedWithOllamaContainer() {
            assertThat(this.connectionDetails.getUrl().toString())
                    .isEqualTo("http://" + container.getHost() + ":" + container.getFirstMappedPort());
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ImportAutoConfiguration(OllamaAutoConfiguration.class)
    static class TestConfiguration {

    }

}
