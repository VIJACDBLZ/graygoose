package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.*;

import java.util.List;

public interface AlertTriggerEventDao {
    void insert(AlertTriggerEvent alertTriggerEvent);

    void delete(AlertTriggerEvent alertTriggerEvent);

    List<AlertTriggerEvent> findAll();

    List<AlertTriggerEvent> findByAlert(Alert alert);

    List<AlertTriggerEvent> findByAlert(long alertId);

    AlertTriggerEvent find(long id);
}