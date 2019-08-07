package com.thoughtworks.fabric;


import org.json.JSONObject;

public class State {
    protected String key;

    /**
     * @param {String|Object} class An identifiable class of the instance
     * @param {keyParts[]}    elements to pull together to make a key for the objects
     */
    public State() {

    }

    String getKey() {
        return this.key;
    }

    public String[] getSplitKey() {
        return State.splitKey(this.key);
    }

    /**
     * Convert object to buffer containing JSON data serialization Typically used
     * before putState()ledger API
     *
     * @param {Object} JSON object to serialize
     * @return {buffer} buffer with the data to store
     */
    public static String serialize(Object object) {
        return new JSONObject(object).toString();
    }

    /**
     * Join the keyParts to make a unififed string
     *
     * @param (String[]) keyParts
     */
    public static String makeKey(String[] keyParts) {
        return String.join(":", keyParts);
    }

    public static String[] splitKey(String key) {
        System.out.println("Splittin gkey " + key + "   " + java.util.Arrays.asList(key.split(":")));
        return key.split(":");
    }

}
