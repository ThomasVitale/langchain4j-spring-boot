package io.thomasvitale.langchain4j.spring.core.http;

import java.time.Duration;

import org.springframework.util.Assert;

/**
 * Configuration options for HTTP clients.
 */
public class HttpClientConfig {

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

    // Getters and Setters

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getSslBundle() {
        return sslBundle;
    }

    public void setSslBundle(String sslBundle) {
        this.sslBundle = sslBundle;
    }

    public boolean isLogRequests() {
        return logRequests;
    }

    public void setLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
    }

    public boolean isLogResponses() {
        return logResponses;
    }

    public void setLogResponses(boolean logResponses) {
        this.logResponses = logResponses;
    }

    // Builders

    public static HttpClientConfig create() {
        return new HttpClientConfig();
    }

    public HttpClientConfig withConnectTimeout(Duration connectTimeout) {
        Assert.notNull(connectTimeout, "connectTimeout cannot be null");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpClientConfig withReadTimeout(Duration readTimeout) {
        Assert.notNull(readTimeout, "readTimeout cannot be null");
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpClientConfig withSslBundle(String sslBundle) {
        this.sslBundle = sslBundle;
        return this;
    }

    public HttpClientConfig withLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
        return this;
    }

    public HttpClientConfig withLogResponses(boolean logResponses) {
        this.logResponses = logResponses;
        return this;
    }

}
