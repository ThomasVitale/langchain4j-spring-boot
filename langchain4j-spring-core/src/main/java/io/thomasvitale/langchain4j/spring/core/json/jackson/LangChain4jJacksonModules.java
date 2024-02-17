package io.thomasvitale.langchain4j.spring.core.json.jackson;

import java.util.List;

import com.fasterxml.jackson.databind.Module;

import io.thomasvitale.langchain4j.spring.core.chat.messages.jackson.ChatMessageJacksonModule;
import io.thomasvitale.langchain4j.spring.core.document.jackson.DocumentJacksonModule;
import io.thomasvitale.langchain4j.spring.core.embedding.jackson.EmbeddingJacksonModule;
import io.thomasvitale.langchain4j.spring.core.image.jackson.ImageJacksonModule;
import io.thomasvitale.langchain4j.spring.core.tool.jackson.ToolJacksonModule;
import io.thomasvitale.langchain4j.spring.core.vectorstore.jackson.VectorStoreJacksonModule;

/**
 * Gives access to all the Jackson modules configured in LangChain4j Spring.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModules(LangChain4jJacksonModules.getModules());
 * </pre>
 *
 * @author Thomas Vitale
 */
public class LangChain4jJacksonModules {

    /**
     * List of all the available LangChain4j Jackson modules.
     */
    public static List<Module> getModules() {
        return List.of(new ChatMessageJacksonModule(), new DocumentJacksonModule(), new EmbeddingJacksonModule(),
                new ImageJacksonModule(), new ToolJacksonModule(), new VectorStoreJacksonModule());
    }

}
