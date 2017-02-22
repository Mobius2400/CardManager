package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Das on 2/18/2017.
 */

public class inventoryDisplayController extends Activity {

    ArrayList<YugiohCard> inventoryCards;
    ListView searchResult;
    Spinner rarities;
    ArrayList<String> distinctRarities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        cardDatabase db = new cardDatabase(this);
        inventoryCards = db.getAllInventoryCards();

        searchResult = (ListView) findViewById(R.id.searchInventoryResults);
        searchResult.setAdapter(new inventoryAdapter(this, inventoryCards));

        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = searchResult.getItemAtPosition(position);
                YugiohCard fullObject = (YugiohCard) o;
                Toast.makeText(getBaseContext(), "You have chosen: " + fullObject.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
