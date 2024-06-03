package com.sg.bierkasse.views.overviews;

import java.util.ArrayList;
import java.util.List;

import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Kassenstand")
@Route(value = "overview", layout = MainLayout.class)
@Uses(Icon.class)
@CssImport(
        themeFor = "vaadin-grid",
        value = "views.css"
)
public class Overview extends Composite<VerticalLayout> {

    PersonServiceImpl personService;

    public Overview(PersonServiceImpl personService) {
        this.personService = personService;

        Grid<PersonDTO> grid = new Grid<>(PersonDTO.class, false);
        grid.addColumn(PersonDTO::getState).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(PersonDTO::getLastName).setHeader("Nachname");
        grid.addColumn(PersonDTO::getEmail).setHeader("Email");
        grid.addColumn(PersonDTO::getFormattedBalance).setHeader("Kassenstand").setKey("summe");
        grid.addColumn(PersonDTO::lastPayedOn).setHeader("Nicht eingezahlt seit");
        grid.addColumn(PersonDTO::lastPositiveOn).setHeader("In Minus seit");

        grid.getColumnByKey("summe").
                setClassNameGenerator(summe -> summe.getBalance() >= 0 ? "c-green" : "c-red");

        List<PersonDTO> people = new ArrayList<>(personService.findAll());
        people.sort(PersonDTO::compareTo);

        grid.setItems(people);

        getContent().add(grid);
        getContent().setHeight("100%");
    }

}
