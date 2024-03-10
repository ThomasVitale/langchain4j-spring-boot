package io.thomasvitale.langchain4j.spring.core.model.prompt;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.langchain4j.spi.prompt.PromptTemplateFactory;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STLexer;

/**
 * A factory for creating a {@link Template} instance based on the StringTemplate library.
 * <p>
 * Template variables support the following formats: {var} and {{var}}.
 * <p>
 * Adapted from the Spring AI implementation.
 */
public class SpringPromptTemplateFactory implements PromptTemplateFactory {

    @Override
    public Template create(Input input) {
        return new STTemplate(input.getTemplate());
    }

    static class STTemplate implements Template {

        private final ST st;

        public STTemplate(String template) {
            try {
                this.st = new ST(getNormalizedTemplate(template), '{', '}');
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("The template string is not valid", ex);
            }
        }

        @Override
        public String render(Map<String, Object> variables) {
            validateVariables(variables);
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                if (st.getAttribute(entry.getKey()) != null) {
                    st.remove(entry.getKey());
                }
                st.add(entry.getKey(), entry.getValue());
            }
            return st.render();
        }

        private String getNormalizedTemplate(String template) {
            return Stream.of(template)
                .map(t -> t.replace("{{", "{"))
                .map(t -> t.replace("}}", "}"))
                .collect(Collectors.joining());
        }

        private void validateVariables(Map<String, Object> variables) {
            var inputVariables = getInputVariables();

            var missingEntries = new HashSet<>(inputVariables);
            missingEntries.removeIf(variables.keySet()::contains);
            if (!missingEntries.isEmpty()) {
                throw new IllegalStateException(
                        "Not all template variables were replaced. Missing variable names are: " + missingEntries);
            }

            var nullEntries = new HashSet<>(inputVariables);
            nullEntries.removeIf(key -> variables.get(key) != null && variables.get(key).toString() != null);
            if (!nullEntries.isEmpty()) {
                throw new IllegalStateException(
                        "Not all input variables have a non-empty value. Variables with null values are: "
                                + nullEntries);
            }
        }

        private Set<String> getInputVariables() {
            TokenStream tokens = st.impl.tokens;
            return IntStream.range(0, tokens.range())
                .mapToObj(tokens::get)
                .filter(token -> token.getType() == STLexer.ID)
                .map(Token::getText)
                .collect(Collectors.toSet());
        }

    }

}
