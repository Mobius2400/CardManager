package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Das on 2/24/2017.
 */

public class backupDatabase {

    static File sd_card = Environment.getExternalStorageDirectory();

    public static void restoreDB(Context context){
        try{
            if(sd_card.canWrite()){
                File backupDB = context.getDatabasePath(Contract.databaseName);
                String backupDBPath = String.format("%s.bak", Contract.databaseName);
                File currentDB = new File(sd_card, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileInputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "Backup restored from SD card.", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "No backup found.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void backupDB(Context context){
        try{
            File data = Environment.getDataDirectory();
            if(sd_card.canWrite()){
                File currentDB = context.getDatabasePath(Contract.databaseName);
                String backupDBPath = String.format("%s.bak", Contract.databaseName);
                File backupDB = new File(sd_card, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileInputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "Inventory backed to SD card.", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
