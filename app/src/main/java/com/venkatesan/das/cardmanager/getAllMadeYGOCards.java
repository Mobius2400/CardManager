package com.venkatesan.das.cardmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;

/**
 * Created by Das on 2/18/2017.
 */

public class getAllMadeYGOCards {

    public static ArrayList<String> setupAllCards(){
        ArrayList<String> allCards = new ArrayList<>();
        //Get List of Cards From URL
        try {
            // Make Connection
            URL allCardURL = new URL(Contract.allCardsURL2);
            HttpURLConnection connection = (HttpURLConnection) allCardURL.openConnection();
            //Use Post
            connection.setRequestMethod("GET");
            connection.setAllowUserInteraction(false);
            //Parse Result
            BufferedReader bufferedReturn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuilder sb = new StringBuilder();
            while ((input = bufferedReturn.readLine()) != null){
                sb.append(input);
            }
            bufferedReturn.close();
            JSONObject response = new JSONObject(sb.toString());
            JSONArray cards = response.getJSONArray("cards");
            for(int card = 0; card < cards.length(); card++){
                allCards.add(cards.getString(card));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allCards;
    }
}
