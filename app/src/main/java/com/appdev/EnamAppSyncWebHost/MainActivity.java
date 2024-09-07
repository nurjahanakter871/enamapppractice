package com.appdev.EnamAppSyncWebHost;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private EditText etName;
    private RadioGroup rgGender;

    private Spinner spStatus;
    private EditText etDateOfBirth; // Button for selecting date of birth
    private Calendar dobCalendar; // Calendar instance for date of birth
    private SimpleDateFormat sdf; // Date format for displaying

    private EditText etDateOfDeath; // Button for selecting date of Death
    private Calendar dodCalendar; // Calendar instance for date of Death
    private SimpleDateFormat sdobdf; // Date format for displaying dod
    private Spinner spOccupation;
    private CheckBox cbHobby1;
    private CheckBox cbHobby2;
    private CheckBox cbHobby3;

    //Variable declaratin for logic to handle visibility
    private TextView tvDobLabel;
    private EditText btnSelectDob;
    private TextView tvDodLabel;
    private EditText btnSelectDod;
    private Spinner spLiveOrDeath;



    private DBHandler dbHandler;

    private Button btnSave, btnRead ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        rgGender = findViewById(R.id.rgGender);
        spStatus = findViewById(R.id.spLiveOrDeath);
        etDateOfDeath = findViewById(R.id.btnSelectDod);
        etDateOfBirth = findViewById(R.id.btnSelectDob);
        spOccupation = findViewById(R.id.spOccupation);
        cbHobby1 = findViewById(R.id.cbHobby1);
        cbHobby2 = findViewById(R.id.cbHobby2);
        cbHobby3 = findViewById(R.id.cbHobby3);

        // Initialize views
        tvDobLabel = findViewById(R.id.tvDobLabel);
        tvDodLabel = findViewById(R.id.tvDodLabel);
        btnSelectDob = findViewById(R.id.btnSelectDob);
        btnSelectDod = findViewById(R.id.btnSelectDod);



        spLiveOrDeath = findViewById(R.id.spLiveOrDeath);

        // Set initial visibility of fields
        tvDobLabel.setVisibility(View.GONE);
        btnSelectDob.setVisibility(View.GONE);
        tvDodLabel.setVisibility(View.GONE);
        btnSelectDod.setVisibility(View.GONE);



        dbHandler = new DBHandler(this);
        btnSave = findViewById(R.id.btnSave);
        btnRead= findViewById(R.id.btnRead);

       etDateOfBirth.setText("0000-00-00");
        etDateOfDeath.setText("0000-00-00");


        // Initialize the Calendar instance and Date format
        dobCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        dodCalendar = Calendar.getInstance();
        sdobdf =new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());


        etDateOfBirth.setOnClickListener(v ->
                showDatePickerDialog()
        );
        etDateOfBirth.setFocusable(false);
        etDateOfBirth.setClickable(true);


        etDateOfDeath.setOnClickListener(v ->
                showDatePickerDialog4Death()
        );
        etDateOfDeath.setFocusable(false);
        etDateOfDeath.setClickable(true);

        // Set listener for status spinner
        spLiveOrDeath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String status = adapterView.getItemAtPosition(position).toString();
                handleVisibilityBasedOnStatus(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveParticipant();

            }
        });



        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,ViewParticipantActivity.class);
                startActivity(i);
            }
        });



    }


    // BELLOW METHODS ARE INDEPENDENT -  showDatePickerDialog(), saveParticipant(), getSelectedGender(), getSelectedHobbies()
    private void showDatePickerDialog() {
        int year = dobCalendar.get(Calendar.YEAR);
        int month = dobCalendar.get(Calendar.MONTH);
        int day = dobCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    dobCalendar.set(selectedYear, monthOfYear, dayOfMonth);
                    String selectedDate = sdf.format(dobCalendar.getTime());
                    etDateOfBirth.setText(selectedDate);
                }, year, month, day);
        // Set the maximum date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showDatePickerDialog4Death() {
        int year = dodCalendar.get(Calendar.YEAR);
        int month = dodCalendar.get(Calendar.MONTH);
        int day = dodCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    dodCalendar.set(selectedYear, monthOfYear, dayOfMonth);
                    String selectedDate = sdobdf.format(dodCalendar.getTime());
                    etDateOfDeath.setText(selectedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void handleVisibilityBasedOnStatus(String status) {
        if (status.equalsIgnoreCase("Live")) {
            tvDobLabel.setVisibility(View.VISIBLE);
            btnSelectDob.setVisibility(View.VISIBLE);
            tvDodLabel.setVisibility(View.GONE);
            btnSelectDod.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase("Death")) {
            tvDobLabel.setVisibility(View.GONE);
            btnSelectDob.setVisibility(View.GONE);
            tvDodLabel.setVisibility(View.VISIBLE);
            btnSelectDod.setVisibility(View.VISIBLE);
        } else {
            tvDobLabel.setVisibility(View.GONE);
            btnSelectDob.setVisibility(View.GONE);
            tvDodLabel.setVisibility(View.GONE);
            btnSelectDod.setVisibility(View.GONE);
        }
    }


    private void saveParticipant() {
        String name = etName.getText().toString().trim();
        String gender = getSelectedGender();
        String status = spStatus.getSelectedItem().toString();
        String dob = etDateOfBirth.getText().toString();
        String dod = etDateOfDeath.getText().toString();
        String occupation = spOccupation.getSelectedItem().toString();
        boolean Hobby1 = cbHobby1.isChecked();
        boolean Hobby2 = cbHobby2.isChecked();
        boolean Hobby3 = cbHobby3.isChecked();

        // Validate Date of Death
        if (!dod.isEmpty()) {
            try {
                Date dodDate = sdobdf.parse(dod);
                Date currentDate = new Date(System.currentTimeMillis());

                if (dodDate != null && dodDate.after(currentDate)) {
                    // Dod is greater than the current date
                    showDodValidationAlert();
                    return;
                }
            } catch (ParseException e) {
                // Handle parsing exception if necessary
                e.printStackTrace();
            }
        }

        // Construct the newHobbies string based on the selected checkboxes
        StringBuilder HobbiesBuilder = new StringBuilder();
        if (Hobby1) {
            HobbiesBuilder.append("Hobby1,");
        }
        if (Hobby2) {
            HobbiesBuilder.append("Hobby2,");
        }
        if (Hobby3) {
            HobbiesBuilder.append("Hobby3,");
        }
        // Remove the trailing comma if any
        String Hobbies = HobbiesBuilder.toString();
        if (Hobbies.endsWith(",")) {
            Hobbies = Hobbies.substring(0, Hobbies.length() - 1);
        }


           if (!name.isEmpty() && !gender.isEmpty() && spStatus.getSelectedItemPosition() != 0 && spOccupation.getSelectedItemPosition() != 0 && isAnyHobbySelected()) {
            // Save participant to database
            dbHandler.addParticipant(name, gender,status, dob,dod, occupation, Hobbies);

            // Clear input fields
            etName.setText("");
            rgGender.clearCheck();
            spStatus.setSelection(0);
            etDateOfBirth.setText("Select Date of Birth");
            etDateOfDeath.setText("Select Date of Death");
            spOccupation.setSelection(0);
            cbHobby1.setChecked(false);
            cbHobby2.setChecked(false);
            cbHobby3.setChecked(false);


            Toast.makeText(this, "Participant saved successfully", Toast.LENGTH_SHORT).show();
            Intent i= new Intent(MainActivity.this,ViewParticipantActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
        }


    }

    private void showDodValidationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Date of Death");
        builder.setMessage("Date of Death cannot be greater than the current date.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private String getSelectedGender() {
        int selectedRadioButtonId = rgGender.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton radioButton = findViewById(selectedRadioButtonId);
            return radioButton.getText().toString();
        }

        return "";
    }

    private boolean isAnyHobbySelected() {
        return cbHobby1.isChecked() || cbHobby2.isChecked() || cbHobby3.isChecked();
    }





    @Override
    protected void onResume() {
        super.onResume();
        // Perform actions to be done when the activity is resumed
        // Update UI, refresh data, register listeners, etc.
    }
}
