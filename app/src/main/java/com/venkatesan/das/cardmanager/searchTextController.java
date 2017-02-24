package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Das on 2/8/2017.
 */

public class searchTextController extends Activity {
    AutoCompleteTextView cardName;
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
        setContentView(R.layout.activity_search_text);

        //Create Auto Complete Text
        cardName = (AutoCompleteTextView)findViewById(R.id.cardNameByName);
        cardName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        allCardsDatabase db = new allCardsDatabase(this);
        ArrayList<String> allCards = db.getAllMadeCards();
        allCards.toArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allCards);
        cardName.setAdapter(adapter);

        // Create variable references to each widget.
        nameDisplay = (TextView)findViewById(R.id.cardNameShow);
        resultMessage = (TextView)findViewById(R.id.resultMessage);

        displayResults = (ListView)findViewById(R.id.resultSet);
        displayResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToCardDisplay(position);
            }
        });

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
                card_name = cardName.getText().toString().trim();
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

    public void sendToCardDisplay(int position){
        Intent cardDisplay = new Intent(searchTextController.this, cardDisplayController.class);
        YugiohCard chosenCard = resultCards[position];

        //Pass on data to new Activity
        Bundle sendToCardActivity = new Bundle();
        sendToCardActivity.putString(Contract.nameKey, card_name);
        sendToCardActivity.putString(Contract.tagKey, chosenCard.getPrint_tag());
        sendToCardActivity.putString(Contract.rarityKey, chosenCard.getRarity());
        cardDisplay.putExtras(sendToCardActivity);

        //Finally, start activity
        startActivity(cardDisplay);
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
                toAdd(yugiohCards);
                sendToCardDisplay(0);
            }
            else if(yugiohCards.length > 1){
                setSearchProgress("Multiple Versions Found:");
                toAdd(yugiohCards);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (searchTextController.this, android.R.layout.simple_list_item_1, results);
                displayResults.setAdapter(adapter);
                ((EditText) findViewById(R.id.cardNameByName)).setText("");
            }

        }
    }
}
