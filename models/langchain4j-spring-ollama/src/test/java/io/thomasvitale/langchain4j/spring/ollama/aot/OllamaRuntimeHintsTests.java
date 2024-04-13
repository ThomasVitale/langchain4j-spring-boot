package io.thomasvitale.langchain4j.spring.ollama.aot;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.TypeReference;

import io.thomasvitale.langchain4j.spring.ollama.api.ChatRequest;

import static io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints.findAllClassesInPackage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.aot.hint.predicate.RuntimeHintsPredicates.reflection;

/**
 * Unit tests for {@link OllamaRuntimeHints}.
 */
class OllamaRuntimeHintsTests {

    @Test
    void hintsAreRegistered() {
        RuntimeHints runtimeHints = new RuntimeHints();
        OllamaRuntimeHints ollamaRuntimeHints = new OllamaRuntimeHints();
        ollamaRuntimeHints.registerHints(runtimeHints, null);

        Set<TypeReference> apiClasses = findAllClassesInPackage(ChatRequest.class.getPackageName());
        for (TypeReference apiClass : apiClasses) {
            assertThat(runtimeHints).matches(reflection().onType(apiClass));
        }
    }

}
