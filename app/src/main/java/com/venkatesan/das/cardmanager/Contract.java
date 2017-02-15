package com.venkatesan.das.cardmanager;

/**
 * Created by Das on 2/15/2017.
 */

public class Contract {
    final static String base = "http://yugiohprices.com/api/";
    final static String baseURL_allVersions = base + "card_versions/";
    final static String baseURL_dataByTagandRarity = base + "price_history/";
    final static String baseURL_imageByName = base + "card_image/";

    private Contract() {}

    public static String getBase() {
        return base;
    }

    public static String getBaseURL_allVersions() {
        return baseURL_allVersions;
    }

    public static String getBaseURL_dataByTagandRarity() {
        return baseURL_dataByTagandRarity;
    }

    public static String getBaseURL_imageByName() {
        return baseURL_imageByName;
    }
}
