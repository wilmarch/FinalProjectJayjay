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

    public static Response getUserById(String id) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get("/user/" + id);
    }

    public static Response createUser(Object body) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .body(body)
                .when()
                .post("/user/create");
    }

    public static Response deleteUser(String id) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .delete("/user/" + id);
    }

}