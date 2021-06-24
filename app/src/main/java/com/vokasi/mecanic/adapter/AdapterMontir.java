package com.vokasi.mecanic.adapter;


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

import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.activity.DetailedMontirInfo;
import com.vokasi.mecanic.model.UserMontir;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterMontir extends RecyclerView.Adapter<AdapterMontir.HolderMontir> {
    private Context context;

    public AdapterMontir(Context context, ArrayList<UserMontir> montirList) {
        this.context = context;
        this.montirList = montirList;
    }

    public ArrayList<UserMontir> montirList;

    @NonNull
    @Override
    public HolderMontir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_recommended_montir, parent, false);
        return new HolderMontir(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMontir holder, int position) {
        UserMontir userMontir = montirList.get(position);
        if (userMontir.getKeahlian().startsWith("Motor")){
                holder.spek.setText(R.string.motor);
            }else{
                holder.spek.setText(R.string.mobil);
            }
        holder.nama.setText(userMontir.getFirstName());
        Picasso.with(context).load(userMontir.getPhoto_Uri())
                .centerCrop().fit().into(holder.fotoMontir);
        holder.ratingBar.setRating(Float.parseFloat(userMontir.getRating()));
        holder.jumlahRating.setText("(" + userMontir.getRating()+")");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context, DetailedMontirInfo.class);
                newIntent.putExtra("namaDepan", userMontir.getFirstName());
                newIntent.putExtra("bengkel", userMontir.getBengkel());
                newIntent.putExtra("jamBuka", userMontir.getBuka());
                newIntent.putExtra("jamTutup", userMontir.getTutup());
                newIntent.putExtra("spek", userMontir.getKeahlian());
                newIntent.putExtra("alamat", userMontir.getAlamat());
                newIntent.putExtra("email", userMontir.getEmail());
                newIntent.putExtra("fotoUrl", userMontir.getPhoto_Uri());
                newIntent.putExtra("nomor", userMontir.getPhoneNum());
                newIntent.putExtra("kota", userMontir.getKota());
                newIntent.putExtra("area", userMontir.getArea());
                newIntent.putExtra("rating", userMontir.getRating());
                holder.itemView.getContext().startActivity(newIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return montirList.size();
    }


    class HolderMontir extends RecyclerView.ViewHolder {

        private TextView nama, spek, jumlahRating;
        private ImageView fotoMontir;
        private RatingBar ratingBar;

        public HolderMontir(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.montirTv);
            spek = itemView.findViewById(R.id.montirSpek);
            jumlahRating = itemView.findViewById(R.id.jumlahRating);
            fotoMontir = itemView.findViewById(R.id.fotoMontir);
            ratingBar = itemView.findViewById(R.id.ratingBarRecommended);

        }
    }
}
