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
        this.spenden = p.getSpendeEntities().stream().map(SpendeDTO::new).toList();
        this.excelRelevant = p.isExcelRelevant();
        this.berichtReceiver = p.isBerichtReceiver();
    }

    public PersonDTO(String id, String firstName, String lastName, String email, String state, List<BillDTO> bills, List<RechnungDTO> invoices, List<SpendeDTO> spenden, Boolean excelRelevant, Boolean berichtReceiver) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.state = state;
        this.bills = bills;
        this.invoices = invoices;
        this.spenden = spenden;
        this.excelRelevant = excelRelevant;
        this.berichtReceiver = berichtReceiver;
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

    public String getLastName() {
        return lastName;
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

    public List<RechnungDTO> getInvoices() {
        return invoices;
    }

    public boolean isExcelRelevant() {
        return excelRelevant;
    }

    public List<SpendeDTO> getSpenden() {
        return spenden;
    }

    public boolean isBerichtReceiver() {
        return berichtReceiver;
    }

    public PersonEntity toPersonEntity() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return new PersonEntity(_id, firstName, lastName, email, state,
                bills.stream().map(BillDTO::toBillEntity).toList(),
                invoices.stream().map(RechnungDTO::toRechnungEntity).toList(),
                spenden.stream().map(SpendeDTO::toSpendeEntity).toList(),
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

    public boolean isNotAHAndHB() {
        return !Objects.equals(this.state, UserState.AH.name) && !Objects.equals(this.state, UserState.HB.name) && (!Objects.equals(this.state, UserState.S.name)) || this.berichtReceiver;
    }
}