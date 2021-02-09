package com.revature.repos;

import java.util.List;

/**
 * Implements the CRUD interface, contains actual SQL statements to interact with database
 *
 * @param <T>
 */

public class CRUDImpl<T> implements CRUD {


    @Override
    public void save(Object newObj) {

        System.out.println("Not implemented");
    }

    @Override
    public List<T> findAll() {

        System.out.println("Not implemented");
        return null;
    }

    @Override
    public Object findById(int id) {
        System.out.println("Not implemented");
        return null;
    }

    @Override
    public boolean update(Object updatedObj) {
        System.out.println("Not implemented");
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        System.out.println("Not implemented");
        return false;
    }
}
