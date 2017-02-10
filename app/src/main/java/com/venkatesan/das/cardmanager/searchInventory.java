package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.venkatesan.das.cardmanager.R.id.editText;
import static com.venkatesan.das.cardmanager.R.id.searchText;

/**
 * Created by Das on 2/8/2017.
 */

public class searchInventory extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_text);
    }

    EditText cardName;
    ArrayList<String> results = new ArrayList<String>();
    ListView displayResults;
    Button goSearch;
    public void onSearch(View goSearch){
      cardName = (EditText)findViewById(R.id.editText);
      String card_name = cardName.getText().toString();
      results.clear();
      if(card_name == null || card_name.trim().equals("")){
          Toast.makeText(getBaseContext(), "Input is empty", Toast.LENGTH_SHORT).show();
      }
      else {
          // method to get results.
          ArrayAdapter<String> adapter = new ArrayAdapter<String>
                  (this, android.R.layout.simple_list_item_1, results);
          displayResults.setAdapter(adapter);
          ((EditText) findViewById(R.id.editText)).setText("");
      }
    }
}
