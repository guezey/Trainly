package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fliers.trainly.R;

/**
 * Activity class directing user to company or customer login page
 * @version 03.05.2021
 * @author Alp Afyonluoğlu
 */
public class WelcomeActivity extends AppCompatActivity {
    // Properties
    private final int COMPANY_LOGIN = 1;
    private final int CUSTOMER_LOGIN = 2;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Variables
        ImageView imgAbout;
        Button btCustomer;
        Button btCompany;

        // Code
        imgAbout = findViewById( R.id.imgAboutW);
        btCustomer = findViewById( R.id.btCustomerW);
        btCompany = findViewById( R.id.btCompanyW);

        imgAbout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // Variables
                Intent intent;

                // Code
                intent = new Intent( getApplicationContext(), HelpActivity.class);
                startActivity( intent);
            }
        });

        btCompany.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // Variables
                Intent intent;

                // Code
                intent = new Intent( getApplicationContext(), LoginActivity.class);
                intent.putExtra( "loginType", COMPANY_LOGIN);
                startActivity( intent);
            }
        });

        btCustomer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // Variables
                Intent intent;

                // Code
                intent = new Intent( getApplicationContext(), LoginActivity.class);
                intent.putExtra( "loginType", CUSTOMER_LOGIN);
                startActivity( intent);
            }
        });
    }
}