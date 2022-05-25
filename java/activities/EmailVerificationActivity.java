package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.users.Customer;
import com.fliers.trainly.models.trips.Places;
import com.fliers.trainly.models.users.User;

/**
 * Activity class verifying email link and logging user in
 * @version 03.05.2021
 * @author Alp Afyonluoğlu
 */
public class EmailVerificationActivity extends AppCompatActivity {
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
        setContentView( R.layout.activity_email_verification);

        // Variables
        SharedPreferences preferences;
        String emailLink;
        User currentUser;
        int loginType;
        int tempLoginType;
        Intent intent;
        ConstraintLayout layoutLoading;
        ConstraintLayout layoutEmailVerified;
        Button btContinue;
        TextView tvTitleEmailVerified;
        TextView tvDescriptionEmailVerified;
        Places places;

        // Code
        layoutLoading = findViewById( R.id.layoutLoadingV);
        layoutEmailVerified = findViewById( R.id.layoutEmailVerifiedV);
        btContinue = findViewById( R.id.btContinueV);
        tvTitleEmailVerified = findViewById( R.id.tvTitleEmailVerifiedV);
        tvDescriptionEmailVerified = findViewById( R.id.tvDescriptionEmailVerifiedV);

        emailLink = getIntent().getData().toString();
        preferences = getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);

        loginType = preferences.getInt( LOGGED_IN_USER_TYPE, NO_LOGIN);
        tempLoginType = preferences.getInt( TEMP_LOGGED_IN_USER_TYPE, CUSTOMER_LOGIN);

        if ( loginType != NO_LOGIN) {
            // User previously logged in
            intent = new Intent( getApplicationContext(), SplashActivity.class);
            startActivity( intent);
            finish();
        }
        else {
            // Complete login verification process
            if ( tempLoginType == COMPANY_LOGIN) {
                currentUser = new Company( getApplicationContext());
            }
            else {
                currentUser = new Customer( getApplicationContext());
            }

            places = new Places( getApplicationContext());
            places.update( new Places.ServerSyncListener() {
                @Override
                public void onSync( boolean isSynced) {
                    if ( !isSynced) {
                        Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Places.setInstance( places);

                        currentUser.completeLogin( emailLink, new User.LoginListener() {
                            @Override
                            public void onLogin( boolean isLoggedIn) {
                                // Variables
                                String name;

                                // Code
                                if ( !isLoggedIn) {
                                    // Invalid link
                                    Toast.makeText( getApplicationContext(), "This link is no longer valid", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    User.setCurrentUserInstance( currentUser);

                                    // Check if user has a new account
                                    if ( !currentUser.isNewAccount()) {
                                        name = currentUser.getName();
                                        // Get only first name
                                        if ( name.contains( " ")) {
                                            name = name.split( " ")[0];
                                        }
                                        tvTitleEmailVerified.setText( "Welcome " + name + "!");
                                        tvDescriptionEmailVerified.setText( "Everything is ready for you. You can continue to your account.");

                                        preferences.edit().putInt( LOGGED_IN_USER_TYPE, tempLoginType).apply();
                                    }

                                    btContinue.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick( View v) {
                                            // Variables
                                            Intent continueIntent;

                                            // Code
                                            if ( currentUser.isNewAccount()) {
                                                continueIntent = new Intent( getApplicationContext(), RegisterActivity.class);
                                            }
                                            else {
                                                continueIntent = new Intent( getApplicationContext(), SplashActivity.class);
                                            }

                                            startActivity( continueIntent);
                                            finish();
                                        }
                                    });

                                    // Hide loading layout
                                    layoutLoading.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                    layoutEmailVerified.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}