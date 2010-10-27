package com.codeforces.graygoose.dao;

import com.codeforces.graygoose.model.AbstractEntity;

import java.util.List;

public interface BasicDao<T extends AbstractEntity> {
    T find(long id);

    void insert(T entity);

    void delete(T entity);

    void markDeleted(T entity);

    void unmarkDeleted(T entity);

    List<T> findAll();
}