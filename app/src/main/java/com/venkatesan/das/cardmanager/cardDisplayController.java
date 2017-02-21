package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    final String preferencesKey = Contract.sharedPreferences;
    SharedPreferences pref;
    final String bulk_manage = Contract.bulkManage;
    final String auto_commit = Contract.autoCommit;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_display);
        Bundle cardInfo = getIntent().getExtras();
        thisCard = new YugiohCard();
        db = new cardDatabase(this);
        pref = getSharedPreferences(preferencesKey, MODE_PRIVATE);

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
        // Set Button Listeners
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                quantity = 1;
                //Check bulk add option
                if(!thisCard.getName().equals("") && thisCard.getHigh() != 0.0 && !thisCard.getRarity().equals("")){
                    if(pref.getBoolean(bulk_manage, false)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Enter Quantity:");
                        //Setup Quantity Input
                        final EditText input = new EditText(v.getContext());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);
                        //Setup Buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quantity = Integer.parseInt(input.getText().toString());
                                Intent cardDisplay = new Intent(cardDisplayController.this, mainActivityController.class);
                                Boolean autoCommit = pref.getBoolean(auto_commit, false);
                                //Check autoCommit
                                if(autoCommit){
                                    if(db.getIDFromInventory(thisCard) == -1){
                                        YugiohCard adder = thisCard;
                                        adder.setNumInventory(quantity);
                                        db.addCardToInventory(adder);
                                        Toast.makeText(getBaseContext(), "Card added to inventory.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(cardDisplay);
                                    }
                                    else{
                                        db.addQuantityFromInventory(thisCard, quantity);
                                        Toast.makeText(getBaseContext(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(cardDisplay);
                                    }
                                }
                                else{
                                    if(db.getIDFromCart(thisCard) == -1){
                                        YugiohCard adder = thisCard;
                                        adder.setNumInventory(quantity);
                                        db.addCardToCart(adder);
                                        Toast.makeText(getBaseContext(), "Card added to cart.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(cardDisplay);
                                    }
                                    else{
                                        db.addQuantityFromCart(thisCard, quantity);
                                        Toast.makeText(getBaseContext(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(cardDisplay);
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    //Add card to inventory without bulk options.
                    else if(!pref.getBoolean(bulk_manage, false)){
                        Intent cardDisplay = new Intent(cardDisplayController.this, mainActivityController.class);
                        if(db.getIDFromCart(thisCard) == -1){
                            YugiohCard adder = thisCard;
                            adder.setNumInventory(quantity);
                            db.addCardToCart(adder);
                            Toast.makeText(getBaseContext(), "Card added to cart.", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(cardDisplay);
                        }
                        else{
                            db.addQuantityFromCart(thisCard, quantity);
                            Toast.makeText(getBaseContext(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(cardDisplay);
                        }
                    }
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = 1;
                //Check bulk remove option.
                if (!thisCard.getName().equals("") && thisCard.getHigh() != 0.0 && !thisCard.getRarity().equals("")) {
                    if (pref.getBoolean(bulk_manage, false)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Enter Quantity:");
                        //Setup Quantity Input
                        final EditText input = new EditText(v.getContext());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);
                        //Setup Buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quantity = Integer.parseInt(input.getText().toString());
                                Boolean autoCommit = pref.getBoolean(auto_commit, false);
                                //Check autoCommit
                                if(autoCommit){
                                    autoCommitRemoveCard();
                                }
                                else{
                                    removeCard();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    else if (!pref.getBoolean(bulk_manage, false)) {
                        removeCard();
                    }
                }
            }
        });
    }

    private void autoCommitRemoveCard(){
        Intent mainScreen = new Intent(cardDisplayController.this, mainActivityController.class);
        int inventoryID = db.getIDFromInventory(thisCard);
        if(inventoryID == -1){
            Toast.makeText(getBaseContext(), "You don't have this card.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        else if(quantity > db.getQuantityFromInventory(inventoryID)){
            Toast.makeText(getBaseContext(), "You don't have the required quantity.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        else if(quantity == db.getQuantityFromInventory(inventoryID)){
            db.deleteFromInventory(inventoryID);
            Toast.makeText(getBaseContext(), "Removed card from inventory.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        else if(quantity < db.getQuantityFromInventory(inventoryID)){
            db.removeQuantityFromInventory(thisCard, quantity);
            Toast.makeText(getBaseContext(), "Removed " + quantity + " from inventory.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
    }

    private void removeCard(){
        Intent mainScreen = new Intent(cardDisplayController.this, mainActivityController.class);
        int cartID = db.getIDFromCart(thisCard);
        int inventoryID = db.getIDFromInventory(thisCard);

        //Card doesn't exist in cart.
        if (cartID == -1 && inventoryID == -1) {
            Toast.makeText(getBaseContext(), "You don't have this card.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        //Card is in cart and removing less than total in cart.
        else if (cartID != -1 && quantity < db.getQuantityFromCart(cartID)) {
            db.removeQuantityFromCart(thisCard, quantity);
            Toast.makeText(getBaseContext(), "Removed " + quantity + " from cart.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        //Card is in cart and removing equal to total in cart.
        else if (cartID != -1 && quantity == db.getQuantityFromCart(cartID)) {
            db.deleteFromCart(cartID);
            Toast.makeText(getBaseContext(), "Removed " + quantity + " from cart.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(mainScreen);
        }
        //Card is in cart and removing more than total in cart.
        else if (cartID != -1 && quantity > db.getQuantityFromCart(cartID)) {
            int remainder = db.getQuantityFromCart(cartID) - quantity;
            if (inventoryID == -1 || (inventoryID != -1 && db.getQuantityFromInventory(inventoryID) < remainder)) {
                Toast.makeText(getBaseContext(), "Not permitted: removing more than quantity owned.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(mainScreen);
            } else if (inventoryID != -1 && db.getQuantityFromInventory(inventoryID) >= remainder) {
                db.deleteFromCart(cartID);
                YugiohCard remover = thisCard;
                remover.setNumInventory(0 - remainder);
                db.addCardToCart(remover);
                Toast.makeText(getBaseContext(), "Added task to cart.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(mainScreen);
            }
        }
        //Card is not in cart and removing less than total in inventory.
        else if (cartID == -1 && inventoryID != -1) {
            if (db.getQuantityFromInventory(inventoryID) > quantity) {
                YugiohCard remover = thisCard;
                remover.setNumInventory(0 - quantity);
                db.addCardToCart(remover);
                Toast.makeText(getBaseContext(), "Added task to cart.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(mainScreen);
            }
        }
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
