package com.vokasi.mecanic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterKota extends RecyclerView.Adapter<AdapterKota.HolderKota> {
    private Context context;

    public AdapterKota (Context context, ArrayList<ModelKota> listKota) {
        this.context = context;
        this.listKota = listKota;
    }

    public ArrayList<ModelKota>listKota;

    @NonNull
    public HolderKota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_kota, parent, false);
        return new AdapterKota.HolderKota(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKota.HolderKota holder, int position) {
        ModelKota modelKota = listKota.get(position);
        holder.kota.setText(modelKota.getKota());

    }

    @Override
    public int getItemCount() {
        return listKota.size();
    }




    class HolderKota extends RecyclerView.ViewHolder {

        private TextView kota;

        public HolderKota(@NonNull View itemView) {
            super(itemView);

            kota = itemView.findViewById(R.id.namaKota);


        }
    }




}
