package com.beatrice.ambulanceaid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterHospitals extends RecyclerView.Adapter<AdapterHospitals.HolderShop>{
    private Context context;
    public ArrayList<ModelAmubulance> shopsList;

    public AdapterHospitals(Context context, ArrayList<ModelAmubulance> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }

    @NonNull
    @Override
    public HolderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_hospital, parent, false);

        return new HolderShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderShop holder, int position) {

        ModelAmubulance modelShop = shopsList.get(position);
        String accountType = modelShop.getAccountType();
        String name = modelShop.getName();
        String email = modelShop.getEmail();
        String online = modelShop.getOnline();
        String phone = modelShop.getPhoneNumber();
        final String uid = modelShop.getUid();



        holder.shopNameTv.setText(name);
        holder.phoneTv.setText(phone);
        holder.addressTv.setText(email);
        if (online.equals("true")){

            holder.onlineIv.setVisibility(View.VISIBLE);
        }
        else{
            holder.onlineIv.setVisibility(View.GONE);
        }




        // handle click listener for shop details

        holder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( context, BookingActivity.class );
                intent.putExtra ( "shopUid",uid );
                context.startActivity ( intent );
            }
        } );

    }

    private float ratingSum =0;
    private void loadReviews(ModelAmubulance modelShop , HolderShop holder) {

        String shopUid = modelShop.getUid ( );

        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child ( shopUid ).child ( "Ratings" )
                .addValueEventListener ( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // clear list before adding data into it
                        ratingSum=0;
                        for(DataSnapshot ds: dataSnapshot.getChildren ()){
                            float rating = Float.parseFloat ( ""+ds.child ( "ratings" ).getValue () );
                            ratingSum = ratingSum+rating;


                        }
                        long numberOfReviews = dataSnapshot.getChildrenCount ();
                        float avgRating = ratingSum/numberOfReviews;

                        holder.ratingBar.setRating ( avgRating );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    class HolderShop extends RecyclerView.ViewHolder{

        private ImageView shopIv,onlineIv;
        private TextView shopClosedTv, shopNameTv, phoneTv,addressTv;
        private RatingBar ratingBar;

        public HolderShop(@NonNull View itemView) {
            super(itemView);

            shopIv = itemView.findViewById(R.id.shopIv);
            onlineIv = itemView.findViewById(R.id.onlineIv);
            shopClosedTv = itemView.findViewById(R.id.shopClosedTv);
            shopNameTv = itemView.findViewById(R.id.shopNameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}
