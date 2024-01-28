package io.thomasvitale.langchain4j.autoconfigure.ollama;

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
    String getUrl();

}
