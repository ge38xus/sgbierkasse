package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.BillDTO;
import com.vaadin.flow.component.grid.Grid;

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

}
