package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Das on 9/19/2017.
 */

public class zipCodeDatabase extends SQLiteOpenHelper{
    Context myContext;

    public zipCodeDatabase(Context context){
        // CursorFactory is null.
        super(context, Contract.zipCodeDBName, null, Contract.zipDatabaseVersion);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(Contract.zipCodeCreateTable);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Contract.zipCodeTable);
        // Create tables again
        onCreate(db);
    }

    void addCard(String toAdd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues attributes = new ContentValues();
        InputStream csvInput = myContext.getResources().openRawResource(R.raw.zip_code_database);
        CSVFile zipFile = new CSVFile(csvInput);
        ArrayList zipList = zipFile.read();
        for(Iterator<String[]> i = zipList.iterator(); i.hasNext();){
            //Get fields.
            String[] row = i.next();
            attributes.put(Contract.zipCode, row[0]);
            attributes.put(Contract.primaryCity, row[3]);
            attributes.put(Contract.state, row[6]);
            //Insert to database.
            db.insert(Contract.zipCodeTable, null, attributes);
        }
        db.close();
    }

    private class CSVFile {
        InputStream inputStream;

        public CSVFile(InputStream inputStream){
            this.inputStream = inputStream;
        }

        public ArrayList read(){
            ArrayList resultList = new ArrayList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(",");
                    resultList.add(row);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: "+ex);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    throw new RuntimeException("Error while closing input stream: "+e);
                }
            }
            return resultList;
        }
    }
}
