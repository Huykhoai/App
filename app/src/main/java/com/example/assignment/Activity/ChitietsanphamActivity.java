package com.example.assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.assignment.Modul.Giohang;
import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChitietsanphamActivity extends AppCompatActivity {
        Toolbar toolbar;
        TextView txtName,txtGia,txtChitiet;
        ImageView imageView;
        Spinner spinner;
        Button button;
        int id =0;
        String tensp="";
        int giasp=0;
        String hinhanhsp="";
        String motasp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        Anhxa();
        Spiner();
        ActionBAar();
        GetData();
        EvenButton();
    }

    private void EvenButton() {
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(MainActivity.manggiohang.size()>0){
                       int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                       boolean exits = false;
                       for (int i=0;i<MainActivity.manggiohang.size();i++){
                        if(MainActivity.manggiohang.get(i).getId()== id){
                            MainActivity.manggiohang.get(i).setSoluong(MainActivity.manggiohang.get(i).getSoluong()+soluong);
                            if(MainActivity.manggiohang.get(i).getSoluong()>10){
                                MainActivity.manggiohang.get(i).setSoluong(10);
                            }

                            MainActivity.manggiohang.get(i).setGiasp(MainActivity.manggiohang.get(i).getGiasp()* giasp);
                            exits= true;
                        }
                       }
                       if(exits==false){
                           int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                           int Giamoi = giasp *sl;
                           MainActivity.manggiohang.add(new Giohang(id,tensp,Giamoi,hinhanhsp,sl));
                       }
                   } else {
                       int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                       int Giamoi = giasp *sl;
                       MainActivity.manggiohang.add(new Giohang(id,tensp,Giamoi,hinhanhsp,sl));
                   }
                   Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
                   startActivity(intent);
               }
           });

    }

    private void GetData() {
        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinchitiet");
          id = sanpham.getId();
          tensp=sanpham.getTensanpham();
          giasp=sanpham.getGiasanpham();
          hinhanhsp=sanpham.getHinhanhsanpham();
          motasp=sanpham.getMotasanpham();
          //xu ly

        txtName.setText(tensp);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGia.setText("Giá: "+decimalFormat.format(giasp));
        txtChitiet.setText(motasp);
        Picasso.get().load(hinhanhsp)
                .placeholder(R.drawable.home)
                .error(R.drawable.erro)
                .into(imageView);

    }

    private void ActionBAar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Spiner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> sl= new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(sl);
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarchitietsanpham);
        spinner = findViewById(R.id.spinner);
        txtName = findViewById(R.id.textviewtenchitietsanpham);
        txtGia = findViewById(R.id.textviewgiachitietsanpham);
        txtChitiet = findViewById(R.id.textviewmotachitietsanpham);
        button = findViewById(R.id.buttondatmua);
        imageView = findViewById(R.id.imageviewchitietsanpham);
    }
}