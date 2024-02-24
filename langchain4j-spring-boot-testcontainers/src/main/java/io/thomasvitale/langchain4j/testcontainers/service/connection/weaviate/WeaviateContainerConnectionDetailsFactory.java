package io.thomasvitale.langchain4j.testcontainers.service.connection.weaviate;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.weaviate.WeaviateContainer;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;
import io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate.WeaviateConnectionDetails;

/**
 * {@link ContainerConnectionDetailsFactory} to create {@link WeaviateConnectionDetails}
 * from a {@link ServiceConnection @ServiceConnection}-annotated {@link WeaviateContainer}
 * using the {@code "weaviate"} image.
 *
 * @author Thomas Vitale
 */
class WeaviateContainerConnectionDetailsFactory
        extends ContainerConnectionDetailsFactory<WeaviateContainer, WeaviateConnectionDetails> {

    WeaviateContainerConnectionDetailsFactory() {
    }

    @Override
    protected WeaviateConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<WeaviateContainer> source) {
        return new WeaviateContainerConnectionDetails(source);
    }

    /**
     * {@link ChromaConnectionDetails} backed by a {@link ContainerConnectionSource}.
     */
    private static final class WeaviateContainerConnectionDetails extends ContainerConnectionDetails<WeaviateContainer>
            implements WeaviateConnectionDetails {

        private WeaviateContainerConnectionDetails(ContainerConnectionSource<WeaviateContainer> source) {
            super(source);
        }

        @Override
        public String getScheme() {
            return "http";
        }

        @Override
        public String getHost() {
            return getContainer().getHttpHostAddress();
        }

        @Override
        public String getApiKey() {
            return "";
        }
    }

}
