package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Customer;
import com.fliers.trainly.models.users.User;

/**
 * Controls Customer Account page.
 * @author Ali Emir Güzey
 * @version 03.05.2021
 */
public class CustomerAccountActivity extends AppCompatActivity {

    Customer currentUser;

    /**
     * Manipulates view once available
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);

        currentUser = (Customer) Customer.getCurrentUserInstance();
        ImageView btBack = findViewById(R.id.imageView2);
        TextView tvAccountName = findViewById(R.id.tvNameCustomer);
        TextView tvDiscountPoints = findViewById(R.id.tvDiscountPoints);
        TextView tvEmailValue = findViewById(R.id.tvEmailValueCustomer);
        EditText etChangeName = findViewById(R.id.etChangeNameCustomer);
        Button btChangeName = findViewById(R.id.btChangeName);
        Button btGoToHistory = findViewById(R.id.btGoToHistory);

        btBack.setOnClickListener( new View.OnClickListener() {
            /**
             * Returns to previous page on click.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btChangeName.setOnClickListener(new View.OnClickListener() {
            /**
             * Sets customer name to entered value on click.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                if ( currentUser.isConnectedToInternet()) {
                    currentUser.setName(etChangeName.getText().toString());
                    currentUser.saveToServer(new User.ServerSyncListener() {
                        @Override
                        public void onSync(boolean isSynced) {
                            if (isSynced)
                                Toast.makeText( getApplicationContext(), "Changed name", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText( getApplicationContext(), "Cannot reach Trainly servers at the moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText( getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        btGoToHistory.setOnClickListener(new View.OnClickListener() {
            /**
             * Navigates to Travel History page on click.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                goToTravelHistory();
            }
        });

        tvEmailValue.setText( currentUser.getEmail());
        tvDiscountPoints.setText( "Discount points: " + String.valueOf(currentUser.getDiscountPoints()));
        tvAccountName.setText( currentUser.getName());
    }

    /**
     * Creates intent to go to travel history page.
     */
    public void goToTravelHistory() {
        Intent switchActivityIntent = new Intent(this, TravelHistoryActivity.class);
        startActivity(switchActivityIntent);
    }
}