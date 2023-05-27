package com.nhom7.doanquanlysanpham;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends ArrayAdapter {
    Activity activity;
    Context context;
    ArrayList<SanPham> arr;

    public SanPhamAdapter(@NonNull Activity activity, int resource, @NonNull ArrayList<SanPham> objects) {
        super(activity, resource, objects);
        this.activity = (Activity) activity;
        this.arr = objects;
        this.context = activity.getApplicationContext();
    }


    @Override
    public int getCount(){
        return arr.size();
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_sanpham_listitem, null);
        TextView name = (TextView) view.findViewById(R.id.tv_Ten);
        TextView cost =  (TextView) view.findViewById(R.id.tv_Dongia);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        name.setText(this.arr.get(position).getTen());
        cost.setText(this.arr.get(position).getDongia()+"vnd");
        if (this.arr.get(position).getPic()==null)
            icon.setImageResource(Functions.getImageID(this.context, this.arr.get(position).getPicDrawable()));
        else
            icon.setImageBitmap(Functions.ConvertArrayToBitmap(arr.get(position).getPic()));
        return view;
    }
}
