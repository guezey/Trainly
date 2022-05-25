package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.users.Customer;
import com.fliers.trainly.models.users.User;

/**
 * Activity class sending verification email to user-provided email address
 * @version 03.05.2021
 * @author Alp Afyonluoğlu
 */
public class LoginActivity extends AppCompatActivity {
    // Properties
    private final String TEMP_LOGGED_IN_USER_TYPE = "tempLoggedInUserType";
    private final int COMPANY_LOGIN = 1;
    private final int CUSTOMER_LOGIN = 2;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Variables
        int loginType;
        ImageView imgBack;
        TextView tvTitle;
        TextView tvEmailWarning;
        ConstraintLayout layoutEnterEmail;
        ConstraintLayout layoutLoading;
        ConstraintLayout layoutEmailSent;
        TextView tvDescriptionEmailSent;
        EditText etEmail;
        Button btContinue;
        User currentUser;
        SharedPreferences preferences;
        preferences = getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);

        // Code
        imgBack = findViewById( R.id.imgBackL);
        tvTitle = findViewById( R.id.tvTitleL);
        tvEmailWarning = findViewById( R.id.tvEmailWarningL);
        layoutEnterEmail = findViewById( R.id.layoutEnterEmailL);
        layoutLoading = findViewById( R.id.layoutLoadingL);
        layoutEmailSent = findViewById( R.id.layoutEmailSentL);
        etEmail = findViewById( R.id.etEmailL);
        btContinue = findViewById( R.id.btContinueL);
        tvDescriptionEmailSent = findViewById( R.id.tvDescriptionEmailSentL);

        // Set page title according to login type
        loginType = getIntent().getIntExtra("loginType", COMPANY_LOGIN);
        if ( loginType == CUSTOMER_LOGIN) {
            tvTitle.setText( "Customer Login");
            currentUser = new Customer( getApplicationContext());
        }
        else {
            tvTitle.setText( "Company Login");
            currentUser = new Company( getApplicationContext());
        }

        etEmail.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {
                // Empty method
            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count) {
                // Only enable button if the email address is valid
                if ( currentUser.checkEmailValidity( etEmail.getText().toString())) {
                    btContinue.setEnabled( true);
                    tvEmailWarning.setVisibility( View.INVISIBLE);
                }
                else {
                    btContinue.setEnabled( false);
                    tvEmailWarning.setVisibility( View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged( Editable s) {
                // Empty method
            }
        });

        btContinue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // Check network connection
                if ( !currentUser.isConnectedToInternet()) {
                    Toast.makeText( getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Hide login layout and show loading layout
                    btContinue.setEnabled( false);
                    etEmail.setEnabled( false);
                    layoutEnterEmail.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                    layoutLoading.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                    currentUser.login( etEmail.getText().toString(), new User.EmailListener() {
                        @Override
                        public void onEmailSent( String email, boolean isSent) {
                            if ( !isSent) {
                                // Show login layout again and show error message
                                layoutLoading.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                layoutEnterEmail.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                btContinue.setEnabled( true);
                                etEmail.setEnabled( true);

                                Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // Show email sent layout
                                tvDescriptionEmailSent.setText( "We just sent a verification link to your email address, " + email + ". Click on the link in the email to complete the verification process.");
                                layoutLoading.animate().alpha( 0).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();
                                layoutEmailSent.animate().alpha( 1).setDuration( 250).setInterpolator( new DecelerateInterpolator()).start();

                                // Save temp login type to local storage
                                preferences.edit().putInt( TEMP_LOGGED_IN_USER_TYPE, loginType).apply();
                            }
                        }
                    });
                }
            }
        });

        imgBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                finish();
            }
        });
    }
}