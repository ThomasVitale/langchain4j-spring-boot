package io.thomasvitale.langchain4j.spring.core.aot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Register runtime hints for Jackson.
 * <p>
 * Should not be needed if https://github.com/oracle/graalvm-reachability-metadata/pull/483 gets merged.
 */
public class JacksonRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(PropertyNamingStrategies.SnakeCaseStrategy.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
    }

}
