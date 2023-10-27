package com.example.assignment.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public  class checkConnection {
      public static boolean HaveNetworking(Context context){
          boolean wifi=false, mobile= false;
          ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
          for(NetworkInfo info : networkInfos){
           if(info.getTypeName().equals("WIFI")){
               if(info.isConnected()){
                   wifi= true;
               }
           }
              if(info.getTypeName().equals("MOBILE")){
                  if(info.isConnected()){
                      mobile= true;
                  }
              }
          }
          return wifi|| mobile;
      }
      public static void ShowToastLong(Context context,String str){
          Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
      }
}
