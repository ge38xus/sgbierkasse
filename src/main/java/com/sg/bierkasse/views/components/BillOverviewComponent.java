package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.BeerBillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.vaadin.flow.component.grid.Grid;

import java.util.ArrayList;

public class BillOverviewComponent extends Grid<BeerBillDTO> {

    public BillOverviewComponent() {
        super(BeerBillDTO.class, false);
        addColumn(BeerBillDTO::formattedDate).setHeader("Datum");
        addColumn(BeerBillDTO::formattedValue).setHeader("Summe").setKey("summe");
        addColumn(BeerBillDTO::descr).setHeader("Beschreibung");
        addColumn(BeerBillDTO::red).setHeader("Rot");
        addColumn(BeerBillDTO::blue).setHeader("Blau");
        addColumn(BeerBillDTO::white).setHeader("Weiß");
        addColumn(BeerBillDTO::green).setHeader("Grün");

        getColumnByKey("summe").
                setClassNameGenerator(summe -> summe.value() >= 0 ? "c-green" : "c-red");

    }

    public BillOverviewComponent(UserComboBox userComboBox) {
        this();
        userComboBox.addValueChangeListener(o -> {
            if (!userComboBox.isEmpty()) {
                PersonDTO personToChange = userComboBox.getValue().value();
                setItems(personToChange.getBills().stream().sorted().toList());
            }
        });
    }

    public void reset() {
        setItems(new ArrayList<>());
    }
}
