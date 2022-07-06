package models.visitor.user;

import base.TestContext;

public class UserPool {
    private UserPool() {
    }

    public static User with(String email, String password) {
        User buyer = new User(email, password);
        TestContext.get().addVisitor(buyer);
        return buyer;
    }

    public static User anonymous() {
        return with("", "");
    }
}
