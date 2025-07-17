package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.*;
import com.sg.bierkasse.repositories.MongoDBPersonRepo;
import com.sg.bierkasse.utils.EmailTemplates;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService implements EntityService<PersonDTO> {

    @Value("${spring.settings.masteruserid}")
    private String MASTER_USER_ID;

    private final MongoDBPersonRepo entityRepository;
    private final EmailService emailService;

    public PersonService(MongoDBPersonRepo entityRepository, EmailService emailService) {
        this.entityRepository = entityRepository;
        this.emailService = emailService;
    }

    @Override
    public PersonDTO save(PersonDTO PersonDTO) {
        return new PersonDTO(entityRepository.save(PersonDTO.toPersonEntity()));
    }

    @Override
    public List<PersonDTO> saveAll(List<PersonDTO> personEntities) {
        return personEntities.stream()
                .map(PersonDTO::toPersonEntity)
                .peek(entityRepository::save)
                .map(PersonDTO::new)
                .toList();
    }

    @Override
    public List<PersonDTO> findAll() {
        return entityRepository.findAll().stream().map(PersonDTO::new).toList();
    }

    @Override
    public List<PersonDTO> findAll(List<String> ids) {
        return entityRepository.findAll(ids).stream().map(PersonDTO::new).toList();
    }

    @Override
    public PersonDTO findOne(String id) {
        return new PersonDTO(entityRepository.findOne(id));
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
    public PersonDTO update(PersonDTO PersonDTO) {
        return new PersonDTO(entityRepository.update(PersonDTO.toPersonEntity()));
    }

    @Override
    public long update(List<PersonDTO> personEntities) {
        return entityRepository.update(personEntities.stream().map(PersonDTO::toPersonEntity).toList());
    }

    public void pushBill(PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate, boolean sendNotification) throws MessagingException, IOException {
        entityRepository.pushBill(personDTO.toPersonEntity(), billDTO.toBillEntity());
        personDTO = this.findOne(personDTO.getId());
        if (sendNotification) {
            emailService.sendMail(personDTO, billDTO, emailTemplate);
        }
    }

    public void pushRechnung(PersonDTO personDTO, RechnungDTO rechnungDTO) {
        entityRepository.pushRechnung(personDTO.toPersonEntity(), rechnungDTO.toRechnungEntity());
        if (rechnungDTO.privateMoneyUsed()) {
            BillDTO billDTO = new BillDTO(0, 0, 0, 0, 0, "", rechnungDTO.value() , rechnungDTO.date(), rechnungDTO.description());
            entityRepository.pushBill(personDTO.toPersonEntity(), billDTO.toBillEntity());
        }
    }

    public void pushSpende(PersonDTO personDTO, SpendeDTO spendeDTO) {
        entityRepository.pushSpende(personDTO.toPersonEntity(), spendeDTO.toSpendeEntity());
    }

    public void paySpende(SpendeDTO spende) {
        entityRepository.paySpende(spende.toSpendeEntity());
    }

    public List<RechnungDTO> getAllRechnungDTOs(){
        return this.findAll().stream()
                .map(PersonDTO::getInvoices)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(RechnungDTO::date))
                .collect(Collectors.toList());
    }

    public List<BillDTO> getAllBills(){
        return this.findAll().stream()
                .map(PersonDTO::getBills)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(BillDTO::date))
                .collect(Collectors.toList());
    }

    public List<SpendeDTO> getAllSpenden(){
        return this.findAll().stream()
                .map(PersonDTO::getSpenden)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void sendBerichtToRelevant() {
        this.findAll().stream()
                .filter(PersonDTO::isBerichtReceiver)
                .forEach(o -> {
                    try {
                        emailService.sendMail(o, EmailTemplates.BERICHT);
                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void sendBerichtToTest() throws MessagingException, IOException {
        emailService.sendMail(findOne(MASTER_USER_ID), EmailTemplates.BERICHT);
    }

    public void useSpende(SpendeDTO spende) {
        // ToDo: implement this
        throw new NotImplementedException("Not implemented yet");
    }
}