package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class settingsController extends Activity {
    EditText userName, location;
    Button setDetails;
    ToggleButton bulkManage, autoCommit;
    final String preferencesKey = Contract.sharedPreferences;
    final String usernameKey = Contract.userName;
    final String locationKey = Contract.location;
    final String bulk_manage = Contract.bulkManage;
    final String auto_commit = Contract.autoCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userName = (EditText)findViewById(R.id.enterUsername);
        userName.setText(getPreferences(usernameKey).toString());

        location = (EditText)findViewById(R.id.enterLocation);
        location.setText(getPreferences(locationKey).toString());

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
    }

    public void saveDetails(){
        String username = findViewById(R.id.enterUsername).toString();
        String location = findViewById(R.id.enterLocation).toString();
        SharedPreferences configurations = getSharedPreferences(preferencesKey, MODE_PRIVATE);

        if (!username.equals("") && !location.equals("")){
            SharedPreferences.Editor editor = configurations.edit();
            editor.putString(usernameKey, username);
            editor.putString(locationKey, location);
            editor.commit();
            Toast.makeText(getBaseContext()," Details stored and validated.", Toast.LENGTH_SHORT).show();
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
}
