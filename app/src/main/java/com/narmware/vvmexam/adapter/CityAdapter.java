package com.narmware.vvmexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narmware.vvmexam.R;
import com.narmware.vvmexam.pojo.City;
import com.narmware.vvmexam.pojo.District;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {

    ArrayList<City> mData;
    Context context;

    public CityAdapter(ArrayList<City> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_state, viewGroup, false);
        TextView textView=itemView.findViewById(R.id.txt_data);
        textView.setText(mData.get(i).getCity_name());
        return itemView;
    }
}
