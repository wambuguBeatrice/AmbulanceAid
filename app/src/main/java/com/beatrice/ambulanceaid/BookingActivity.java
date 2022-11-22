package com.beatrice.ambulanceaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class BookingActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton basic, advanced, radioButton;
    TextView amount;
    Button bookingA;
    private ArrayList<ModelAmubulance> hospitalList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        radioGroup = findViewById(R.id.radioAmbulanceType);
        basic = findViewById(R.id.basicRB);
        advanced = findViewById(R.id.advancedRB);
        amount = findViewById(R.id.amount);
        bookingA = (Button) findViewById(R.id.booking);

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();
        datePicker.setMinDate(System.currentTimeMillis()-1000);

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        int hour = timePicker.getHour();
        int minutes = timePicker.getMinute();

        loadHospitals();

        selectedAmbulence();
    }

    private void loadHospitals() {

        hospitalList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Hospital").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitalList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelAmubulance modelAmubulance = ds.getValue(ModelAmubulance.class);
                    String hospitalName = ""+ds.child("name").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    String ambulenceType;
    private void selectedAmbulence() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        ambulenceType = radioButton.getText().toString().trim();


    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.basicRB:
                if(checked){
                    amount.setText("2500");
                }
                break;
            case R.id.advancedRB:
                if (checked){
                    amount.setText("5000");
                }
                break;
        }
    }

}


