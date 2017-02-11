package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.URL;

public class MainActivity extends Activity {

    //static{ System.loadLibrary("opencv_java"); }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View in_view){
        int buttonID = in_view.getId();
        switch(buttonID){
            case R.id.searchText:
                Intent searchByText = new Intent(MainActivity.this, searchInventory.class);
                startActivity(searchByText);
        }
        switch(buttonID){
            case R.id.exitButton:
                finish();
        }
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            return true;
        }
        return false;
    }
}