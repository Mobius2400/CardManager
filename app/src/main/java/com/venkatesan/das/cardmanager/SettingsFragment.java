package com.venkatesan.das.cardmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragment {
    EditText userName, location;
    Button setDetails, updateDatabase, backupInventory;
    ToggleButton bulkManage, autoCommit;
    final String preferencesKey = Contract.sharedPreferences;
    final String usernameKey = Contract.userName;
    final String locationKey = Contract.location;
    final String bulk_manage = Contract.bulkManage;
    final String auto_commit = Contract.autoCommit;
    allCardsDatabase db;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    class asyncGetAllCards extends AsyncTask<Void, String, ArrayList<String>> {
        ProgressDialog asyncDialog = new ProgressDialog(getActivity());
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
