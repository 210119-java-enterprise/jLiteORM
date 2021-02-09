package com.revature.repos;

import java.util.List;

public interface CRUD<T> {

    void save(T newObj);
    List<T> findAll();
    T findById(int id);
    boolean update(T updatedObj);
    boolean deleteById(int id);
}
