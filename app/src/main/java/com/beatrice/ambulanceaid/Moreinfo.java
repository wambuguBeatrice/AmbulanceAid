package com.beatrice.ambulanceaid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Moreinfo extends AppCompatActivity {

    private EditText user_name, user_email;
    private TextView user_mobile, tv_updateuserdetail;
    ProgressBar progressbar;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;

    //Constant keys for shared preferences
    public final String SHARED_PREFS = "shared_prefs";

    //storing phone
    public final String USER_NAME = "user_name";

    SharedPreferences sharedPreferences;
    String username;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        //hide
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        user_name = findViewById(R.id.user_name);
        user_mobile = findViewById(R.id.user_mobile);
        user_email = findViewById(R.id.user_email);
        progressbar = findViewById(R.id.progressbar);
        tv_updateuserdetail = findViewById(R.id.tv_updateuserdetail);

        //getting data stored in shared prefs
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //passing username
        username = sharedPreferences.getString(USER_NAME, null);

        tv_updateuserdetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = user_name.getText().toString().trim();
                String useremail = user_email.getText().toString().trim();
                String usermobilenumber = user_mobile.getText().toString().trim();

                //check if email and uname is empty
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(Moreinfo.this, "Please fill in your name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(useremail)){
                    Toast.makeText(Moreinfo.this, "Please fill in your email", Toast.LENGTH_LONG).show();
                    return;
                }

                //if email + name are not empty
                //display progress dialog
                progressbar.setVisibility(View.VISIBLE);

                AppUser appUser = new AppUser(username, useremail, usermobilenumber);

                FirebaseDatabase.getInstance()
                        .getReference("appusers")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(appUser)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    //putting values
                                    editor.putString(USER_NAME, user_name.getText().toString());
                                    //save data
                                    editor.apply();

                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(Moreinfo.this, "Welcome to TaskAnt!.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Moreinfo.this, MainActivity.class));
                                    finish();
                                }else{
                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(Moreinfo.this, "Oh no, please try again...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}

