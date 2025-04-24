package com.sg.bierkasse.utils;

public enum EmailTemplates {
    DRINKS_OVERVIEW("Abrechnung von ${current-date}", "resources/drinks-overview.html"),
    BOOK_IN_MONEY("Einzahlung von ${current-date}", "resources/book-money.html"),
    BOOK_OUT_MONEY("Auszahlung von ${current-date}", "resources/pay-out-money.html"),
    BERICHT("Bierkassenbericht zum ${current-date}", "resources/bericht.html") ;
    public final String subject;
    public final String pathToFile;

    EmailTemplates(String subject, String pathToFile){
        this.subject = subject;
        this.pathToFile = pathToFile;
    }
}
