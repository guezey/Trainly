package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Customer;
import com.fliers.trainly.models.trips.Schedule;
import com.fliers.trainly.models.trips.Ticket;
import com.fliers.trainly.models.users.Tickets;
import com.fliers.trainly.models.users.User;

import java.util.ArrayList;

/**
 * Controls Travel History page.
 * @author Ali Emir Güzey
 * @version 03.05.2021
 */
public class TravelHistoryActivity extends AppCompatActivity {

    Customer currentUser;
    ArrayList<Ticket> tickets;

    /**
     * Manipulates view once available.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history);
        ListView history = findViewById(R.id.history);
        ImageView btBack = findViewById(R.id.btBack);
        Button saveButton = findViewById(R.id.button);
        currentUser = (Customer) Customer.getCurrentUserInstance();
        Tickets ticketManager = new Tickets( getApplicationContext());
        tickets = ticketManager.getBoughtTickets(currentUser);

        saveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Saves all feedback to server.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                if ( currentUser.isConnectedToInternet()) {
                    ticketManager.saveFeedback( tickets, currentUser, new Tickets.ServerSyncListener() {
                        @Override
                        public void onSync(boolean isSynced) {
                            if ( isSynced)
                                Toast.makeText( getApplicationContext(), "Saved feedback", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText( getApplicationContext(), "Cannot reach Trainly servers at the moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText( getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

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



        if ( tickets.size() == 0) {
            Toast.makeText( getApplicationContext(), "No tickets found", Toast.LENGTH_SHORT).show();
        }
        else {
            TravelHistoryActivity.CustomAdaptor customAdaptor = new TravelHistoryActivity.CustomAdaptor();
            history.setAdapter( customAdaptor);
        }
    }

    /**
     * Controls list items.
     */
    class CustomAdaptor extends BaseAdapter {
        @Override
        public int getCount() {
            return tickets.size();
        }

        @Override
        public Object getItem( int position) {
            return null;
        }

        @Override
        public long getItemId( int position) {
            return 0;
        }

        /**
         * Manipulates list view.
         * @param position position in list
         * @param convertView convertView
         * @param parent parent
         * @return view
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate( R.layout.list_item_travel_history, null);
            Ticket ticket = tickets.get(position);
            Schedule schedule = ticket.getSeat().getLinkedWagon().getLinkedSchedule();

            // Get view
            TextView tvTicketTitle = view.findViewById(R.id.tvTicketTitle);
            TextView tvDirection = view.findViewById(R.id.tvDirection);
            TextView tvDepTime = view.findViewById(R.id.tvDepTime);
            TextView tvArrTime = view.findViewById(R.id.tvArrTime);
            TextView tvWagonNo = view.findViewById(R.id.tvWagonNo);
            TextView tvSeatNoValue = view.findViewById(R.id.tvSeatNoValue);
            TextView tvPriceValue = view.findViewById(R.id.tvPriceValue);
            ImageView imgStar1 = view.findViewById(R.id.imgStar1);
            ImageView imgStar2 = view.findViewById(R.id.imgStar2);
            ImageView imgStar3 = view.findViewById(R.id.imgStar3);
            ImageView imgStar4 = view.findViewById(R.id.imgStar4);
            ImageView imgStar5 = view.findViewById(R.id.imgStar5);
            EditText etFeedback = view.findViewById(R.id.etFeedback);
            ImageView[] stars = new ImageView[] {imgStar1,imgStar2,imgStar3,imgStar4,imgStar5};

            //Manipulate view
            tvTicketTitle.setText( schedule.getLinkedTrain().getLinkedCompany().getName());
            tvDirection.setText( schedule.getLine().toString());
            tvDepTime.setText( "Departure: " + schedule.getIdRepresentation(schedule.getDepartureDate()).substring(6,8) + "/"
                            + schedule.getIdRepresentation(schedule.getDepartureDate()).substring(4,6) + "/"
                            + schedule.getIdRepresentation(schedule.getDepartureDate()).substring(0,3) + "   "
                            + schedule.getIdRepresentation(schedule.getDepartureDate()).substring(8,10) + ":"
                            + schedule.getIdRepresentation(schedule.getDepartureDate()).substring(10));
            tvArrTime.setText( "Arrival:       " + schedule.getIdRepresentation(schedule.getArrivalDate()).substring(6,8) + "/"
                    + schedule.getIdRepresentation(schedule.getArrivalDate()).substring(4,6) + "/"
                    + schedule.getIdRepresentation(schedule.getArrivalDate()).substring(0,4) + "   "
                    + schedule.getIdRepresentation(schedule.getArrivalDate()).substring(8,10) + ":"
                    + schedule.getIdRepresentation(schedule.getArrivalDate()).substring(10));
            tvWagonNo.setText( "Wagon no: " + String.valueOf(ticket.getSeat().getLinkedWagon().getWagonNumber()));
            tvSeatNoValue.setText( ticket.getSeat().getSeatNumber());
            if ( ticket.getSeat().getLinkedWagon().isBusiness())
                tvPriceValue.setText( String.valueOf(schedule.getBusinessPrice()));
            else
                tvPriceValue.setText( String.valueOf(schedule.getEconomyPrice()));

            for ( int i = 0; i < ticket.getStarRating(); i++) {
                stars[i].setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
            }
            for ( int i = ticket.getStarRating(); i < 5; i++) {
                stars[i].setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
            }

            imgStar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgStar1.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar2.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar3.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar4.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar5.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    ticket.setStarRating(1);
                }
            });

            imgStar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgStar1.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar2.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar3.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar4.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar5.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    ticket.setStarRating(2);
                }
            });

            imgStar3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgStar1.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar2.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar3.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar4.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    imgStar5.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    ticket.setStarRating(3);
                }
            });

            imgStar4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgStar1.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar2.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar3.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar4.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar5.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48));
                    ticket.setStarRating(4);
                }
            });

            imgStar5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgStar1.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar2.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar3.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar4.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    imgStar5.setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48));
                    ticket.setStarRating(5);
                }
            });

            etFeedback.addTextChangedListener( new TextWatcher() {
                @Override
                public void beforeTextChanged( CharSequence s, int start, int count, int after) {
                    // Empty method
                }

                @Override
                public void onTextChanged( CharSequence s, int start, int before, int count) {
                    ticket.setComment(etFeedback.getText().toString());
                }

                @Override
                public void afterTextChanged( Editable s) {
                    // Empty method
                }
            });

            return view;
        }

    }
}