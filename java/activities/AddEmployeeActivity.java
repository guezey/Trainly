package com.fliers.trainly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fliers.trainly.R;
import com.fliers.trainly.models.users.Company;
import com.fliers.trainly.models.trips.Employee;
import com.fliers.trainly.models.trips.Train;
import com.fliers.trainly.models.users.User;

import java.util.ArrayList;
/**
* Manage Employees Page
* A page where Companies can view all of their Employees and continue with adding new ones or deleting existing ones
* @author Erkin Aydın
* @version 03.05.2021
*/

public class AddEmployeeActivity extends AppCompatActivity {

    Company currentUser;
    private ImageView back;
    private Button add;
    private EditText employeeName;
    private Spinner trainId;
    private ArrayList<String> ids;
    private ArrayList<Train> currentTrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        currentUser = (Company) User.getCurrentUserInstance();
        back = (ImageView) findViewById(R.id.imageView2);
        add = (Button) findViewById(R.id.button);
        employeeName = (EditText) findViewById(R.id.editTextTextPersonName);
        trainId = (Spinner) findViewById(R.id.spinner);
        ids = new ArrayList<>(0);
        currentTrains = currentUser.getTrains();

        for(Train train : currentTrains) {
            ids.add(train.getId());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_employee_spinner, ids){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {

                return super.getDropDownView(position, convertView, parent);
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.activity_employee_spinner);
        trainId.setAdapter(spinnerArrayAdapter);

        //To go to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //To add the new Employee with entered specialities
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = employeeName.getText().toString();
                String chosenTrainId = (String) trainId.getSelectedItem();
                Train assignedTrain = null;
                for(Train train : currentTrains) {
                    if( chosenTrainId.equalsIgnoreCase(train.getId().toString())) {
                        assignedTrain = train;
                        break;
                    }
                }
                Employee newEmployee = new Employee( name, assignedTrain);
                finish();
            }
        });
    }
}
