package com.venkatesan.das.cardmanager;

import android.media.Image;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Das on 2/9/2017.
 */

public class YGOPricesAPI {
   final static String base = "http://yugiohprices.com/api/";
   final static String baseURL_allVersions = base + "card_versions/";
   final static String baseURL_dataByTagandRarity = base + "price_history/";
   final static String baseURL_imageByName = base + "card_image/";
   private static String currName = "";

   public static String getURLImageByName(){
        return baseURL_imageByName;
   }

   public static String priceByTag(String print_tag, String rarity) throws MalformedURLException {
       String currTag = print_tag;
       String currRarity = rarity;
       String results = "Error: Something happened";
       StringBuilder returnSet = new StringBuilder();
       try {
           // Make Connection
           URL byTag = new URL(baseURL_dataByTagandRarity + URLEncoder.encode(currTag, "UTF-8") + "?rarity=" +
                   URLEncoder.encode(currRarity, "UTF-8"));
           HttpURLConnection connection = (HttpURLConnection) byTag.openConnection();
           //Use Post
           connection.setRequestMethod("GET");
           connection.setAllowUserInteraction(false);
           //Parse Result
           int length;
           if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400) {
               BufferedReader bufferedReturn = new BufferedReader(new InputStreamReader(connection
                       .getInputStream()));
               String currLine = null;
               while ((currLine = bufferedReturn.readLine()) != null) {
                   returnSet.append(currLine);
               }
               bufferedReturn.close();
           }
           results = returnSet.toString();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return results;
   }

   public static String searchByName(String name) throws MalformedURLException{
       currName = name;
       String results = "Error: Something happened";
       StringBuilder returnSet = new StringBuilder();
       HttpURLConnection connection = null;
       try{
           // Make Connection
           String encoded = name.replace(' ', '+');
           URL byName = new URL(baseURL_allVersions+encoded);
           connection = (HttpURLConnection)byName.openConnection();
           connection.setRequestMethod("GET");
           connection.setAllowUserInteraction(false);

           //Parse Result
           int length;
           if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 400){
               BufferedReader bufferedReturn = new BufferedReader(new InputStreamReader(connection
                       .getInputStream()));
               String currLine = null;

               while ((currLine=bufferedReturn.readLine())!=null) {
                   returnSet.append(currLine);
               }
               bufferedReturn.close();
           }
           results = returnSet.toString();
       }
       catch(Exception e){
           e.printStackTrace();
       }
       finally{
           connection.disconnect();
       }
       return results;
   }

   public static JSONArray toJSON(String results) throws JSONException{
       JSONArray jArray = null;
       try {
           JSONObject jsnObj = new JSONObject(results);
           jArray = jsnObj.getJSONArray("data");
       }
       catch (JSONException m){
           m.printStackTrace();
       }
       return jArray;
   }

   public static ArrayList<YugiohCard> getCardForShortListing(JSONArray resultSet) throws JSONException{
       ArrayList<YugiohCard> cards = new ArrayList<YugiohCard>();
       try{
           for(int i = 0; i < resultSet.length(); i++) {
               YugiohCard toAdd = new YugiohCard();
               JSONObject temp = resultSet.getJSONObject(i);
               toAdd.setName(currName);
               toAdd.setPrint_tag(temp.getString("print_tag"));
               toAdd.setRarity(temp.getString("rarity"));
               cards.add(toAdd);
           }

       }
       catch(Exception n){
           n.printStackTrace();
       }
       return cards;
   }

   public static YugiohCard getCardForDisplay(JSONArray resultSet) throws JSONException{
       YugiohCard thisCard = new YugiohCard();

       try{
           JSONObject temp = resultSet.getJSONObject(0);
           thisCard.setHigh(temp.getDouble("price_high"));
           thisCard.setMedian(temp.getDouble("price_average"));
           thisCard.setLow(temp.getDouble("price_low"));
           thisCard.setShift(temp.getDouble("price_shift") * 100);
       }
       catch(Exception n){
          n.printStackTrace();
       }
       return thisCard;
   }
}
