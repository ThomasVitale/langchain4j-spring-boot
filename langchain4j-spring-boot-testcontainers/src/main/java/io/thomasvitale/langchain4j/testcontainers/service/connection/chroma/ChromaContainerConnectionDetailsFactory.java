package io.thomasvitale.langchain4j.testcontainers.service.connection.chroma;

import java.net.URI;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.chromadb.ChromaDBContainer;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;

/**
 * {@link ContainerConnectionDetailsFactory} to create {@link ChromaConnectionDetails}
 * from a {@link ServiceConnection @ServiceConnection}-annotated {@link ChromaDBContainer}
 * using the {@code "chroma"} image.
 */
public class ChromaContainerConnectionDetailsFactory
        extends ContainerConnectionDetailsFactory<ChromaDBContainer, ChromaConnectionDetails> {

    ChromaContainerConnectionDetailsFactory() {
    }

    @Override
    protected ChromaConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<ChromaDBContainer> source) {
        return new ChromaContainerConnectionDetails(source);
    }

    /**
     * {@link ChromaConnectionDetails} backed by a {@link ContainerConnectionSource}.
     */
    private static final class ChromaContainerConnectionDetails extends ContainerConnectionDetails<ChromaDBContainer>
            implements ChromaConnectionDetails {

        private ChromaContainerConnectionDetails(ContainerConnectionSource<ChromaDBContainer> source) {
            super(source);
        }

        @Override
        public URI getUrl() {
            return URI.create(getContainer().getEndpoint());
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
