package com.sg.bierkasse.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityRepo<R> {

    R save(R entity);

    List<R> saveAll(List<R> entities);

    List<R> findAll();

    List<R> findAll(List<String> ids);

    R findOne(String id);

    long count();

    long delete(String id);

    long delete(List<String> ids);

    long deleteAll();

    R update(R entity);

    long update(List<R> entities);
}