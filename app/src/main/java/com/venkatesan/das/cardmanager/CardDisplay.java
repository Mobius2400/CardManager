package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import static com.venkatesan.das.cardmanager.YGOPricesAPI.getCardForDisplay;
import static com.venkatesan.das.cardmanager.YGOPricesAPI.toJSON;

public class CardDisplay extends Activity {

    YugiohCard thisCard;
    TextView name;
    TextView cardRarity;
    TextView cardTag;
    TextView quantityInStock;
    TextView hi;
    TextView med;
    TextView low;
    TextView shift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_display);
        Bundle cardInfo = getIntent().getExtras();
        thisCard = new YugiohCard();

        //Create card
        thisCard.setName(cardInfo.getString("card_name"));
        thisCard.setPrint_tag(cardInfo.getString("print_tag"));
        thisCard.setRarity(cardInfo.getString("rarity"));
        thisCard.setCardImage((ImageView)findViewById(R.id.cardImage));

        // Define fields
        name = (TextView)findViewById(R.id.cardNameWithCard);
        cardRarity = (TextView)findViewById(R.id.Rarity);
        cardTag = (TextView)findViewById(R.id.printTag);
        quantityInStock = (TextView)findViewById(R.id.qtyInInventory);
        hi = (TextView)findViewById(R.id.priceHigh);
        med = (TextView)findViewById(R.id.priceMedian);
        low = (TextView)findViewById(R.id.priceLow);
        shift = (TextView)findViewById(R.id.priceShift );

        // Render fields
        name.setText(thisCard.getName());
        cardTag.setText(thisCard.getPrint_tag());
        cardRarity.setText(thisCard.getRarity());
        new asyncImageLoad(thisCard.getName(), thisCard.getCardImage()).execute();
        new asyncGetPrices().execute();
//        thisCard.setHigh(Double.parseDouble(hi.getText().toString()));
//        thisCard.setMedian(Double.parseDouble(med.getText().toString()));
//        thisCard.setLow(Double.parseDouble(low.getText().toString()));
//        thisCard.setShift(Double.parseDouble(shift.getText().toString()));
        System.out.println(thisCard.toString());
    }

    public class asyncGetPrices extends AsyncTask<Void, Void, YugiohCard> {

        @Override
        protected YugiohCard doInBackground(Void... params) {
            YugiohCard temp = null;
            try {
                String priceData = YGOPricesAPI.priceByTag(thisCard.getPrint_tag(), thisCard.getRarity());
                JSONArray priceHistory = toJSON(priceData);
                temp = getCardForDisplay(priceHistory);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        protected void onPostExecute(YugiohCard yugiohCard) {
            super.onPostExecute(yugiohCard);
            hi.setText("$"+Double.toString(yugiohCard.getHigh()));
            med.setText("$"+Double.toString(yugiohCard.getMedian()));
            low.setText("$"+Double.toString(yugiohCard.getLow()));
            shift.setText(Double.toString(Math.round((yugiohCard.getShift()*100)/100))+"%");
        }
    }
}
