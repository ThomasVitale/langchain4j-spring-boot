package io.thomasvitale.langchain4j.autoconfigure.observation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatPromptObservationFilter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ObservationsAutoConfiguration}.
 */
class ObservationsAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ObservationsAutoConfiguration.class));

    @Test
    void promptObservationDefault() {
        contextRunner.run(context -> {
            assertThat(context).doesNotHaveBean(ChatPromptObservationFilter.class);
        });
    }

    @Test
    void promptObservationEnabled() {
        contextRunner
        .withPropertyValues("langchain4j.observations.include-prompt-messages=true")
        .run(context -> {
            assertThat(context).hasSingleBean(ChatPromptObservationFilter.class);
        });
    }

}
