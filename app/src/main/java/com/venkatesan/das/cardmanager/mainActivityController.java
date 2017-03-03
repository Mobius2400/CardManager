package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class mainActivityController extends AppCompatActivity {

    //static{ System.loadLibrary("opencv_java"); }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Setup Menu
    private Menu thisMenu = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        thisMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, thisMenu);
        setOptions();
        return true;
    }

    private void setOptions(){
        MenuItem bulkAdd = thisMenu.findItem(R.id.bulkAdd_icon);
        MenuItem autoCommit = thisMenu.findItem(R.id.autoCommit_icon);
        SharedPreferences pref = getSharedPreferences(Contract.sharedPreferences, MODE_PRIVATE);
        bulkAdd.setVisible(pref.getBoolean(Contract.bulkManage, false));
        autoCommit.setVisible(pref.getBoolean(Contract.autoCommit, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent settings = new Intent(mainActivityController.this, settingsController.class);
                startActivity(settings);
                break;
            default:
                break;
        }
        return true;
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
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) return true;
        return false;
    }
}