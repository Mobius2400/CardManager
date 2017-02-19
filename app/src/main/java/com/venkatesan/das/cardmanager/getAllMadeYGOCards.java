package com.venkatesan.das.cardmanager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

/**
 * Created by Das on 2/18/2017.
 */

public class getAllMadeYGOCards {

    public void setupAllCards(){
        //Get List of Cards From URL
        try {
            File input = new File("/tmp/allCards.html");
            Document doc = Jsoup.parse(input, "UTF-8", Contract.allCardsURL);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
