package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.UserDao;
import com.codeforces.graygoose.model.User;

public class UserDaoImpl extends ApplicationDaoImpl<User> implements UserDao {
    private static final String PASSWORD_SALT = "$e96ea31e54c52901";
    @Override
    public User findByEmailAndPassword(String email, String password) {
        return findOnlyBy(true, "email=? AND passwordSha1=SHA1(?)", email, password + PASSWORD_SALT);
    }

    @Override
    public User findByEmail(String email) {
        return findOnlyBy(true, "email=?", email);
    }

    @Override
    public void register(User newUser, String password) {
        insert(newUser);
        getJacuzzi().execute("UPDATE `User` SET passwordSha1=SHA1(?) WHERE id=?", password + PASSWORD_SALT, newUser.getId());
    }
}
