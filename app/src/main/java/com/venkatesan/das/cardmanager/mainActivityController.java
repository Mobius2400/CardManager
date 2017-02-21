package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

public class mainActivityController extends Activity {

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
                Intent searchByText = new Intent(mainActivityController.this, searchTextController.class);
                startActivity(searchByText);
                break;
        }
        switch(buttonID){
            case R.id.viewInventory:
                Intent inventory = new Intent(mainActivityController.this, inventoryDisplayController.class);
                startActivity(inventory);
                break;
        }
        switch(buttonID){
            case R.id.viewCart:
                Intent cart = new Intent(mainActivityController.this, cartListController.class);
                startActivity(cart);
                break;
        }
        switch(buttonID){
            case R.id.Settings:
                Intent settings = new Intent(mainActivityController.this, settingsController.class);
                startActivity(settings);
                break;
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) return true;
        return false;
    }
}