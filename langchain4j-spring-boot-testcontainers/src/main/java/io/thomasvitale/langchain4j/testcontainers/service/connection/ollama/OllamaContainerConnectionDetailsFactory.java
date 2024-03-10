package io.thomasvitale.langchain4j.testcontainers.service.connection.ollama;

import java.net.URI;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.ollama.OllamaContainer;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;

/**
 * {@link ContainerConnectionDetailsFactory} to create {@link OllamaConnectionDetails}
 * from a {@link ServiceConnection @ServiceConnection}-annotated {@link OllamaContainer}
 * using the {@code "ollama"} image.
 *
 * @author Thomas Vitale
 */
public class OllamaContainerConnectionDetailsFactory
        extends ContainerConnectionDetailsFactory<OllamaContainer, OllamaConnectionDetails> {

    OllamaContainerConnectionDetailsFactory() {
    }

    @Override
    protected OllamaConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<OllamaContainer> source) {
        return new OllamaContainerConnectionDetails(source);
    }

    /**
     * {@link OllamaConnectionDetails} backed by a {@link ContainerConnectionSource}.
     */
    private static final class OllamaContainerConnectionDetails extends ContainerConnectionDetails<OllamaContainer>
            implements OllamaConnectionDetails {

        private OllamaContainerConnectionDetails(ContainerConnectionSource<OllamaContainer> source) {
            super(source);
        }

        @Override
        public URI getUrl() {
            return URI.create("http://%s:%s".formatted(getContainer().getHost(), getContainer().getFirstMappedPort()));
        }

    }

}
