package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;

import java.util.List;

public interface AlertDao {
    Alert find(long id);

    Alert find(long id, boolean ignoreDeleted);

    List<Alert> findAll();

    void insert(Alert alert);

    void markDeleted(Alert alert);

    void update(Alert alert);
}
