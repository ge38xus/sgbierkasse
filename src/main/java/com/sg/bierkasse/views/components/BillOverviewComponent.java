package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.vaadin.flow.component.grid.Grid;

import java.util.ArrayList;

public class BillOverviewComponent extends Grid<BillDTO> {

    public BillOverviewComponent() {
        super(BillDTO.class, false);
        addColumn(BillDTO::formattedDate).setHeader("Datum");
        addColumn(BillDTO::formattedValue).setHeader("Summe").setKey("summe");
        addColumn(BillDTO::red).setHeader("Rot");
        addColumn(BillDTO::blue).setHeader("Blau");
        addColumn(BillDTO::white).setHeader("Weiß");
        addColumn(BillDTO::green).setHeader("Grün");

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
