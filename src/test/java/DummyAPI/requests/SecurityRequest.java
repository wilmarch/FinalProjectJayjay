package DummyAPI.requests;

import DummyAPI.configs.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SecurityRequest {

    public static Response getWithoutAppId(String endpoint) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .when()
                .get(endpoint);
    }

    public static Response getWithCustomAppId(String endpoint, String customAppId) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("app-id", customAppId)
                .when()
                .get(endpoint);
    }

    public static Response postWithoutAppId(String endpoint, Object body) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint);
    }
}