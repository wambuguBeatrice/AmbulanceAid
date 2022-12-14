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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;


    Button btnsignup;
    EditText inputName,inputEmail, inputpassword, inputpassword1, input_phoneNumber,input_EmergencyPersonName,input_EmergencyPersonContacts;
    LinearLayout linklogin;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_signup);

        //hide
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize
        input_EmergencyPersonContacts = findViewById(R.id.input_EmergencyPersonContacts);
        input_EmergencyPersonName = findViewById(R.id.input_EmergencyPersonName);
        input_phoneNumber = findViewById(R.id.input_PhoneNumber);
        inputName = findViewById(R.id.input_Name);
        linklogin =(LinearLayout) findViewById(R.id.link_login);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputpassword = (EditText) findViewById(R.id.input_password);
        inputpassword1 = (EditText) findViewById(R.id.input_password1);
        btnsignup = (Button) findViewById(R.id.btn_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        // Set listeners
        linklogin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
    }

    // Register user
    private String email, password, confirmPassword, name,phoneNumber, emergencyPersonName,  emergencyPersonPhoneNumber;
    private void registerUser(){
         email = inputEmail.getText().toString().trim();

       password = inputpassword.getText().toString().trim();
         confirmPassword = inputpassword1.getText().toString().trim();
        name = inputName.getText().toString().trim();
        phoneNumber = input_phoneNumber.getText().toString().trim();
        emergencyPersonName = input_EmergencyPersonName.getText().toString().trim();
        emergencyPersonPhoneNumber = input_EmergencyPersonContacts.getText().toString().trim();

        // If email is empty, please enter email
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        // If password is empty, please enter password
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // If confirm password is empty, please confirm
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering your account. Please wait...");
        progressDialog.show();

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Successfully registered user, please verify through user email
                                        saveToFirebase();
                                        Toast.makeText(context, "Kindly check your email for verification.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(context, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            finish();
                            // Redirect to login activity
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"Could not register you ... Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveToFirebase() {
        progressDialog.setMessage("Saving account info");
        final String timestamp = "" +System.currentTimeMillis();

        // set up to save to firebase
        HashMap <String, Object > hashMap = new HashMap<>();
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("email", "" + email);
        hashMap.put("name", "" + name);
        hashMap.put("phoneNumber", "" + phoneNumber);
        hashMap.put("emergencyPersonName", "" + emergencyPersonName);
        hashMap.put("emergencyPersonPhoneNumber", "" + emergencyPersonPhoneNumber);
        hashMap.put("accountType","User");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // db update
                progressDialog.dismiss();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View view){
        if (view == btnsignup){
            // Register user
            registerUser();
        }
        else if (view == linklogin){
            finish();
            // Redirect to login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
