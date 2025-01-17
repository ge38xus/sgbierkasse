package com.sg.bierkasse.services;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.ConventDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.dtos.RechnungDTO;
import com.sg.bierkasse.utils.PersonDTORankingWrapper;
import com.sg.bierkasse.utils.UserState;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    public static final String SPENDEN_USER_ID = "662d79bc151104579194f5b0";

    private final PersonServiceImpl personService;
    private final ConventDTO lastConvent;

    public StatisticsService(PersonServiceImpl personService, ConventServiceImpl conventService) {
        this.personService = personService;
        this.lastConvent = conventService.getLastConvent();
    }

    public double getInvoicesSum() {
        return personService.getAllRechnungDTOs().stream()
                .filter(rechnungDTO -> rechnungDTO.date().after(lastConvent.date()))
                .mapToDouble(RechnungDTO::value)
                .sum();
    }

    public double getConsumptionSum() {
        return Math.abs(personService.getAllBills().stream()
                .filter(billDTO -> billDTO.date().after(lastConvent.date()))
                .filter(billDTO -> billDTO.value() < 0 && billDTO.isConsumptionBill())
                .mapToDouble(BillDTO::value)
                .sum());
    }

    public double getGuthabenInvoicesSum() {
        return personService.getAllRechnungDTOs().stream()
                .filter(rechnungDTO -> rechnungDTO.date().after(lastConvent.date()) && !rechnungDTO.privateMoneyUsed())
                .mapToDouble(RechnungDTO::value)
                .sum();
    }

    public double getMoneyInSum() {
        double positiveChangesOnUserAccountsSum = personService.getAllBills().stream()
                .filter(billDTO -> billDTO.date().after(lastConvent.date()))
                .filter(billDTO -> billDTO.value() > 0)
                .mapToDouble(BillDTO::value)
                .sum();
        double positiveInvoiceChangesOnUserAccountsSum = this.getGuthabenInvoicesSum();
        return positiveChangesOnUserAccountsSum - positiveInvoiceChangesOnUserAccountsSum;
    }

    public List<PersonDTORankingWrapper> getXKings(ToIntFunction<BillDTO> toIntFunction) {
        return personService.findAll().stream()
                .filter(personDTO -> !personDTO.getState().contentEquals(UserState.S.name))
                .map(personDTO -> new PersonDTORankingWrapper(personDTO, personDTO.getNoOfXSince(lastConvent.date(), toIntFunction)))
                .sorted(Comparator.comparing(PersonDTORankingWrapper::getCounter).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public ConventDTO getLastConvent() {
        return lastConvent;
    }

    public double calculateAllMinusActiveAccounts() {
        return Math.abs(personService.findAll().stream()
                .filter(personDTO -> !Objects.equals(personDTO.getState(), UserState.AH.name))
                .map(PersonDTO::getBalance)
                .filter(o -> o < 0)
                .reduce((Double::sum))
                .orElse(0.0));
    }

    public double calculateAllMinusAccountsForDate(Date date) {
        return Math.abs(personService.findAll().stream()
                .map(p -> p.getBalanceUpToDate(date))
                .filter(o -> o < 0)
                .reduce((Double::sum))
                .orElse(0.0));
    }

    public double calculateAllMinusAHAccounts() {
        return Math.abs(personService.findAll().stream()
                .filter(personDTO -> Objects.equals(personDTO.getState(), UserState.AH.name))
                .map(PersonDTO::getBalance)
                .filter(o -> o < 0)
                .reduce((Double::sum))
                .orElse(0.0));
    }

    public double calculateAllPlusActiveAccounts() {
        return personService.findAll().stream()
                .map(PersonDTO::getBalance)
                .filter(o -> o > 0)
                .reduce((Double::sum))
                .orElse(0.0);
    }

    public double calculateAllPlusAccountsForDate(Date date) {
        return Math.abs(personService.findAll().stream()
                .filter(personDTO -> !Objects.equals(personDTO.getState(), UserState.AH.name))
                .map(p -> p.getBalanceUpToDate(date))
                .filter(o -> o >= 0)
                .reduce((Double::sum))
                .orElse(0.0));
    }

    public double calculateAllPlusAHAccounts() {
        return personService.findAll().stream()
                .filter(personDTO -> Objects.equals(personDTO.getState(), UserState.AH.name))
                .map(PersonDTO::getBalance)
                .filter(o -> o > 0)
                .reduce((Double::sum))
                .orElse(0.0);
    }

    public double getSpendenStandForDate(Date date) {
        return personService.findOne(SPENDEN_USER_ID).getBalanceUpToDate(date);
    }
}
