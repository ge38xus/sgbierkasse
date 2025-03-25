package com.sg.bierkasse.views.billentries;


import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.services.BierstandService;
import com.sg.bierkasse.services.StatisticsService;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.views.components.BierstandGrid;
import com.sg.bierkasse.views.components.MyDatePicker;
import com.sg.bierkasse.views.components.MyIntegerField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Date;

import static com.sg.bierkasse.dtos.BillDTO.*;

@PageTitle("Bierkeller Stand")
@Route(value = "/bierkellerstand", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class BierstandView extends Composite<VerticalLayout> {

    private final MyDatePicker datePicker = new MyDatePicker("Datum");
    private final MyIntegerField blue = new MyIntegerField("Blaue Kisten");
    private final MyIntegerField red = new MyIntegerField("Rote Kisten");
    private final MyIntegerField white = new MyIntegerField("Weiße Kisten");
    private final NumberField wein = UIUtils.getEuroField("Wein Wert");
    private final NumberField sonstiges = UIUtils.getEuroField("Sonstiges");
    private final NumberField kassenstand = UIUtils.getEuroField("Kassenstand");
    private final BierstandGrid grid;
    private final Button save = UIUtils.getSaveButton("Save");

    private final BierstandService bierstandService;


    public BierstandView(BierstandService bierstandService, StatisticsService statisticsService) {
        this.bierstandService = bierstandService;
        grid = new BierstandGrid(bierstandService);

        this.initForm();

        save.addClickListener(o -> {
            Date date = datePicker.getPickedDate();
            int redCnt = red.getIntValue();
            int blueCnt = blue.getIntValue();
            int whiteCnt = white.getIntValue();
            double weinWert = wein.getValue() != null ? wein.getValue() : 0.0;
            double sonstigesWert = sonstiges.getValue() != null ? sonstiges.getValue() : 0.0;
            double kassenStand = kassenstand.getValue() != null ? kassenstand.getValue() : 0.0;

            double sum = redCnt * RED_VALUE * 20 +
                    blueCnt * BLUE_VALUE * 20 +
                    whiteCnt * WHITE_VALUE * 20 +
                    weinWert + sonstigesWert;

            BierstandDTO bierstandDTO = new BierstandDTO(null, date, redCnt, blueCnt, whiteCnt, weinWert, sonstigesWert, sum,
                    statisticsService.getSpendenStandForDate(date), statisticsService.calculateAllPlusAccountsForDate(date),
                    statisticsService.calculateAllMinusAccountsForDate(date), kassenStand);
            bierstandService.save(bierstandDTO);

            this.resetForm();
            UIUtils.showSuccessNotification();
        });
    }

    private void initForm() {
        VerticalLayout layoutColumn = new VerticalLayout();
        H3 header = new H3();

        HorizontalLayout layoutRowDatum = UIUtils.createHorizontalRowLayout();
        HorizontalLayout layoutRowKeller = UIUtils.createHorizontalRowLayout();
        HorizontalLayout layoutRowControls = new HorizontalLayout();

        grid.init(false);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        layoutColumn.setWidth("100%");
        layoutColumn.setHeight("min-content");
        layoutColumn.setFlexGrow(1.0, layoutRowDatum);

        header.setText("Bierkeller aufschreiben");
        header.setWidth("100%");

        getContent().add(layoutColumn);
        getContent().add(grid);

        layoutColumn.add(header);
        layoutColumn.add(layoutRowDatum);
        layoutColumn.add(layoutRowKeller);
        layoutColumn.add(layoutRowControls);

        layoutRowDatum.add(datePicker);
        layoutRowDatum.add(kassenstand);
        layoutRowKeller.add(blue);
        layoutRowKeller.add(red);
        layoutRowKeller.add(white);
        layoutRowKeller.add(wein);
        layoutRowKeller.add(sonstiges);
        layoutRowControls.add(save);
    }

    private void resetForm() {
        blue.setValue(0);
        red.setValue(0);
        white.setValue(0);
        wein.setValue(0.0);
        sonstiges.setValue(0.0);
        grid.setItems(bierstandService.findAll());
    }
}
