package com.vokasi.mecanic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        holder.nama.setText(userMontir.getFirstName());
        holder.bengkel.setText(userMontir.getBengkel());
        holder.alamat.setText(userMontir.getAlamat());
        holder.jamOperasi.setText(userMontir.getBuka() + "-"+ userMontir.getTutup());
        holder.spek.setText(userMontir.getKeahlian());

    }

    @Override
    public int getItemCount() {
        return montirListFull.size();
    }


    class HolderMontir extends RecyclerView.ViewHolder{

        private TextView nama, spek, bengkel, alamat, jamOperasi;

        public HolderMontir(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.namaMontir);
            bengkel = itemView.findViewById(R.id.namaBengkel);
            alamat = itemView.findViewById(R.id.alamatBengkel);
            jamOperasi = itemView.findViewById(R.id.jamOperasi);
            spek = itemView.findViewById(R.id.spek);


        }
    }
}
