package com.venkatesan.das.cardmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    static{ System.loadLibrary("opencv_java"); }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchCardbyName(View v){
        EditText cardName = (EditText) findViewById(R.id.cardSearcher);
    }
}
