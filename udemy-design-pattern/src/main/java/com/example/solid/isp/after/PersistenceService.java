package com.example.solid.isp.after;

public interface PersistenceService<T extends Entity> {

    void save(T entity);

    void delete(T entity);

    T findById(Long id);
}
