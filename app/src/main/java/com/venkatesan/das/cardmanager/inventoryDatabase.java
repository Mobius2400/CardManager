package com.venkatesan.das.cardmanager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


/**
 * Created by Das on 2/15/2017.
 */

public class inventoryDatabase extends SQLiteOpenHelper {

    public inventoryDatabase(Context context){
        // CursorFactory is null.
        super(context, Contract.databaseName, null, Contract.databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Contract.inventoryTable + "("
                + Contract.ID + " INTEGER PRIMARY KEY," + Contract.cardName + " TEXT,"
                + Contract.print_tag + " TEXT," + Contract.rarity + " TEXT,"
                + Contract.price_high + " REAL," + Contract.price_median + " REAL,"
                + Contract.price_low + " REAL," + Contract.price_shift + " REAL,"
                + Contract.numInventory + " INTEGER" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Contract.inventoryTable);

        // Create tables again
        onCreate(db);
    }

    void addCard(YugiohCard toAdd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues attributes = new ContentValues();

        //Get fields.
        attributes.put(Contract.cardName, toAdd.getName());
        attributes.put(Contract.print_tag, toAdd.getPrint_tag());
        attributes.put(Contract.rarity, toAdd.getRarity());
        attributes.put(Contract.price_high, toAdd.getHigh());
        attributes.put(Contract.price_median, toAdd.getMedian());
        attributes.put(Contract.price_low, toAdd.getLow());
        attributes.put(Contract.price_shift, toAdd.getShift());
        attributes.put(Contract.numInventory, toAdd.getNumInventory());
        //Insert to database.
        db.insert(Contract.inventoryTable, null, attributes);
        db.close();
    }

//    YugiohCard getCard(int ID){
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(Contract.inventoryTable, new String[] { Contract.ID,
//                        Contract.cardName, Contract.print_tag, Contract.rarity,
//                        Contract.price_high, Contract.price_median, Contract.price_low,
//                        Contract.price_shift, Contract.numInventory}, Contract.ID + "=?",
//                new String[] { String.valueOf(ID) }, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return null;
//    }
}
