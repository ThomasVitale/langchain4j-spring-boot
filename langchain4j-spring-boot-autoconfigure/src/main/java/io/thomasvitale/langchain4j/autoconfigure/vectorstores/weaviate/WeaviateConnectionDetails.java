package io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Details required to establish a connection to a Weaviate server.
 *
 * @author Thomas Vitale
 */
public interface WeaviateConnectionDetails extends ConnectionDetails {

    /**
     * Schema to interact with the Weaviate server.
     */
    String getScheme();

    /**
     * Host (and port) where the Weaviate server is running.
     */
    String getHost();

    /**
     * API key to authenticate with the Weaviate server when using the API Token method.
     */
    String getApiKey();

}
