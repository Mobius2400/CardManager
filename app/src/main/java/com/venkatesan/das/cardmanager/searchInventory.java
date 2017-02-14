package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class searchInventory extends Activity implements AdapterView.OnItemClickListener{
    EditText cardName;
    TextView nameDisplay;
    TextView resultMessage;
    ArrayList<String> results = new ArrayList<String>();
    YugiohCard[] resultCards;
    ListView displayResults;
    Button goSearch;
    String card_name = "";
    AsyncQuery searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_text);

        // Create variable references to each widget.
        cardName = (EditText)findViewById(R.id.cardNameByName);
        nameDisplay = (TextView)findViewById(R.id.cardNameShow);
        resultMessage = (TextView)findViewById(R.id.resultMessage);
        displayResults = (ListView)findViewById(R.id.resultSet);
        searcher = new AsyncQuery();
        cardName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cardName.setText("");
            }
        });

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
                        nameDisplay.setText(cardName.getText());
                        setSearchProgress("Searching...");
                        onSearch(v);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setSearchProgress(String message){
        resultMessage.setText(message);
    }

    public void onSearch(View goSearch) throws MalformedURLException{
        results.clear();
        try{
            searcher.execute(card_name);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(results.size() > 0 && results.size() != 1){
            Intent cardDisplay = new Intent(searchInventory.this, CardDisplay.class);
            YugiohCard chosenCard = resultCards[position];

            //Pass on data to new Activity
            Bundle sendToCardActivity = new Bundle();
            sendToCardActivity.putString("card_name", card_name);
            sendToCardActivity.putString("print_tag", chosenCard.getPrint_tag());
            sendToCardActivity.putString("rarity", chosenCard.getRarity());
            cardDisplay.putExtras(sendToCardActivity);

            //Finally, start activity
            startActivity(cardDisplay);
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
            resultCards = yugiohCards;
            if(yugiohCards.length == 0){
                setSearchProgress("No Results Found");
            }
            else if(yugiohCards.length == 1){
                // Redirect to nextActivity with yugiohCards[0];
            }
            else if(yugiohCards.length > 1){
                setSearchProgress("Multiple Versions Found:");
                toAdd(yugiohCards);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (searchInventory.this, android.R.layout.simple_list_item_1, results);
                displayResults.setAdapter(adapter);
                ((EditText) findViewById(R.id.cardNameByName)).setText("");

            }

        }
    }
}
