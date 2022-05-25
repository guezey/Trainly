package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Customer;
import com.fliers.trainly.models.trips.Ticket;
import com.fliers.trainly.models.users.User;

/**
 * Buy Ticket Page
 * A page where customer can buy the ticket s/he selected with his card number, its expiration date
 * and CVV
 * @author Erkin Aydın
 * @version 03.05.2021
 */
public class BuyTicketActivity extends AppCompatActivity {

    Customer currentUser;
    ImageView back;
    Button buy;
    Ticket ticket;
    TextView line;
    TextView departureDate;
    TextView arrivalDate;
    TextView wagonNo;
    TextView seatNo;
    TextView price;
    TextView availableDiscountPoints;
    EditText cardNumber;
    EditText expirationDate;
    EditText cvv;
    CheckBox discountBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        currentUser = (Customer) User.getCurrentUserInstance();
        back = (ImageView) findViewById(R.id.imageView2);
        buy = (Button) findViewById(R.id.button);
        //ticket = ...;
        departureDate = (TextView) findViewById(R.id.cap1);
        arrivalDate = (TextView) findViewById(R.id.lin1);
        wagonNo = (TextView) findViewById(R.id.lin3);
        seatNo = (TextView) findViewById(R.id.s12);
        price = (TextView) findViewById(R.id.s2);
        cardNumber = (EditText) findViewById(R.id.editTextTextEmailAddress);
        expirationDate = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        cvv = (EditText) findViewById(R.id.editTextTextEmailAddress3);
        availableDiscountPoints = (TextView) findViewById(R.id.textView40);
        discountBox = (CheckBox) findViewById(R.id.checkBox2);

//        departureDate.setText();
//        arrivalDate.setText();
//        wagonNo.setText();
//        seatNo.setText();
//        price.setText();
          availableDiscountPoints.setText("Available discount points: " + currentUser.getDiscountPoints());
//        discountBox.setText();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
            }
        });


    }
}
