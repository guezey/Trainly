package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.users.User;

/**
 * Controls Company Home page.
 * @author Ali Emir Güzey
 * @version 03.05.2021
 */
public class CompanyAccountActivity extends AppCompatActivity {

    Company currentUser;

    /**
     * Manipulates view once available.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_account);

        currentUser = (Company) Company.getCurrentUserInstance();
        ImageView btBack = findViewById(R.id.imageView2);
        TextView tvAccountName = findViewById(R.id.tvAccountName);
        TextView tvTrainNum = findViewById(R.id.tvTrainNum);
        TextView tvEmailValue = findViewById(R.id.tvEmailValue);
        EditText etChangeName = findViewById(R.id.etChangeName);
        Button btChangeName = findViewById(R.id.button);

        btBack.setOnClickListener(new View.OnClickListener() {
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
             * Sets company's name to entered value on click.
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

        tvEmailValue.setText( currentUser.getEmail());
        tvTrainNum.setText( "Train number: " + String.valueOf(currentUser.getTrains().size()));
        tvAccountName.setText( currentUser.getName());
    }
}