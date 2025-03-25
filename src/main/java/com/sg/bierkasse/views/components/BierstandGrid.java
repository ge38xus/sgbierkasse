package com.sg.bierkasse.views.components;

import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.services.BierstandService;
import com.vaadin.flow.component.grid.Grid;

import static com.sg.bierkasse.services.PDFService.NUMBER_OF_BIERSTANDS_TO_BE_INCLUDED;

public class BierstandGrid extends Grid<BierstandDTO> {

    private final BierstandService bierstandService;

    public BierstandGrid(BierstandService bierstandService) {
        this.bierstandService = bierstandService;
    }

    public BierstandGrid init(boolean shortned) {
        addColumn(BierstandDTO::formattedDate).setHeader("Datum");
        addColumn(BierstandDTO::formattedKassenStand).setHeader("Kassenstand");
        addColumn(BierstandDTO::formattedGuthaben).setHeader("Guthabenstand");
        addColumn(BierstandDTO::formattedSpenden).setHeader("Spendenstand");
        addColumn(BierstandDTO::formattedSum).setHeader("Kellerwert");
        addColumn(BierstandDTO::roteKisten).setHeader("Rote Kisten");
        addColumn(BierstandDTO::blaueKisten).setHeader("Blaue Kisten");
        addColumn(BierstandDTO::weisseKisten).setHeader("Weiße Kisten");
        addColumn(BierstandDTO::formattedWein).setHeader("Wein Wert");
        addColumn(BierstandDTO::formattedRest).setHeader("Rest");
        if (shortned) {
            setItems(bierstandService.findAll().stream().limit(NUMBER_OF_BIERSTANDS_TO_BE_INCLUDED).toList());
        } else {
            setItems(bierstandService.findAll());
        }
        return this;
    }
}
