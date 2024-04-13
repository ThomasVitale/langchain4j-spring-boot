package io.thomasvitale.langchain4j.spring.openai.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints;
import io.thomasvitale.langchain4j.spring.openai.api.Usage;

/**
 * Register runtime hints for OpenAI.
 */
public class OpenAiRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        for (var typeReference : Langchain4jRuntimeHints.findAllClassesInPackage(Usage.class.getPackageName()))
            hints.reflection().registerType(typeReference, MemberCategory.values());
    }

}
