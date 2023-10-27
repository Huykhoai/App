package com.example.assignment.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterTainghe extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayList;

    public AdapterTainghe(Context context, ArrayList<Sanpham> arrayList) {
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
        ViewHolderTainghe tainghe = null;
        if(view==null){
            tainghe = new ViewHolderTainghe();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_sanham, null);
            tainghe.txtName = view.findViewById(R.id.txtNamesp);
            tainghe.txtGia = view.findViewById(R.id.txtGiasp);
            tainghe.txtMota = view.findViewById(R.id.txtChitietsp);
            tainghe.imgAnh = view.findViewById(R.id.image_sanpham);
            view.setTag(tainghe);
        }else {
            tainghe = (ViewHolderTainghe) view.getTag();
        }
        //xu ly
        Sanpham sanpham = arrayList.get(i);
        tainghe.txtName.setText(sanpham.getTensanpham());
        tainghe.txtName.setMaxLines(2);
        tainghe.txtName.setEllipsize(TextUtils.TruncateAt.END);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tainghe.txtGia.setText("Giá: "+decimalFormat.format(sanpham.getGiasanpham()));
        tainghe.txtMota.setText(sanpham.getMotasanpham());
        tainghe.txtMota.setMaxLines(2);
        tainghe.txtMota.setEllipsize(TextUtils.TruncateAt.END);
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.home)
                .error(R.drawable.erro)
                .into(tainghe.imgAnh);
        return view;
    }

    public class ViewHolderTainghe{
        TextView txtName, txtGia, txtMota;
        ImageView imgAnh;
    }
}
