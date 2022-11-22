package com.beatrice.ambulanceaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HospitalMainActivity extends AppCompatActivity {

    private Button button4;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        setContentView(R.layout.activity_hospitalmain);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalMainActivity.this,HospitalRequests.class);
                startActivity(intent);
            }
        });
    button5.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(HospitalMainActivity.this,HospitalBookings.class);
            startActivity(intent);
        }
    });
    }
}
