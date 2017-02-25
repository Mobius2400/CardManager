package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class settingsController extends Activity {
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
        setContentView(R.layout.activity_settings);
        db = new allCardsDatabase(this);

        userName = (EditText)findViewById(R.id.enterUsername);
        userName.setText(getPreferences(usernameKey));

        location = (EditText)findViewById(R.id.enterLocation);
        location.setText(getPreferences(locationKey));

        userName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                userName.setText("");
            }
        });

        location.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                location.setText("");
            }
        });

        setDetails = (Button)findViewById(R.id.setDetails);
        setDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });

        bulkManage = (ToggleButton)findViewById(R.id.bulkButton);
        bulkManage.setChecked(getChecked(bulk_manage));
        bulkManage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getSharedPreferences(preferencesKey, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(bulkManage.isChecked()){
                    editor.putBoolean(bulk_manage, true);
                }
                if(!bulkManage.isChecked()){
                    editor.putBoolean(bulk_manage, false);
                }
                editor.commit();
            }
        });

        autoCommit = (ToggleButton)findViewById(R.id.autoCommitButton);
        autoCommit.setChecked(getChecked(auto_commit));
        autoCommit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getSharedPreferences(preferencesKey, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(autoCommit.isChecked()){
                    editor.putBoolean(auto_commit, true);
                }
                if(!autoCommit.isChecked()){
                    editor.putBoolean(auto_commit, false);
                }
                editor.commit();
            }
        });

        updateDatabase = (Button)findViewById(R.id.updateDatabase);
        updateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new asyncGetAllCards().execute();
            }
        });

        backupInventory = (Button)findViewById(R.id.backupInventory);
        final cardDatabase db = new cardDatabase(this);
        if(db.totalCards() > 0){
            backupInventory.setText(Contract.backupButton);
        }
        else{
            backupInventory.setText(Contract.restoreButton);
        }
        backupInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.totalCards() > 0){
                    backupDatabase.backupDB();
                    Toast.makeText(getBaseContext(),"Inventory backed up.", Toast.LENGTH_SHORT).show();
                }
                else{
                    backupDatabase.restoreDB();
                    Toast.makeText(getBaseContext(),"Inventory imported.", Toast.LENGTH_SHORT).show();
                    backupInventory.setText(Contract.backupButton);
                }
            }
        });
    }

    public void saveDetails(){
        String username = userName.getText().toString();
        String location2 = location.getText().toString();
        SharedPreferences configurations = getSharedPreferences(preferencesKey, MODE_PRIVATE);

        if (!username.equals("") && !location.equals("")){
            SharedPreferences.Editor editor = configurations.edit();
            editor.putString(usernameKey, username);
            editor.putString(locationKey, location2);
            editor.commit();
            Toast.makeText(getBaseContext(), "Details stored and validated.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(), "No blank values, Please Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPreferences(String key){
        SharedPreferences getter = getSharedPreferences(preferencesKey, MODE_PRIVATE);
        return getter.getString(key, "<none set>");
    }

    private Boolean getChecked(String key){
        SharedPreferences getter = getSharedPreferences(preferencesKey, MODE_PRIVATE);
        return getter.getBoolean(key, false);
    }

    public class asyncGetAllCards extends AsyncTask<Void, String, ArrayList<String>> {
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
                for(int i = 0; i < allCards.size(); i++){
                    String card = allCards.get(i);
                    publishProgress(Integer.toString(i));
                    if(db.getIDFromName(card) == -1){
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
