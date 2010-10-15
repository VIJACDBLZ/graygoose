package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;

import java.util.List;

public interface AlertDao {
    void insert(Alert alert);

    void delete(Alert alert);

    List<Alert> findAll();

    Alert find(long id);
}
