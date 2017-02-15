package com.venkatesan.das.cardmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Das on 2/13/2017.
 */

public class asyncImageLoad extends AsyncTask<Void, Void, Bitmap> {
    private String getImageURL;
    private ImageView putCardImage;

    public asyncImageLoad(String cardName, ImageView destinationView){
        try {
            getImageURL = YGOPricesAPI.getURLImageByName() + URLEncoder.encode(cardName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        putCardImage = destinationView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(getImageURL);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myImage = BitmapFactory.decodeStream(input);
            return myImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        putCardImage.setImageBitmap(bitmap);
    }
}
