package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewCartFragment extends Fragment {

    Button btnaddToInventory;
    Button btndeleteSelected;
    ArrayList<YugiohCard> cartCards;
    ArrayList<YugiohCard> isSelected;
    ListView searchResult;
    cartAdapter adapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        isSelected = new ArrayList<>();

        setupCartList(view);

        btnaddToInventory = (Button)view.findViewById(R.id.commitToInventory);
        btnaddToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean moved = moveToInventory(isSelected);
                if(moved){
                    Toast.makeText(getActivity(), "Selected Cards added to Inventory.", Toast.LENGTH_SHORT).show();
                    //Reload cart items.
                    setupCartList(view);
                }
            }
        });

        btndeleteSelected = (Button)view.findViewById(R.id.discardSelected);
        btndeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelected();
                Toast.makeText(getActivity(), "Removed selected.", Toast.LENGTH_SHORT).show();
                //Reload cart items.
                setupCartList(view);
            }
        });
        return view;
    }

    private void setupCartList(View view){
        cardDatabase db = new cardDatabase(getActivity());
        cartCards = db.getAllCartCards();
        searchResult = (ListView) view.findViewById(R.id.searchResults);
        adapter = new cartAdapter(getActivity(), cartCards);
        searchResult.setAdapter(adapter);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YugiohCard listitem = (YugiohCard)searchResult.getItemAtPosition(position);
                Boolean isChecked = ((CheckBox)view.findViewById(R.id.selectCard)).isChecked();
                if(isChecked){
                    isSelected.remove(listitem);
                    ((CheckBox)view.findViewById(R.id.selectCard)).setChecked(false);
                    view.setBackgroundColor(0x00000);
                }
                else if(!isChecked){
                    isSelected.add(listitem);
                    ((CheckBox)view.findViewById(R.id.selectCard)).setChecked(true);
                    view.setBackgroundColor(getResources().getColor(R.color.selected));
                }
            }
        });
        db.close();
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

    public Boolean moveToInventory(ArrayList<YugiohCard> cartCards){
        cardDatabase db = new cardDatabase(getActivity());
        if(cartCards.size() == 0){
            Toast.makeText(getActivity(), "No cards to add.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (cartCards.size() > 0){
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
            return true;
        }
        db.close();
        return false;
    }

    public void deleteSelected(){
        cardDatabase db = new cardDatabase(getActivity());
        for(YugiohCard card: isSelected){
            db.deleteFromCart(db.getIDFromCart(card));
        }
        db.close();
    }
}
