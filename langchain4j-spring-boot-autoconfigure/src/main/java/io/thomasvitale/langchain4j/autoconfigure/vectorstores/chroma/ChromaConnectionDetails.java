package io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma;

import java.net.URI;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Details required to establish a connection to a Chroma server.
 *
 * @author Thomas Vitale
 */
public interface ChromaConnectionDetails extends ConnectionDetails {

    /**
     * URL where to contact the Chroma server.
     */
    URI getUrl();

    /**
     * API token to authenticate with the Chroma server when using the API Token method.
     */
    String getApiToken();

    /**
     * Username to authenticate with the Chroma server when using the Basic Authentication
     * method.
     */
    String getUsername();

    /**
     * Password to authenticate with the Chroma server when using the Basic Authentication
     * method.
     */
    String getPassword();

}
