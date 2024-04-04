package io.thomasvitale.langchain4j.spring.core.moderation.observation;

import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.docs.ObservationDocumentation;

/**
 * Observation created around a moderation model execution.
 */
public enum ModerationObservation implements ObservationDocumentation {

    IMAGE_OBSERVATION {
        @Override
        public String getName() {
            return "langchain4j.moderation";
        }

        @Override
        public String getContextualName() {
            return "langchain4j moderation";
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return ModerationLowCardinalityTags.values();
        }

        @Override
        public KeyName[] getHighCardinalityKeyNames() {
            return KeyName.merge();
        }

        @Override
        public String getPrefix() {
            return "langchain4j";
        }
    };

    enum ModerationLowCardinalityTags implements KeyName {

        /**
         * Name of the chat model.
         */
        MODEL_NAME {
            @Override
            public String asString() {
                return "langchain4j.moderation.model.name";
            }
        },

        /**
         * Name of the model provider.
         */
        MODEL_PROVIDER {
            @Override
            public String asString() {
                return "langchain4j.moderation.model.provider";
            }
        },

    }

}
