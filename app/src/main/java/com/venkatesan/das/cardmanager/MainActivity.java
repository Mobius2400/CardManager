package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

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
                break;
        }
        switch(buttonID){
            case R.id.configSettings:
                Intent settings = new Intent(MainActivity.this, Settings.class);
                startActivity(settings);
                break;
        }
        switch(buttonID){
            case R.id.exitButton:
                finish();
        }
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) return true;
        return false;
    }
}