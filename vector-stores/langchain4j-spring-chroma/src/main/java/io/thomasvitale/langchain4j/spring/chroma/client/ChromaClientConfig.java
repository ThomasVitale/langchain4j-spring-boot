package io.thomasvitale.langchain4j.spring.chroma.client;

import java.net.URI;
import java.time.Duration;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Options for configuring the Chroma client.
 *
 * @author Thomas Vitale
 */
public record ChromaClientConfig(
//@formatter:off
        URI url,
        Duration connectTimeout,
        Duration readTimeout,
        @Nullable
        String sslBundle,
        @Nullable
        String apiToken,
        @Nullable
        String username,
        @Nullable
        String password
//@formatter:on
) {

    public ChromaClientConfig {
        Assert.notNull(url, "url must not be null");
        Assert.notNull(connectTimeout, "connectTimeout must not be null");
        Assert.notNull(readTimeout, "readTimeout must not be null");
        Assert.isTrue(apiToken == null || username == null && password == null,
                "apiToken and username/password cannot be used together");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private URI url = URI.create("http://localhost:8000");

        private Duration connectTimeout = Duration.ofSeconds(10);

        private Duration readTimeout = Duration.ofSeconds(60);

        @Nullable
        private String sslBundle;

        @Nullable
        private String apiToken;

        @Nullable
        private String username;

        @Nullable
        private String password;

        private Builder() {
        }

        public Builder url(URI url) {
            this.url = url;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder sslBundle(String sslBundle) {
            this.sslBundle = sslBundle;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public ChromaClientConfig build() {
            return new ChromaClientConfig(url, connectTimeout, readTimeout, sslBundle, apiToken, username, password);
        }

    }

}
