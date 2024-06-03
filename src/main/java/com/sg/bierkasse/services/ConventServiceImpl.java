package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.ConventDTO;
import com.sg.bierkasse.repositories.MongoDBConventRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ConventServiceImpl implements EntityService<ConventDTO> {

    private final MongoDBConventRepo entityRepository;

    public ConventServiceImpl(MongoDBConventRepo entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public ConventDTO save(ConventDTO conventDTO) {
        return new ConventDTO(entityRepository.save(conventDTO.toConventEntity()));
    }

    @Override
    public List<ConventDTO> saveAll(List<ConventDTO> personEntities) {
        return personEntities.stream()
                .map(ConventDTO::toConventEntity)
                .peek(entityRepository::save)
                .map(ConventDTO::new)
                .toList();
    }

    @Override
    public List<ConventDTO> findAll() {
        return entityRepository.findAll().stream().map(ConventDTO::new).toList();
    }

    @Override
    public List<ConventDTO> findAll(List<String> ids) {
        return entityRepository.findAll(ids).stream().map(ConventDTO::new).toList();
    }

    @Override
    public ConventDTO findOne(String id) {
        return new ConventDTO(entityRepository.findOne(id));
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
    public ConventDTO update(ConventDTO conventDTO) {
        return new ConventDTO(entityRepository.update(conventDTO.toConventEntity()));
    }

    @Override
    public long update(List<ConventDTO> personEntities) {
        return entityRepository.update(personEntities.stream().map(ConventDTO::toConventEntity).toList());
    }

    public ConventDTO getLastConvent(){
        return this.findAll().stream().max(Comparator.comparing(ConventDTO::date)).orElse(new ConventDTO(null, new Date(), "default"));
    }
}