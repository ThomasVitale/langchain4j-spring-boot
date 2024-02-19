package io.thomasvitale.langchain4j.testcontainers.service.connection.chroma;

import java.net.URI;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;

/**
 * {@link ContainerConnectionDetailsFactory} to create {@link ChromaConnectionDetails}
 * from a {@link ServiceConnection @ServiceConnection}-annotated {@link GenericContainer}
 * using the {@code "chroma"} image.
 *
 * @author Thomas Vitale
 */
class ChromaContainerConnectionDetailsFactory
        extends ContainerConnectionDetailsFactory<Container<?>, ChromaConnectionDetails> {

    ChromaContainerConnectionDetailsFactory() {
        super("chroma");
    }

    @Override
    protected ChromaConnectionDetails getContainerConnectionDetails(
            ContainerConnectionSource<Container<?>> source) {
        return new ChromaContainerConnectionDetails(source);
    }

    /**
     * {@link ChromaConnectionDetails} backed by a {@link ContainerConnectionSource}.
     */
    private static final class ChromaContainerConnectionDetails extends ContainerConnectionDetails<Container<?>>
            implements ChromaConnectionDetails {

        private ChromaContainerConnectionDetails(ContainerConnectionSource<Container<?>> source) {
            super(source);
        }

        @Override
        public URI getUrl() {
            return URI.create("http://%s:%s".formatted(getContainer().getHost(), getContainer().getFirstMappedPort()));
        }

        @Override
        public String getApiToken() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

        @Override
        public String getUsername() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

        @Override
        public String getPassword() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

    }

}
