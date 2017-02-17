package com.venkatesan.das.cardmanager;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by Das on 2/15/2017.
 */

public class Contract {

    //URL Attributes
    final static String base = "http://yugiohprices.com/api/";
    final static String baseURL_allVersions = base + "card_versions/";
    final static String baseURL_dataByTagandRarity = base + "price_history/";
    final static String baseURL_imageByName = base + "card_image/";

    //Shared Preferences:
    final static String sharedPreferences = "sharedPreferences";
        //User Settings Attributes
        final static String userName = "user_name";
        //Inventory Settings Attributes
        final static String bulkAdd = "bulk_add";
        final static String bulkRemove = "bulk_remove";
        final static String autoCommit = "auto_commit";

    //Bundle Attributes
    final static String nameKey = "card_name";
    final static String tagKey = "print_tag";
    final static String rarityKey = "rarity";

    //Database Attributes
    final static String databaseName = "CardManagerDatabase";
    final static int databaseVersion = 1;
    //Column names
    final static String ID = "_id";
    final static String cardName = "Name";
    final static String print_tag = "Tag";
    final static String rarity = "Rarity";
    final static String price_high = "High";
    final static String price_median = "Median";
    final static String price_low = "Low";
    final static String price_shift = "Shift";
    final static String numInventory = "Inventory";
    //Table names
    final static String inventoryTable = "Inventory";
    final static String cartTable = "Card";
    //Create Table Query
    final static String inventoryCreateTable = "CREATE TABLE " + Contract.cartTable + "("
            + Contract.ID + " INTEGER PRIMARY KEY," + Contract.cardName + " TEXT,"
            + Contract.print_tag + " TEXT," + Contract.rarity + " TEXT,"
            + Contract.price_high + " REAL," + Contract.price_median + " REAL,"
            + Contract.price_low + " REAL," + Contract.price_shift + " REAL,"
            + Contract.numInventory + " INTEGER" + ")";
    final static String cartCreateTable = "CREATE TABLE " + Contract.inventoryTable + "("
            + Contract.ID + " INTEGER PRIMARY KEY," + Contract.cardName + " TEXT,"
            + Contract.print_tag + " TEXT," + Contract.rarity + " TEXT,"
            + Contract.price_high + " REAL," + Contract.price_median + " REAL,"
            + Contract.price_low + " REAL," + Contract.price_shift + " REAL,"
            + Contract.numInventory + " INTEGER" + ")";

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
