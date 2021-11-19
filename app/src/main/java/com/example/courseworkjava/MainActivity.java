package com.example.courseworkjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import android.os.Bundle;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.Validator;





import java.util.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener{

    EditText edtNote;
    Spinner spnFurnitureType;
    // require field
    @NotEmpty
    EditText edtName, edtPrice, edtStartDate, edtEndDate, edtAddress;
    Spinner spnPropertyType, spnBedroom;
    Button btnSubmit;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    private void view(){
        edtName = (EditText) findViewById(R.id.name);
        edtAddress = (EditText) findViewById(R.id.address);
        edtPrice = (EditText) findViewById(R.id.price);
        spnPropertyType = (Spinner) findViewById(R.id.propertyType);
        spnBedroom = (Spinner) findViewById(R.id.bedroom);
        edtStartDate = (EditText) findViewById(R.id.startDate);
        edtEndDate = (EditText) findViewById(R.id.endDate);
        spnFurnitureType = (Spinner) findViewById(R.id.furnitureType);
        edtNote = (EditText) findViewById(R.id.note);
        btnSubmit = (Button) findViewById(R.id.submitBtn);

        //Spinner data
        ArrayAdapter<CharSequence> propertyTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.propertyType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> bedroomAdapter = ArrayAdapter.createFromResource(this,
                R.array.bedroom, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> furnitureTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.furnitureType, android.R.layout.simple_spinner_item);

        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furnitureTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnPropertyType.setAdapter(propertyTypeAdapter);
        spnPropertyType.setOnItemSelectedListener(this);
        spnBedroom.setAdapter(bedroomAdapter);
        spnBedroom.setOnItemSelectedListener(this);
        spnFurnitureType.setAdapter(furnitureTypeAdapter);
        spnFurnitureType.setOnItemSelectedListener(this);

        //Button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                builder.setTitle("Confirm your information ");
                builder.setMessage("Username: " + edtName.getText().toString() + "\n" +
                        "Address: " + edtAddress.getText().toString() + "\n" +
                                "Property Type: " + spnPropertyType.getSelectedItem().toString() + "\n" +
                                "Bedrooms: " + spnBedroom.getSelectedItem().toString() + "\n" +
                        "Start Date: " + edtStartDate.getText().toString() + "\n" +
                        "End Date: " + edtEndDate.getText().toString() + "\n" +
                        "Price: " + edtPrice.getText().toString() + "\n" +
                        "Furniture Type: " + spnFurnitureType.getSelectedItem().toString() + "\n" +
                        "Note: " + edtNote.getText().toString());
                //validate
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handleButton_onPress(view);
                    };
                });
                //cancel
                builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    };
                });
                builder.show();
            }
        });

        //Date
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartDate();
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectEndDate();
            }
        });
    }

    private void handleButton_onPress(View view) {
        validator.validate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        //get day data
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();
        //check EndDate Date
        if(endDate.compareTo(startDate) < 0) {
            edtEndDate.setError(getText(R.string.endDate_startDate));
        }
        //check Start Date
        if(dateFormat.format(currentDate).compareTo(startDate) > 0) {
            edtStartDate.setError(getText(R.string.startDate_currentDate));
        }
    }

    //Date
    private void selectStartDate(){
        //today
        Calendar calendar = Calendar.getInstance();
        //declare basic date data
        //date
        int dateNow = calendar.get(Calendar.DATE);
        //month
        int monthNow = calendar.get(Calendar.MONTH);
        //year
        int yearNow = calendar.get(Calendar.YEAR);
        //handle data
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //set calendar date = curent
                calendar.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //set new data
                edtStartDate.setText(sdf.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dateNow);
        //limit date
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void selectEndDate(){
        Calendar calendar = Calendar.getInstance();
        int dateNow = calendar.get(Calendar.DATE);
        int monthNow = calendar.get(Calendar.MONTH);
        int yearNow = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                edtEndDate.setText(sdf.format(calendar.getTime()));
            }
        }, yearNow, monthNow, dateNow);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onValidationSucceeded() {
        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String price = edtPrice.getText().toString();
        String note = edtNote.getText().toString();
        String propertyType = spnPropertyType.getSelectedItem().toString();
        String bedRoom = spnBedroom.getSelectedItem().toString();
        String furnitureType = spnFurnitureType.getSelectedItem().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        if(dateFormat.format(currentDate).compareTo(startDate) > 0) {
            return;
        } else if (endDate.compareTo(startDate) < 0) {
            return;
        } else if (dateFormat.format(currentDate).compareTo(endDate) > 0) {
            return;
        } else {
            Toast.makeText(this,"Submit Successfully" + "\n" +
                    "Name: " + name + "\n" +
                            "Address: " + address + "\n" +
                            "Property Type: " + propertyType + "\n" +
                            "Bed Room: " + bedRoom + "\n" +
                            "Start Date: " + startDate + "\n" +
                            "End Date: " + endDate + "\n" +
                            "Price: " + price + "\n" +
                            "Furniture Type: " + furnitureType + "\n" +
                            "Note: " + note,
                    Toast.LENGTH_LONG).show();
        };
    };

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


}