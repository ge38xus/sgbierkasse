package com.sg.bierkasse.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sg.bierkasse.dtos.BierstandDTO;
import com.sg.bierkasse.dtos.BillDTO;
import com.sg.bierkasse.dtos.PersonDTO;
import com.sg.bierkasse.utils.PersonDTORankingWrapper;
import org.springframework.beans.factory.annotation.Value;
import com.sg.bierkasse.utils.helpers.FormatUtils;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import static com.sg.bierkasse.views.exportviews.PDFUtils.*;

@Service
public class PDFService {

    @Value("${spring.settings.payment.iban}")
    private String BIERKASSEIBAN;
    @Value("${spring.settings.payment.paypal}")
    private String PAYPALKONTO;
    @Value("${spring.settings.inhabername}")
    private String INHABER;

    private final PersonService personService;
    private final StatisticsService statisticsService;
    private final BierstandService bierstandService;

    public static final Integer NUMBER_OF_BIERSTANDS_TO_BE_INCLUDED = 5;
    private static final List<String> PERSON_HEADERS = List.of("Status", "Name", "Kontostand", "Das letzte Mal eingezahlt am", "Das letzte mal im Plus am");
    private static final List<String> STATISTICS_HEADERS = List.of("Eingezahlt", "Bestellt", "Konsumiert");
    private static final List<String> RANKING_HEADERS = List.of("Blau Sieger", "Rot Sieger", "Weiß Sieger");

    private static final Font H1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
    private static final Font H2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);

    public PDFService(PersonService personService, StatisticsService statisticsService, BierstandService bierstandService) {
        this.personService = personService;
        this.statisticsService = statisticsService;
        this.bierstandService = bierstandService;
    }

    public void createPDFOverview() throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream("Bierkassenbericht.pdf"));

        document.open();

        document.add(new Chunk("Bierkassenbericht zum " + FormatUtils.formatDateToDisplay(new Date()), H1));
        document.add(generateEmptyRow(30));
        document.add(this.generateBierkasseOverview());
        document.add(generateEmptyRow(20));
        document.add(new Chunk("Bierkasse Summen " + statisticsService.getLastConvent().formattedDate() + " - " + FormatUtils.formatDateToDisplay(new Date()) , H2));
        document.add(generateEmptyRow(5));
        document.add(this.generateStatistics());
        document.add(generateEmptyRow(20));
        document.add(new Chunk("Kontostand Einzelpersonen zum " + FormatUtils.formatDateToDisplay(new Date()) , H2));
        document.add(generateEmptyRow(5));
        document.add(this.generatePersonOverview());
        document.add(generateEmptyRow(20));
        document.add(new Chunk("Ranking " + statisticsService.getLastConvent().formattedDate() + " - " + FormatUtils.formatDateToDisplay(new Date()) , H2));
        document.add(generateEmptyRow(5));
        document.add(this.generateRanking());
        document.add(generateEmptyRow(70));

        document.add(this.generateFooter());
        document.close();
    }

    private PdfPTable generateStatistics() {
        PdfPTable table = getPdfTableWithHeader(STATISTICS_HEADERS);
        addStatisticsTableData(table);
        return table;
    }

    private void addStatisticsTableData(PdfPTable table) {
        double moneyInSum = statisticsService.getMoneyInSum();
        table.addCell(generateCell(FormatUtils.formatDoubleToEuro(moneyInSum), 0));
        double invoicesSum = statisticsService.getInvoicesSum();
        table.addCell(generateCell(FormatUtils.formatDoubleToEuro(invoicesSum), 0));
        double consumptionSum = statisticsService.getConsumptionSum();
        table.addCell(generateCell(FormatUtils.formatDoubleToEuro(consumptionSum), 0));

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

    private PdfPTable generateBierkasseOverview() {
        List<BierstandDTO> lastBierkassenStands = bierstandService.findAll().stream().limit(NUMBER_OF_BIERSTANDS_TO_BE_INCLUDED).toList();

        List<String> dates = new java.util.ArrayList<>(List.of("Datum"));
        dates.addAll(
                lastBierkassenStands.stream()
                        .map(BierstandDTO::formattedDate)
                        .toList());

        PdfPTable table = getPdfTableWithHeader(dates);

        List<String> kassenstand = lastBierkassenStands.stream().map(BierstandDTO::formattedKassenStand).toList();
        addHorizontalContentRowWithHeader(table, "Kassenstand", kassenstand, 1);

        List<String> kellerwert = lastBierkassenStands.stream().map(BierstandDTO::formattedSum).toList();
        addHorizontalContentRowWithHeader(table, "Kellerwert", kellerwert, 0);

        List<String> spendenwert = lastBierkassenStands.stream().map(BierstandDTO::formattedSpenden).toList();
        addHorizontalContentRowWithHeader(table, "Spendenstand", spendenwert, 1);

        List<String> guthabenwert = lastBierkassenStands.stream().map(BierstandDTO::formattedGuthaben).toList();
        addHorizontalContentRowWithHeader(table, "Guthabenstand", guthabenwert, 0);

        List<String> schuldenwert = lastBierkassenStands.stream().map(BierstandDTO::formattedSchulden).toList();
        addHorizontalContentRowWithHeader(table, "Schuldenstand", schuldenwert, 1);

        //ToDo: more to be added

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
        iban.setPhrase(new Phrase("Inhaber: " + INHABER, footerFont));
        table.addCell(iban);
        iban.setPhrase(new Phrase("IBAN: " + BIERKASSEIBAN, footerFont));
        table.addCell(iban);
        iban.setPhrase(new Phrase("Paypal: " + PAYPALKONTO, footerFont));
        table.addCell(iban);
        return table;
    }
}
