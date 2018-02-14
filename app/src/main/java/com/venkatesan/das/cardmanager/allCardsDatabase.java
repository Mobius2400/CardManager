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
        super(context, Contract.allCardDBName, null, Contract.allCardsDatabaseVersion);
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

    public void deleteAllCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.allCardsTable, null, null);
        db.close();
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

    public String getAllMadeCards() {
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
        StringBuilder sb = new StringBuilder();
        for (String s : YugiohCards)
        {
            sb.append(s);
            sb.append("\t");
        }
        return sb.toString();
    }
}
