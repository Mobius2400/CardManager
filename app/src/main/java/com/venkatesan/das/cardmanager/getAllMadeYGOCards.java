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
        disableSSLCertCheck();
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

    private static void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
