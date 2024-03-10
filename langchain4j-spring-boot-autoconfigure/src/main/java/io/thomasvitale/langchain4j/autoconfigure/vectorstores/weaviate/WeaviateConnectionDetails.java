package io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate;

import java.net.URI;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Details required to establish a connection to a Weaviate server.
 */
public interface WeaviateConnectionDetails extends ConnectionDetails {

    /**
     * URL where the Weaviate server is running.
     */
    URI getUrl();

    /**
     * API key to authenticate with the Weaviate server when using the API Token method.
     */
    String getApiKey();

}
