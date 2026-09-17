package DummyAPI.context;

import io.restassured.response.Response;

public class TestContext {
    private Response response;
    private String userId;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}