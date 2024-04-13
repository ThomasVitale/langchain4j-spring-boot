package io.thomasvitale.langchain4j.spring.ollama.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints;
import io.thomasvitale.langchain4j.spring.ollama.api.ChatRequest;

/**
 * Register runtime hints for Ollama.
 */
public class OllamaRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        for (var typeReference : Langchain4jRuntimeHints.findAllClassesInPackage(ChatRequest.class.getPackageName()))
            hints.reflection().registerType(typeReference, MemberCategory.values());
    }

}
