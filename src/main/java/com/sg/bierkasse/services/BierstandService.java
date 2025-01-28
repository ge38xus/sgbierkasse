package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.repositories.MongoDBBierstandRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BierstandService implements EntityService<BierstandDTO> {

    private final MongoDBBierstandRepo entityRepository;

    public BierstandService(MongoDBBierstandRepo entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public BierstandDTO save(BierstandDTO bierstandDTO) {
        return new BierstandDTO(entityRepository.save(bierstandDTO.toBierstandEntity()));
    }

    @Override
    public List<BierstandDTO> saveAll(List<BierstandDTO> personEntities) {
        return personEntities.stream()
                .map(BierstandDTO::toBierstandEntity)
                .peek(entityRepository::save)
                .map(BierstandDTO::new)
                .toList();
    }

    @Override
    public List<BierstandDTO> findAll() {
        return entityRepository.findAll().stream().map(BierstandDTO::new).sorted(Comparator.comparing(BierstandDTO::date).reversed()).toList();
    }

    @Override
    public List<BierstandDTO> findAll(List<String> ids) {
        return entityRepository.findAll(ids).stream().map(BierstandDTO::new).toList();
    }

    @Override
    public BierstandDTO findOne(String id) {
        return new BierstandDTO(entityRepository.findOne(id));
    }

    @Override
    public long count() {
        return entityRepository.count();
    }

    @Override
    public long delete(String id) {
        return entityRepository.delete(id);
    }

    @Override
    public long delete(List<String> ids) {
        return entityRepository.delete(ids);
    }

    @Override
    public long deleteAll() {
        return entityRepository.deleteAll();
    }

    @Override
    public BierstandDTO update(BierstandDTO bierstandDTO) {
        return new BierstandDTO(entityRepository.update(bierstandDTO.toBierstandEntity()));
    }

    @Override
    public long update(List<BierstandDTO> personEntities) {
        return entityRepository.update(personEntities.stream().map(BierstandDTO::toBierstandEntity).toList());
    }
}