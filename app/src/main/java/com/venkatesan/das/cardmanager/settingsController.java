package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.ArrayList;

public class settingsController extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class asyncGetAllCards extends AsyncTask<Void, String, ArrayList<String>> {
        ProgressDialog asyncDialog = new ProgressDialog(settingsController.this);
        ArrayList<String> allCards;
        allCardsDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog.setMessage("Searching for update...");
            asyncDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            allCards = new ArrayList<>();
            try {
                allCards = getAllMadeYGOCards.setupAllCards();
                for (int i = 0; i < allCards.size(); i++) {
                    String card = allCards.get(i);
                    publishProgress(Integer.toString(i));
                    if (db.getIDFromName(card) == -1) {
                        db.addCard(card);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return allCards;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int totalCards = allCards.size();
            asyncDialog.setMessage("Checking " + values[0] + " of " + totalCards);
            asyncDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> allCards) {
            super.onPostExecute(allCards);
            asyncDialog.setMessage("Added " + allCards.size() + " cards");
            asyncDialog.dismiss();
        }
    }
}
