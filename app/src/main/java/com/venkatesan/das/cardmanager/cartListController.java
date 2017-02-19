package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Das on 2/16/2017.
 */

public class cartListController extends Activity {

    Button btnaddToInventory;
    Button btndeleteSelected;
    ArrayList<YugiohCard> cartCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        cardDatabase db = new cardDatabase(this);
        cartCards = db.getAllCartCards();
        db.close();

        btnaddToInventory = (Button)findViewById(R.id.commitToInventory);
        btnaddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToInventory(cartCards);
                Toast.makeText(getBaseContext(), "All cards added to inventory.", Toast.LENGTH_SHORT).show();
            }
        });

        final ListView searchResult = (ListView) findViewById(R.id.searchResults);
        searchResult.setAdapter(new cartAdapter(this, cartCards));

        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = searchResult.getItemAtPosition(position);
                YugiohCard fullObject = (YugiohCard) o;
                Toast.makeText(getBaseContext(), "You have chosen: " + fullObject.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moveToInventory(ArrayList<YugiohCard> cartCards){
        cardDatabase db = new cardDatabase(this);
        for(YugiohCard currCard: cartCards){
            int ID = db.getIDFromInventory(currCard);
            if(ID == -1){
                db.addCardToInventory(currCard);
                db.deleteFromCart(ID);
            }
            else if(ID != -1){
                db.addQuantityFromInventory(currCard, db.getQuantityFromCart(ID));
                db.deleteFromCart(ID);
            }
        }
        db.close();
    }
}