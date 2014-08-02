package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.User;

public interface UserDao {
    /**
     * @param email Email
     * @param password Password
     * @return User or {@code null}.
     */
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    void register(User newUser, String password);
}
