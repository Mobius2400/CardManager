package com.venkatesan.das.cardmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Das on 9/19/2017.
 */

public class ZipCodeAPI {

    final private static String zipCodeURL = Contract.zipCodeURL;

    public static String locationByZip(String zip) throws IOException {
        String results = "Error. Invalid Zip Code.";
        StringBuilder returnSet = new StringBuilder();

        try{
            // Make Connection
            URL byZip = new URL(zipCodeURL + "info.json/" + URLEncoder.encode(zip, "UTF-8") + "/degrees");
            HttpURLConnection connection = (HttpURLConnection)byZip.openConnection();
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
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return results;
    }
}
