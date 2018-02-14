package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class SearchImageResultActivity extends AppCompatActivity {

    final String title = "Choose A Version:";

    private TextView nameDisplay;
    private ListView displayResults;
    private String[] cardsInfo;
    private String cardName;

    public SearchImageResultActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionBarTheme);
        setContentView(R.layout.fragment_search_image);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Receive bundle
        Bundle cards = getIntent().getExtras();
        if(cards != null){
            cardsInfo = cards.getStringArray(Contract.cardInfoCode);
            cardName = cards.getString(Contract.cardCode);
        }
        // Inflate the layout
        ((TextView)findViewById(R.id.OCRResultMessage)).setText("Multiple Versions Found, Choose One:");
        // Set Card Name in Display.
        nameDisplay = (TextView)findViewById(R.id.OCRCardNameShow);
        nameDisplay.setText(cardName);
        //Set Clickable adapter with OCR results.
        displayResults = (ListView)findViewById(R.id.OCRResultSet);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (SearchImageResultActivity.this, android.R.layout.simple_list_item_1, cardsInfo);
        displayResults.setAdapter(adapter);
        displayResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get card info to send to CardDisplay
                String info = cardsInfo[position];
                String[] splitInfo = info.split(" ");
                Intent toCardDisplay = new Intent(SearchImageResultActivity.this, CardDisplayActivity.class);
                Bundle values = new Bundle();
                values.putString(Contract.nameKey, cardName);
                values.putString(Contract.tagKey, splitInfo[0].trim());
                values.putString(Contract.rarityKey, splitInfo[2].trim());
                toCardDisplay.putExtras(values);
                //Move to cardDisplay
                startActivity(toCardDisplay);
            }
        });
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
}
