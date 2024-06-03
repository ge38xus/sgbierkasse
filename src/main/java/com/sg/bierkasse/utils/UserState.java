package com.sg.bierkasse.utils;

public enum UserState {
    S("S", "Specials"),

    F("F", "Fuchs"),
    CB("CB", "CB"),
    CK("CK", "CK"),
    iaCB("iaCB", "iaCB"),
    AH("AH", "Alte Herr"),
    HB("HB", "Hausbewohner");

    public final String name;
    public final String fullName;
    UserState(String name, String fullName){
        this.name = name;
        this.fullName = fullName;
    }
}
