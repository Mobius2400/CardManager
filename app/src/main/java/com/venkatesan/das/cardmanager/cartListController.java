package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Das on 2/16/2017.
 */

public class cartListController extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        ArrayList<YugiohCard> sample = GetSampleCart();

        final ListView searchResults = (ListView)findViewById(R.id.searchResults);
        searchResults.setAdapter(new cartAdapter(this, sample));

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = searchResults.getItemAtPosition(position);
                YugiohCard fullObject = (YugiohCard)o;
                Toast.makeText(getBaseContext(), "You have chosen: " + fullObject.getName(), Toast.LENGTH_LONG).show();
                if(view != null){
                    CheckBox checkBox = (CheckBox)findViewById(R.id.selectCard);
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        });
    }

    private void Delete(){
        // Delete selected and update cart.
    }

    // Tester Class
    private ArrayList<YugiohCard> GetSampleCart(){
        ArrayList<YugiohCard> results = new ArrayList<YugiohCard>();

        YugiohCard sr1 = new YugiohCard();
        sr1.setName("John Smith");
        sr1.setPrint_tag("Dallas, TX");
        sr1.setRarity("214-555-1234");
        results.add(sr1);

        sr1 = new YugiohCard();
        sr1.setName("Jane Doe");
        sr1.setPrint_tag("Atlanta, GA");
        sr1.setRarity("469-555-2587");
        results.add(sr1);

        sr1 = new YugiohCard();
        sr1.setName("Steve Young");
        sr1.setPrint_tag("Miami, FL");
        sr1.setRarity("305-555-7895");
        results.add(sr1);

        sr1 = new YugiohCard();
        sr1.setName("Fred Jones");
        sr1.setPrint_tag("Las Vegas, NV");
        sr1.setRarity("612-555-8214");
        results.add(sr1);

        return results;
    }
}
