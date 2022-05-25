package com.fliers.trainly.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fliers.trainly.R;
import com.fliers.trainly.models.trips.Place;
import com.fliers.trainly.models.trips.Places;
import com.fliers.trainly.models.users.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.Calendar;

/**
 * Controls customer homepage.
 * @author Ali Emir Güzey
 * @version 03.05.2021
 */
public class CustomerHomeActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final LatLng NORTHEASTERN_BOUND = new LatLng( 42.632028, 45.675553);
    private static final LatLng SOUTHWESTERN_BOUND = new LatLng( 34.879173, 24.824881);

    private GoogleMap mMap;
    private Spinner departureSpinner;
    private Spinner arrivalSpinner;
    private Marker prevDepMarker;
    private Marker prevArrMarker;
    private int mYear, mMonth, mDay;
    private Places places;
    private TextView txtDate;
    private DrawerLayout drawer;

    /**
     * Manipulates the activity once created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        departureSpinner = (Spinner)findViewById(R.id.departureSpinner);
        arrivalSpinner = (Spinner)findViewById(R.id.arrivalSpinner);
        txtDate = (TextView) findViewById(R.id.textView28);
        Button search = findViewById(R.id.buttonSearch);

        User currentUser = User.getCurrentUserInstance();
        drawer = findViewById( R.id.drawerLayoutCustomer);
        NavigationView navView = findViewById( R.id.navViewCustomer);
        View headerView = navView.getHeaderView( 0);
        TextView tvTextNavHeader = headerView.findViewById( R.id.tvTextNavHeader);
        TextView tvSubTextNavHeader = headerView.findViewById( R.id.tvSubTextNavHeader);

        tvTextNavHeader.setText( currentUser.getName());
        tvSubTextNavHeader.setText( "Customer");
        navView.setNavigationItemSelectedListener( this);
        navView.getMenu().getItem( 0).setChecked( true);

        ImageView drawerButton = findViewById( R.id.drawerButtonCustomer);
        drawerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                drawer.openDrawer( Gravity.LEFT);
            }
        });


        places = Places.getInstance();

        ArrayList<String> names = new ArrayList<>();
        names.add("Select");
        for (Place p : places.getAll()) {
            names.add(p.getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_places, names){
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

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_places);
        departureSpinner.setAdapter(spinnerArrayAdapter);
        arrivalSpinner.setAdapter(spinnerArrayAdapter);

        departureSpinner.setOnItemSelectedListener(this);
        arrivalSpinner.setOnItemSelectedListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            /**
             * Shows error if necessary, navigates to Tickets Activity otherwise.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                if ( !departureSpinner.getSelectedItem().equals("Select") && !arrivalSpinner.getSelectedItem().equals("Select")
                    && !txtDate.getText().equals("Click to add date")  && !txtDate.getText().equals("You need to add date")) {
                    goToTicketsActivity();
                }
                else {
                    if (departureSpinner.getSelectedItem().equals("Select")) {
                        TextView errorText = (TextView) departureSpinner.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                    }
                    if (arrivalSpinner.getSelectedItem().equals("Select")) {
                        TextView errorText = (TextView) arrivalSpinner.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                    }
                    if (txtDate.getText().equals("Click to add date") || txtDate.getText().equals("You need to add date")) {
                        txtDate.setTextColor(Color.RED);
                        txtDate.setText("You need to add date");
                    }
                }
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            /**
             * Opens date selector on click.
             * @param view view
             */
            @Override
            public void onClick( View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerHomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            /**
                             * Changes date select text accordingly.
                             * @param view view
                             * @param year selected year
                             * @param monthOfYear selected month
                             * @param dayOfMonth selected day
                             */
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if ( dayOfMonth < 10 || monthOfYear < 9) {
                                    if (dayOfMonth < 10 && monthOfYear < 9) {
                                        txtDate.setText("0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                                    }
                                    else if (dayOfMonth >= 10) {
                                        txtDate.setText(dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                                    }
                                    else {
                                        txtDate.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }
                                else {
                                    txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                                txtDate.setTextColor(Color.BLACK);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setLatLngBoundsForCameraTarget( new LatLngBounds( SOUTHWESTERN_BOUND, NORTHEASTERN_BOUND));
        mMap.moveCamera(CameraUpdateFactory.newLatLng( new LatLng(39.93677, 32.84647)));
        mMap.setMinZoomPreference( 4);
    }

    /**
     * Listens to and controls spinners.
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if ( parent == departureSpinner) {
            if (!parent.getItemAtPosition(pos).toString().equals("Select")) {
                if (prevDepMarker != null)
                    prevDepMarker.remove();
                Place place = places.findByName(parent.getItemAtPosition(pos).toString());
                // Add a marker and move the camera
                LatLng location = new LatLng(place.getLatitude(), place.getLongitude());
                prevDepMarker = mMap.addMarker(
                        new MarkerOptions().position(location).title("Departure: " + parent.getItemAtPosition(pos).toString()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
        else if ( parent == arrivalSpinner) {
            if (!parent.getItemAtPosition(pos).toString().equals("Select")) {
                if (prevArrMarker != null)
                    prevArrMarker.remove();
                Place place = places.findByName(parent.getItemAtPosition(pos).toString());
                // Add a marker and move the camera
                LatLng location = new LatLng(place.getLatitude(), place.getLongitude());
                prevArrMarker = mMap.addMarker(
                        new MarkerOptions().position(location).title("Arrival: " + parent.getItemAtPosition(pos).toString()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Navigates to Tickets activity when searched for tickets.
     */
    private void goToTicketsActivity() {
        Intent switchActivityIntent = new Intent(this, TicketsActivity.class);
        switchActivityIntent.putExtra("departure", departureSpinner.getSelectedItem().toString());
        switchActivityIntent.putExtra( "arrival", arrivalSpinner.getSelectedItem().toString());
        switchActivityIntent.putExtra( "date", txtDate.getText());
        startActivity(switchActivityIntent);
    }

    /**
     * Starts intent on drawer item select
     * @param item selected drawer item
     * @return
     * @author Alp Afyonluoğlu
     */
    @Override
    public boolean onNavigationItemSelected( @NonNull MenuItem item) {
        // Variables
        Intent intent;
        User currentUser;

        // Code
        if ( item.getItemId() == R.id.navAccountCustomer) {
            intent = new Intent( getApplicationContext(), CustomerAccountActivity.class);
            startActivity( intent);
        }
        else if ( item.getItemId() == R.id.navLogOutCustomer) {
            currentUser = User.getCurrentUserInstance();
            currentUser.logout();

            intent = new Intent( getApplicationContext(), SplashActivity.class);
            startActivity( intent);
            finish();
        }

        drawer.closeDrawer(Gravity.LEFT);
        return true;
    }
}
