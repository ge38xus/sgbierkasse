package com.sg.bierkasse.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.utils.PersonDTORankingWrapper;
import com.sg.bierkasse.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import static com.sg.bierkasse.views.exportviews.PDFUtils.*;

@Service
public class PDFService {

    private static final String BIERKASSEIBAN = "DE92 5002 4024 7529 7915 31";
    private static final String PAYPALKONTO = "bierkasse.sg@gmail.com";

    private final PersonServiceImpl personService;
    private final StatisticsService statisticsService;

    private static final List<String> PERSON_HEADERS = List.of("Status", "Name", "Kontostand", "Das letzte Mal eingezahlt am", "Das letzte mal im Plus am");
    private static final List<String> STATISTICS_HEADERS = List.of("Eingezahlt", "Bestellt", "Konsumiert");
    private static final List<String> DAY_STATISTICS_HEADERS = List.of("Guthaben Aktive", "Guthaben AH", "Schulden Aktive", "Schulden AH");
    private static final List<String> RANKING_HEADERS = List.of("Blau Sieger", "Rot Sieger", "Weiß Sieger");

    public PDFService(PersonServiceImpl personService, StatisticsService statisticsService) {
        this.personService = personService;
        this.statisticsService = statisticsService;
    }

    public void createPDFOverview() throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream("Bierkassenbericht.pdf"));

        document.open();
        Font h1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font h2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);

        Chunk chunk = new Chunk("Bierkassenbericht zum " + Utils.formatDateToDisplay(new Date()), h1);
        document.add(chunk);
        document.add(this.generateEmptyRow(30));
        document.add(new Chunk("Bierkasse Summen " + statisticsService.getLastConvent().formattedDate() + " - " + Utils.formatDateToDisplay(new Date()) , h2));
        document.add(this.generateEmptyRow(5));
        document.add(this.generateStatistics());
        document.add(this.generateEmptyRow(20));
        document.add(new Chunk("Guthaben und Schulden zum " + Utils.formatDateToDisplay(new Date()) + " Gesamt", h2));
        document.add(this.generateEmptyRow(5));
        document.add(this.generateDayStatistics());
        document.add(this.generateEmptyRow(20));
        document.add(new Chunk("Kontostand Einzelpersonen zum " + Utils.formatDateToDisplay(new Date()) , h2));
        document.add(this.generateEmptyRow(5));
        document.add(this.generatePersonOverview());
        document.add(this.generateEmptyRow(20));
        document.add(new Chunk("Ranking " + statisticsService.getLastConvent().formattedDate() + " - " + Utils.formatDateToDisplay(new Date()) , h2));
        document.add(this.generateEmptyRow(5));
        document.add(this.generateRanking());
        document.add(this.generateEmptyRow(100));
        document.add(this.generateFooter());
        document.close();
    }

    private PdfPTable generateFooter() {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(PageSize.A4.getWidth() - 74);
        table.setLockedWidth(true);
        PdfPCell iban = new PdfPCell();
        iban.setHorizontalAlignment(Element.ALIGN_CENTER);
        iban.setBorderWidth(0);
        iban.setPhrase(new Phrase("Bierkasse S-G!", footerFont));
        table.addCell(iban);
        iban.setPhrase(new Phrase("Inhaber: Tadeusz Czyzewicz", footerFont));
        table.addCell(iban);
        iban.setPhrase(new Phrase("IBAN: " + BIERKASSEIBAN, footerFont));
        table.addCell(iban);
        iban.setPhrase(new Phrase("Paypal: " + PAYPALKONTO, footerFont));
        table.addCell(iban);
        return table;
    }

    private PdfPTable generateDayStatistics() {
        PdfPTable table = getPdfTableWithHeader(DAY_STATISTICS_HEADERS);
        addStatisticsDayData(table);
        return table;
    }

    private void addStatisticsDayData(PdfPTable table) {
        double positiveAccountsActive = statisticsService.calculateAllPlusActiveAccounts();
        table.addCell(generateCell(Utils.formatDoubleToEuro(positiveAccountsActive), 0));
        double positiveAccountsAH = statisticsService.calculateAllPlusAHAccounts();
        table.addCell(generateCell(Utils.formatDoubleToEuro(positiveAccountsAH), 0));
        double negativeAccountsActive = statisticsService.calculateAllMinusActiveAccounts();
        table.addCell(generateCell(Utils.formatDoubleToEuro(negativeAccountsActive), 0));
        double negativeAccountsAH = statisticsService.calculateAllMinusAHAccounts();
        table.addCell(generateCell(Utils.formatDoubleToEuro(negativeAccountsAH), 0));

        table.completeRow();
    }

    private PdfPTable generateEmptyRow(int height) {
        PdfPTable pdfPTable = new PdfPTable(1);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorderWidth(0);
        pdfPCell.setFixedHeight(height);
        pdfPTable.addCell(pdfPCell);
        return pdfPTable;
    }



    private PdfPTable generateStatistics() {
        PdfPTable table = getPdfTableWithHeader(STATISTICS_HEADERS);
        addStatisticsTableData(table);
        return table;
    }

    private PdfPTable generateRanking() {
        PdfPTable table = getPdfTableWithHeader(RANKING_HEADERS);
        List<PersonDTORankingWrapper> blueKings = statisticsService.getXKings(BillDTO::blue);
        List<PersonDTORankingWrapper> redKings = statisticsService.getXKings(BillDTO::red);
        List<PersonDTORankingWrapper> whiteKings = statisticsService.getXKings(BillDTO::white);

        for (int place = 0; place < 3; place++) {
            table.addCell(generateRankCell(blueKings.get(place), place + 1));
            table.addCell(generateRankCell(redKings.get(place), place + 1));
            table.addCell(generateRankCell(whiteKings.get(place), place + 1));
        }

        return table;
    }


    private void addStatisticsTableData(PdfPTable table) {
        double moneyInSum = statisticsService.getMoneyInSum();
        table.addCell(generateCell(Utils.formatDoubleToEuro(moneyInSum), 0));
        double invoicesSum = statisticsService.getInvoicesSum();
        table.addCell(generateCell(Utils.formatDoubleToEuro(invoicesSum), 0));
        double consumptionSum = statisticsService.getConsumptionSum();
        table.addCell(generateCell(Utils.formatDoubleToEuro(consumptionSum), 0));


        table.completeRow();
    }

    private PdfPTable generatePersonOverview() throws DocumentException {
        PdfPTable table = getPdfTableWithHeader(PERSON_HEADERS);
        table.setWidths(new float[] {0.08f, 0.4f, 0.12f, 0.2f, 0.2f});
        addRows(table);
        return table;
    }


    private void addRows(PdfPTable table) {
        List<PersonDTO> people = personService.findAll().stream()
                                    .filter(PersonDTO::isExcelRelevant)
                                    .sorted(PersonDTO::compareTo)
                                    .toList();
        int counter = 0;
        for (PersonDTO personDTO: people) {
            table.addCell(generateCell(personDTO.getState(), counter));
            table.addCell(generateCell(personDTO.getLastName(), counter));
            table.addCell(generateCell(personDTO.getFormattedBalance(), counter));
            table.addCell(generateCell(personDTO.lastPayedOn(), counter));
            table.addCell(generateCell(personDTO.lastPositiveOn(), counter));
            counter++;
        }
    }
}
