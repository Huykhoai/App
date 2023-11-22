package com.example.assignment.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignment.Adapter.AdapterPhoto;
import com.example.assignment.Adapter.AdapterSanphammoinhat;
import com.example.assignment.Adapter.Adapterloaisp;
import com.example.assignment.Interface.interfaceRecycle;
import com.example.assignment.Modul.Giohang;
import com.example.assignment.Modul.Loaisanpham;
import com.example.assignment.Modul.Photo;
import com.example.assignment.Modul.Sanpham;
import com.example.assignment.R;
import com.example.assignment.util.checkConnection;
import com.example.assignment.util.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity   {
    public static ArrayList<Giohang> manggiohang;

         String user;
         Toolbar toolbar;
         DrawerLayout drawerLayout;
         RecyclerView recyclerView;
         ArrayList<Sanpham> mangmoinhat;
         AdapterSanphammoinhat adaptermoinhat;
         ArrayList<Loaisanpham> mangloaisp;
         Adapterloaisp adapterloaisp;
         ListView listView;
         int idloaisp;
         String tenloaisp="";
         String hinhanhloaisp="";
         //Viewpaper
        ViewPager2 viewPager2;
        List<Photo> listphoto;
        CircleIndicator3 circleIndicator3;
        Handler handler = new Handler(Looper.myLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                   int currentposision = viewPager2.getCurrentItem();
                   if(currentposision== listphoto.size()-1){
                      viewPager2.setCurrentItem(0);
                   }else {
                       viewPager2.setCurrentItem(currentposision+1);
                   }
            }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkConnection.HaveNetworking(getApplicationContext())){
            Anhxa();
            GetData();
            ActionBar();
            clickItem();
            getSanphammoinhat();
            ViewPager();
        }else {
            checkConnection.ShowToastLong(getApplicationContext(),"không có mạng");
        }

    }

    private void ViewPager() {
                 listphoto = mangQuangcao();
        AdapterPhoto adapterPhoto = new AdapterPhoto(this,listphoto);
        viewPager2.setAdapter(adapterPhoto);
        circleIndicator3.setViewPager(viewPager2);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable,3000);
    }

    private List<Photo> mangQuangcao(){
        ArrayList<Photo> mangquangcao = new ArrayList<>();
        mangquangcao.add(new Photo(R.drawable.dongho1));
        mangquangcao.add(new Photo(R.drawable.tainghe1));
        mangquangcao.add(new Photo(R.drawable.dongho2));
        mangquangcao.add(new Photo(R.drawable.tainghe2));
        mangquangcao.add(new Photo(R.drawable.dongho3));
        mangquangcao.add(new Photo(R.drawable.tainghe3));
      return mangquangcao;
    }

    private void getSanphammoinhat() {
       RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.sanphammoinhat, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {
               int id=0;
               String tensanpham = "";
               Integer giasanpham = 0;
               String hinhanhsanpham = "";
               String motasanpham = "";
               int idSanpham = 0;
               if(response != null){
                   for(int i=0; i< response.length();i++){
                       try {
                           JSONObject jsonObject = response.getJSONObject(i);
                           id = jsonObject.getInt("id");
                           tensanpham = jsonObject.getString("tensanpham");
                           giasanpham = jsonObject.getInt("giasanpham");
                           motasanpham = jsonObject.getString("motasanpham");
                           hinhanhsanpham = jsonObject.getString("hinhanhsanpham");
                           idSanpham  = jsonObject.getInt("idsanpham");

                       } catch (JSONException e) {
                           throw new RuntimeException(e);
                       }
                      mangmoinhat.add(new Sanpham(id,tensanpham,giasanpham,motasanpham,hinhanhsanpham,idSanpham));
                       adaptermoinhat.notifyDataSetChanged();
                   }

               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       requestQueue.add(jsonArrayRequest);
    }

    private void clickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i){
                   case 0:
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(intent);
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case 1:
                       Intent intent1 = new Intent(getApplicationContext(), DonghoActivity.class);
                       Bundle bundle= new Bundle();
                       bundle.putSerializable("idloaisanpham", mangloaisp.get(i).getId());
                       bundle.putString("user", user);
                       intent1.putExtras(bundle);
                       startActivity(intent1);
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case 2:
                       Intent intent2 = new Intent(getApplicationContext(), TaingheActivity.class);
                       intent2.putExtra("idloaitainghe", mangloaisp.get(i).getId());
                       startActivity(intent2);
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;

               }
            }
        });
    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.actionbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout =findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdanloaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i< response.length();i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        idloaisp = jsonObject.getInt("id");
                         tenloaisp = jsonObject.getString("tenloaisapham");
                         hinhanhloaisp = jsonObject.getString("hinhloaisanpham");
                         mangloaisp.add(new Loaisanpham(idloaisp,tenloaisp,hinhanhloaisp));
                         adapterloaisp.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
               mangloaisp.add(3,new Loaisanpham(0, "Đổi Mật khẩu", "https://img.freepik.com/premium-vector/password-reset-icon-flat-vector-design_116137-4571.jpg"));
               mangloaisp.add(4,new Loaisanpham(0, "Logout", "https://w7.pngwing.com/pngs/475/85/png-transparent-computer-icons-logout-angle-text-black-thumbnail.png"));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void Anhxa() {
        if(manggiohang ==null){
            manggiohang = new ArrayList<>();
        }else {

        }
        toolbar = findViewById(R.id.toolbarTrangchu);
        viewPager2 = findViewById(R.id.viewpaper);
        circleIndicator3 = findViewById(R.id.circleIndicator3);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisanpham(0, "Home", "https://cdn-icons-png.flaticon.com/512/25/25694.png"));
        adapterloaisp = new Adapterloaisp(getApplicationContext(), mangloaisp);
        listView = findViewById(R.id.listviewSanpham);
        listView.setAdapter(adapterloaisp);
        recyclerView = findViewById(R.id.recycleViewTrangchu);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        mangmoinhat = new ArrayList<>();
        adaptermoinhat = new AdapterSanphammoinhat(getApplicationContext(),mangmoinhat);
        adaptermoinhat.setOnItemClickListener(new interfaceRecycle() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(),ChitietsanphamActivity.class);
                intent.putExtra("thongtinchitiet", mangmoinhat.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptermoinhat);
        Intent intent = getIntent();
         user = intent.getStringExtra("user");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adaptermoinhat.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptermoinhat.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.giohang){
            Intent intent = new Intent(getApplicationContext(),GiohangActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}