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
}