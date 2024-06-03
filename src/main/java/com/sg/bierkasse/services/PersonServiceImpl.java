package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.RechnungDTO;
import com.sg.bierkasse.dtos.SpendeDTO;
import com.sg.bierkasse.repositories.MongoDBPersonRepo;
import com.sg.bierkasse.utils.EmailTemplates;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements EntityService<PersonDTO> {

    private final MongoDBPersonRepo entityRepository;
    private final EmailService emailService;

    public PersonServiceImpl(MongoDBPersonRepo entityRepository, EmailService emailService) {
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

    public void pushBill(PersonDTO personDTO, BillDTO billDTO, EmailTemplates emailTemplate) {
        entityRepository.pushBill(personDTO.toPersonEntity(), billDTO.toBillEntity());
        personDTO = this.findOne(personDTO.getId());
        emailService.sendMail(personDTO, billDTO, emailTemplate);
    }

    public void pushRechnung(PersonDTO personDTO, RechnungDTO rechnungDTO) {
        entityRepository.pushRechnung(personDTO.toPersonEntity(), rechnungDTO.toRechnungEntity());
        if (rechnungDTO.privateMoneyUsed()) {
            BillDTO billDTO = new BillDTO(0, 0, 0, 0, rechnungDTO.value(), rechnungDTO.date());
            entityRepository.pushBill(personDTO.toPersonEntity(), billDTO.toBillEntity());
        }
    }

//    public void pushSpende(PersonDTO personDTO, SpendeDTO spendeDTO) {
////        entityRepository.pushSpende(personDTO.toPersonEntity(), spendeDTO.toSpendeEntity());
//    }

    public void paySpende(SpendeDTO spende) {

    }

    public List<RechnungDTO> getAllRechnungDTOs(){
        return this.findAll().stream()
                .map(PersonDTO::getInvoices)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<BillDTO> getAllBills(){
        return this.findAll().stream()
                .map(PersonDTO::getBills)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<SpendeDTO> getAllSpenden(){
        return this.findAll().stream()
                .map(PersonDTO::getSpenden)
                .flatMap(List::stream)
                .collect(Collectors.toList());

    }

    public void sendEmailToActiven() {
        emailService.sendMail(this.findOne("662d6c939b1dd66748b79c06"), EmailTemplates.BERICHT);

        this.findAll().stream().filter(PersonDTO::isNotAH).filter(PersonDTO::isExcelRelevant).forEach( o -> emailService.sendMail(o, EmailTemplates.BERICHT));
    }
}