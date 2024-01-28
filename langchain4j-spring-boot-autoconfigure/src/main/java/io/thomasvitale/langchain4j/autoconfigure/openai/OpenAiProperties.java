package io.thomasvitale.langchain4j.autoconfigure.openai;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Common configuration properties for the OpenAI clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(OpenAiProperties.CONFIG_PREFIX)
public class OpenAiProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai";

    /**
     * Base URL of the OpenAI API.
     */
    private URI baseUrl = URI.create("https://api.openai.com/v1/");

    /**
     * OpenAI APY Key.
     */
    private String apiKey;

    /**
     * Which organization to use for OpenAI requests.
     */
    private String organizationId;

    /**
     * Timeout for OpenAI calls.
     */
    private Duration timeout = Duration.ofSeconds(10);

    /**
     * Maximum number of times an OpenAI call will be retried.
     */
    private Integer maxRetries = 3;

    /**
     * Unique identifier representing your end-user, which can help OpenAI to monitor
     * and detect abuse.
     */
    private String user;

    /**
     * Enable logging for OpenAI requests.
     */
    private Boolean logRequests = false;

    /**
     * Enable logging for OpenAI responses.
     */
    private Boolean logResponses = false;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getLogRequests() {
        return logRequests;
    }

    public void setLogRequests(Boolean logRequests) {
        this.logRequests = logRequests;
    }

    public Boolean getLogResponses() {
        return logResponses;
    }

    public void setLogResponses(Boolean logResponses) {
        this.logResponses = logResponses;
    }

}
