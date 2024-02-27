package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

import java.net.URI;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Details required to establish a connection to an Ollama server.
 *
 * @author Thomas Vitale
 */
public interface OllamaConnectionDetails extends ConnectionDetails {

    /**
     * URL where to contact the Ollama server.
     */
    URI getUrl();

}
