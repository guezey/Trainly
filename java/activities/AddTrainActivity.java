package com.fliers.trainly.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.trips.Train;
import com.fliers.trainly.models.users.User;

import java.util.ArrayList;

/**
* Add Train Page
* A page for Companies to add train to their collections.
* @author Erkin Aydın
* @version 03.05.2021
*/

public class AddTrainActivity extends AppCompatActivity {

    Company currentUser;
    private ConstraintLayout minus1;
    private ConstraintLayout plus1;
    private ConstraintLayout minus2;
    private ConstraintLayout plus2;
    private TextView businessCounter;
    private TextView economyCounter;
    private EditText businessPrice;
    private EditText economyPrice;
    private ImageView back;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_train);

        currentUser = (Company) User.getCurrentUserInstance();
        minus1 = (ConstraintLayout) findViewById(R.id.idl321);
        plus1 = (ConstraintLayout) findViewById(R.id.idl31);
        minus2 = (ConstraintLayout) findViewById(R.id.idl322);
        plus2 = (ConstraintLayout) findViewById(R.id.idl32);
        businessCounter = (TextView) findViewById(R.id.textView412);
        economyCounter = (TextView) findViewById(R.id.textView42);
        businessPrice = (EditText) findViewById(R.id.editTextNumber);
        economyPrice = (EditText) findViewById(R.id.editTextNumber2);
        add = (Button) findViewById(R.id.button);
        back = (ImageView) findViewById(R.id.imageView2);

        //To decrease the amount of businessWagons
        minus1.setOnClickListener( new View.OnClickListener() {

            public void onClick(View view) {

                int i = Integer.parseInt(businessCounter.getText().toString());
                if( i >= 1) {
                    i-=1;
                    businessCounter.setText( "" + i);
                }
            }
        });

        //To increase the amount of businessWagons
        plus1.setOnClickListener( new View.OnClickListener() {

            public void onClick(View view) {

                int i = Integer.parseInt(businessCounter.getText().toString());
                int j = Integer.parseInt(economyCounter.getText().toString());
                if( i + j < 30) {
                    i += 1;
                    businessCounter.setText("" + i);
                }
            }
        });

        //To decrease the amount of economyWagons
        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = Integer.parseInt(economyCounter.getText().toString());
                if( i > 0) {
                     i -= 1;
                    economyCounter.setText( "" + i);
                }
            }
        });

        //To increase the amount of economyWagons
        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = Integer.parseInt(economyCounter.getText().toString());
                int j = Integer.parseInt(businessCounter.getText().toString());
                if( i + j < 30) {
                    i += 1;
                    economyCounter.setText("" + i);
                }
            }
        });

        //To inform the Company that economyPrice is bigger than or equal to businessPrice 
        businessPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(businessPrice.getText().toString().trim().equalsIgnoreCase("")||
                        economyPrice.getText().toString().trim().equalsIgnoreCase(""))) {
                    if (Double.parseDouble(businessPrice.getText().toString()) <= Double.parseDouble(economyPrice.getText().toString())) {

                        businessPrice.setError("Are You Sure About That?");
                    }
                }
            }
        });

        //To inform the Company that economyPrice is bigger than or equal to businessPrice 
        economyPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(businessPrice.getText().toString().trim().equalsIgnoreCase("")||
                        economyPrice.getText().toString().trim().equalsIgnoreCase(""))) {
                    if (Double.parseDouble(businessPrice.getText().toString()) <= Double.parseDouble(economyPrice.getText().toString())) {

                        businessPrice.setError("Are You Sure About That?");
                    }
                }
            }
        });

        //To add the train
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int businessNum = Integer.parseInt( businessCounter.getText().toString());
                int economyNum = Integer.parseInt( economyCounter.getText().toString());
                double bPrice = Double.parseDouble( businessPrice.getText().toString());
                double ePrice = Double.parseDouble( economyPrice.getText().toString());
                ArrayList<Train> currentTrains = currentUser.getTrains();
                int biggestId = 1;
                for( Train a : currentTrains) {
                    if( biggestId < Integer.parseInt(a.getId())) {
                        biggestId = Integer.parseInt(a.getId());
                    }
                }
                biggestId++;
                StringBuilder idRoot = new StringBuilder( biggestId + "");
                String addId = "";
                for( int i = idRoot.length(); i < 3; i++) {
                    idRoot.insert(0, "0");
                }
                String id = String.valueOf(idRoot);
                Train newTrain = new Train( currentUser, businessNum, economyNum, bPrice, ePrice, id);
                currentUser.addTrain( newTrain);

                add.setEnabled( false);
                currentUser.saveToServer( new User.ServerSyncListener() {
                    @Override
                    public void onSync( boolean isSynced) {
                        if ( isSynced) {
                            Toast.makeText( getApplicationContext(), "New train is saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                            add.setEnabled( true);
                        }
                    }
                });
            }
        });

        //To go to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
