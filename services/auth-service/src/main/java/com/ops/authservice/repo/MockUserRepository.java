package com.ops.authservice.repo;

import com.ops.authservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MockUserRepository {

    private final Map<String, User> users = new HashMap<>();

    public MockUserRepository() {
        users.put("steve", new User("steve", "employee", 700000));
        users.put("lucas", new User("lucas", "viewer", 5000));
        users.put("wheeler", new User("wheeler", "admin", 15000));
        users.put("dustin", new User("dustin", "employee", 2000));
        users.put("eleven", new User("eleven", "viewer", 100));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public void updateUser(User user) {
        users.put(user.getUsername(), user);
    }
}
