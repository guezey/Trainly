package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.User;

/**
 * Activity class getting user info to register account
 * @version 03.05.2021
 * @author Alp Afyonluoğlu
 */
public class RegisterActivity extends AppCompatActivity {
    // Properties
    private final String LOGGED_IN_USER_TYPE = "loggedInUserType";
    private final String TEMP_LOGGED_IN_USER_TYPE = "tempLoggedInUserType";
    private final int NO_LOGIN = 0;
    private final int COMPANY_LOGIN = 1;
    private final int CUSTOMER_LOGIN = 2;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_register);

        // Variables
        SharedPreferences preferences;
        User currentUser;
        int loginType;
        int tempLoginType;
        ConstraintLayout layoutRegister;
        ConstraintLayout layoutLoading;
        ConstraintLayout layoutRegistered;
        TextView tvDescription;
        EditText etName;
        EditText etEmail;
        Button btRegister;
        Button btContinue;
        TextView tvPrivacyPolicy;

        // Code
        layoutRegister = findViewById( R.id.layoutRegisterR);
        layoutLoading = findViewById( R.id.layoutLoadingR);
        layoutRegistered = findViewById( R.id.layoutRegisteredR);
        tvDescription = findViewById( R.id.tvDescriptionR);
        etName = findViewById( R.id.etNameR);
        etEmail = findViewById( R.id.etEmailR);
        btRegister = findViewById( R.id.btRegisterR);
        btContinue = findViewById( R.id.btContinueR);
        tvPrivacyPolicy = findViewById( R.id.tvPrivacyPolicyR);

        preferences = getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);
        tempLoginType = preferences.getInt( TEMP_LOGGED_IN_USER_TYPE, CUSTOMER_LOGIN);
        currentUser = User.getCurrentUserInstance();

        // Update UI if customer is logging in
        if ( tempLoginType == CUSTOMER_LOGIN) {
            tvDescription.setText( "To complete your registration, enter your full name below.");
            etName.setHint( "Full name");
        }
        etEmail.setText( currentUser.getEmail());

        etName.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {
                // Empty method
            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count) {
                if ( etName.getText().toString().length() >= 3) {
                    btRegister.setEnabled( true);
                }
                else {
                    btRegister.setEnabled( false);
                }
            }

            @Override
            public void afterTextChanged( Editable s) {
                // Empty method
            }
        });

        btRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                if ( !currentUser.isConnectedToInternet()) {
                    Toast.makeText( getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Show loading layout
                    layoutRegister.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                    layoutLoading.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                    btRegister.setEnabled( false);
                    etName.setEnabled( false);
                    tvPrivacyPolicy.setEnabled( false);

                    currentUser.setName( etName.getText().toString());

                    currentUser.saveToServer( new User.ServerSyncListener() {
                        @Override
                        public void onSync( boolean isSynced) {
                            if ( !isSynced) {
                                // Show register layout again and show error message
                                layoutLoading.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                layoutRegister.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                btRegister.setEnabled( true);
                                etName.setEnabled( true);
                                tvPrivacyPolicy.setEnabled( true);

                                Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                preferences.edit().putInt( LOGGED_IN_USER_TYPE, tempLoginType).apply();

                                // Hide loading layout
                                layoutLoading.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                layoutRegistered.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                btContinue.setEnabled( true);

                                btContinue.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick( View v) {
                                        // Variables
                                        Intent continueIntent;

                                        // Code
                                        continueIntent = new Intent( getApplicationContext(), SplashActivity.class);

                                        startActivity( continueIntent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        tvPrivacyPolicy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // Variables
                Uri webPageUri;
                Intent webPageIntent;

                // Code
                webPageUri = Uri.parse( "https://trainly-app.web.app/privacy-policy/");
                webPageIntent = new Intent( Intent.ACTION_VIEW, webPageUri);
                startActivity( webPageIntent);
            }
        });
    }
}