package com.example.assignment.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.QuickContactBadge;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment.Adapter.AdapterDongho;
import com.example.assignment.Adapter.AdapterTainghe;
import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.example.assignment.util.checkConnection;
import com.example.assignment.util.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaingheActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    ArrayList<Sanpham> mangtainghe;
    AdapterTainghe adaptertainghe;
    int idloaisanpham = 0;
    int page = 1;
    View footer;
    boolean isLoadMore= false;
    boolean  limitData=false;
    mHander mHander;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tainghe);
        if(checkConnection.HaveNetworking(getApplicationContext())){
            Anhxa();
            ActionBar();
            getData(page);
            getIdTainghe();
            LoadMore();
        }

    }

    private void LoadMore() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1 == i2 && i2!=0 &&isLoadMore == false && limitData ==false){
                    isLoadMore =true;
                    Thread thread = new ThreadData();
                    thread.start();
                }
            }
        });
    }

    public class mHander extends Handler{
          @Override
          public void handleMessage(@NonNull Message msg) {
              switch (msg.what){
                  case 0:
                      listView.addFooterView(footer);
                      break;
                  case 1:
                      getData(++page);
                      isLoadMore = false;
                      break;
              }
              super.handleMessage(msg);

          }
      }
      public class ThreadData extends Thread{
          @Override
          public void run() {
              mHander.sendEmptyMessage(0);
              try {
                  Thread.sleep(3000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Message message = mHander.obtainMessage(1);
                      mHander.sendMessage(message);
                      adaptertainghe.notifyDataSetChanged();
                  }
              });

              super.run();
          }
      }
    private void getIdTainghe() {
        idloaisanpham = getIntent().getIntExtra("idloaitainghe", -1);
        Log.d( "getIdTainghe: ", String.valueOf(idloaisanpham));
    }

    private void getData(int p) {
       final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String path = server.duongdansanpham+String.valueOf(p);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "jsonDongho: ", response.toString());
                int id=0;
                String tentainghe= "";
                Integer giatainghe = 0;
                String hinhanhtaighe = "";
                String motatainghe = "";
                int idSanpham = 0;
                if(response != null && response.length() !=2){
                    listView.removeFooterView(footer);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i<jsonArray.length();i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tentainghe = jsonObject.getString("tensanpham");
                            giatainghe = jsonObject.getInt("giasanpham");
                            motatainghe = jsonObject.getString("motasanpham");
                            hinhanhtaighe = jsonObject.getString("hinhanhsanpham");
                            idSanpham = jsonObject.getInt("idsanpham");
                            mangtainghe.add(new Sanpham(id, tentainghe, giatainghe,motatainghe,hinhanhtaighe, idSanpham));
                            adaptertainghe.notifyDataSetChanged();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    limitData =true;
                    listView.removeFooterView(footer);
                    checkConnection.ShowToastLong(getApplicationContext(), "không còn dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("idsanpham", String.valueOf(idloaisanpham));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }


    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarTainghe);
        listView = findViewById(R.id.listviewTainghe);
        mangtainghe = new ArrayList<>();
        adaptertainghe = new AdapterTainghe(getApplicationContext(), mangtainghe);
        adaptertainghe.notifyDataSetChanged();
        listView.setAdapter(adaptertainghe);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChitietsanphamActivity.class);
                intent.putExtra("thongtinchitiet", mangtainghe.get(i));
                startActivity(intent);
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.progressbar, null);
        mHander = new mHander();
    }
}