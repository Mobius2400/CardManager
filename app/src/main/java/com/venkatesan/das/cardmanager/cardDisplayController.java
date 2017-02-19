package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import static com.venkatesan.das.cardmanager.YGOPricesAPI.getCardForDisplay;
import static com.venkatesan.das.cardmanager.YGOPricesAPI.toJSON;

public class cardDisplayController extends Activity {

    YugiohCard thisCard;
    TextView name;
    TextView cardRarity;
    TextView cardTag;
    TextView quantityInStock;
    TextView hi;
    TextView med;
    TextView low;
    TextView shift;
    ImageButton add;
    ImageButton remove;
    cardDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_display);
        Bundle cardInfo = getIntent().getExtras();
        thisCard = new YugiohCard();
        db = new cardDatabase(this);

        //Create card
        thisCard.setName(cardInfo.getString(Contract.nameKey));
        thisCard.setPrint_tag(cardInfo.getString(Contract.tagKey));
        thisCard.setRarity(cardInfo.getString(Contract.rarityKey));
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
        add = (ImageButton)findViewById(R.id.addInventory);
        add.setAlpha((float) 0.0);
        remove = (ImageButton)findViewById(R.id.removeInventory);
        remove.setAlpha((float) 0.0);

        // Render fields
        name.setText(thisCard.getName());
        cardTag.setText(thisCard.getPrint_tag());
        cardRarity.setText(thisCard.getRarity());
        // Set Image
        new asyncImageLoad(thisCard.getName(), thisCard.getCardImage()).execute();
        // Set Inventory
        updateNumberInStock();
        // Set Prices
        new asyncGetPrices().execute();

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            int quantity = 1;
            //Check autoCommit
            //Check bulk add option

            //Add card to inventory.
            if(!thisCard.getName().equals("") && thisCard.getHigh() != 0.0){
                Intent cardDisplay = new Intent(cardDisplayController.this, mainActivityController.class);
                if(db.getIDFromCart(thisCard) == -1){
                    YugiohCard adder = thisCard;
                    adder.setNumInventory(thisCard.getNumInventory()+1);
                    db.addCardToCart(adder);
                    Toast.makeText(getBaseContext(), "Card added to cart.", Toast.LENGTH_SHORT).show();
                    startActivity(cardDisplay);
                }
                else{
                    db.addQuantityFromCart(thisCard, quantity);
                    Toast.makeText(getBaseContext(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
                    startActivity(cardDisplay);
                }
            }
            }
        });
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            //Check autoCommit
            //Check bulk remove option.

            //Remove card from inventory.
            if(!thisCard.getName().equals("") && thisCard.getHigh() != 0.0){
                Intent cardDisplay = new Intent(cardDisplayController.this, mainActivityController.class);
                int ID = db.getIDFromCart(thisCard);
                System.out.println(ID);
                if(ID == -1){
                    Toast.makeText(getBaseContext(), "You don't have this card.", Toast.LENGTH_SHORT).show();
                    startActivity(cardDisplay);
                }
                else if(ID != -1 && db.getQuantityFromCart(ID) == 1){
                    db.deleteFromCart(ID);
                    Toast.makeText(getBaseContext(), "Deleted the card from cart.", Toast.LENGTH_SHORT).show();
                    startActivity(cardDisplay);
                }
                else{
                    db.removeQuantityFromCart(thisCard, 1);
                    Toast.makeText(getBaseContext(), "Removed 1 copy from cart.", Toast.LENGTH_SHORT).show();
                    startActivity(cardDisplay);
                }
            }
            }
        });
    }

    public void updateNumberInStock(){
        int ID = db.getIDFromInventory(thisCard);
        System.out.println(ID);
        int quantity;
        if(ID != -1){
            thisCard.setNumInventory(thisCard.getNumInventory() + db.getQuantityFromInventory(ID));
        }
        quantityInStock.setText(Integer.toString(thisCard.getNumInventory()));
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
            thisCard.setHigh(yugiohCard.getHigh());
            thisCard.setMedian(yugiohCard.getMedian());
            thisCard.setLow(yugiohCard.getLow());
            thisCard.setShift(yugiohCard.getShift());
            add.setAlpha((float) 1.0);
            remove.setAlpha((float) 1.0);
        }
    }
}
