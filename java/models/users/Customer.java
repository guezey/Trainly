package com.fliers.trainly.models.users;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Customer Object 
 * @author Cengizhan Terzioğlu
 * @version 22.04.2021
*/
public class Customer extends User {
    // Properties
    private final String POINTS = "points";

    private int discountPoints;

    // Constructors
    /**
     * Initializes a new customer
     * @param context application context
     */
    public Customer( Context context) {
        super( context);
        setDiscountPoints( 0 );
    }

    /**
     * Initializes a new customer with given id
     * @param id customer id
     * @param context application context
     */
    public Customer( String id, Context context) {
        super( context);
        this.id = id;
        setDiscountPoints( 0 );
    }

    // Methods

    /**
     * Getter method for discount points
     * @return how many discount points that customer has
     */
    public int getDiscountPoints() {
        return this.discountPoints;
    }

    /**
     * Set method for discount points
     * @param discountPoints new value for discount points
     */
    public void setDiscountPoints( int discountPoints ) {
        this.discountPoints = discountPoints;
    }

    /**
     * Logs in the current user account
     * @param update whether user data should be updated with the
     *               server data or not
     * @param listener ServerSyncListener interface that is called
     *                 when data is retrieved from server or loaded
     *                 from local storage
     * @author Alp Afyonluoğlu
     */
    @Override
    public void getCurrentUser( boolean update, ServerSyncListener listener) {
        super.getCurrentUser( update, new ServerSyncListener() {
            @Override
            public void onSync( boolean isSynced) {
                if ( isSynced && !update) {
                    discountPoints = preferences.getInt( POINTS, 0);
                }

                listener.onSync( isSynced);
            }
        });
    }

    /**
     * Saves user data to local storage
     */
    @Override
    protected void saveToLocalStorage() {
        super.saveToLocalStorage();
        preferences.edit().putInt( POINTS, discountPoints).apply();
    }

    /**
     * Saves local user data to server
     * @param listener ServerSyncListener interface that is called
     *                 when data is sent to server
     * @author Alp Afyonluoğlu
     */
    @Override
    public void saveToServer( ServerSyncListener listener) {
        super.saveToServer( new ServerSyncListener() {
            @Override
            public void onSync( boolean isSynced) {
                if ( isSynced) {
                    // Variables
                    FirebaseDatabase database;
                    DatabaseReference reference;

                    // Code
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference( SERVER_KEY + "/Users/" + id);

                    reference.child( POINTS).setValue( String.valueOf( discountPoints));
                    listener.onSync( true);
                }
                else {
                    listener.onSync( false);
                }
            }
        });
    }

    /**
     * Updates local user data with data retrieved from server
     * @param listener ServerSyncListener interface that is called
     *                 when data is retrieved from server
     * @author Alp Afyonluoğlu
     */
    @Override
    public void getFromServer( ServerSyncListener listener) {
        super.getFromServer( new ServerSyncListener() {
            @Override
            public void onSync( boolean isSynced) {
                if ( isSynced) {
                    // Variables
                    FirebaseDatabase database;
                    DatabaseReference reference;

                    // Code
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference( SERVER_KEY + "/Users/" + id);

                    // Retrieve data of the user with given email address from server
                    reference.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot dataSnapshot) {
                            // Variables
                            HashMap<String, String> userData;

                            // Code
                            reference.removeEventListener( this);

                            // Check if username entry exists on server or not
                            if ( dataSnapshot.exists()) {
                                userData = (HashMap<String, String>) dataSnapshot.getValue();

                                // Check whether passwords match or not
                                discountPoints = Integer.parseInt( userData.get( POINTS));

                                listener.onSync( true);
                            }
                            else {
                                listener.onSync( false);
                            }
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
                    listener.onSync( false);
                }
            }
        });
    }
}