package com.venkatesan.das.cardmanager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Das on 2/18/2017.
 */

public class getAllMadeYGOCards {

    public static ArrayList<String> setupAllCards() throws KeyManagementException, NoSuchAlgorithmException {
        ArrayList<String> allCards = new ArrayList<>();
        //Get List of Cards From URL
        try {
            // Make Connection
            URL allCardURL = new URL(Contract.allCardsURL);
            HttpURLConnection connection = (HttpURLConnection) allCardURL.openConnection();
            //Use Post
            connection.setRequestMethod("GET");
            connection.setAllowUserInteraction(false);
            //Parse Result
            int length;
            BufferedReader bufferedReturn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = bufferedReturn.readLine()) != null) {
                    if(line.contains("/single/")){
                        String[] temp = line.split(">");
                        String[] temp2 = temp[1].split("<");
                        String toAdd = temp2[0];
                        allCards.add(toAdd);
                }
            }
            Set<String> hs = new HashSet<>();
            hs.addAll(allCards);
            allCards.clear();
            allCards.addAll(hs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allCards;
    }
}
