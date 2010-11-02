package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.Alert;

import java.util.List;

public interface AlertDao extends BasicDao<Alert> {
    Alert find(long id);

    List<Alert> findAll();

    void insert(Alert alert);

    void markDeleted(Alert alert);

    void update(Alert alert);
}
