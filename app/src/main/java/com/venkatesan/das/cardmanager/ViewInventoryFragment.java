package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewInventoryFragment extends Fragment {

    ArrayList<YugiohCard> inventoryCards;
    int totalCards;
    TextView numCards;
    ListView searchResult;

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
        db.close();

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
}
