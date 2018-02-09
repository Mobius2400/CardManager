package com.venkatesan.das.cardmanager;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;

public class SearchImageFragment extends Fragment {

    File myFilesDir;
    ImageView showMarkedImgView;
    Bitmap myImg, scaledGrayImg;
    String imageText;
    View myView;
    private TessBaseAPI mTess;
    Context context;
    private ProgressDialog asyncDialog;

    private final int camera_resultCode = 1;

    private OnFragmentInteractionListener mListener;

    public SearchImageFragment() {}

    public static SearchImageFragment newInstance() {
        SearchImageFragment fragment = new SearchImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFilesDir = new File(Contract.imgSavePathDir);
        myFilesDir.mkdirs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_search_image, container, false);
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Contract.imgSavePath)));
        startActivityForResult(camera_intent, camera_resultCode);
        showMarkedImgView = (ImageView)myView.findViewById(R.id.markedImg);
        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == camera_resultCode){
            if(resultCode == RESULT_OK){
                //Process Captured Image
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inPreferredConfig = Bitmap.Config.ARGB_8888;
                myImg = BitmapFactory.decodeFile(Contract.imgSavePath, op);
                //Picasso.with(getContext()).load(Contract.grayImgSavePath).into(showMarkedImgView);

                //Extract Text
                new asyncTessOCR().execute();

                //Return to Home Page
                //getActivity().onBackPressed();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess = new TessBaseAPI();
        mTess.init(Contract.tessDataPath, "eng");
        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        mTess.end();
        return result;
    }

    public class asyncTessOCR extends AsyncTask<Void, String, String>{

        public asyncTessOCR(){
        }
        @Override
        protected void onPreExecute(){
            asyncDialog = new ProgressDialog(getContext());
            asyncDialog.setMessage("Recognizing Image...");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            //Convert to grayscale and store
            publishProgress("Grayscaling Image...");
            scaledGrayImg = toGrayscale(myImg);
            myImg.recycle();
            scaledGrayImg = scaleBitmap(scaledGrayImg, scaledGrayImg.getWidth()/2, scaledGrayImg.getHeight()/2);
            saveImage(scaledGrayImg, Contract.grayImgSavePath);

            //Get OCR Result
            publishProgress("Detecting Text...");
            imageText = getOCRResult(scaledGrayImg);
            return imageText;
        }

        @Override
        protected void onProgressUpdate(String... value) {
            super.onProgressUpdate(value);
            asyncDialog.setMessage(value[0]);
            asyncDialog.show();
        }

        @Override
        protected void onPostExecute(String input) {
            super.onPostExecute(input);
            asyncDialog.dismiss();
            System.out.println(imageText);
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Boolean saveImage(Bitmap img, String filepath){
        Boolean success = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filepath);
            img.compress(Bitmap.CompressFormat.PNG, 100, out);
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());
        return output;
    }
}