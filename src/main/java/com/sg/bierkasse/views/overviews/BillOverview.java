package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.Utils;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Buchungen")
@Route(value = "bill-overview", layout = MainLayout.class)
@Uses(Icon.class)
@CssImport(
        themeFor = "vaadin-grid",
        value = "views.css"
)
public class BillOverview extends Composite<VerticalLayout> {

    PersonServiceImpl personService;

    public BillOverview(PersonServiceImpl personService) {
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        this.personService = personService;

        Grid<BillDTO> grid = new Grid<>(BillDTO.class, false);
        grid.addColumn(BillDTO::formattedDate).setHeader("Datum");
        grid.addColumn(BillDTO::formattedValue).setHeader("Summe").setKey("summe");
        grid.addColumn(BillDTO::red).setHeader("Rot");
        grid.addColumn(BillDTO::blue).setHeader("Blau");
        grid.addColumn(BillDTO::white).setHeader("Weiß");
        grid.addColumn(BillDTO::green).setHeader("Grün");

        grid.getColumnByKey("summe").
                setClassNameGenerator(summe -> summe.value() >= 0 ? "c-green" : "c-red");

        ComboBox<PersonRecord> comboBox = Utils.getComboBoxWithPersonDTOData(personService.findAll());
        H4 h3 = new H4();
        comboBox.addValueChangeListener(o -> {
            String chosenPersonId = o.getValue().value().getId();
            PersonDTO chosenPerson = personService.findOne(chosenPersonId);
            grid.setItems(chosenPerson.getBills());
            h3.setText("Kassenstand: " + Utils.formatDoubleToEuro(chosenPerson.getBalance()));
        });
        horizontalLayout.add(comboBox);
        horizontalLayout.add(h3);
        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
    }
}
