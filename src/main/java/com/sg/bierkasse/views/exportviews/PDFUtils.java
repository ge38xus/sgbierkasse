package com.sg.bierkasse.views.exportviews;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sg.bierkasse.utils.PersonDTORankingWrapper;

import java.util.List;

public class PDFUtils {

    public static PdfPTable getPdfTableWithHeader(List<String> headers) {
        PdfPTable table = new PdfPTable(headers.size());
        table.setTotalWidth(PageSize.A4.getWidth() - 74);
        table.setLockedWidth(true);
        headers.forEach(columnTitle -> addHeaderCellToTableWithStringContent(table, columnTitle));
        return table;
    }

    public static PdfPTable generateEmptyRow(int height) {
        PdfPTable pdfPTable = new PdfPTable(1);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorderWidth(0);
        pdfPCell.setFixedHeight(height);
        pdfPTable.addCell(pdfPCell);
        return pdfPTable;
    }

    public static void addHorizontalContentRowWithHeader(PdfPTable table, String header, List<String> content, int color) {
        addHeaderCellToTableWithStringContent(table, header);
        for (String el: content) {
            table.addCell(generateCell(el, color));
        }
        table.completeRow();
    }
    public static void addHeaderCellToTableWithStringContent(PdfPTable table, String columnTitle) {
        PdfPCell header = new PdfPCell();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new BaseColor(2, 133, 12));
        header.setBackgroundColor(new BaseColor(190, 190, 190));
        header.setBorderWidth(0);
        header.setPhrase(new Phrase(columnTitle, font));
        table.addCell(header);
    }

    public static PdfPCell generateCell(String content, int counter) {
        PdfPCell cell = new PdfPCell();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        if (counter % 2 == 0) {
            cell.setBackgroundColor(new BaseColor(220, 220, 220));
        } else {
            cell.setBackgroundColor(new BaseColor(230, 230, 230));
        }
        cell.setBorderWidth(0);
        cell.setPhrase(new Phrase(content, font));
        return cell;
    }

    public static PdfPCell generateRankCell(PersonDTORankingWrapper wrapper, int place) {
        PdfPCell cell = new PdfPCell();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        switch (place) {
            case 1 -> {
                cell.setBackgroundColor(new BaseColor(252, 186, 3));
                font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            }
            case 2 -> cell.setBackgroundColor(new BaseColor(230, 230, 230));
            case 3 -> cell.setBackgroundColor(new BaseColor(220, 220, 220));
        }
        cell.setBorderWidth(0);
        cell.setPhrase(new Phrase(place + ". " + wrapper.toString(), font));
        return cell;
    }
}
