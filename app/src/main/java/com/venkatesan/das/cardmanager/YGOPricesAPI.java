package com.venkatesan.das.cardmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Das on 2/9/2017.
 */

public class YGOPricesAPI {
   public static String searchByName(String name) throws MalformedURLException{
       String results = "";
       try{
           // Make Connection
           final URL byName = new URL("http://yugiohprices.com/api/card_data/");
           URLConnection connection = byName.openConnection();

           //Use Post
           connection.setDoOutput(true);
           connection.setAllowUserInteraction(false);

           //Send Query
           String query = byName+URLEncoder.encode(name,"UTF-8");
           PrintStream poster = new PrintStream(connection.getOutputStream());
           poster.print(query);
           poster.close();

           //Parse Result
           ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
           byte[] buffer = new byte[1024];
           int length;
           //InputStreamReader resultReturn = new InputStreamReader(connection.getInputStream());
           while((length = connection.getInputStream().read(buffer)) != -1){
               resultStream.write(buffer,0,length);
           }

           //Turn to String
           results = resultStream.toString("UTF-8");
       }
       catch(Exception e){
           e.printStackTrace();
       }
   return results;
   }

   public static JSONArray toJSON(String results) throws JSONException{
       JSONArray jArray = null;
       try {
           jArray = new JSONArray(results);
       }
       catch (Exception m){
           m.printStackTrace();
       }
       return jArray;
   }

   public static ArrayList<YugiohCard> getCard(JSONArray resultSet) throws JSONException{
       ArrayList<YugiohCard> cards = new ArrayList<YugiohCard>();
       try{
           for(int i = 0; i < resultSet.length(); i++) {
               YugiohCard toAdd = new YugiohCard();
               JSONObject temp = resultSet.getJSONObject(i);
               toAdd.setName(temp.getString("name"));
           }

       }
       catch(Exception n){
           n.printStackTrace();
       }
       return cards;
   }
}
