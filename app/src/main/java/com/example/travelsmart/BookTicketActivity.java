package com.example.travelsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookTicketActivity extends AppCompatActivity {

    private String selectedDistrict, selectedState;                 //vars to hold the values of selected State and District
    private TextView tvStateSpinner, tvDistrictSpinner,textViewFare;             //declaring TextView to show the errors
    private Spinner stateSpinner, districtSpinner;                  //Spinners
    private ArrayAdapter<CharSequence> stateAdapter, districtAdapter;   //declare adapters for the spinners

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);

        //State Spinner Initialisation
        stateSpinner = findViewById(R.id.spinner_from_station);    //Finds a view that was identified by the android:id attribute in xml

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.from_station, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(stateAdapter);            //Set the adapter to the spinner to populate the State Spinner

        //When any item of the stateSpinner is selected
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Define City Spinner but we will populate the options through the selected state
                districtSpinner = findViewById(R.id.spinner_to_station);

                selectedState = stateSpinner.getSelectedItem().toString();      //Obtain the selected State

                int parentID = parent.getId();
                if (parentID == R.id.spinner_from_station){
                    switch (selectedState){
                        case "Select Your Station": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.default_select_from_station, R.layout.spinner_layout);
                            break;
                        case "PCMC": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.PCMC, R.layout.spinner_layout);
                            break;
                        case "Sant Tukaram Nagar": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.Sant_Tukaram_Nagar, R.layout.spinner_layout);
                            break;
                        case "Bhosari": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.Bhosari, R.layout.spinner_layout);
                            break;
                        case "Kasarwadi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.Kasarwadi, R.layout.spinner_layout);
                            break;
                        case "Phugewadi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.Phugewadi, R.layout.spinner_layout);
                            break;
                        case "Vanaz": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.Vanaz, R.layout.spinner_layout);
                            break;
                        default:  break;
                    }
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    districtSpinner.setAdapter(districtAdapter);        //Populate the list of Districts in respect of the State selected

                    //To obtain the selected District from the spinner
                    districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDistrict = districtSpinner.getSelectedItem().toString();
                            textViewFare = findViewById(R.id.textView_fare);
                            if(selectedState.equals("PCMC") && selectedDistrict.equals("Sant Tukaram Nagar")) {


                                textViewFare.setText("Fare Amount : 9.5 ₨");
                            }
                            else if(selectedState.equals("PCMC") && selectedDistrict.equals("Bhosari")) {


                                textViewFare.setText("Fare Amount : 9.5 ₨");
                            }
                            else if(selectedState.equals("PCMC") && selectedDistrict.equals("Kasarwadi")) {


                                textViewFare.setText("Fare Amount : 19.0 ₨");
                            }
                            else if(selectedState.equals("PCMC") && selectedDistrict.equals("Phugewadi")) {

                                textViewFare.setText("Fare Amount : 19.0 ₨");
                            }
                            else {
                                textViewFare.setText("Fare Amount : ");
                            }


                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        Button submitButton;                                //To display the selected State and District
        submitButton = findViewById(R.id.button_submit);
        tvStateSpinner = findViewById(R.id.textView_from_station);
        tvDistrictSpinner = findViewById(R.id.textView_to_station);

        submitButton.setOnClickListener(v -> {
            if (selectedState.equals("Select Your Station")) {
                Toast.makeText(BookTicketActivity.this, "Please select your Station from the list", Toast.LENGTH_LONG).show();
                tvStateSpinner.setError("Station is required!");      //To set error on TextView
                tvStateSpinner.requestFocus();
            } else if (selectedDistrict.equals("Select Your Station")) {
                Toast.makeText(BookTicketActivity.this, "Please select your Station from the list", Toast.LENGTH_LONG).show();
                tvDistrictSpinner.setError("Station is required!");
                tvDistrictSpinner.requestFocus();
                tvStateSpinner.setError(null);                      //To remove error from stateSpinner
            } else {
                tvStateSpinner.setError(null);
                tvDistrictSpinner.setError(null);
                Intent intent=new Intent(BookTicketActivity.this,QrGeneratorActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
