package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fliers.trainly.R;
import com.fliers.trainly.models.trips.Schedule;
import com.fliers.trainly.models.trips.Seat;
import com.fliers.trainly.models.users.Tickets;
import com.fliers.trainly.models.trips.Wagon;

import java.util.ArrayList;

public class TicketInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Schedule schedule;
    ConstraintLayout layoutWagon;
    TextView tvWagonOfSeat;
    TextView tvSeatNoOfTicketValue;
    TextView tvPriceOfTicketValue;
    ArrayList<ConstraintLayout> seatLayouts;
    ConstraintLayout letterB1;
    ConstraintLayout letterB2;
    ConstraintLayout letterB3;
    Tickets ticketManager;
    boolean isSelected;
    int wagonPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_info);

        Intent intent = getIntent();
        schedule = Schedule.getTempInstance();
        Spinner spinnerWagons = findViewById(R.id.spinnerWagon);
        TextView tvTicketTitle = findViewById(R.id.tvTicketTitle);
        tvWagonOfSeat = findViewById(R.id.tvWagonOfSeat);
        tvSeatNoOfTicketValue = findViewById(R.id.tvSeatNoOfTicketValue);
        tvPriceOfTicketValue = findViewById(R.id.tvPriceOfTicketValue);
        TextView tvLine = findViewById(R.id.tvLine);
        TextView tvDep = findViewById(R.id.tvDepSchedule);
        TextView tvArr = findViewById(R.id.tvArrSchedule);
        TextView tvPrices = findViewById(R.id.tvPricesOfSchedule);
        layoutWagon = findViewById(R.id.layoutWagon);

        ticketManager = new Tickets( getApplicationContext());
        wagonPos = 1;

        letterB1 = findViewById(R.id.letterB1);
        letterB2 = findViewById(R.id.letterB2);
        letterB3 = findViewById(R.id.letterB3);

        seatLayouts = new ArrayList<>();
        ConstraintLayout seat1 = findViewById(R.id.seat1);
        seatLayouts.add( seat1);
        ConstraintLayout seat2 = findViewById(R.id.seat2);
        seatLayouts.add( seat2);
        ConstraintLayout seat3 = findViewById(R.id.seat3);
        seatLayouts.add( seat3);
        ConstraintLayout seat4 = findViewById(R.id.seat4);
        seatLayouts.add( seat4);
        ConstraintLayout seat5 = findViewById(R.id.seat5);
        seatLayouts.add( seat5);
        ConstraintLayout seat6 = findViewById(R.id.seat6);
        seatLayouts.add( seat6);
        ConstraintLayout seat7 = findViewById(R.id.seat7);
        seatLayouts.add( seat7);
        ConstraintLayout seat8 = findViewById(R.id.seat8);
        seatLayouts.add( seat8);
        ConstraintLayout seat9 = findViewById(R.id.seat9);
        seatLayouts.add( seat9);
        ConstraintLayout seat10 = findViewById(R.id.seat10);
        seatLayouts.add( seat10);
        ConstraintLayout seat11 = findViewById(R.id.seat11);
        seatLayouts.add( seat11);
        ConstraintLayout seat12 = findViewById(R.id.seat12);
        seatLayouts.add( seat12);
        ConstraintLayout seat13 = findViewById(R.id.seat13);
        seatLayouts.add( seat13);
        ConstraintLayout seat14 = findViewById(R.id.seat14);
        seatLayouts.add( seat14);
        ConstraintLayout seat15 = findViewById(R.id.seat15);
        seatLayouts.add( seat15);
        ConstraintLayout seat16 = findViewById(R.id.seat16);
        seatLayouts.add( seat16);
        ConstraintLayout seat17 = findViewById(R.id.seat17);
        seatLayouts.add( seat17);
        ConstraintLayout seat18 = findViewById(R.id.seat18);
        seatLayouts.add( seat18);
        ConstraintLayout seat19 = findViewById(R.id.seat19);
        seatLayouts.add( seat19);
        ConstraintLayout seat20 = findViewById(R.id.seat20);
        seatLayouts.add( seat20);
        ConstraintLayout seat21 = findViewById(R.id.seat21);
        seatLayouts.add( seat21);
        ConstraintLayout seat22 = findViewById(R.id.seat22);
        seatLayouts.add( seat22);
        ConstraintLayout seat23 = findViewById(R.id.seat23);
        seatLayouts.add( seat23);
        ConstraintLayout seat24 = findViewById(R.id.seat24);
        seatLayouts.add( seat24);
        ConstraintLayout seat25 = findViewById(R.id.seat25);
        seatLayouts.add( seat25);
        ConstraintLayout seat26 = findViewById(R.id.seat26);
        seatLayouts.add( seat26);
        ConstraintLayout seat27 = findViewById(R.id.seat27);
        seatLayouts.add( seat27);
        ConstraintLayout seat28 = findViewById(R.id.seat28);
        seatLayouts.add( seat28);
        ConstraintLayout seat29 = findViewById(R.id.seat29);
        seatLayouts.add( seat29);
        ConstraintLayout seat30 = findViewById(R.id.seat30);
        seatLayouts.add( seat30);
        ConstraintLayout seat31 = findViewById(R.id.seat31);
        seatLayouts.add( seat31);
        ConstraintLayout seat32 = findViewById(R.id.seat32);
        seatLayouts.add( seat32);
        ConstraintLayout seat33 = findViewById(R.id.seat33);
        seatLayouts.add( seat33);
        ConstraintLayout seat34 = findViewById(R.id.seat34);
        seatLayouts.add( seat34);
        ConstraintLayout seat35 = findViewById(R.id.seat35);
        seatLayouts.add( seat35);
        ConstraintLayout seat36 = findViewById(R.id.seat36);
        seatLayouts.add( seat36);
        ConstraintLayout seat37 = findViewById(R.id.seat37);
        seatLayouts.add( seat37);
        ConstraintLayout seat38 = findViewById(R.id.seat38);
        seatLayouts.add( seat38);
        ConstraintLayout seat39 = findViewById(R.id.seat39);
        seatLayouts.add( seat39);
        ConstraintLayout seat40 = findViewById(R.id.seat40);
        seatLayouts.add( seat40);
        ConstraintLayout seat41 = findViewById(R.id.seat41);
        seatLayouts.add( seat41);
        ConstraintLayout seat42 = findViewById(R.id.seat42);
        seatLayouts.add( seat42);
        ConstraintLayout seat43 = findViewById(R.id.seat43);
        seatLayouts.add( seat43);
        ConstraintLayout seat44 = findViewById(R.id.seat44);
        seatLayouts.add( seat44);

        tvTicketTitle.setText( schedule.getLinkedTrain().getLinkedCompany().getName());
        tvLine.setText( schedule.getLine().toString());
        tvDep.setText("Departure: " + Schedule.calendarToString( schedule.getDepartureDate()));
        tvArr.setText("Arrival       " + Schedule.calendarToString( schedule.getArrivalDate()));
        tvPrices.setText("*Economy class wagon seats: $" + String.valueOf( schedule.getEconomyPrice())
                + "\n*Business class wagon seats: $" + String.valueOf( schedule.getBusinessPrice()));
        tvWagonOfSeat.setText( "Wagon: ");
        tvSeatNoOfTicketValue.setText( "");
        tvPriceOfTicketValue.setText( "");

        isSelected = false;
        for ( int i = 0; i < seatLayouts.size(); i++) {
            final int count = i;
            seatLayouts.get( i).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( !isSelected) {
                        seatLayouts.get( count).setBackground( getResources().getDrawable( R.drawable.outline_rounded_corner_so));
                        isSelected = true;

                        Seat seat = schedule.getWagon( wagonPos).getSeats().get( count);
                        tvSeatNoOfTicketValue.setText( seat.getSeatNumber());

                        double price = schedule.getEconomyPrice();
                        if ( schedule.getWagon( wagonPos).isBusiness()) {
                            price = schedule.getBusinessPrice();
                        }

                        tvPriceOfTicketValue.setText( price + "$");

                    }
                }
            });
        }

        ArrayList<Wagon> wagons = schedule.getWagons();
        ArrayList<String> spinnerItems = new ArrayList<>();

        String item;
        for ( int i = 0; i < wagons.size(); i++ ) {
            item = "Wagon " + String.valueOf(i + 1);
            if (wagons.get(i).isBusiness())
                item = item + " (Business class)";
            else
                item = item + " (Economy class)";
            spinnerItems.add( item);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_wagons, spinnerItems){
            /**
             * Shows spinner items.
             * @param position
             * @param convertView
             * @param parent
             * @return
             */
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {

                return super.getDropDownView(position, convertView, parent);
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_wagons);
        spinnerWagons.setAdapter(spinnerArrayAdapter);

        spinnerWagons.setOnItemSelectedListener( this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        try {
            tvWagonOfSeat.setText( "Wagon: " + ( pos + 1));
            ArrayList<Seat> seats = schedule.getWagon( pos).getSeats();
            wagonPos = pos;

            // Fill seats
            for ( int count = 0; count < seats.size(); count++) {
                if ( ticketManager.isSeatEmpty( seats.get( count))) {
                    seatLayouts.get( count).setBackground( getResources().getDrawable( R.drawable.outline_rounded_corner_sf));
                }
                else {
                    seatLayouts.get( count).setBackground( getResources().getDrawable( R.drawable.outline_rounded_corner_s));
                }
            }

            if ( pos < schedule.getLinkedTrain().getBusinessWagonNum()) {
                // Business

                for ( int count = 32; count < seatLayouts.size(); count++) {
                    seatLayouts.get( count).setVisibility( View.GONE);
                }
                letterB1.setVisibility( View.GONE);
                letterB2.setVisibility( View.GONE);
                letterB3.setVisibility( View.GONE);
            }
            else {
                // Economy

                for ( int count = 32; count < seatLayouts.size(); count++) {
                    seatLayouts.get( count).setVisibility( View.VISIBLE);
                }
                letterB1.setVisibility( View.VISIBLE);
                letterB2.setVisibility( View.VISIBLE);
                letterB3.setVisibility( View.VISIBLE);
            }
        }
        catch (Exception e) {
            Log.e( "APP_DEBUG", e.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}