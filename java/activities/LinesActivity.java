package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.trips.Line;
import com.fliers.trainly.models.trips.Place;
import com.fliers.trainly.models.trips.Places;
import com.fliers.trainly.models.users.User;

import java.util.ArrayList;

/**
 * Controls Lines page.
 * @author Ali Emir Güzey
 * @author Erkin Aydın
 * @version 04.05.2021
 */
public class LinesActivity extends AppCompatActivity {

    Company currentUser;
    Places placeManager;
    private AutoCompleteTextView acPlace1;
    private AutoCompleteTextView acPlace2;
    private EditText etLatitude1;
    private EditText etLongitude1;
    private EditText etLatitude2;
    private EditText etLongitude2;
    private double lat1;
    private double lon1;
    private double lat2;
    private double lon2;
    private Button button;

    private ImageView back;
    ArrayList<Line> lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);

        currentUser = (Company) User.getCurrentUserInstance();
        placeManager = Places.getInstance();
        acPlace1 = findViewById(R.id.acPlace1);
        acPlace2 = findViewById(R.id.acPlace2);
        etLatitude1 = findViewById(R.id.etLatitude1);
        etLongitude1 = findViewById(R.id.etLongitude1);
        etLatitude2 = findViewById(R.id.etLatitude2);
        etLongitude2 = findViewById(R.id.etLongitude2);
        button = (Button) findViewById(R.id.button);
        back = (ImageView) findViewById(R.id.imageView2);

        ArrayList<String> placeNames = new ArrayList<>();
        for ( Place p : placeManager.getAll() ) {
            placeNames.add(p.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_lines, placeNames);

        adapter.setNotifyOnChange(true);
        acPlace1.setAdapter(adapter);
        acPlace2.setAdapter(adapter);

        back.setOnClickListener( new View.OnClickListener() {

            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener( new View.OnClickListener() {

            public void onClick(View view) {

                if( !(currentUser.isConnectedToInternet())) {

                    Toast.makeText( getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!(acPlace1.getText().toString().equals("")
                            || acPlace2.getText().toString().equals("")
                            || etLatitude1.getText().toString().equals("")
                            || etLongitude1.getText().toString().equals("")
                            || etLatitude2.getText().toString().equals("")
                            || etLongitude2.getText().toString().equals(""))) {

                        Place departurePlace;
                        Place arrivalPlace;
                        ArrayList<Place> placesToSave;
                        Line newNormalLine;
                        Line newReverseLine;
                        String nameOf1;
                        String nameOf2;

                        placesToSave = new ArrayList<>();
                        lat1 = Double.parseDouble(etLatitude1.getText().toString());
                        lon1 = Double.parseDouble(etLongitude1.getText().toString());
                        lat2 = Double.parseDouble(etLatitude2.getText().toString());
                        lon2 = Double.parseDouble(etLongitude2.getText().toString());
                        nameOf1 = acPlace1.getText().toString().substring(0,1).toUpperCase() + acPlace1.getText().toString().substring(1).toLowerCase();
                        nameOf2 = acPlace2.getText().toString().substring(0,1).toUpperCase() + acPlace2.getText().toString().substring(1).toLowerCase();

                        if ( etLatitude1.isEnabled()) {
                            departurePlace = new Place(nameOf1, lat1, lon1);
                            placesToSave.add(departurePlace);
                        }
                        else
                            departurePlace = placeManager.findByName(nameOf1);

                        if ( etLatitude2.isEnabled()) {
                            arrivalPlace = new Place(nameOf2, lat2, lon2);
                            placesToSave.add(arrivalPlace);
                        }
                        else
                            arrivalPlace = placeManager.findByName(nameOf2);

                        newNormalLine = new Line( departurePlace, arrivalPlace);
                        newReverseLine = new Line( arrivalPlace, departurePlace);

                        currentUser.addLine( newNormalLine);
                        currentUser.addLine( newReverseLine);

                        savePlace( placesToSave);

                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Please check your inputs", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        acPlace1.addTextChangedListener( new TextWatcher() {

            String name;

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {
                // Empty method
            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count) {

                if ( acPlace1.getText().toString().length() != 0) {
                    name = acPlace1.getText().toString().substring(0,1).toUpperCase() + acPlace1.getText().toString().substring(1).toLowerCase();
                    if ( placeManager.findByName(name) != null) {
                        etLatitude1.setEnabled(false);
                        etLatitude1.setText(String.valueOf(placeManager.findByName(name).getLatitude()));
                        etLongitude1.setEnabled(false);
                        etLongitude1.setText(String.valueOf(placeManager.findByName(name).getLongitude()));
                    }
                    else {
                        etLatitude1.setEnabled( true);
                        etLatitude1.setText( "");
                        etLongitude1.setEnabled( true);
                        etLongitude1.setText( "");
                    }
                }
            }

            @Override
            public void afterTextChanged( Editable s) {
                // Empty method
            }
        });

        acPlace2.addTextChangedListener( new TextWatcher() {

            String name;

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {
                // Empty method
            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count) {
                if ( acPlace2.getText().toString().length() != 0) {
                    name = acPlace2.getText().toString().substring(0,1).toUpperCase() + acPlace2.getText().toString().substring(1).toLowerCase();
                    if ( placeManager.findByName(name) != null) {
                        etLatitude2.setEnabled(false);
                        etLatitude2.setText(String.valueOf(placeManager.findByName(name).getLatitude()));
                        etLongitude2.setEnabled(false);
                        etLongitude2.setText(String.valueOf(placeManager.findByName(name).getLongitude()));
                    }
                    else {
                        etLatitude2.setEnabled( true);
                        etLatitude2.setText( "");
                        etLongitude2.setEnabled( true);
                        etLongitude2.setText( "");
                    }
                }
            }

            @Override
            public void afterTextChanged( Editable s) {
                // Empty method
            }
        });

        ListView listLines = findViewById( R.id.listLines );
        lines = currentUser.getLines();

        if ( lines.size() == 0 ) {
            Toast.makeText( getApplicationContext(), "No lines found", Toast.LENGTH_SHORT).show();
        }
        else {
            LinesActivity.CustomAdaptor customAdaptor = new LinesActivity.CustomAdaptor();
            listLines.setAdapter( customAdaptor );
        }

    }

    /**
     * Adaptor for listing the line cards
     * @author Cengizhan Terzioğlu
     * @version 03.05.2021
     */
    class CustomAdaptor extends BaseAdapter {
        /**
         * Getter method for the line count for a company
         * @return
         */
        @Override public int getCount() {
            return lines.size();
        }

        @Override public Object getItem( int position) {
            return null;
        }

        @Override public long getItemId( int position) {
            return 0;
        }

        /**
         * Getter method for the view of a line card
         * @param position position of the line in the company's line array list
         * @param convertView
         * @param parent
         * @return the view of a line card
         */
        @Override public View getView( final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate( R.layout.list_item_lines, null);

            // Get title text view
            TextView tvLineTitle = view.findViewById( R.id.tvLine );
            tvLineTitle.setText( currentUser.getLines().get(position).toString() );

            return view;
        }
    }

    /**
     * Saves places to server
     * @param placesToSave places to be saved to server
     * @author Alp Afyonluoğlu
     */
    private void savePlace( ArrayList<Place> placesToSave) {
        // Variables
        Place place;

        // Code
        if ( placesToSave.size() > 0) {
            // Add places one by one
            place = placesToSave.get( 0);
            placesToSave.remove( 0);
            placeManager.add( place, new Places.ServerSyncListener() {
                @Override
                public void onSync( boolean isSynced) {
                    if ( isSynced) {
                        savePlace( placesToSave);
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            // Save added line to server
            currentUser.saveToServer( new User.ServerSyncListener() {
                @Override
                public void onSync( boolean isSynced) {
                    if ( isSynced) {
                        Toast.makeText( getApplicationContext(), "New line is saved", Toast.LENGTH_SHORT).show();
                        acPlace1.setText( "");
                        acPlace2.setText( "");
                        etLatitude1.setText( "");
                        etLongitude1.setText( "");
                        etLatitude2.setText( "");
                        etLongitude2.setText( "");
                    }
                    else {
                        Toast.makeText( getApplicationContext(), "Trainly servers are unavailable at the moment", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}