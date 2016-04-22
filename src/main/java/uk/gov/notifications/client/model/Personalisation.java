package uk.gov.notifications.client.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents information used to personalize a message sent through GovNotify service.
 */
public class Personalisation implements Serializable {

    private Map<String, String> store = new HashMap<>();

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(store);
    }


    /**
     * Returns a fluent builder for personalisation.
     * @return fluent builder for personalisation
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of personalisation.
     */
    public static class Builder {

        private Personalisation personalisation = new Personalisation();

        /**
         * Adds a field that is used to fill in a placeholder in the notification template.
         * @param name field name
         * @param value field value
         * @return Builder object for personalisation
         */
        public Builder field(String name, String value) {
            Objects.requireNonNull(name, "Field name cannot be null");
            Objects.requireNonNull(value, "Field value cannot be null");

            personalisation.store.put(name, value);
            return this;
        }

        /**
         * Validates and builds personalisation object.
         * @return personalisation object
         */
        public Personalisation build() {
            validate();
            return personalisation;
        }

        private void validate() {

            if (personalisation.store.size() == 0) {
                throw new IllegalArgumentException(
                        "Personalisation when set must have at least one field"
                );
            }
        }
    }

    /**
     * Creates Personalisation object from JSON string.
     * @param json json string
     * @return validated Personalisation object
     */
    public static Personalisation fromJsonString(String json) {

        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONObject obj = new JSONObject(json);
        Personalisation.Builder personalisation = builder();
        for (String key : obj.keySet()) {
            personalisation.field(key, obj.getString(key));
        }
        return personalisation.build();
    }
}
