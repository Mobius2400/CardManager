package com.venkatesan.das.cardmanager;

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

   final static String baseURL_allVersions = "http://yugiohprices.com/api/card_versions/";
   static String testName = "";
   static String currName = "";

   public static void main(String[] args){
       testName = "Blue-Eyes White Dragon";
       try {
           System.out.println(searchByName(testName));
//           JSONArray tester = toJSON(searchByName(testName));
//           for(int i = 0; i < tester.length(); i++){
//               JSONObject printer = tester.getJSONObject(i);
//               System.out.print(printer.getString("print_tag"));
//               System.out.println(printer.getString("rarity"));
//           }
       }
       catch (MalformedURLException e) {
           e.printStackTrace();
       } //catch (JSONException e) {
           //e.printStackTrace();
       //}
   }

   public static String searchByName(String name) throws MalformedURLException{
       currName = name;
       String results = "Error: Something happened";
       StringBuilder returnSet = new StringBuilder();
       try{
           // Make Connection
           URL byName = new URL(baseURL_allVersions+URLEncoder.encode(name,"UTF-8"));
           HttpURLConnection connection = (HttpURLConnection)byName.openConnection();

           //Use Post
           connection.setDoOutput(true);
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
       }
       catch(Exception e){
           e.printStackTrace();
       }
       results = returnSet.toString();
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
}
