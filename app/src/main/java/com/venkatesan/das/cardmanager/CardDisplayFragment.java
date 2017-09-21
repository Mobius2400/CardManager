package com.venkatesan.das.cardmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;
import static com.venkatesan.das.cardmanager.YGOPricesAPI.getCardForDisplay;
import static com.venkatesan.das.cardmanager.YGOPricesAPI.toJSON;

public class CardDisplayFragment extends Fragment {
    private static final String CARD_NAME = Contract.nameKey;
    private static final String PRINT_TAG = Contract.tagKey;
    private static final String RARITY = Contract.rarityKey;

    private String mCardName;
    private String mPrintTag;
    private String mRarity;

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

    private OnFragmentInteractionListener mListener;

    public CardDisplayFragment() {}

    public static CardDisplayFragment newInstance(String param1, String param2, String param3) {
        CardDisplayFragment fragment = new CardDisplayFragment();
        Bundle args = new Bundle();
        args.putString(CARD_NAME, param1);
        args.putString(PRINT_TAG, param2);
        args.putString(RARITY, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardName = getArguments().getString(CARD_NAME);
            mPrintTag = getArguments().getString(PRINT_TAG);
            mRarity = getArguments().getString(RARITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_card_display, container, false);
        thisCard = new YugiohCard();
        db = new cardDatabase(getActivity());
        pref = getActivity().getSharedPreferences(preferencesKey, MODE_PRIVATE);

        //Create card
        thisCard.setName(mCardName);
        thisCard.setPrint_tag(mPrintTag);
        thisCard.setRarity(mRarity);
        thisCard.setCardImage((ImageView)view.findViewById(R.id.cardImage));

        // Define fields
        name = (TextView)view.findViewById(R.id.cardNameWithCard);
        cardRarity = (TextView)view.findViewById(R.id.Rarity);
        cardTag = (TextView)view.findViewById(R.id.printTag);
        quantityInStock = (TextView)view.findViewById(R.id.qtyInInventory);
        hi = (TextView)view.findViewById(R.id.priceHigh);
        med = (TextView)view.findViewById(R.id.priceMedian);
        low = (TextView)view.findViewById(R.id.priceLow);
        shift = (TextView)view.findViewById(R.id.priceShift );
        add = (ImageButton)view.findViewById(R.id.addInventory);
        add.setAlpha((float) 0.0);
        remove = (ImageButton)view.findViewById(R.id.removeInventory);
        remove.setAlpha((float) 0.0);

        // Render fields
        name.setText(thisCard.getName());
        cardTag.setText(thisCard.getPrint_tag());
        cardRarity.setText(thisCard.getRarity());
        // Set Image
        String getImageURL = null;
        try {
            getImageURL = Contract.baseURL_imageByName + URLEncoder.encode(thisCard.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Picasso.with(this.getContext()).load(getImageURL).into(thisCard.getCardImage());
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
                                Intent cardDisplay = new Intent(getActivity(), MainActivity.class);
                                Boolean autoCommit = pref.getBoolean(auto_commit, false);
                                //Check autoCommit
                                if(autoCommit){
                                    if(db.getIDFromInventory(thisCard) == -1){
                                        YugiohCard adder = thisCard;
                                        adder.setNumInventory(quantity);
                                        db.addCardToInventory(adder);
                                        Toast.makeText(getActivity(), "Card added to inventory.", Toast.LENGTH_SHORT).show();
                                        startActivity(cardDisplay);
                                    }
                                    else{
                                        db.addQuantityFromInventory(thisCard, quantity);
                                        Toast.makeText(getActivity(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
                                        startActivity(cardDisplay);
                                    }
                                }
                                else{
                                    if(db.getIDFromCart(thisCard) == -1){
                                        YugiohCard adder = thisCard;
                                        adder.setNumInventory(quantity);
                                        db.addCardToCart(adder);
                                        Toast.makeText(getActivity(), "Card added to cart.", Toast.LENGTH_SHORT).show();
                                        startActivity(cardDisplay);
                                    }
                                    else{
                                        db.addQuantityFromCart(thisCard, quantity);
                                        Toast.makeText(getActivity(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
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
                        Intent cardDisplay = new Intent(getActivity(), MainActivity.class);
                        if(db.getIDFromCart(thisCard) == -1){
                            YugiohCard adder = thisCard;
                            adder.setNumInventory(quantity);
                            db.addCardToCart(adder);
                            Toast.makeText(getActivity(), "Card added to cart.", Toast.LENGTH_SHORT).show();
                            startActivity(cardDisplay);
                        }
                        else{
                            db.addQuantityFromCart(thisCard, quantity);
                            Toast.makeText(getActivity(), "Increased quantity by " + quantity, Toast.LENGTH_SHORT).show();
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
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void autoCommitRemoveCard(){
        Intent mainScreen = new Intent(getActivity(), MainActivity.class);
        int inventoryID = db.getIDFromInventory(thisCard);
        if(inventoryID == -1){
            Toast.makeText(getActivity(), "You don't have this card.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        else if(quantity > db.getQuantityFromInventory(inventoryID)){
            Toast.makeText(getActivity(), "You don't have the required quantity.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        else if(quantity == db.getQuantityFromInventory(inventoryID)){
            db.deleteFromInventory(inventoryID);
            Toast.makeText(getActivity(), "Removed card from inventory.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        else if(quantity < db.getQuantityFromInventory(inventoryID)){
            db.removeQuantityFromInventory(thisCard, quantity);
            Toast.makeText(getActivity(), "Removed " + quantity + " from inventory.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
    }

    private void removeCard(){
        Intent mainScreen = new Intent(getActivity(), MainActivity.class);
        int cartID = db.getIDFromCart(thisCard);
        int inventoryID = db.getIDFromInventory(thisCard);

        //Card doesn't exist in cart.
        if (cartID == -1 && inventoryID == -1) {
            Toast.makeText(getActivity(), "You don't have this card.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        //Card is in cart and removing less than total in cart.
        else if (cartID != -1 && quantity < db.getQuantityFromCart(cartID)) {
            db.removeQuantityFromCart(thisCard, quantity);
            Toast.makeText(getActivity(), "Removed " + quantity + " from cart.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        //Card is in cart and removing equal to total in cart.
        else if (cartID != -1 && quantity == db.getQuantityFromCart(cartID)) {
            db.deleteFromCart(cartID);
            Toast.makeText(getActivity(), "Removed " + quantity + " from cart.", Toast.LENGTH_SHORT).show();
            startActivity(mainScreen);
        }
        //Card is in cart and removing more than total in cart.
        else if (cartID != -1 && quantity > db.getQuantityFromCart(cartID)) {
            int remainder = db.getQuantityFromCart(cartID) - quantity;
            if (inventoryID == -1 || (inventoryID != -1 && db.getQuantityFromInventory(inventoryID) < remainder)) {
                Toast.makeText(getActivity(), "Not permitted: removing more than quantity owned.", Toast.LENGTH_SHORT).show();
                startActivity(mainScreen);
            } else if (inventoryID != -1 && db.getQuantityFromInventory(inventoryID) >= remainder) {
                db.deleteFromCart(cartID);
                YugiohCard remover = thisCard;
                remover.setNumInventory(0 - remainder);
                db.addCardToCart(remover);
                Toast.makeText(getActivity(), "Added task to cart.", Toast.LENGTH_SHORT).show();
                startActivity(mainScreen);
            }
        }
        //Card is not in cart and removing less than total in inventory.
        else if (cartID == -1 && inventoryID != -1) {
            if (db.getQuantityFromInventory(inventoryID) > quantity) {
                YugiohCard remover = thisCard;
                remover.setNumInventory(0 - quantity);
                db.addCardToCart(remover);
                Toast.makeText(getActivity(), "Added task to cart.", Toast.LENGTH_SHORT).show();
                startActivity(mainScreen);
            }
        }
    }

    public void updateNumberInStock(){
        int ID = db.getIDFromInventory(thisCard);
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
