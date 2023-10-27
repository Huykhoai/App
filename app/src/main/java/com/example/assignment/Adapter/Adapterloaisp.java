package com.example.assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment.Modul.Loaisanpham;
import com.example.assignment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapterloaisp extends BaseAdapter {
    Context context;
    ArrayList<Loaisanpham> arrayList;

    public Adapterloaisp(Context context, ArrayList<Loaisanpham> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderloasp viewHolderloasp = null;
         if(view == null){
             viewHolderloasp = new ViewHolderloasp();
             LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             view = inflater.inflate(R.layout.item_loaisp, null);
             viewHolderloasp.textView = view.findViewById(R.id.textviewLoaisp);
             viewHolderloasp.imageView = view.findViewById(R.id.imageLoaisp);
             view.setTag(viewHolderloasp);
         }else {
             viewHolderloasp = (ViewHolderloasp) view.getTag();
         }
         //xu ly su kien
        Loaisanpham loaisanpham = (Loaisanpham) getItem(i);
         viewHolderloasp.textView.setText(loaisanpham.getTenloaisp());
        Picasso.get().load(loaisanpham.getHinhanhsp())
                .placeholder(R.drawable.home)
                .error(R.drawable.erro)
                .into(viewHolderloasp.imageView);
        return view;
    }
   private class ViewHolderloasp{
        TextView textView;
        ImageView imageView;
   }
}
