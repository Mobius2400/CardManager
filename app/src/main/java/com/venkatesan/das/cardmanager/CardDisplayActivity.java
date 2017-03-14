package com.venkatesan.das.cardmanager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class CardDisplayActivity extends AppCompatActivity {

    final String title = "View Card";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionBarTheme);
        setContentView(R.layout.activity_card_display);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Extras
        Bundle extras = getIntent().getExtras();
        String card_name = extras.getString(Contract.nameKey);
        String print_tag = extras.getString(Contract.tagKey);
        String rarity = extras.getString(Contract.rarityKey);
        renderFragment(card_name, print_tag, rarity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void renderFragment(String name, String print_tag, String rarity){
        CardDisplayFragment cardDisplayFragment = new CardDisplayFragment();

        Bundle sendToCardActivity = new Bundle();
        sendToCardActivity.putString(Contract.nameKey, name);
        sendToCardActivity.putString(Contract.tagKey, print_tag);
        sendToCardActivity.putString(Contract.rarityKey, rarity);
        cardDisplayFragment.setArguments(sendToCardActivity);

        //Inflate the fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_card_display, cardDisplayFragment);
        fragmentTransaction.commit();
    }
}