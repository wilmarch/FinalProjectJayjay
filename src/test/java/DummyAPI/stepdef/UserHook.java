package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.requests.UserRequest;
import io.cucumber.java.After;

public class UserHook {

    private final TestContext context;

    public UserHook(TestContext context) {
        this.context = context;
    }

    @After("@cleanup_user")
    public void cleanupCreatedUser() {
        String userId = context.getUserId();
        if (userId != null && !userId.trim().isEmpty()) {
            UserRequest.deleteUser(userId);
            context.setUserId(null);
        }
    }
}