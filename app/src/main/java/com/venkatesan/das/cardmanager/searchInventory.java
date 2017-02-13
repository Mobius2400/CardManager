package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.venkatesan.das.cardmanager.R.id.cardNameByName;
import static com.venkatesan.das.cardmanager.R.id.searchText;

/**
 * Created by Das on 2/8/2017.
 */

public class searchInventory extends Activity {
    EditText cardName;
    ArrayList<String> results = new ArrayList<String>();
    ListView displayResults;
    Button goSearch;
    String card_name = "";
    AsyncQuery searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_text);
        cardName = (EditText)findViewById(R.id.cardNameByName);
        displayResults = (ListView)findViewById(R.id.resultSet);
        goSearch = (Button)findViewById(R.id.searchButton);
        searcher = new AsyncQuery();
        cardName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cardName.setText("");
            }
        });
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_name = cardName.getText().toString();
                if(card_name == null || card_name.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Input is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        onSearch(v);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onSearch(View goSearch) throws MalformedURLException{
        results.clear();
        try{
            // Test ListView
            results.add("123");
            results.add("234");

            //Search By Card Name
            searcher.execute(card_name);

            //Display Results
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                      (searchInventory.this, android.R.layout.simple_list_item_1, results);
            displayResults.setAdapter(adapter);
            ((EditText) findViewById(R.id.cardNameByName)).setText("");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void toAdd(YugiohCard[] toadd){
        for(YugiohCard card: toadd){
            String print_tag = card.getPrint_tag();
            String rarity = card.getRarity();
            results.add(print_tag + " - " + rarity);
        }
    }

    public class AsyncQuery extends AsyncTask<String, Void, YugiohCard[]> {
        @Override

        protected YugiohCard[] doInBackground(String... input)
        {
            YugiohCard[] iterator;
            card_name = input[0];
            String output = "";
            try {
                output = YGOPricesAPI.searchByName(card_name);
                if (output.length() > 0){
                    ArrayList<YugiohCard> cardVersions = new ArrayList<YugiohCard>();
                    cardVersions = YGOPricesAPI.getCardForShortListing(YGOPricesAPI.toJSON(output));
                    iterator = cardVersions.toArray(new YugiohCard[cardVersions.size()]);
                    if (iterator.length >= 1){
                        return iterator;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(YugiohCard[] yugiohCards) {
            super.onPostExecute(yugiohCards);
            toAdd(yugiohCards);
        }
    }
}
