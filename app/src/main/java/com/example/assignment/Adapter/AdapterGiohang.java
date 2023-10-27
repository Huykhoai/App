package com.example.assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment.Activity.GiohangActivity;
import com.example.assignment.Activity.MainActivity;
import com.example.assignment.Modul.Giohang;
import com.example.assignment.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterGiohang  extends BaseAdapter {
     Context context;
     ArrayList<Giohang> arrayList;

    public AdapterGiohang(Context context, ArrayList<Giohang> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if(arrayList != null)
        return arrayList.size();
        return 0;
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
        ViewHolderGiohang viewHolderGiohang = null;

        if(view==null){
            viewHolderGiohang = new ViewHolderGiohang();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_giohang, null);
            viewHolderGiohang.txtName = view.findViewById(R.id.textviewgiohang);
            viewHolderGiohang.txtGia = view.findViewById(R.id.textviewgiagiohang);
            viewHolderGiohang.imageView= view.findViewById(R.id.imageviewgiohang);
            viewHolderGiohang.btnTru = view.findViewById(R.id.buttonminus);
            viewHolderGiohang.btnTang= view.findViewById(R.id.buttonplus);
            viewHolderGiohang.btnValues = view.findViewById(R.id.buttonvalues);
            view.setTag(viewHolderGiohang);
        }else {
            viewHolderGiohang = (ViewHolderGiohang) view.getTag();
        }
        Giohang giohang1 = arrayList.get(i);
        viewHolderGiohang.txtName.setText(giohang1.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolderGiohang.txtGia.setText("Giá; "+ decimalFormat.format(giohang1.getGiasp()));
        Picasso.get().load(giohang1.getHinhanhsp())
                .placeholder(R.drawable.home)
                .error(R.drawable.erro)
                .into(viewHolderGiohang.imageView);
        viewHolderGiohang.btnValues.setText(String.valueOf(giohang1.getSoluong()));
        int sl = Integer.parseInt(viewHolderGiohang.btnValues.getText().toString());
        if(sl>9){
            viewHolderGiohang.btnTang.setVisibility(View.INVISIBLE);
            viewHolderGiohang.btnTru.setVisibility(View.VISIBLE);
        } else if (sl<1) {
            viewHolderGiohang.btnTru.setVisibility(View.INVISIBLE);
            viewHolderGiohang.btnTang.setVisibility(View.VISIBLE);
        }
        ViewHolderGiohang finalViewHolderGiohang = viewHolderGiohang;
        viewHolderGiohang.btnTang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  int slmoinhat = Integer.parseInt(finalViewHolderGiohang.btnValues.getText().toString())+1;
                  int slhientai = MainActivity.manggiohang.get(i).getSoluong();
                  long giahientai = MainActivity.manggiohang.get(i).getGiasp();
                  //gan sl moi nhat
                    MainActivity.manggiohang.get(i).setSoluong(slmoinhat);
                    //set gia moi nhat
                    long giamoinhat = (giahientai* slmoinhat)/ slhientai;
                    MainActivity.manggiohang.get(i).setGiasp((int) giamoinhat);
                    GiohangActivity.UpdateTongtien();
                    //
                    DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
                    finalViewHolderGiohang.txtGia.setText("Giá: "+decimalFormat1.format(giamoinhat));
                    if (slmoinhat>9){
                        finalViewHolderGiohang.btnTang.setVisibility(View.INVISIBLE);
                        finalViewHolderGiohang.btnValues.setText(String.valueOf(slmoinhat));
                        finalViewHolderGiohang.btnTru.setVisibility(View.VISIBLE);
                    }else{
                        finalViewHolderGiohang.btnTang.setVisibility(View.VISIBLE);
                        finalViewHolderGiohang.btnValues.setText(String.valueOf(slmoinhat));
                        finalViewHolderGiohang.btnTru.setVisibility(View.VISIBLE);
                    }
                }
            });
        viewHolderGiohang.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoinhat = Integer.parseInt(finalViewHolderGiohang.btnValues.getText().toString())-1;
                int slhientai = MainActivity.manggiohang.get(i).getSoluong();
                long giahientai = MainActivity.manggiohang.get(i).getGiasp();
                //gan sl moi nhat
                MainActivity.manggiohang.get(i).setSoluong(slmoinhat);
                //set gia moi nhat
                long giamoinhat = (giahientai* slmoinhat)/ slhientai;
                MainActivity.manggiohang.get(i).setGiasp((int) giamoinhat);
                GiohangActivity.UpdateTongtien();
                //
                DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
                finalViewHolderGiohang.txtGia.setText("Giá: "+decimalFormat1.format(giamoinhat));
                if (slmoinhat<2){
                    finalViewHolderGiohang.btnTang.setVisibility(View.VISIBLE);
                    finalViewHolderGiohang.btnValues.setText(String.valueOf(slmoinhat));
                    finalViewHolderGiohang.btnTru.setVisibility(View.INVISIBLE);
                }else {
                    finalViewHolderGiohang.btnTang.setVisibility(View.VISIBLE);
                    finalViewHolderGiohang.btnValues.setText(String.valueOf(slmoinhat));
                    finalViewHolderGiohang.btnTru.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    public class ViewHolderGiohang{
        TextView txtName, txtGia;
        ImageView imageView;
        Button btnTru,btnTang,btnValues;
    }
}
