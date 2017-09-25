package com.venkatesan.das.cardmanager;

import android.app.Activity;
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
    Bitmap myImg, myImgMarked;
    String imageText;
    private TessBaseAPI mTess;
    Context context;
    Bitmap markedImg;
    private ProgressDialog asyncDialog;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

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
        asyncDialog = new ProgressDialog(getContext());
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getContext(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Contract.imgSavePath)));
        startActivityForResult(camera_intent, camera_resultCode);
        return inflater.inflate(R.layout.fragment_search_image, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == camera_resultCode){
            if(resultCode == RESULT_OK){
                //Process Captured Image
                new asyncOpenCVMarkup().execute();
                //imageText = myAnalyzer.processImage(myImg);
                //System.out.println(imageText);
                //getActivity().onBackPressed();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public Bitmap detectText(Bitmap img) {
        Mat mMat = new Mat();
        System.out.println("Input Image: " + img.getWidth() + " " + img.getHeight());
        Bitmap croppedImage = Bitmap.createBitmap(img.getWidth(),img.getHeight()/3, Bitmap.Config.ARGB_8888);
        System.out.println("Cropped Image: " + croppedImage.getWidth() + " " + croppedImage.getHeight());
        Utils.bitmapToMat(croppedImage, mMat);
        MatOfKeyPoint keypoint = new MatOfKeyPoint();
        List<KeyPoint> listpoint;
        KeyPoint kpoint;
        Scalar CONTOUR_COLOR = new Scalar(255);
        Mat mask = Mat.zeros(mMat.size(), CvType.CV_8UC1);
        int rectanx1;
        int rectany1;
        int rectanx2;
        int rectany2;
        int imgsize = mMat.height() * mMat.width();
        Scalar zeos = new Scalar(0, 0, 0);
        Mat kernel = new Mat(1, 500, CvType.CV_8UC1, Scalar.all(255));
        Mat morbyte = new Mat();
        Mat hierarchy = new Mat();

        List<MatOfPoint> contour2 = new ArrayList<MatOfPoint>();

        Rect rectan3;
        FeatureDetector detector = FeatureDetector
                .create(FeatureDetector.MSER);
        detector.detect(mMat, keypoint);
        listpoint = keypoint.toList();

        for (int ind = 0; ind < listpoint.size(); ind++) {
            kpoint = listpoint.get(ind);
            rectanx1 = (int) (kpoint.pt.x - 0.5 * kpoint.size);
            rectany1 = (int) (kpoint.pt.y - 0.5 * kpoint.size);
            rectanx2 = (int) (kpoint.size);
            rectany2 = (int) (kpoint.size);
            if (rectanx1 <= 0)
                rectanx1 = 1;
            if (rectany1 <= 0)
                rectany1 = 1;
            if ((rectanx1 + rectanx2) > mMat.width())
                rectanx2 = mMat.width() - rectanx1;
            if ((rectany1 + rectany2) > mMat.height())
                rectany2 = mMat.height() - rectany1;
            Rect rectant = new Rect(rectanx1, rectany1, rectanx2, rectany2);
            try {
                Mat roi = new Mat(mask, rectant);
                roi.setTo(CONTOUR_COLOR);
            } catch (Exception ex) {
                Log.d("mylog", "mat roi error " + ex.getMessage());
            }
        }

        Imgproc.morphologyEx(mask, morbyte, Imgproc.MORPH_DILATE, kernel);
        Imgproc.findContours(morbyte, contour2, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

//        Bitmap bmp = null;
//        for (int ind = 0; ind < contour2.size(); ind++) {
//            rectan3 = Imgproc.boundingRect(contour2.get(ind));
//            try {
//                Mat croppedPart;
//                croppedPart = mMat.submat(rectan3);
//                bmp = Bitmap.createBitmap(croppedPart.width(), croppedPart.height(), Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(croppedPart, bmp);
//            } catch (Exception e) {
//                Log.d(null, "cropped part data error " + e.getMessage());
//            }
//            if (bmp != null) {
//                System.out.println(getOCRResult(bmp));
//            }
//        }
        for (int ind = 0; ind < contour2.size(); ind++) {
            rectan3 = Imgproc.boundingRect(contour2.get(ind));
            if (rectan3.area() > 0.5 * imgsize || rectan3.area() < 100
                    || rectan3.width / rectan3.height < 2) {
                Mat roi = new Mat(morbyte, rectan3);
                roi.setTo(zeos);

            } else{
                Imgproc.rectangle(mMat, rectan3.br(), rectan3.tl(),
                        CONTOUR_COLOR);
                Utils.matToBitmap(mMat, markedImg);
            }
        }
        return markedImg;
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess = new TessBaseAPI();
        mTess.init(Contract.tessDataPath, "eng");
        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        mTess.end();
        return result;
    }

    public class asyncOpenCVMarkup extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            asyncDialog.setMessage("Recognizing Image...");
            asyncDialog.show();
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //myImg = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.summoned_skull, op);
            myImg = BitmapFactory.decodeFile(Contract.imgSavePath, op);
            showMarkedImgView = (ImageView)getView().findViewById(R.id.markedImg);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            myImgMarked = detectText(myImg);
            return myImgMarked;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            asyncDialog.setMessage("Detecting Text...");
            asyncDialog.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            showMarkedImgView.setImageBitmap(bitmap);
            asyncDialog.dismiss();
        }
    }
}