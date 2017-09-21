package com.venkatesan.das.cardmanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DatabaseAllCardsActivity extends AppCompatActivity {

    final String title = "Update Card Database";
    private allCardsDatabase db;
    private ProgressDialog asyncDialog;
    ListView addedCards;
    TextView textCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionBarTheme);
        setContentView(R.layout.activity_allcards_database);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new allCardsDatabase(this);
        asyncDialog = new ProgressDialog(this);
        addedCards = (ListView)findViewById(R.id.addedCards);
        textCards = (TextView)findViewById(R.id.textAddedCards);
        new asyncGetAllCards().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class asyncGetAllCards extends AsyncTask<Void, String, ArrayList<String>> {

        ArrayList<String> existingCards;
        ArrayList<String> allCards;
        ArrayList<String> newCards;

        public asyncGetAllCards(){
            allCards = new ArrayList<>();
            existingCards = new ArrayList<>();
            newCards = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textCards.setText("Checking for new cards...");
            asyncDialog.setMessage("Searching for update...");
            asyncDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
            allCards = getAllMadeYGOCards.setupAllCards();
            existingCards = db.getAllMadeCards();
            if(allCards.size() > existingCards.size()){
                for (int i = 0; i < allCards.size(); i++) {
                    String card = allCards.get(i);
                    publishProgress(Integer.toString(i));
                    if (db.getIDFromName(card) == -1) {
                        db.addCard(card);
                        newCards.add(card);
                    }
                }
                return newCards;
            }
            else{
                return newCards;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int totalCards = allCards.size();
            asyncDialog.setMessage("Checking " + values[0] + " of " + totalCards);
            asyncDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> newCards) {
            super.onPostExecute(newCards);
            if(newCards.size() > 0){
                textCards.setText("Added " + newCards.size() + " new cards.");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getApplicationContext(), android.R.layout.simple_list_item_1, newCards.toArray(new String[newCards.size()]));
                addedCards.setAdapter(adapter);
                asyncDialog.setMessage("Added " + newCards.size() + " cards");
                asyncDialog.dismiss();
            }
            else{
                textCards.setText("No new cards found.");
                asyncDialog.setMessage("Database up to date.");
                asyncDialog.dismiss();
            }
        }
    }
}
