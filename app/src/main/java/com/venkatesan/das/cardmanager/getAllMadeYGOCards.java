package com.venkatesan.das.cardmanager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public static void main (String[] args){
    }

    public static ArrayList<String> setupAllCards() throws KeyManagementException, NoSuchAlgorithmException {
        ArrayList<String> allCards = new ArrayList<>();
        disableSSLCertCheck();
        //Get List of Cards From URL
        try {
            Document doc  = Jsoup.connect(Contract.allCardsURL).get();
            Elements metaElems = doc.select("a");
            for(Element a: metaElems){
                allCards.add(a.text());
            }
            Set<String> removeDuplicates = new HashSet<>();
            removeDuplicates.addAll(allCards);
            allCards.clear();
            allCards.addAll(removeDuplicates);
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
