package io.thomasvitale.langchain4j.spring.core.image.observation;

import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.docs.ObservationDocumentation;

/**
 * Observation created around an image model execution.
 */
public enum ImageObservation implements ObservationDocumentation {

    IMAGE_OBSERVATION {
        @Override
        public String getName() {
            return "langchain4j.image";
        }

        @Override
        public String getContextualName() {
            return "langchain4j image";
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return ImageLowCardinalityTags.values();
        }

        @Override
        public KeyName[] getHighCardinalityKeyNames() {
            return ImageHighCardinalityTags.values();
        }

        @Override
        public String getPrefix() {
            return "langchain4j";
        }
    };

    enum ImageLowCardinalityTags implements KeyName {

        /**
         * Name of the chat model.
         */
        MODEL_NAME {
            @Override
            public String asString() {
                return "langchain4j.image.model.name";
            }
        },

        /**
         * Name of the model provider.
         */
        MODEL_PROVIDER {
            @Override
            public String asString() {
                return "langchain4j.image.model.provider";
            }
        },

    }

    enum ImageHighCardinalityTags implements KeyName {

        /**
         * Number of images to generate.
         */
        IMAGE_NUMBER {
            @Override
            public String asString() {
                return "langchain4j.image.number";
            }
        }

    }

}
