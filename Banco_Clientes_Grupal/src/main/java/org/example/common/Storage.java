package org.example.common;

import io.reactivex.rxjava3.core.Observable;

import java.io.File;

public interface Storage<T> {
    Observable<T> importFile(File file);
    void exportFile(File file, Observable<T> items);
}
