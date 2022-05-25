package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fliers.trainly.BuildConfig;
import com.fliers.trainly.R;

/**
 * Activity class showing app related info
 * @version 03.05.2021
 * @author Alp Afyonluoğlu
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Variables
        ImageView imgBack;
        TextView tvVersionName;

        // Code
        imgBack = findViewById( R.id.imgBackH);
        tvVersionName = findViewById( R.id.tvVersionNameH);

        tvVersionName.setText( "App version: " + BuildConfig.VERSION_NAME);

        imgBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                finish();
            }
        });
    }
}