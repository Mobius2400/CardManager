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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Das on 2/18/2017.
 */

public class inventoryDisplayController extends Activity {

    ArrayList<YugiohCard> inventoryCards;
    int totalCards;
    TextView numCards;
    ListView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        cardDatabase db = new cardDatabase(this);
        inventoryCards = db.getAllInventoryCards();
        totalCards = db.totalCards();

        numCards = (TextView)findViewById(R.id.numCards);
        numCards.setText(Integer.toString(totalCards));

        searchResult = (ListView) findViewById(R.id.searchInventoryResults);
        searchResult.setAdapter(new inventoryAdapter(this, inventoryCards));
    }
}
