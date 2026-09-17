package DummyAPI.models;

import java.util.HashMap;
import java.util.Map;

public class UserPayload {

    public static Map<String, Object> createValidUser(String firstName, String lastName, String email) {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", firstName);
        body.put("lastName", lastName);
        body.put("email", email);
        return body;
    }

    public static Map<String, Object> createMissingFieldUser(String fieldToOmit, String email) {
        Map<String, Object> body = new HashMap<>();
        if (!fieldToOmit.equalsIgnoreCase("firstName")) body.put("firstName", "TestFirst");
        if (!fieldToOmit.equalsIgnoreCase("lastName")) body.put("lastName", "TestLast");
        if (!fieldToOmit.equalsIgnoreCase("email")) body.put("email", email);
        return body;
    }

    public static Map<String, Object> createUpdatePayload(Map<String, String> fields) {
        return new HashMap<>(fields);
    }

    public static Map<String, Object> createSingleFieldUpdatePayload(String fieldName, String value) {
        Map<String, Object> body = new HashMap<>();
        body.put(fieldName, value);
        return body;
    }

    public static Map<String, Object> createLocationUpdatePayload(String street, String city, String state,
                                                                  String country, String timezone) {
        Map<String, Object> location = new HashMap<>();
        location.put("street", street);
        location.put("city", city);
        location.put("state", state);
        location.put("country", country);
        location.put("timezone", timezone);

        Map<String, Object> body = new HashMap<>();
        body.put("location", location);
        return body;
    }
}