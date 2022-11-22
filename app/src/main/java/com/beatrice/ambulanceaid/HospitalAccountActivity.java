package com.beatrice.ambulanceaid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HospitalAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    Button btn_HospitalSignUp;
    EditText input_hospitalName,input_hospitalEmail, input_HospitalPhoneNumber, input_hospitalpassword, input_hospitalpassword1;
    LinearLayout linklogin;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        setContentView(R.layout.activity_hospitalaccount);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //initialize
        input_hospitalEmail = findViewById(R.id.input_hospitalEmail);
        input_hospitalName = findViewById(R.id.input_hospitalEmail);
        input_HospitalPhoneNumber = findViewById(R.id.input_HospitalPhoneNumber);
        input_hospitalpassword = findViewById(R.id.input_hospitalpassword);
        linklogin =(LinearLayout) findViewById(R.id.link_login);
        input_hospitalpassword1 = (EditText) findViewById(R.id.input_hospitalpassword1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        //not sure
        linklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_HospitalSignUp = findViewById(R.id.btn_Hospitalsignup);
        btn_HospitalSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
    }

    String hospitalName, hospitalEmail, hospitalNumber, password, confirmPassword,email;
    private void inputData() {
        hospitalName = input_hospitalName.getText().toString().trim();
        password = input_hospitalpassword.getText().toString().trim();
        confirmPassword = input_hospitalpassword1.getText().toString().trim();
        hospitalEmail =  input_hospitalEmail.getText().toString().trim();
        hospitalNumber = input_HospitalPhoneNumber.getText().toString().trim();

        // If email is empty, please enter email
        if (TextUtils.isEmpty(hospitalEmail)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        // If password is empty, please enter password
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // If confirm password is empty, please confirm
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering your account. Please wait...");
        progressDialog.show();

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(hospitalEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Successfully registered user, please verify through user email
                                        saveToFirebase();
                                        Toast.makeText(context, "Kindly check your email for verification.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            finish();
                            // Redirect to login activity
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(HospitalAccountActivity.this, "Could not register you ... Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void saveToFirebase() {
        progressDialog.setMessage("Saving account info");
        final String timestamp = "" +System.currentTimeMillis();

        // set up to save to firebase
        HashMap<String, Object > hashMap = new HashMap<>();
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("email", "" + hospitalEmail);
        hashMap.put("name", "" + hospitalName);
        hashMap.put("phoneNumber", "" + hospitalNumber);
        hashMap.put("accountType","Hospital");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // db update
                progressDialog.dismiss();
                startActivity(new Intent(HospitalAccountActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                startActivity(new Intent(HospitalAccountActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
