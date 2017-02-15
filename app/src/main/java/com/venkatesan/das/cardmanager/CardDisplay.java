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

    public static YugiohCard thisCard;

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
        TextView name = (TextView)findViewById(R.id.cardNameWithCard);
        TextView cardRarity = (TextView)findViewById(R.id.Rarity);
        TextView cardTag = (TextView)findViewById(R.id.printTag);
        TextView quantityInStock = (TextView)findViewById(R.id.qtyInInventory);
        TextView hi = (TextView)findViewById(R.id.priceHigh);
        TextView med = (TextView)findViewById(R.id.priceMedian);
        TextView low = (TextView)findViewById(R.id.priceLow);
        TextView shift = (TextView)findViewById(R.id.priceShift );

        // Render fields
        name.setText(thisCard.getName());
        cardTag.setText(thisCard.getPrint_tag());
        cardRarity.setText(thisCard.getRarity());
        new asyncImageLoad(thisCard.getName(), thisCard.getCardImage()).execute();
        new asyncGetPrices().execute();
        hi.setText("$"+Double.toString(thisCard.getHigh()));
        med.setText("$"+Double.toString(thisCard.getMedian()));
        low.setText("$"+Double.toString(thisCard.getLow()));
        shift.setText(Double.toString(thisCard.getShift())+"%");
    }

    public void setPrices(YugiohCard yugiohCard){
        thisCard.setLow(yugiohCard.getLow());
        thisCard.setMedian(yugiohCard.getMedian());
        thisCard.setHigh(yugiohCard.getHigh());
        thisCard.setShift(yugiohCard.getShift());
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
            setPrices(yugiohCard);
        }
    }
}
