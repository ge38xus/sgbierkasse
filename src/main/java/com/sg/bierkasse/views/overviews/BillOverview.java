package com.sg.bierkasse.views.overviews;

import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.views.components.BillOverviewComponent;
import com.sg.bierkasse.views.components.UserComboBox;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
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

        UserComboBox userComboBox = new UserComboBox(personService, true);
        BillOverviewComponent grid = new BillOverviewComponent(userComboBox);
        horizontalLayout.add(userComboBox);
        layoutColumn.add(horizontalLayout);
        layoutColumn.add(grid);
        layoutColumn.setHeight("100%");
        getContent().add(layoutColumn);
        getContent().setHeight("100%");
    }
}
