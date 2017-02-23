package com.venkatesan.das.cardmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;



/**
 * Created by Das on 2/22/2017.
 */

public class allCardsDatabase extends SQLiteOpenHelper {

    public allCardsDatabase(Context context){
        // CursorFactory is null.
        super(context, Contract.allCardDBName, null, Contract.databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.allCardsCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Contract.allCardsTable);
        // Create tables again
        onCreate(db);
    }

    void addCard(String toAdd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues attributes = new ContentValues();
        //Get fields.
        attributes.put(Contract.cardName, toAdd);
        //Insert to database.
        db.insert(Contract.allCardsTable, null, attributes);
        db.close();
    }

    public int getIDFromName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        int ID = -1;
        Cursor cursor = db.rawQuery("SELECT " + Contract.ID + " FROM " +
                Contract.allCardsTable + " WHERE " + Contract.cardName + "=?", new String[] {name});
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            ID = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return ID;
    }

    public ArrayList<String> getAllMadeCards() {
        ArrayList<String> YugiohCards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contract.allCardsTable,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String YugiohCard = cursor.getString(1);
            YugiohCards.add(YugiohCard);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        db.close();
        return YugiohCards;
    }
}
