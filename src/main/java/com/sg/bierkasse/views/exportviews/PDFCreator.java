package com.sg.bierkasse.views.exportviews;

import com.itextpdf.text.DocumentException;
import com.sg.bierkasse.dtos.ConventDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.ConventServiceImpl;
import com.sg.bierkasse.services.PDFService;
import com.sg.bierkasse.services.PersonServiceImpl;
import com.sg.bierkasse.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.bson.types.ObjectId;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Bierkassenbericht")
@Route(value = "export", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class PDFCreator extends Composite<VerticalLayout> {

    private final PersonServiceImpl personService;

    private final ConventServiceImpl conventService;
    private final PDFService pdfService;

    private Checkbox sendCheckbox;
    private Checkbox testBerichtCheckbox;

    public PDFCreator(PersonServiceImpl personService, ConventServiceImpl conventService, PDFService pdfService) {
        this.personService = personService;
        this.pdfService = pdfService;
        this.conventService = conventService;

        getContent().add(generateGrid());

        getContent().add(generateFooter());
        getContent().setHeight("100%");
    }

    private Grid<PersonDTO> generateGrid() {
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
        people = people.stream()
                .filter(PersonDTO::isExcelRelevant)
                .sorted(PersonDTO::compareTo)
                .collect(Collectors.toList());
        grid.setItems(people);
        return grid;
    }

    private HorizontalLayout generateFooter() {
        sendCheckbox = new Checkbox("Versende an alle CB, iaCB, F");
        sendCheckbox.setValue(false);
        testBerichtCheckbox = new Checkbox("*Testbericht");
        testBerichtCheckbox.setValue(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(generateExportButton());
        horizontalLayout.add(sendCheckbox);
        horizontalLayout.add(testBerichtCheckbox);
        return horizontalLayout;
    }

    private Button generateExportButton() {
        Button export = new Button("Export");
        export.addClickListener(o -> {
            try {
                pdfService.createPDFOverview();
                if (!testBerichtCheckbox.getValue()) {
                    ConventDTO conventDTO = new ConventDTO(new ObjectId(), new Date(), "");
                    conventService.save(conventDTO);
                    Notification notification = Notification
                            .show("Export saved!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                if (sendCheckbox.getValue()) {
                    personService.sendEmailToActiven();
                    Notification notification = Notification
                            .show("Export sent!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            } catch (DocumentException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return export;
    }


}
