package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewInventoryFragment extends Fragment {

    ArrayList<YugiohCard> inventoryCards;
    int totalCards;
    TextView numCards;
    ListView searchResult;
    ArrayList<String> distinctRarities;
    Spinner raritySelector;
    private OnFragmentInteractionListener mListener;

    public ViewInventoryFragment() {}

    public static ViewInventoryFragment newInstance() {
        ViewInventoryFragment fragment = new ViewInventoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_inventory, container, false);
        cardDatabase db = new cardDatabase(getActivity());
        inventoryCards = db.getAllInventoryCards();
        totalCards = db.totalCards();
        distinctRarities = db.getDistinctRarities();
        db.close();

        raritySelector = (Spinner)view.findViewById(R.id.raritySelect);
        setupRarityFilter();
        raritySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rarityFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        numCards = (TextView)view.findViewById(R.id.numCards);
        numCards.setText(Integer.toString(totalCards));

        searchResult = (ListView)view.findViewById(R.id.searchInventoryResults);
        searchResult.setAdapter(new inventoryAdapter(getActivity(), inventoryCards));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupRarityFilter(){
        ArrayList<String> modifiedDistinctRarities = distinctRarities;
        modifiedDistinctRarities.add(0, "Show All");
        String[] rarities = distinctRarities.toArray(new String[distinctRarities.size()]);
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rarities);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        raritySelector.setAdapter(spinnerAdapter);
    }

    private void rarityFilter(int position){
        String rarity = distinctRarities.get(position);
        ArrayList<YugiohCard> filteredInventory = new ArrayList<>();
        if(rarity.equals("Show All")){
            searchResult.setAdapter(new inventoryAdapter(getActivity(), inventoryCards));
        }
        else{
            for(YugiohCard currCard: inventoryCards){
                if(currCard.getRarity().equals(rarity)){
                    filteredInventory.add(currCard);
                }
            }
            searchResult.setAdapter(new inventoryAdapter(getActivity(), filteredInventory));
        }
    }
}
