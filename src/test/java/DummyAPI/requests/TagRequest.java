package DummyAPI.requests;

import DummyAPI.configs.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TagRequest {

    public static Response getTags() {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get("/tag");
    }

    public static Response getTagsWithoutAuth() {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/tag");
    }

    public static Response getTagsWithCustomAppId(String customAppId) {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .header("app-id", customAppId)
                .when()
                .get("/tag");
    }

    public static Response getTagsWithCustomPath(String endpointPath) {
        return RestAssured.given()
                .spec(ApiConfig.getRequestSpec())
                .when()
                .get(endpointPath);
    }
}