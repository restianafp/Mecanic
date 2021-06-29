package com.vokasi.mecanic.activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.vokasi.mecanic.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddress extends AsyncTask<Location, Void, ArrayList<String>> {
    private Context mContext;
    private OnTaskCompleted mListener;
    FetchAddress(Context context, OnTaskCompleted listener){
        mContext=context;
        mListener=listener;
    }

    @Override
    protected ArrayList<String> doInBackground(Location... locations) {
        Geocoder geocoder=new Geocoder(mContext, Locale.getDefault());
        Location location=locations[0];
        List<Address> addresses=null;
        ArrayList<String> addressParts;
        String resultMsg;
        try {
            addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),  1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses==null || addresses.size()==0){
            addressParts=new ArrayList<>();
            addressParts.add(mContext.getString(R.string.data_not_found));
        }else{
            Address address=addresses.get(0);
            addressParts=new ArrayList<>();
//            for(int i=0;
//                i<=address.getMaxAddressLineIndex();
//                i++){
//                addressParts.add(address.getAddressLine(i));
//            }
//            resultMsg= TextUtils.join("\n", addressParts);
            addressParts.add(address.getThoroughfare());
            addressParts.add(address.getSubLocality());
            addressParts.add(address.getLocality());
            addressParts.add(address.getSubAdminArea());
            addressParts.add(String.valueOf(address.getLatitude()));
            addressParts.add(String.valueOf(address.getLongitude()));
        }
        return addressParts;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        mListener.onTaskCompleted(s);
        super.onPostExecute(s);
    }

    interface OnTaskCompleted{
        void onTaskCompleted(ArrayList<String> result);
    }
}
