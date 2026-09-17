package DummyAPI.requests;

import DummyAPI.configs.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserRequest {

    public static Response getUserList() {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get("/user");
    }

    public static Response getUserListWithPagination(int page, int limit) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .queryParam("page", page)
                .queryParam("limit", limit)
                .when()
                .get("/user");
    }

    public static Response getUserById(String userId) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .pathParam("id", userId)
                .when()
                .get("/user/{id}");
    }
}