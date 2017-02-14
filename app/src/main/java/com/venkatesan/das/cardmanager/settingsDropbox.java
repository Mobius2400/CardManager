package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class settingsDropbox extends Activity {
    EditText userName, password;
    final String preferencesKey = "userPreferences";
    final String usernameKey = "ownerName";
    final String passwordKey = "dropboxPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        userName = (EditText)findViewById(R.id.enterUsername);
        userName.setText(getPreferences(usernameKey));

        password = (EditText)findViewById(R.id.enterPassword);
        password.setText(getPreferences(passwordKey));

        userName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                userName.setText("");
            }
        });

        password.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                password.setText("");
            }
        });
    }

    public void saveDropboxCredentials(View v){
        String username = findViewById(R.id.enterUsername).toString();
        String password = findViewById(R.id.enterPassword).toString();
        SharedPreferences configurations = getSharedPreferences(preferencesKey, MODE_PRIVATE);

        //Validate credentials are real.
        if (true){
            SharedPreferences.Editor editor = configurations.edit();
            editor.putString(usernameKey, username);
            editor.putString(passwordKey, password);
            editor.commit();
            Toast.makeText(getBaseContext()," Settings stored and validated.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(), "Error, Please Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPreferences(String key){
        SharedPreferences getter = getSharedPreferences(preferencesKey, MODE_PRIVATE);
        return getter.getString(key, "<none set>");
    }
}
