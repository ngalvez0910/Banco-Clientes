package org.example.common;

import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> getAll();
    Optional<T> getById(ID id);
    T create(T entity);
    T update(ID id, T entity);
    Boolean delete(ID id);
}
