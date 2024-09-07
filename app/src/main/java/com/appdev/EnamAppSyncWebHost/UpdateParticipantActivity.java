package com.appdev.EnamAppSyncWebHost;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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


public class UpdateParticipantActivity extends AppCompatActivity {

    private EditText editTextName, editTextDob, editTextDod;
    private RadioGroup radioGroupGender;
    private Spinner spinnerStatus;
    private Calendar dobCalendar; // Calendar instance for date of birth
    private Calendar dodCalendar; // Calendar instance for date of death
    private SimpleDateFormat sdf; // Date format for displaying date
    private SimpleDateFormat sdobdf; // Date format for displaying date
    private Spinner spinnerOccupation;
    private CheckBox checkBoxHobby1;
    private CheckBox checkBoxHobby2;
    private CheckBox checkBoxHobby3;
    private Button btnUpdate;
    private DBHandler dbHandler;
    private ParticipantModal participant;


    //Variable declaratin for logic to handle visibility
    private TextView tvDobLabel;
    private EditText btnSelectDob;
    private TextView tvDodLabel;
    private EditText btnSelectDod;
    private Spinner spLiveOrDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_participant);

        editTextName = findViewById(R.id.editTextNameUpdate);
        radioGroupGender = findViewById(R.id.rgGenderUpdate);
        spinnerStatus= findViewById(R.id.spStatusUpdate) ;
        editTextDob = findViewById(R.id.btnSelectDobUpdate);
        editTextDod= findViewById(R.id.btnSelectDodUpdate);
        spinnerOccupation = findViewById(R.id.spOccupationUpdate);
        checkBoxHobby1 = findViewById(R.id.cbHobby1Update);
        checkBoxHobby2 = findViewById(R.id.cbHobby2Update);
        checkBoxHobby3 = findViewById(R.id.cbHobby3Update);
        btnUpdate = findViewById(R.id.btnUpdate);


        // Initialize views
        tvDobLabel = findViewById(R.id.tvDobLabel);
        tvDodLabel = findViewById(R.id.tvDodLabel);
        btnSelectDob = findViewById(R.id.btnSelectDobUpdate);
        btnSelectDod = findViewById(R.id.btnSelectDodUpdate);
        spLiveOrDeath = findViewById(R.id.spStatusUpdate);

        // Set initial visibility of fields
        tvDobLabel.setVisibility(View.GONE);
        btnSelectDob.setVisibility(View.GONE);
        tvDodLabel.setVisibility(View.GONE);
        btnSelectDod.setVisibility(View.GONE);



        //Initialize DBHandler
        dbHandler = new DBHandler(this);

        // get participant id from intent
        Intent intent=getIntent();
        int participantID=intent.getIntExtra("PARTICIPANT_ID",-1);
        String participantStatus= intent.getStringExtra("status");
        String participantDOB= intent.getStringExtra("dob");
        String participantDOD= intent.getStringExtra("dod");
        String participantOccu= intent.getStringExtra("occu");
      //  String participantHobbies=intent.getStringExtra("hobbies");  // may have later

        //Retrieve participant ID from Database
        participant = dbHandler.getParticipant(participantID);

         //Set initial values in the views
        editTextName.setText(participant.getName());

        //Gender data retrieve and setting
        if(participant.getGender().equals("Male")){
            radioGroupGender.check(R.id.rbMale);

        } else if (participant.getGender().equals("Female")){
            radioGroupGender.check(R.id.rbFemale);
        }

        //spinnerStatus
        ArrayAdapter<String> statusAdapter = (ArrayAdapter<String>) spinnerStatus.getAdapter();
        int statusPosition = statusAdapter.getPosition(participantStatus);
        spinnerStatus.setSelection(statusPosition);

        //Set initial values in the views
        editTextDob.setText(participantDOB);
        editTextDod.setText(participantDOD);

       // Set the selected item in the Spinner
        ArrayAdapter<String> occupationAdapter = (ArrayAdapter<String>) spinnerOccupation.getAdapter();
        int occupationPosition = occupationAdapter.getPosition(participantOccu);
        spinnerOccupation.setSelection(occupationPosition);

        // Retrieve hobbies data and set checkbox states
       String hobbies = dbHandler.getParticipantHobbies(participantID);
        if (hobbies.contains("Hobby1")) {
            checkBoxHobby1.setChecked(true);
        }
        if (hobbies.contains("Hobby2")) {
            checkBoxHobby2.setChecked(true);
        }
        if (hobbies.contains("Hobby3")) {
            checkBoxHobby3.setChecked(true);
        }




        // Initialize the dobCalendar instance

        dobCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextDob.setFocusable(false);
        editTextDob.setClickable(true);
        editTextDob.setOnClickListener(v ->
                showDatePickerDialog()
        );

        dodCalendar = Calendar.getInstance();
        sdobdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextDod.setOnClickListener(v ->
                showDatePickerDialog4Dod()
        );

        editTextDod.setFocusable(false);
        editTextDod.setClickable(true);

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated values from views
                String newName = editTextName.getText().toString();
                String newGender = getSelectedGender();
                String newStatus= spinnerStatus.getSelectedItem().toString();
                String newDob = editTextDob.getText().toString().trim();
                String newDod = editTextDod.getText().toString().trim();
                String newOccupation = spinnerOccupation.getSelectedItem().toString();
                boolean newHobby1 = checkBoxHobby1.isChecked();
                boolean newHobby2 = checkBoxHobby2.isChecked();
                boolean newHobby3 = checkBoxHobby3.isChecked();

                if (!newDod.isEmpty()) {
                    try {
                        Date dodDate = sdobdf.parse(newDod);
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
                StringBuilder newHobbiesBuilder = new StringBuilder();
                if (newHobby1) {
                    newHobbiesBuilder.append("Hobby1,");
                }
                if (newHobby2) {
                    newHobbiesBuilder.append("Hobby2,");
                }
                if (newHobby3) {
                    newHobbiesBuilder.append("Hobby3,");
                }
                // Remove the trailing comma if any
                String newHobbies = newHobbiesBuilder.toString();
                if (newHobbies.endsWith(",")) {
                    newHobbies = newHobbies.substring(0, newHobbies.length() - 1);
                }

                // Update the participant in the database

                if (!newName.isEmpty() && !newGender.isEmpty() &&  spinnerStatus.getSelectedItemPosition() != 0 &&spinnerOccupation.getSelectedItemPosition() != 0 && isAnyHobbySelected()) {
                    // Save participant to database
                    dbHandler.updateParticipant(participant.getId(), newName, newGender,newStatus, newDob, newDod, newOccupation, newHobbies);

                    Toast.makeText(UpdateParticipantActivity.this, "Participant updated successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(UpdateParticipantActivity.this, "Please enter all data", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    private void showDodValidationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Date of Death");
        builder.setMessage("Date of Death cannot be greater than the current date.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }


    private void showDatePickerDialog() {
        int year = dobCalendar.get(Calendar.YEAR);
        int month = dobCalendar.get(Calendar.MONTH);
        int day = dobCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateParticipantActivity.this,
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    dobCalendar.set(selectedYear, monthOfYear, dayOfMonth);
                    String selectedDate = sdf.format(dobCalendar.getTime());
                    editTextDob.setText(selectedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showDatePickerDialog4Dod() {
        int year = dodCalendar.get(Calendar.YEAR);
        int month = dodCalendar.get(Calendar.MONTH);
        int day = dodCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateParticipantActivity.this,
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    dodCalendar.set(selectedYear, monthOfYear, dayOfMonth);
                    String selectedDate = sdobdf.format(dodCalendar.getTime());
                    editTextDod.setText(selectedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }


    private String getSelectedGender() {
        int selectedRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.rbMale) {
            return "Male";
        } else if (selectedRadioButtonId == R.id.rbFemale) {
            return "Female";
        } else {
            return "";
        }
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
    private boolean isAnyHobbySelected() {
        return checkBoxHobby1.isChecked() || checkBoxHobby2.isChecked() || checkBoxHobby3.isChecked();
    }
    protected void onResume(){

        super.onResume();
    }


}
