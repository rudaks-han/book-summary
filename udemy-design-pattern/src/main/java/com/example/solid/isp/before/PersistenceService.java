package com.example.solid.isp.before;

import java.util.List;

public interface PersistenceService<T extends Entity> {

    void save(T entity);

    void delete(T entity);

    T findById(Long id);

    List<T> findByName(String name);
}
