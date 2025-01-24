package com.sg.bierkasse.views.billentries;


import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.services.BierstandService;
import com.sg.bierkasse.services.StatisticsService;
import com.sg.bierkasse.utils.Utils;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import static com.sg.bierkasse.dtos.BillDTO.*;

@PageTitle("Bierkeller Stand")
@Route(value = "/bierkellerstand", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class BierstandView extends Composite<VerticalLayout> {

    public BierstandView(BierstandService bierstandService, StatisticsService statisticsService) {
        VerticalLayout layoutColumn = new VerticalLayout();

        H3 h3 = new H3();
        VerticalLayout layoutColumn3 = new VerticalLayout();

        HorizontalLayout layoutRowDatum = Utils.createHorizontalRowLayout();
        HorizontalLayout layoutRowKeller = Utils.createHorizontalRowLayout();

        DatePicker datePicker = new DatePicker("Datum");
        datePicker.setLocale(new Locale("de", "DE"));
        IntegerField blue = Utils.getIntegerField("Blaue Kisten");
        IntegerField red = Utils.getIntegerField("Rote Kisten");
        IntegerField white = Utils.getIntegerField("Weiße Kisten");
        NumberField wein = Utils.getEuroField("Wein Wert");
        NumberField sonstiges = Utils.getEuroField("Sonstiges");
        NumberField kassenstand = Utils.getEuroField("Kassenstand");

        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button save = new Button();
        Grid<BierstandDTO> grid = new Grid<>(BierstandDTO.class, false);
        grid.addColumn(BierstandDTO::formattedDate).setHeader("Datum");
        grid.addColumn(BierstandDTO::formattedKassenStand).setHeader("Kassenstand");
        grid.addColumn(BierstandDTO::formattedSum).setHeader("Kellerwert");
        grid.addColumn(BierstandDTO::roteKisten).setHeader("Rote Kisten");
        grid.addColumn(BierstandDTO::blaueKisten).setHeader("Blaue Kisten");
        grid.addColumn(BierstandDTO::weisseKisten).setHeader("Weiße Kisten");
        grid.addColumn(BierstandDTO::formattedWein).setHeader("Wein Wert");
        grid.addColumn(BierstandDTO::formattedRest).setHeader("Rest");

        grid.setItems(bierstandService.findAll());
        save.addClickListener(o -> {
            Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            int redCnt = red.getValue() != null ? red.getValue() : 0;
            int blueCnt = blue.getValue() != null ? blue.getValue() : 0;
            int whiteCnt = white.getValue() != null ? white.getValue() : 0;
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
            blue.setValue(0);
            red.setValue(0);
            white.setValue(0);
            wein.setValue(0.0);
            sonstiges.setValue(0.0);
            Notification notification = Notification
                    .show("Submitted!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            grid.setItems(bierstandService.findAll());
        });

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        layoutColumn.setWidth("100%");
        layoutColumn.setHeight("min-content");
        layoutColumn.setFlexGrow(1.0, layoutColumn3);

        h3.setText("Bierkeller aufschreiben");
        h3.setWidth("100%");

        layoutColumn3.setWidthFull();
        layoutColumn3.setWidth("100%");
        layoutColumn3.setFlexGrow(1.0, layoutRowDatum);

        save.setText("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(layoutColumn);
        getContent().add(grid);
        layoutColumn.add(h3);
        layoutColumn.add(layoutColumn3);
        layoutColumn3.add(layoutRowDatum);

        layoutRowDatum.add(datePicker);
        layoutRowDatum.add(kassenstand);
        layoutRowKeller.add(blue);
        layoutRowKeller.add(red);
        layoutRowKeller.add(white);
        layoutRowKeller.add(wein);
        layoutRowKeller.add(sonstiges);

        layoutColumn3.add(layoutRowKeller);
        layoutColumn.add(layoutRow2);
        layoutRow2.add(save);
    }
}
