package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.application.R;

public class OurServices extends AppCompatActivity {

    TextView gtext,gitext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_services);

        gtext = findViewById(R.id.gtext);
        gitext = findViewById(R.id.gitext);

        Typeface sans = Typeface.createFromAsset(getAssets(), "font/opensans.ttf");
        Typeface lato=Typeface.createFromAsset(OurServices.this.getAssets(), "font/latobold.ttf");

//        gtext.setTypeface(sans);
        gitext.setTypeface(lato);

    }
}
