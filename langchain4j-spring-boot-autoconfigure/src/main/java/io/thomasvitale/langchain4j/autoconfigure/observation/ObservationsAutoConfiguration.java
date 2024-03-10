package io.thomasvitale.langchain4j.autoconfigure.observation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatPromptObservationFilter;

/**
 * Auto-configuration for LangChain4j observations.
 *
 * @author Thomas Vitale
 */
@AutoConfiguration
@EnableConfigurationProperties({ ObservationsProperties.class })
public class ObservationsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ObservationsAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = ObservationsProperties.CONFIG_PREFIX, name = "include-prompt-messages", havingValue = "true", matchIfMissing = false)
    ChatPromptObservationFilter chatPromptObservationFilter() {
        logger.warn("You have enabled the inclusion of the prompt body in the observations, with the risk of exposing sensitive or private information. Please, be careful!");
        return new ChatPromptObservationFilter();
    }

}
