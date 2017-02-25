package com.venkatesan.das.cardmanager;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Das on 2/24/2017.
 */

public class backupDatabase {

    public static void backupDB(){
        File sd_path = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        File currentDB = null;
        File backupDB = null;
        if(sd_path.canWrite()){
            String dbPath = Contract.dbPath;
            String backupPath = Contract.databaseName;
            currentDB = new File(data, dbPath);
            backupDB = new File(sd_path, backupPath);
        }
        if(currentDB.exists()){
            try {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void restoreDB(){
        File sd_path = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        File currentDB = null;
        File backupDB = null;
        if(sd_path.canRead()){
            String dbPath = Contract.dbPath;
            String backupPath = Contract.databaseName;
            currentDB = new File(data, dbPath);
            backupDB = new File(sd_path, backupPath);
        }
        if(currentDB.exists()){
            try {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                src.transferFrom(dst, 0, dst.size());
                src.close();
                dst.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
