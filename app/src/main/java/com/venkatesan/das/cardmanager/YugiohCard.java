package com.venkatesan.das.cardmanager;

import android.widget.ImageView;

/**
 * Created by Das on 2/9/2017.
 */

public class YugiohCard {
    String name;
    String print_tag;
    double low, median, high, shift;
    int numInventory;
    String rarity;
    ImageView cardImage;

    public YugiohCard(){
        name = "";
        print_tag = "";
        low = 0.0;
        median = 0.0;
        high = 0.0;
        shift = 0.0;
        numInventory = 0;
        rarity = "";
        cardImage = null;
    }

    public YugiohCard(String newname, String newprint_tag, double newhigh, double newmedian, double newlow, double newshift,
                      int newnumInventory, String newrarity){
        name = newname;
        print_tag = newprint_tag;
        low = newlow;
        median = newmedian;
        high = newhigh;
        shift = newshift;
        numInventory = newnumInventory;
        rarity = newrarity;
        cardImage = null;
    }

    @Override
    public String toString() {
        return "YugiohCard{" +
                "name='" + name + '\'' +
                ", print_tag='" + print_tag + '\'' +
                ", low=" + low +
                ", median=" + median +
                ", high=" + high +
                ", shift=" + shift +
                ", numInventory=" + numInventory +
                ", rarity='" + rarity + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrint_tag() {
        return print_tag;
    }

    public void setPrint_tag(String print_tag) {
        this.print_tag = print_tag;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getShift() {
        return shift;
    }

    public void setShift(double shift) {
        this.shift = shift;
    }

    public int getNumInventory() {
        return numInventory;
    }

    public void setNumInventory(int numInventory) {
        this.numInventory = numInventory;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public ImageView getCardImage() { return cardImage; }

    public void setCardImage(ImageView cardImage) { this.cardImage = cardImage; }
}
