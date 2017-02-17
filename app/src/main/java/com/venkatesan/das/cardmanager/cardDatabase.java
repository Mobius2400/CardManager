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

public class cardDatabase extends SQLiteOpenHelper {

    public cardDatabase(Context context){
        // CursorFactory is null.
        super(context, Contract.databaseName, null, Contract.databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.inventoryCreateTable);
        db.execSQL(Contract.cartCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Contract.cartTable);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.inventoryTable);
        // Create tables again
        onCreate(db);
    }

    void addCardToCart(YugiohCard toAdd){
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
        db.insert(Contract.cartTable, null, attributes);
        db.close();
    }

    void deleteFromCart(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM " + Contract.cartTable + " WHERE " + Contract.ID + " =?",
                new String[] {Integer.toString(ID)});
    }

    public YugiohCard getCardFromCart(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        YugiohCard sendCard = null;
        Cursor cursor = db.query(Contract.cartTable, new String[] { Contract.ID,
                        Contract.cardName, Contract.print_tag, Contract.rarity,
                        Contract.price_high, Contract.price_median, Contract.price_low,
                        Contract.price_shift, Contract.numInventory}, Contract.ID + "=?",
                new String[] { String.valueOf(ID) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            sendCard = cursorToCard(cursor);
        }
        return sendCard;
    }

    public int getIDFromCart(YugiohCard thisCard){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + Contract.ID + " FROM " +
                Contract.cartTable + " WHERE " + Contract.cardName + "=? AND " + Contract.print_tag
                + "=? AND " + Contract.rarity + "=?", new String[] {thisCard.getName(), thisCard.getPrint_tag(),
                thisCard.getRarity()});
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public int getQuantityFromCart(int ID){
        return this.getReadableDatabase().rawQuery("SELECT " + Contract.numInventory + " FROM " +
                Contract.cartTable + " WHERE " + Contract.ID + "=?", new String[] {Integer.toString(ID)}).getInt(0);
    }

    public void setQuantityToCart(int ID, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Contract.cartTable + " SET " + Contract.numInventory + " = " + quantity
                + " WHERE " + Contract.ID + " = " + ID);
    }

    public void addQuantityFromCart(YugiohCard thisCard, int quantity){
        int currQuantity = getQuantityFromCart(getIDFromCart(thisCard)) + quantity;
        setQuantityToCart(getIDFromCart(thisCard), currQuantity);
    }

    public void removeQuantityFromCart(YugiohCard thisCard, int quantity){
        int currQuantity = getQuantityFromCart(getIDFromCart(thisCard));
        if(currQuantity >= quantity){
            currQuantity -= quantity;
        }
        else{
            currQuantity = 0;
        }
        setQuantityToCart(getIDFromCart(thisCard), currQuantity);
    }

    ///////////////////////////////////////////////// Inventory Table //////////////////////////////////////////////

    public void addCardToInventory(YugiohCard toAdd){
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

    void deleteFromInventory(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM " + Contract.inventoryTable + " WHERE " + Contract.ID + " =?",
                new String[] {Integer.toString(ID)});
    }

    public YugiohCard getCardFromInventory(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        YugiohCard sendCard = null;
        Cursor cursor = db.query(Contract.inventoryTable, new String[] { Contract.ID,
                        Contract.cardName, Contract.print_tag, Contract.rarity,
                        Contract.price_high, Contract.price_median, Contract.price_low,
                        Contract.price_shift, Contract.numInventory}, Contract.ID + "=?",
                new String[] { String.valueOf(ID) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            sendCard = cursorToCard(cursor);
        }
        return sendCard;
    }

    private YugiohCard cursorToCard(Cursor cursor){
        YugiohCard result = new YugiohCard();
        result.setName(cursor.getString(1));
        result.setPrint_tag(cursor.getString(2));
        result.setRarity(cursor.getString(3));
        result.setHigh(cursor.getDouble(4));
        result.setMedian(cursor.getDouble(5));
        result.setLow(cursor.getDouble(6));
        result.setShift(cursor.getDouble(7));
        result.setNumInventory(cursor.getInt(8));
        return result;
    }

    public int getIDFromInventory(YugiohCard thisCard){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + Contract.ID + " FROM " +
                Contract.inventoryTable + " WHERE " + Contract.cardName + "=? AND " + Contract.print_tag
                + "=? AND " + Contract.rarity + "=?", new String[] {thisCard.getName(), thisCard.getPrint_tag(),
                thisCard.getRarity()});
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public int getQuantityFromInventory(int ID){
        return this.getReadableDatabase().rawQuery("SELECT " + Contract.numInventory + " FROM " +
                Contract.inventoryTable + " WHERE " + Contract.ID + "=?", new String[] {Integer.toString(ID)}).getInt(0);
    }

    public void setQuantityFromInventory(int ID, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Contract.inventoryTable + " SET " + Contract.numInventory + " = " + quantity
                + " WHERE " + Contract.ID + " = " + ID);
    }

    public void addQuantityFromInventory(YugiohCard thisCard, int quantity){
        int currQuantity = this.getQuantityFromInventory(getIDFromInventory(thisCard)) + quantity;
        setQuantityFromInventory(getIDFromInventory(thisCard), currQuantity);
    }

    public void removeQuantityFromInventory(YugiohCard thisCard, int quantity){
        int currQuantity = getQuantityFromInventory(getIDFromInventory(thisCard));
        if(currQuantity >= quantity){
            currQuantity -= quantity;
        }
        else{
            currQuantity = 0;
        }
        setQuantityFromInventory(getIDFromInventory(thisCard), currQuantity);
    }
}
