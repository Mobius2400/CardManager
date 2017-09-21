package com.venkatesan.das.cardmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by Das on 9/21/2017.
 */

public class cardImageOCR {
    private TessBaseAPI myTess;

    public cardImageOCR(){
        myTess = new TessBaseAPI();
    }

    public String processImage(Bitmap img){
        myTess.init(null, "eng");
        myTess.setImage(img);
        String out = myTess.getUTF8Text();
        myTess.end();
        return out;
    }
}
