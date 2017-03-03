package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.venkatesan.das.cardmanager.YGOPricesAPI.getCardForDisplay;
import static com.venkatesan.das.cardmanager.YGOPricesAPI.toJSON;

public class settingsController extends PreferenceActivity {
    EditText userName, location;
    Button setDetails, updateDatabase, backupInventory;
    ToggleButton bulkManage, autoCommit;
    final String preferencesKey = Contract.sharedPreferences;
    final String usernameKey = Contract.userName;
    final String locationKey = Contract.location;
    final String bulk_manage = Contract.bulkManage;
    final String auto_commit = Contract.autoCommit;
    allCardsDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        class asyncGetAllCards extends AsyncTask<Void, String, ArrayList<String>> {
            ProgressDialog asyncDialog = new ProgressDialog(settingsController.this);
            ArrayList<String> allCards;

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
}
