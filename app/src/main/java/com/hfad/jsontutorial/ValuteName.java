package com.hfad.jsontutorial;

public enum ValuteName {
    AUD("AUD"),
    AZN("AZN"),
    GBP("GBP"),
    AMD("AMD"),
    BYN("BYN"),
    BGN("BGN"),
    BRL("BRL"),
    HUF("HUF"),
    HKD("HKD"),
    DKK("DKK"),
    USD("USD"),
    EUR("EUR"),
    INR("INR"),
    KZR("KZT"),
    CAD("CAD"),
    KGS("KGS"),
    CNY("CNY"),
    MDL("MDL"),
    NOK("NOK"),
    PLN("PLN"),
    RON("RON"),
    XDR("XDR"),
    SGD("SGD"),
    TJS("TJS"),
    TRY("TRY"),
    TMT("TMT"),
    UZS("UZS"),
    UAH("UAH"),
    CZK("CZK"),
    SEK("SEK"),
    CHF("CHF"),
    ZAR("ZAR"),
    KRW("KRW"),
    JPY("JPY");

    private final String valuteName;

    ValuteName(String valuteName) {
        this.valuteName = valuteName;
    }

    public String getValuteName() {
        return valuteName;
    }
}
