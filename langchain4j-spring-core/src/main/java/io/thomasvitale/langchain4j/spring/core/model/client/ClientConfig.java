package io.thomasvitale.langchain4j.spring.core.model.client;

import java.time.Duration;

import org.springframework.util.Assert;

/**
 * Configuration options for the HTTP clients.
 */
public class ClientConfig {

    /**
     * Maximum time to wait for a connection.
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * Maximum time to wait for a response.
     */
    private Duration readTimeout = Duration.ofSeconds(60);

    /**
     * SSL certificate bundle to use to establish a secure connection.
     */
    private String sslBundle;

    /**
     * Whether to log requests.
     */
    private boolean logRequests = false;

    /**
     * Whether to log responses.
     */
    private boolean logResponses = false;

    // Getters

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public String getSslBundle() {
        return sslBundle;
    }

    public boolean getLogRequests() {
        return logRequests;
    }

    public boolean getLogResponses() {
        return logResponses;
    }

    // Builders

    public static ClientConfig create() {
        return new ClientConfig();
    }

    public ClientConfig withConnectTimeout(Duration connectTimeout) {
        Assert.notNull(connectTimeout, "connectTimeout cannot be null");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ClientConfig withReadTimeout(Duration readTimeout) {
        Assert.notNull(readTimeout, "readTimeout cannot be null");
        this.readTimeout = readTimeout;
        return this;
    }

    public ClientConfig withSslBundle(String sslBundle) {
        this.sslBundle = sslBundle;
        return this;
    }

    public ClientConfig withLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
        return this;
    }

    public ClientConfig withLogResponses(boolean logResponses) {
        this.logResponses = logResponses;
        return this;
    }

}
