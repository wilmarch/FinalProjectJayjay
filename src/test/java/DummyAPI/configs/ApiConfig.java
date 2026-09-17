package DummyAPI.configs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    public static final String BASE_URL = "https://dummyapi.io/data/v1";
    public static final String APP_ID = "63a804408eb0cb069b57e43a";

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("app-id", APP_ID)
                .setContentType(ContentType.JSON)
                .build();
    }
}