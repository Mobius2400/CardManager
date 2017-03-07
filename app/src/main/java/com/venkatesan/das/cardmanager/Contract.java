package com.venkatesan.das.cardmanager;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by Das on 2/15/2017.
 */

public class Contract {

    private Contract() {}

    //URL Attributes
        // List of All YGO Cards
        final static String allCardsURL = "https://www.yugiohcardguide.com/card_list.html";
        // YGOPrices URLs
        final static String base = "http://yugiohprices.com/api/";
        final static String baseURL_allVersions = base + "card_versions/";
        final static String baseURL_dataByTagandRarity = base + "price_history/";
        final static String baseURL_imageByName = base + "card_image/";

    //Shared Preferences:
    final static String sharedPreferences = "com.venkatesan.das.cardmanager_preferences";
        //Database Attributes
        final static String firstUpdate = "first_update";
        //User SettingsActivity Attributes
        final static String userName = "user_name";
        final static String location = "location";
        //Inventory SettingsActivity Attributes
        final static String bulkManage = "bulk_manage";
        final static String autoCommit = "auto_commit";

    //Bundle Attributes
    final static String nameKey = "card_name";
    final static String tagKey = "print_tag";
    final static String rarityKey = "rarity";

    //Database Attributes
    final static String databaseName = "CardManagerDatabase";
    final static String allCardDBName = "AllCardsDatabase";
    final static int databaseVersion = 1;
    final static String dbPath = "//data//com.venkatesan.das.cardmanager//databases//" + Contract.databaseName;
    final static String backupButton = "Backup Database";
    final static String restoreButton = "Restore Backup";
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
    final static String allCardsTable = "AllCards";
    final static String inventoryTable = "Inventory";
    final static String cartTable = "Card";
    //Create Table Query
    final static String allCardsCreateTable = "CREATE TABLE " + Contract.allCardsTable + "("
            + Contract.ID + " INTEGER PRIMARY KEY, " + Contract.cardName + " TEXT" + ")";
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
}
