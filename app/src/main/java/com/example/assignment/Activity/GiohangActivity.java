package com.example.assignment.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.Adapter.AdapterGiohang;
import com.example.assignment.R;

import java.text.DecimalFormat;

public class GiohangActivity extends AppCompatActivity {
     Toolbar toolbar;
     ListView listView;
     public  static TextView Tvtongtien;
     TextView thongbao;
     Button btnXacnhan,btnQuayve;
     AdapterGiohang adapterGiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        Anhxa();
        ActionBar();
        ClickItemListView();
        EventButton();
        UpdateTongtien();
        CheckData();
    }

    private void CheckData() {
        if(MainActivity.manggiohang.size()<1){
            thongbao.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            adapterGiohang.notifyDataSetChanged();
        }else {
            thongbao.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            adapterGiohang.notifyDataSetChanged();
        }
    }

    public static void UpdateTongtien() {
         long tongtien = 0;
         for(int i=0; i<MainActivity.manggiohang.size();i++){
             tongtien += MainActivity.manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
         Tvtongtien.setText("Giá: "+ decimalFormat.format(tongtien));
    }

    private void EventButton() {
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.manggiohang.size()>0){
                    Intent intent = new Intent(getApplicationContext(), ThongtinkhachhangActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(GiohangActivity.this, "gio hang chua co san pham nao", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnQuayve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ClickItemListView() {
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if (MainActivity.manggiohang.size() ==0) {
                   thongbao.setVisibility(View.VISIBLE);
               }else {
                   MainActivity.manggiohang.remove(i);
                   adapterGiohang.notifyDataSetChanged();
                   UpdateTongtien();
                   if(MainActivity.manggiohang.size()==0){
                       thongbao.setVisibility(View.VISIBLE);
                   }else {
                       thongbao.setVisibility(View.INVISIBLE);
                       adapterGiohang.notifyDataSetChanged();
                       UpdateTongtien();
                   }
               }
           }
       });
    }


    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbargiohang);
        listView = findViewById(R.id.listviewgiohang);
        Tvtongtien = findViewById(R.id.textviewtongtien);
        thongbao = findViewById(R.id.textviewthongbao);
        btnQuayve=findViewById(R.id.buttontieptucmuahang);
        btnXacnhan = findViewById(R.id.buttonthanhtoangiohang);
        adapterGiohang = new AdapterGiohang(getApplicationContext(), MainActivity.manggiohang);
        listView.setAdapter(adapterGiohang);

    }


}