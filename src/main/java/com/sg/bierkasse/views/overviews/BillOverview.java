package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.utils.PersonRecord;
import com.sg.bierkasse.utils.helpers.UIUtils;
import com.sg.bierkasse.views.components.BillOverviewComponent;
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
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Buchungen")
@Route(value = "bill-overview", layout = MainLayout.class)
@Uses(Icon.class)
@CssImport(
        themeFor = "vaadin-grid",
        value = "views.css"
)
@RolesAllowed("ADMIN")
public class BillOverview extends Composite<VerticalLayout> {

    public BillOverview(PersonService personService) {
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Grid<BillDTO> grid = new BillOverviewComponent();
        ComboBox<PersonRecord> comboBox = UIUtils.getComboBoxWithPersonDTOData(personService.findAll());
        H4 h3 = new H4();
        comboBox.addValueChangeListener(o -> {
            String chosenPersonId = o.getValue().value().getId();
            PersonDTO chosenPerson = personService.findOne(chosenPersonId);
            grid.setItems(chosenPerson.getBills());
            h3.setText("Kassenstand: " + FormatUtils.formatDoubleToEuro(chosenPerson.getBalance()));
        });
        horizontalLayout.add(comboBox);
        horizontalLayout.add(h3);
        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
        getContent().setHeight("100%");
    }
}
