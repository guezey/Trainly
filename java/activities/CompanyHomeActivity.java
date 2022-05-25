package com.fliers.trainly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fliers.trainly.R;
import com.fliers.trainly.models.trips.Schedule;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.trips.Ticket;
import com.fliers.trainly.models.users.Tickets;
import com.fliers.trainly.models.users.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Company Homepage Activity
 * Companies can use this page to learn statistics and go to several other pages by a menu
 * @author Cengizhan Terzioğlu
 * @version 03.05.2021
 */

public class CompanyHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Properties
    Company company;
    TextView trainNumber;
    TextView customerLastWeek;
    TextView lineNumber;
    TextView employeeNumber;
    TextView revenueLastWeek;
    TextView balance;
    TextView starPointText;
    ImageView[] starPoint;
    ArrayList<Ticket> tickets;
    private DrawerLayout drawer;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        // Get recently sold tickets of the company
        company = ( Company ) Company.getCurrentUserInstance();
        Tickets ticketManager = new Tickets( getApplicationContext() );
        tickets = ticketManager.getRecentlySoldTickets( company );

        drawer = findViewById( R.id.drawerLayoutCompany);
        NavigationView navView = findViewById( R.id.navViewCompany);
        View headerView = navView.getHeaderView( 0);
        TextView tvTextNavHeader = headerView.findViewById( R.id.tvTextNavHeader);
        TextView tvSubTextNavHeader = headerView.findViewById( R.id.tvSubTextNavHeader);

        tvTextNavHeader.setText( company.getName());
        tvSubTextNavHeader.setText( "Company");
        navView.setNavigationItemSelectedListener( this);
        navView.getMenu().getItem( 0).setChecked( true);

        ImageView drawerButton = findViewById( R.id.drawerButtonCompany);
        drawerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                drawer.openDrawer( Gravity.LEFT);
            }
        });

        // Generate the statistics on the screen
        trainNumber = findViewById( R.id.tvTrainNumber);
        customerLastWeek = findViewById( R.id.tvLastWeekCustomer);
        lineNumber = findViewById( R.id.tvLineNumber);
        employeeNumber = findViewById( R.id.tvNumberEmployees);
        revenueLastWeek = findViewById( R.id.tvAverageRevenue);
        balance = findViewById( R.id.tvBalance);
        starPointText = findViewById( R.id.tvAveragePoint);

        trainNumber.setText( company.getTrains().size() + "" );
        customerLastWeek.setText( tickets.size() + "" );
        lineNumber.setText( company.getLines().size() + "" );
        employeeNumber.setText( company.getEmployees().size() + "" );
        revenueLastWeek.setText( company.getLastWeeksRevenue() + "$" );
        balance.setText( company.getBalance() + "$" );
        starPointText.setText( "Average point: " + company.getAveragePoint() + "/5" );

        // Generate star points on the screen
        double roundAverage = Math.round( company.getAveragePoint() * 2 ) / 2;
        int starIndex = 0;
        for ( double i = roundAverage; i < 0; i-- ) {
            if ( i >= 1 )
                starPoint[starIndex].setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_48 ) );
            else if ( i == 0.5 )
                starPoint[starIndex].setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_half_48 ) );
            else
                starPoint[starIndex].setImageDrawable( getResources().getDrawable( R.drawable.ic_baseline_star_outline_48 ) );
            starIndex++;
        }

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
        if ( item.getItemId() == R.id.navTrains) {
            intent = new Intent( getApplicationContext(), TrainsActivity.class);
            startActivity( intent);
        }
        else if ( item.getItemId() == R.id.navLines) {
            intent = new Intent( getApplicationContext(), LinesActivity.class);
            startActivity( intent);
        }
        else if ( item.getItemId() == R.id.navEmployees) {
            intent = new Intent( getApplicationContext(), EmployeesActivity.class);
            startActivity( intent);
        }
        else if ( item.getItemId() == R.id.navAccountCompany) {
            intent = new Intent( getApplicationContext(), CompanyAccountActivity.class);
            startActivity( intent);
        }
        else if ( item.getItemId() == R.id.navLogOutCompany) {
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