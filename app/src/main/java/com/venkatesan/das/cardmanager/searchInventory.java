package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.venkatesan.das.cardmanager.R.id.editText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_text);
        cardName = (EditText)findViewById(R.id.editText);
        displayResults = (ListView)findViewById(R.id.resultSet);
        goSearch = (Button)findViewById(R.id.searchButton);
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
            results.add("123");
            results.add("234");
            results.add(YGOPricesAPI.searchByName(card_name));
            //Search By Card Name
            ArrayList<YugiohCard> cardVersions = new ArrayList<YugiohCard>();
            cardVersions = YGOPricesAPI.getCardForShortListing(YGOPricesAPI.toJSON(YGOPricesAPI.searchByName(card_name)));
            YugiohCard[] iterator = cardVersions.toArray(new YugiohCard[cardVersions.size()]);
            for(int i = 0; i < iterator.length; i++){

                results.add(iterator[i].getPrint_tag());
            }

            //Display Results
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                      (searchInventory.this, android.R.layout.simple_list_item_1, results);
            displayResults.setAdapter(adapter);
            ((EditText) findViewById(R.id.editText)).setText("");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
