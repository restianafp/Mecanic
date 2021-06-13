package com.vokasi.mecanic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        holder.nama.setText(userMontir.getFirstName());
        holder.spek.setText(userMontir.getKeahlian());
        Picasso.with(context).load(userMontir.getPhoto_Uri())
                .centerCrop().fit().into(holder.fotoMontir);

    }

    @Override
    public int getItemCount() {
        return montirList.size();
    }


    class HolderMontir extends RecyclerView.ViewHolder {

        private TextView nama, spek;
        private ImageView fotoMontir;

        public HolderMontir(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.montirTv);
            spek = itemView.findViewById(R.id.montirSpek);
            fotoMontir = itemView.findViewById(R.id.fotoMontir);

        }
    }
}
