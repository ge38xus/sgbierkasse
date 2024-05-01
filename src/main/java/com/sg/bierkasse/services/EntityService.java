package com.sg.bierkasse.services;

import java.util.List;

public interface EntityService<R> {

    R save(R R);

    List<R> saveAll(List<R> entities);

    List<R> findAll();

    List<R> findAll(List<String> ids);

    R findOne(String id);

    long count();

    long delete(String id);

    long delete(List<String> ids);

    long deleteAll();

    R update(R R);

    long update(List<R> entities);
}
