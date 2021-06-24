package com.vokasi.mecanic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.activity.DetailedMontirInfo;
import com.vokasi.mecanic.model.UserMontir;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterMontirFull extends RecyclerView.Adapter<AdapterMontirFull.HolderMontir>{
    private Context context;

    public AdapterMontirFull(Context context, ArrayList<UserMontir> montirListFull) {
        this.context = context;
        this.montirListFull = montirListFull;
    }

    public ArrayList<UserMontir> montirListFull;

    @NonNull
    @Override
    public HolderMontir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_montir_full, parent, false);
        return new HolderMontir(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMontir holder, int position) {
        UserMontir userMontir = montirListFull.get(position);
        if (Locale.getDefault().getDisplayLanguage().startsWith("English")){
            if (userMontir.getKeahlian().startsWith("Motor")){
                holder.spek.setText("Motorcycle");
            }else {
                holder.spek.setText("Car");
            }
        }else {
            holder.spek.setText(userMontir.getKeahlian());
        }
        holder.nama.setText(userMontir.getFirstName());
        holder.bengkel.setText(userMontir.getBengkel());
        holder.alamat.setText(userMontir.getKota().toUpperCase());
        holder.jamOperasi.setText(userMontir.getBuka() + "-"+ userMontir.getTutup());
        Picasso.with(context).load(userMontir.getPhoto_Uri())
                .centerCrop().fit().into(holder.fotoMontir);
        holder.rating.setText(userMontir.getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedMontirInfo.class);
                intent.putExtra("namaDepan", userMontir.getFirstName());
                intent.putExtra("bengkel", userMontir.getBengkel());
                intent.putExtra("jamBuka", userMontir.getBuka());
                intent.putExtra("jamTutup", userMontir.getTutup());
                intent.putExtra("spek", userMontir.getKeahlian());
                intent.putExtra("alamat", userMontir.getAlamat());
                intent.putExtra("email", userMontir.getEmail());
                intent.putExtra("fotoUrl", userMontir.getPhoto_Uri());
                intent.putExtra("nomor", userMontir.getPhoneNum());
                intent.putExtra("kota", userMontir.getKota());
                intent.putExtra("area", userMontir.getArea());
                intent.putExtra("rating", userMontir.getRating());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return montirListFull.size();
    }


    class HolderMontir extends RecyclerView.ViewHolder{

        private TextView nama, spek, bengkel, alamat, jamOperasi, rating;
        private ImageView fotoMontir;

        public HolderMontir(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.namaMontir);
            bengkel = itemView.findViewById(R.id.namaBengkel);
            alamat = itemView.findViewById(R.id.alamatBengkel);
            jamOperasi = itemView.findViewById(R.id.jamOperasi);
            spek = itemView.findViewById(R.id.spek);
            fotoMontir = itemView.findViewById(R.id.fotoMontir2);
            rating = itemView.findViewById(R.id.rating);


        }
    }
}
