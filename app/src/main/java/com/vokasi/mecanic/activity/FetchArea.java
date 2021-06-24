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

public class FetchArea extends AsyncTask<Location, Void, String> {
    private Context mContext;
    private OnTaskCompleted mListener;
    FetchArea(Context context, OnTaskCompleted listener){
        mContext=context;
        mListener=listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder=new Geocoder(mContext, Locale.getDefault());
        Location location=locations[0];
        List<Address> addresses=null;
        String resultMsg;
        try {
            addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),  1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses==null || addresses.size()==0){
            resultMsg = mContext.getString(R.string.data_not_found);
        }else{
            Address address=addresses.get(0);
            resultMsg = address.getSubAdminArea();

//            for(int i=0;
//                i<=address.getMaxAddressLineIndex();
//                i++){
//                addressParts.add(address.getAddressLine(i));
//            }
//            resultMsg= TextUtils.join("\n", addressParts);

        }
        return resultMsg;
    }

    @Override
    protected void onPostExecute(String s) {
        mListener.onTaskCompleted(s);
        super.onPostExecute(s);
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }
}
