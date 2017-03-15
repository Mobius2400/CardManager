package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class SearchTextFragment extends Fragment {

    AutoCompleteTextView cardName;
    TextView nameDisplay;
    TextView resultMessage;
    ArrayList<String> results = new ArrayList<String>();
    YugiohCard[] resultCards;
    ListView displayResults;
    Button goSearch;
    String card_name = "";
    SearchTextFragment.AsyncQuery searcher;

    private OnFragmentInteractionListener mListener;

    public SearchTextFragment() {}

    public static SearchTextFragment newInstance() {
        SearchTextFragment fragment = new SearchTextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_text, container, false);

        //Create Auto Complete Text
        cardName = (AutoCompleteTextView)view.findViewById(R.id.cardNameByName);
        cardName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        //Give textview focus

        allCardsDatabase db = new allCardsDatabase(getActivity());
        ArrayList<String> allCards = db.getAllMadeCards();
        allCards.toArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, allCards);
        cardName.setAdapter(adapter);
        // Create variable references to each widget.
        nameDisplay = (TextView)view.findViewById(R.id.cardNameShow);
        resultMessage = (TextView)view.findViewById(R.id.resultMessage);

        displayResults = (ListView)view.findViewById(R.id.resultSet);
        displayResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToCardDisplayActivity(position);
            }
        });

        cardName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cardName.setText("");
            }
        });

        goSearch = (Button)view.findViewById(R.id.searchButton);
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_name = cardName.getText().toString().trim();
                if(card_name == null || card_name.trim().equals("")){
                    Toast.makeText(getActivity(), "Input is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        nameDisplay.setText(cardName.getText());
                        setSearchProgress("Searching...");
                        onSearch(v);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void sendToCardDisplayActivity(int position){
        YugiohCard chosenCard = resultCards[position];
        Intent toCardDisplay = new Intent(getActivity(), CardDisplayActivity.class);

        Bundle values = new Bundle();
        values.putString(Contract.nameKey, card_name);
        values.putString(Contract.tagKey, chosenCard.getPrint_tag());
        values.putString(Contract.rarityKey, chosenCard.getRarity());
        toCardDisplay.putExtras(values);
        //Inflate the fragment
        startActivity(toCardDisplay);
    }

    public void setSearchProgress(String message){
        resultMessage.setText(message);
    }

    public void onSearch(View goSearch) throws MalformedURLException{
        results.clear();
        try{

            searcher = new SearchTextFragment.AsyncQuery();
            searcher.execute(card_name);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void toAdd(YugiohCard[] toadd){
        for(YugiohCard card: toadd){
            String print_tag = card.getPrint_tag();
            String rarity = card.getRarity();
            results.add(print_tag + " - " + rarity);
        }
    }

    @Override
    public void onResume(){
        if(resultMessage.getText().equals("Searching...")){
            setSearchProgress("");
            cardName.setText("");
            nameDisplay.setText("");
        }
        super.onResume();
    }

    public class AsyncQuery extends AsyncTask<String, Void, YugiohCard[]> {

        public AsyncQuery() {
        }

        @Override
        protected YugiohCard[] doInBackground(String... input)
        {
            YugiohCard[] iterator;
            card_name = input[0];
            String output = "";
            try {
                output = YGOPricesAPI.searchByName(card_name);
                if (output.length() > 0){
                    ArrayList<YugiohCard> cardVersions = new ArrayList<YugiohCard>();
                    cardVersions = YGOPricesAPI.getCardForShortListing(YGOPricesAPI.toJSON(output));
                    iterator = cardVersions.toArray(new YugiohCard[cardVersions.size()]);
                    if (iterator.length >= 1){
                        return iterator;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(YugiohCard[] yugiohCards) {
            super.onPostExecute(yugiohCards);
            resultCards = yugiohCards;
            if(yugiohCards.length == 0){
                setSearchProgress("No Results Found");
            }
            else if(yugiohCards.length == 1){
                toAdd(yugiohCards);
                sendToCardDisplayActivity(0);
            }
            else if(yugiohCards.length > 1){
                setSearchProgress("Multiple Versions Found:");
                toAdd(yugiohCards);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.simple_list_item_1, results);
                displayResults.setAdapter(adapter);
                ((EditText) getView().findViewById(R.id.cardNameByName)).setText("");
            }

        }
    }
}
