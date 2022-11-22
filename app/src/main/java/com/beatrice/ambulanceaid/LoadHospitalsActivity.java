package com.beatrice.ambulanceaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadHospitalsActivity extends AppCompatActivity {

    private RecyclerView shopsRv, ordersRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelAmubulance> shopsList;
    private AdapterHospitals adapterShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_hospitals);

        firebaseAuth = FirebaseAuth.getInstance ();
        //loadshopdetails();
        shopsRv = (RecyclerView) findViewById(R.id.shopsRv);


        shopsList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("accountType").equalTo("Hospital")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        shopsList.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelAmubulance modelShop = ds.getValue(ModelAmubulance.class);

                            String shopCity = ""+ds.child("city").getValue();

                            //  if (shopCity.equals(myCity)){
                            shopsList.add(modelShop);
                            // }

                            // if you want to see all shops, skip the if statement and this
                            // shopsList.add(modelShop);
                        }
                        adapterShop = new AdapterHospitals(LoadHospitalsActivity.this, shopsList);

                        shopsRv.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }
}