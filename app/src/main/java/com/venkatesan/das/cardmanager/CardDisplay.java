package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.venkatesan.das.cardmanager.R.id.cardNameShow;

public class CardDisplay extends Activity {
    private String card_name;
    private String print_tag;
    private String rarity;
    ImageView card_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_display);
        Bundle cardInfo = getIntent().getExtras();

        card_name = cardInfo.getString("card_name");
        print_tag = cardInfo.getString("print_tag");
        rarity = cardInfo.getString("rarity");
        card_image = (ImageView)findViewById(R.id.cardImage);

        // Define fields
        TextView name = (TextView)findViewById(R.id.cardNameWithCard);
        TextView cardRarity = (TextView)findViewById(R.id.Rarity);
        TextView cardTag = (TextView)findViewById(R.id.printTag);
        TextView quantityInStock = (TextView)findViewById(R.id.qtyInInventory);
        TextView hi = (TextView)findViewById(R.id.High);
        TextView med = (TextView)findViewById(R.id.Median);
        TextView low = (TextView)findViewById(R.id.Low);
        TextView shift = (TextView)findViewById(R.id.Shift);

        // Render fields
        name.setText(card_name);
        cardRarity.setText(rarity);
        cardTag.setText(print_tag);
        new asyncImageLoad(card_name, card_image).execute();
    }
}
