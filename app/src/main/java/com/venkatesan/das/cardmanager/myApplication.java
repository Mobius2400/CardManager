package com.venkatesan.das.cardmanager;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;

import java.util.ArrayList;

/**
 * Created by Das on 2/14/2018.
 */

public class myApplication extends Application{

    private allCardsDatabase db;
    private ProgressDialog asyncDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        //Check for new cards and update if needed.
        db = new allCardsDatabase(this);
        new asyncGetAllCards().execute();
        showLocationAlert();
    }

    private void showLocationAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "check nearby users' data.")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private class asyncGetAllCards extends AsyncTask<Void, Void, Void> {

        String allCards;
        String existingCards;

        public asyncGetAllCards(){}

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ArrayList<String> allCardsList = getAllMadeYGOCards.setupAllCards();
                StringBuilder sb = new StringBuilder();
                for (String s : allCardsList)
                {
                    sb.append(s);
                    sb.append("\t");
                }
                allCards = sb.toString();
                existingCards = db.getAllMadeCards();
                if(allCards.length() > existingCards.length()){
                    db.deleteAllCards();
                    db.addCard(allCards);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
