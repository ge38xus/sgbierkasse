package com.sg.bierkasse.dtos;

import com.sg.bierkasse.entities.PersonEntity;
import com.sg.bierkasse.utils.UserState;
import com.sg.bierkasse.utils.Utils;
import org.bson.types.ObjectId;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;


public class PersonDTO {
        String id;
        String firstName;
        String lastName;
        String email;
        String state;
        List<BillDTO> bills;
        List<RechnungDTO> invoices;
        List<SpendeDTO> spenden;

        boolean excelRelevant;
        boolean berichtReceiver;



    public PersonDTO(PersonEntity p) {
        this.id = p.getId() == null ? new ObjectId().toHexString() : p.getId().toHexString();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.email = p.getEmail();
        this.state = p.getState();
        this.bills = p.getBillEntities().stream().map(BillDTO::new).toList();
        this.invoices = p.getRechnungEntities().stream().map(RechnungDTO::new).toList();
//        this.spenden = p.getSpendeEntities().stream().map(SpendeDTO::new).toList();
        this.excelRelevant = p.isExcelRelevant();
        this.berichtReceiver = p.isBerichtReceiver();
    }

    public PersonDTO(String id, String firstName, String lastName, String email, String state, List<BillDTO> bills, List<RechnungDTO> invoices, Boolean excelRelevant) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.state = state;
        this.bills = bills;
        this.invoices = invoices;
        this.excelRelevant = excelRelevant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<BillDTO> getBills() {
        return bills;
    }

    public void addBill(BillDTO bill) {
        this.bills.add(bill);
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public List<RechnungDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<RechnungDTO> invoices) {
        this.invoices = invoices;
    }

    public boolean isExcelRelevant() {
        return excelRelevant;
    }

    public void setExcelRelevant(boolean excelRelevant) {
        this.excelRelevant = excelRelevant;
    }

    public List<SpendeDTO> getSpenden() {
        return spenden;
    }

    public void setSpenden(List<SpendeDTO> spenden) {
        this.spenden = spenden;
    }

    public boolean isBerichtReceiver() {
        return berichtReceiver;
    }

    public void setBerichtReceiver(boolean berichtReceiver) {
        this.berichtReceiver = berichtReceiver;
    }

    public PersonEntity toPersonEntity() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return new PersonEntity(_id, firstName, lastName, email, state,
                bills.stream().map(BillDTO::toBillEntity).toList(),
                invoices.stream().map(RechnungDTO::toRechnungEntity).toList(),
//                spenden.stream().map(SpendeDTO::toSpendeEntity).toList(),
                excelRelevant, berichtReceiver
        );
    }

    public double getBalance() {
        if (bills != null && !bills.isEmpty()) {
            return bills.stream().mapToDouble(BillDTO::value).sum();
        }
        return 0.0;
    }

    public String getFormattedBalance() {
        return Utils.formatDoubleToEuro(getBalance());
    }
    public int compareTo(PersonDTO other) {
        int i = UserState.valueOf(state).compareTo(UserState.valueOf(other.state));
        if (i != 0) return i;

        return lastName.compareTo(other.lastName);
    }

    public String lastPayedOn() {
        if (bills != null && !bills.isEmpty()) {
            return bills.stream()
                    .filter(o -> (o.value() > 0))
                    .sorted(Comparator.comparing(BillDTO::date).reversed())
                    .map(BillDTO::date)
                    .map(Utils::formatDateToDisplay)
                    .findFirst().orElse(" - ");
        }
        return " - ";
    }

    public String lastPositiveOn() {
        if (bills != null && !bills.isEmpty()) {
            double balance = this.getBalance();
            if (balance >= 0) {
                return " - ";
            }
           for (int i = bills.size() - 1; i >= 0; i--) {
               balance -= bills.get(i).value();
               if (balance >= 0) {
                   return Utils.formatDateToDisplay(bills.get(i).date());
               }
           }
        }
        return " - ";
    }

    public int getNoOfXSince(Date conventDate, ToIntFunction<BillDTO> billDTOToIntFunction) {
        return this.getBills().stream()
                .filter(billDTO -> billDTO.date().after(conventDate))
                .mapToInt(billDTOToIntFunction)
                .sum();
    }

    public boolean isNotAH() {
        return Objects.equals(this.state, UserState.AH.name);
    }
}