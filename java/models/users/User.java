package com.fliers.trainly.models.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

import com.fliers.trainly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Abstract User class to be extended by Customer and Company classes
 * @author Alp Afyonluoğlu
 * @version 25.04.2021
 */
public abstract class User {
    // Properties
    protected static final String SERVER_KEY = "KEY_Tr21iwuS3obrslfL4";
    protected static final String NAME = "name";
    private final String EMAIL = "email";
    private final String TEMP_EMAIL = "tempEmail";
    private final String TEMP_NAME = "tempName";
    private final String DEFAULT_NAME = "DEFAULT";

    private static User currentUserInstance = null;
    protected SharedPreferences preferences;
    protected String name;
    private String email;
    protected String id;
    protected boolean isLoggedIn;
    protected boolean isNewAccount;
    protected boolean isOffline;
    private Context context;

    // Constructors
    /**
     * Initializes a new user
     */
    public User( Context context) {
        this.context = context;
        email = "";
        id = "";
        isLoggedIn = false;
        isNewAccount = true;
        setName( DEFAULT_NAME);
        isOffline = true;

        preferences = context.getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);
    }

    // Methods
    /**
     * Sends login email to the given email address
     * @param email email address of the user
     * @param listener EmailListener interface that is called
     *                 when verification email is sent
     */
    public void login( String email, EmailListener listener) {
        // Variables
        ActionCodeSettings actionCodeSettings;
        FirebaseAuth auth;

        // Code
        if ( !isLoggedIn && checkEmailValidity( email) && isConnectedToInternet()) {
            actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl( "https://trainly-app.web.app/register")
                    .setHandleCodeInApp( true)
                    .setAndroidPackageName( "com.fliers.trainly", false, "19")
                    .build();

            auth = FirebaseAuth.getInstance();
            auth.sendSignInLinkToEmail( email, actionCodeSettings)
                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( @NonNull Task<Void> task) {
                            if ( task.isSuccessful()) {
                                preferences.edit().putString( TEMP_EMAIL, email).apply();
                                preferences.edit().putString( TEMP_NAME, name).apply();

                                listener.onEmailSent( email, true);
                            }
                            else {
                                listener.onEmailSent( email, false);
                            }
                        }
                    });
        }
        else {
            listener.onEmailSent( email, false);
        }
    }

    /**
     * Completes login process by using the link that is sent via email
     * @param emailLink link sent to the email address of the user
     * @param listener LoginListener interface that is called
     *                 when email link is verified
     */
    public void completeLogin( String emailLink, LoginListener listener) {
        // Variables
        FirebaseAuth auth;
        String email;

        // Code
        name = preferences.getString( TEMP_NAME, DEFAULT_NAME);
        email = preferences.getString( TEMP_EMAIL, "");

        auth = FirebaseAuth.getInstance();
        if ( auth.isSignInWithEmailLink( emailLink) && isConnectedToInternet()) {
            auth.signInWithEmailLink( email, emailLink).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( @NonNull Task<AuthResult> task) {
                            if ( task.isSuccessful()) {
                                // Email link verified
                                preferences.edit().putString( EMAIL, email).apply();
                                User.this.email = email;
                                id = email.replace( "@", "_at_");
                                id = id.replace( ".", "_dot_");

                                // Check whether user is registering a new account or logging in to an existing account
                                checkEmailAvailability( email, new EmailAvailabilityCheckListener() {
                                    @Override
                                    public void onEmailAvailabilityCheck(String email, boolean isAvailable) {
                                        isLoggedIn = true;
                                        isNewAccount = isAvailable;

                                        onLoginEmailVerified( listener);
                                    }
                                });
                            }
                            else {
                                // Email link could not be verified
                                listener.onLogin( false);
                            }
                        }
                    });
        }
        else {
            listener.onLogin( false);
        }
    }

    /**
     * Overridable method to be modified by sub classes to assign ids, if
     * required, before server sync
     * @param listener LoginListener interface of completeLogin method to
     *                 be called when server sync is completed
     */
    protected void onLoginEmailVerified( LoginListener listener) {
        if ( isNewAccount) {
            // Register

            // Save user data to server
            saveToServer( new ServerSyncListener() {
                @Override
                public void onSync( boolean isSynced) {
                    isLoggedIn = isSynced;
                    listener.onLogin( isLoggedIn);
                }
            });
        }
        else {
            // Log in

            // Retrieve user data from server
            getFromServer( new ServerSyncListener() {
                @Override
                public void onSync( boolean isSynced) {
                    isLoggedIn = isSynced;
                    listener.onLogin( isLoggedIn);
                }
            });
        }
    }

    /**
     * Getter method for static current user instance
     * @return current user instance
     */
    public static User getCurrentUserInstance() {
        return currentUserInstance;

    }

    /**
     * Setter method for static current user instance
     * @param user current user instance to be set
     */
    public static void setCurrentUserInstance( User user) {
        User.currentUserInstance = user;
    }

    /**
     * Gets name of current user directly from SharedPreferences
     * @param context application context
     * @return name of current user
     */
    public static String getCurrentUserName( Context context) {
        // Variables
        SharedPreferences preferences;

        // Code
        preferences = context.getSharedPreferences( String.valueOf( R.string.app_name), Context.MODE_PRIVATE);
        return preferences.getString( NAME, "");
    }

    /**
     * Logs in the current user account
     * @param update whether user data should be updated with the
     *               server data or not
     * @param listener ServerSyncListener interface that is called
     *                 when data is retrieved from server or loaded
     *                 from local storage
     */
    public void getCurrentUser( boolean update, ServerSyncListener listener) {
        if ( !preferences.getString( EMAIL, "").equals( "")) {
            email = preferences.getString( EMAIL, "");
            id = email.replace( "@", "_at_");
            id = id.replace( ".", "_dot_");
            isLoggedIn = true;
            isNewAccount = false;

            if ( update) {
                getFromServer( new ServerSyncListener() {
                    @Override
                    public void onSync( boolean isSynced) {
                        isOffline = !isSynced;
                        listener.onSync( isSynced);
                    }
                });
            }
            else {
                isOffline = true;
                name = preferences.getString( NAME, DEFAULT_NAME);
                listener.onSync( true);
            }
        }
        else {
            listener.onSync( false);
        }
    }

    /**
     * Checks whether email is registered before or not
     * @param email email address to check
     * @param listener EmailAvailabilityCheckListener interface that is called
     *                 when data is retrieved from server
     */
    public void checkEmailAvailability( String email, EmailAvailabilityCheckListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;
        String id;

        // Code
        if ( isConnectedToInternet()) {
            id = email.replace( "@", "_at_");
            id = id.replace( ".", "_dot_");

            database = FirebaseDatabase.getInstance();
            reference = database.getReference( SERVER_KEY + "/Users/" + id);

            // Retrieve data of user with given email address from server
            reference.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    reference.removeEventListener( this);

                    // Check if email entry exists on server or not
                    if ( dataSnapshot.exists()) {
                        listener.onEmailAvailabilityCheck( email, false);
                    }
                    else {
                        listener.onEmailAvailabilityCheck( email, true);
                    }
                }

                @Override
                public void onCancelled( DatabaseError error) {
                    // Database error occurred
                    reference.removeEventListener( this);

                    listener.onEmailAvailabilityCheck( email, false);
                }
            });
        }
        else {
            listener.onEmailAvailabilityCheck( email, false);
        }
    }

    /**
     * Saves user data to local storage
     */
    protected void saveToLocalStorage() {
        preferences.edit().putString( NAME, name).apply();
    }

    /**
     * Saves local user data to server
     * @param listener ServerSyncListener interface that is called
     *                 when data is sent to server
     */
    public void saveToServer( ServerSyncListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;

        // Code
        if ( isLoggedIn && isConnectedToInternet()) {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference( SERVER_KEY + "/Users/" + id);

            reference.child( NAME).setValue( name);
            saveToLocalStorage();
            listener.onSync( true);
        }
        else {
            listener.onSync( false);
        }
    }

    /**
     * Updates local user data with data retrieved from server
     * @param listener ServerSyncListener interface that is called
     *                 when data is retrieved from server
     */
    public void getFromServer( ServerSyncListener listener) {
        // Variables
        FirebaseDatabase database;
        DatabaseReference reference;

        // Code
        if ( isLoggedIn && isConnectedToInternet()) {
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
                        name = userData.get( NAME);

                        saveToLocalStorage();
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

    /**
     * Checks validity of email address
     * @param email email address to be checked
     * @return whether email address is valid or not
     */
    public boolean checkEmailValidity( String email) {
        // Variables
        String[] temp;
        String username;
        String domainName;
        String domain;

        // Code
        // Split email address into its parts
        if ( email.contains( "@")) {
            temp = email.split( "@");
            username = temp[0];

            if ( temp.length == 2) {
                if ( temp[1].contains( ".")) {
                    temp = temp[1].split( "\\.");

                    if ( temp.length == 2) {
                        domainName = temp[0];
                        domain = temp[1];

                        return 6 <= username.length() && username.length() <= 30 && 2 <= domainName.length() && 2 <= domain.length();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Getter method for name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter method for id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for isLoggedIn
     * @return isLoggedIn
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Getter method for isNewAccount
     * @return isNewAccount
     */
    public boolean isNewAccount() {
        return isNewAccount;
    }

    /**
     * Checks connection status
     * @return whether device is connected to internet or not
     */
    public boolean isConnectedToInternet() {
        // Variables
        ConnectivityManager connectivityManager;

        // Code
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Setter method for name
     * @param name new name
     */
    public void setName( String name) {
        this.name = name;
    }

    /**
     * Logs out user by clearing all user data
     */
    public void logout() {
        SharedPreferences.Editor editor;

        // Program code
        editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public interface EmailAvailabilityCheckListener {
        void onEmailAvailabilityCheck( String email, boolean isAvailable);
    }

    public interface EmailListener {
        void onEmailSent( String email, boolean isSent);
    }

    public interface LoginListener {
        void onLogin( boolean isLoggedIn);
    }

    public interface ServerSyncListener {
        void onSync( boolean isSynced);
    }
}
