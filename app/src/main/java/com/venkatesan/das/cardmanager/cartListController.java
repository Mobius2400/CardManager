package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
    ArrayList<YugiohCard> toDelete;
    ListView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        cardDatabase db = new cardDatabase(this);
        cartCards = db.getAllCartCards();
        toDelete = new ArrayList<>();
        db.close();

        btnaddToInventory = (Button)findViewById(R.id.commitToInventory);
        btnaddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToInventory(cartCards);
                Toast.makeText(getBaseContext(), "All cards added to inventory.", Toast.LENGTH_SHORT).show();
                finish();
                Intent toMain = new Intent(cartListController.this, mainActivityController.class);
                startActivity(toMain);
            }
        });

        btndeleteSelected = (Button)findViewById(R.id.discardSelected);
        btndeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelected();
                Toast.makeText(getBaseContext(), "Removed selected.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });

        searchResult = (ListView) findViewById(R.id.searchResults);
        searchResult.setAdapter(new cartAdapter(this, cartCards));
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YugiohCard selectCard = (YugiohCard)parent.getItemAtPosition(position);
                System.out.println(selectCard.getName());
                Toast.makeText(getApplicationContext(), "Clicked on Row: " + selectCard.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moveToInventory(ArrayList<YugiohCard> cartCards){
        cardDatabase db = new cardDatabase(this);
        for(YugiohCard currCard: cartCards){
            int ID = db.getIDFromInventory(currCard);
            int cartID = db.getIDFromCart(currCard);
            if(ID == -1){
                db.addCardToInventory(currCard);
                db.deleteFromCart(cartID);
            }
            else if(ID != -1){
                db.addQuantityFromInventory(currCard, db.getQuantityFromCart(cartID));
                db.deleteFromCart(cartID);
            }
        }
        db.close();
    }

    public void deleteSelected(){
        cardDatabase db = new cardDatabase(this);
        for(YugiohCard card: toDelete){
            db.deleteFromCart(db.getIDFromCart(card));
        }
        db.close();
    }
}