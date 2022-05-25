package com.fliers.trainly.models.trips;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.fliers.trainly.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class to store all places in a list.
 * @author Ali Emir Güzey
 * @version 29.04.2021
 */
public class Places {
    // Properties
    protected static final String SERVER_KEY = "KEY_Tr21iwuS3obrslfL4";
    private final String PLACE_NAMES = "placeNames";
    private final String PLACE_XS = "placeXs";
    private final String PLACE_YS = "placeYs";
    private final String SEPARATOR = "<S>";

    private static Places instance = null;
    private ArrayList<Place> places;
    private Context context;
    private SharedPreferences preferences;

    // Constructor
    /**
     * Creates an empty place list.
     * @param context application context
     */
    public Places( Context context) {
        this.context = context;
        places = new ArrayList<>();

        preferences = context.getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);
    }

    // Methods
    /**
     * Finds the place with the given name and returns it.
     * @param name place name
     * @return place with the given name
     */
    public Place findByName( String name) {
        for ( Place p: places ) {
            if (p.getName().equals( name))
                return p;
        }

        return null;
    }

    /**
     * Returns the whole list.
     * @return whole place list
     */
    public ArrayList<Place> getAll() {
        return places;
    }

    /**
     * Adds the given place to the list.
     * @param p given place
     * @param listener ServerSyncListener interface that is called
     *                 when data is saved to server
     * @author Alp Afyonluoğlu
     */
    public void add( Place p, ServerSyncListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;
        ArrayList<String> placeNames;
        ArrayList<String> placeXs;
        ArrayList<String> placeYs;

        // Code
        // Only add if device is connected to the internet and
        // current list does not have a place with the new place's name
        if ( isConnectedToInternet() && findByName( p.getName()) == null) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference( SERVER_KEY + "/Places/");

            // Save to server
            reference.child( p.getName()).child( "x").setValue( String.valueOf( p.getLongitude()));
            reference.child( p.getName()).child( "y").setValue( String.valueOf( p.getLatitude()));

            // Save to list in memory
            places.add( p);

            // Save to local storage
            placeNames = stringToArrayList( preferences.getString( PLACE_NAMES, ""));
            placeXs = stringToArrayList( preferences.getString( PLACE_XS, ""));
            placeYs = stringToArrayList( preferences.getString( PLACE_YS, ""));

            placeNames.add( p.getName());
            placeXs.add( String.valueOf( p.getLongitude()));
            placeYs.add( String.valueOf( p.getLatitude()));

            preferences.edit().putString( PLACE_NAMES, arrayListToString( placeNames)).apply();
            preferences.edit().putString( PLACE_XS, arrayListToString( placeXs)).apply();
            preferences.edit().putString( PLACE_YS, arrayListToString( placeYs)).apply();

            listener.onSync( true);
        }
        else {
            listener.onSync( false);
        }
    }

    /**
     * Removes the given place from the list.
     * @param p given place
     * @author Alp Afyonluoğlu
     */
    public void remove( Place p, ServerSyncListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;
        ArrayList<String> placeNames;
        ArrayList<String> placeXs;
        ArrayList<String> placeYs;

        // Code
        // Only remove if device is connected to the internet and
        // current list has a place with the given place's name
        if ( isConnectedToInternet() && findByName( p.getName()) != null) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference( SERVER_KEY + "/Places/");

            // Remove from server
            reference.child( p.getName()).removeValue();

            // Remove from list in memory
            places.add( p);

            // Remove from local storage
            placeNames = stringToArrayList( preferences.getString( PLACE_NAMES, ""));
            placeXs = stringToArrayList( preferences.getString( PLACE_XS, ""));
            placeYs = stringToArrayList( preferences.getString( PLACE_YS, ""));

            for ( int count = 0; count < placeNames.size(); count++) {
                if ( p.getName().equals( placeNames.get( count))) {
                    placeNames.remove( count);
                    placeXs.remove( count);
                    placeYs.remove( count);
                    break;
                }
            }

            preferences.edit().putString( PLACE_NAMES, arrayListToString( placeNames)).apply();
            preferences.edit().putString( PLACE_XS, arrayListToString( placeXs)).apply();
            preferences.edit().putString( PLACE_YS, arrayListToString( placeYs)).apply();

            listener.onSync( true);
        }
        else {
            listener.onSync( false);
        }
    }

    /**
     * Updates places list in memory with server data or
     * previously saved local data depending on network connection
     * @param listener ServerSyncListener interface that is called
     *                 when data is retrieved from server
     * @author Alp Afyonluoğlu
     */
    public void update( ServerSyncListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;
        ArrayList<String> placeNames;
        ArrayList<String> placeXs;
        ArrayList<String> placeYs;
        Place place;

        // Code
        if ( isConnectedToInternet()) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference( SERVER_KEY + "/Places/");

            reference.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    // Variables
                    HashMap<String, String> coordinates;
                    String placeName;
                    ArrayList<String> retrievedPlaceNames;
                    ArrayList<String> retrievedPlaceXs;
                    ArrayList<String> retrievedPlaceYs;
                    Place retrievedPlace;

                    // Code
                    reference.removeEventListener( this);

                    // Check if username entry exists on server or not
                    places = new ArrayList<>();
                    retrievedPlaceNames = new ArrayList<>();
                    retrievedPlaceXs = new ArrayList<>();
                    retrievedPlaceYs = new ArrayList<>();
                    if ( dataSnapshot.exists()) {
                        // Get all places from server one by one
                        for ( DataSnapshot placeInfo : dataSnapshot.getChildren()) {
                            placeName = placeInfo.getKey();
                            coordinates = (HashMap<String, String>) placeInfo.getValue();

                            // Add to list in memory
                            retrievedPlace = new Place( placeName, Double.parseDouble( coordinates.get( "y")), Double.parseDouble( coordinates.get( "x")));
                            places.add( retrievedPlace);

                            // Add to string set to save to local storage
                            retrievedPlaceNames.add( placeName);
                            retrievedPlaceXs.add( coordinates.get( "x"));
                            retrievedPlaceYs.add( coordinates.get( "y"));
                        }
                    }
                    preferences.edit().putString( PLACE_NAMES, arrayListToString( retrievedPlaceNames)).apply();
                    preferences.edit().putString( PLACE_XS, arrayListToString( retrievedPlaceXs)).apply();
                    preferences.edit().putString( PLACE_YS, arrayListToString( retrievedPlaceYs)).apply();

                    listener.onSync( true);
                }

                @Override
                public void onCancelled( DatabaseError error) {
                    // Database error occurred
                    reference.removeEventListener( this);

                    listener.onSync( false);
                }
            });
        }
        else {
            places = new ArrayList<>();

            placeNames = stringToArrayList( preferences.getString( PLACE_NAMES, ""));
            placeXs = stringToArrayList( preferences.getString( PLACE_XS, ""));
            placeYs = stringToArrayList( preferences.getString( PLACE_YS, ""));
            for ( int count = 0; count < placeNames.size(); count++) {
                place = new Place( placeNames.get( count), Double.parseDouble( placeYs.get( count)), Double.parseDouble( placeXs.get( count)));
                places.add( place);
            }

            listener.onSync( true);
        }
    }

    /**
     * Getter method for static places instance
     * @return places instance
     * @author Alp Afyonluoğlu
     */
    public static Places getInstance() {
        return instance;

    }

    /**
     * Setter method for static places instance
     * @param places places instance to be set
     * @author Alp Afyonluoğlu
     */
    public static void setInstance( Places places) {
        Places.instance = places;
    }

    /**
     * Checks connection status
     * @return whether device is connected to internet or not
     * @author Alp Afyonluoğlu
     */
    private boolean isConnectedToInternet() {
        // Variables
        ConnectivityManager connectivityManager;

        // Code
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Converts array list of strings to string to be able to store the
     * array list in SharedPreferences
     * @param arrayList array list to be converted
     * @return converted string
     * @author Alp Afyonluoğlu
     */
    private String arrayListToString( ArrayList<String> arrayList) {
        //  Variables
        StringBuilder stringBuilder;

        // Code
        stringBuilder = new StringBuilder();
        for ( int count = 0; count < arrayList.size(); count++) {
            stringBuilder.append( arrayList.get( count));

            if ( count != arrayList.size() - 1) {
                stringBuilder.append( SEPARATOR);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Converts string to array list of strings to restore array list from
     * stored SharedPreferences string
     * @param string string to be converted
     * @return converted array list of strings
     * @author Alp Afyonluoğlu
     */
    private ArrayList<String> stringToArrayList( String string) {
        // Variables
        String[] array;
        ArrayList<String> arrayList;

        // Code
        arrayList = new ArrayList<>();
        if ( string.contains( SEPARATOR)) {
            array = string.split( SEPARATOR);
            return new ArrayList<>( Arrays.asList( array));
        }
        else {
            if ( !string.equals( "")) {
                arrayList.add( string);
            }
            return arrayList;
        }
    }

    public interface ServerSyncListener {
        void onSync( boolean isSynced);
    }
}
