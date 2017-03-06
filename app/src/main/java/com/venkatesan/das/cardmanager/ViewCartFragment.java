package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewCartFragment extends Fragment {

    Button btnaddToInventory;
    Button btndeleteSelected;
    ArrayList<YugiohCard> cartCards;
    ArrayList<YugiohCard> toDelete;
    ListView searchResult;

    private OnFragmentInteractionListener mListener;

    public ViewCartFragment() {}

    public static ViewCartFragment newInstance() {
        ViewCartFragment fragment = new ViewCartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        cardDatabase db = new cardDatabase(getActivity());
        cartCards = db.getAllCartCards();
        toDelete = new ArrayList<>();
        db.close();
        btnaddToInventory = (Button)view.findViewById(R.id.commitToInventory);
        btnaddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToInventory(cartCards);
                Toast.makeText(getActivity(), "All cards added to inventory.", Toast.LENGTH_SHORT).show();
                Intent toMain = new Intent(getActivity(), MainActivity.class);
                startActivity(toMain);
            }
        });

        btndeleteSelected = (Button)view.findViewById(R.id.discardSelected);
        btndeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelected();
                Toast.makeText(getActivity(), "Removed selected.", Toast.LENGTH_SHORT).show();
            }
        });

        searchResult = (ListView) view.findViewById(R.id.searchResults);
        searchResult.setAdapter(new cartAdapter(getActivity(), cartCards));
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YugiohCard selectCard = (YugiohCard)parent.getItemAtPosition(position);
                System.out.println(selectCard.getName());
                Toast.makeText(getActivity(), "Clicked on Row: " + selectCard.getName(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
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

    public void moveToInventory(ArrayList<YugiohCard> cartCards){
        cardDatabase db = new cardDatabase(getActivity());
        if(cartCards.size() == 0){
            Toast.makeText(getActivity(), "No cards in cart.", Toast.LENGTH_SHORT).show();
        }
        else{
            for(YugiohCard currCard: cartCards){
                int ID = db.getIDFromInventory(currCard);
                int cartID = db.getIDFromCart(currCard);
                if(ID == -1){
                    db.addCardToInventory(currCard);
                    db.deleteFromCart(cartID);
                }
                else if(ID != -1){
                    db.addQuantityFromInventory(currCard, db.getQuantityFromCart(cartID));
                    db.deleteFromCart(cartID);
                }
            }
        }
        db.close();
    }

    public void deleteSelected(){
        cardDatabase db = new cardDatabase(getActivity());
        for(YugiohCard card: toDelete){
            db.deleteFromCart(db.getIDFromCart(card));
        }
        db.close();
    }
}
