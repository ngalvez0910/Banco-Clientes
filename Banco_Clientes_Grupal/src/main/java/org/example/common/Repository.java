package org.example.common;

import io.vavr.control.Either;

import java.util.List;

public interface Repository<T, ID> {
    Either<String, List<T>> getAll();
    Either<String, T> getById(ID id);
    Either<String, T> create(T entity);
    Either<String, T> update(ID id, T entity);
    Either<String, Boolean> delete(ID id);
}
