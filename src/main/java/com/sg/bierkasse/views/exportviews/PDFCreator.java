package com.sg.bierkasse.views.exportviews;

import com.itextpdf.text.DocumentException;
import com.sg.bierkasse.dtos.ConventDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.services.BierstandService;
import com.sg.bierkasse.services.ConventService;
import com.sg.bierkasse.services.PDFService;
import com.sg.bierkasse.services.PersonService;
import com.sg.bierkasse.views.MainLayout;
import com.sg.bierkasse.views.components.BierstandGrid;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Bierkassenbericht")
@Route(value = "export", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class PDFCreator extends Composite<VerticalLayout> {

    private final PersonService personService;
    private final ConventService conventService;
    private final PDFService pdfService;

    private Checkbox sendCheckbox;
    private Checkbox testBerichtCheckbox;

    private Button exportButton;

    public PDFCreator(PersonService personService, ConventService conventService, PDFService pdfService, BierstandService bierstandService) {
        this.personService = personService;
        this.pdfService = pdfService;
        this.conventService = conventService;

        BierstandGrid grid = new BierstandGrid(bierstandService);
        grid.init(true);
        getContent().add(grid);
        grid.setHeight("25%");
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
        grid.setHeight("60%");
        return grid;
    }

    private HorizontalLayout generateFooter() {
        exportButton = generateExportButton();
        sendCheckbox = new Checkbox("Versende Bericht");
        sendCheckbox.setValue(false);
        sendCheckbox.setEnabled(false);
        sendCheckbox.setTooltipText("Sendet Bericht an Alle vermerkten Nutzer und startet neuen Abrechnungsraum.");
        sendCheckbox.addValueChangeListener(o -> checkEnable());
        testBerichtCheckbox = new Checkbox("Testbericht");
        testBerichtCheckbox.setTooltipText("Bericht wird nur an dem Admin nutzer geschickt und nicht in System abgelegt.");
        testBerichtCheckbox.setValue(true);
        testBerichtCheckbox.addValueChangeListener(o -> checkEnable());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(exportButton);
        horizontalLayout.add(sendCheckbox);
        horizontalLayout.add(testBerichtCheckbox);
        return horizontalLayout;
    }

    private void checkEnable() {
        sendCheckbox.setEnabled(!testBerichtCheckbox.getValue());
        if (!sendCheckbox.isEnabled()) {
            sendCheckbox.setValue(false);
        }
        exportButton.setEnabled(sendCheckbox.getValue() || testBerichtCheckbox.getValue());
    }

    private Button generateExportButton() {
        Button export = new Button("Export");
        export.addClickListener(o -> {
            try {
                pdfService.createPDFOverview();
                if (sendCheckbox.getValue()) {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Erstellen & Versenden von Bericht");
                    confirmDialog.setText("Bist du Dir sicher, dass du ein neues Abrechungzeitraum definieren möchtest und anschließend das Bericht über das letzte Abrechnungzeitraum an alle eingetragene F, CB, iaCB und andere entsprechend markierte Nutzer verschicken möchtest?");
                    confirmDialog.setConfirmText("Neues Abrechnungsraum starten & Bericht verschicken");
                    confirmDialog.setRejectText("Cancel");
                    confirmDialog.open();
                    confirmDialog.addConfirmListener(o2 -> {
                        if (!testBerichtCheckbox.getValue()) {
                            ConventDTO conventDTO = new ConventDTO(new ObjectId(), new Date(), "");
                            conventService.save(conventDTO);
                            Notification notification = Notification
                                    .show("Neue Abrechnungperiode wurde definiert!");
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }
                        personService.sendBerichtToRelevant();
                        Notification notification = Notification
                                .show("Bericht wurde versendet!");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    });
                } else {
                    personService.sendBerichtToTest();
                    Notification notification = Notification
                            .show("ONLY TEST SENT TO RECEIVER!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                }
            } catch (DocumentException | MessagingException | IOException e) {
                Notification notification = Notification.show("Fehler: " + e.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        return export;
    }
}
