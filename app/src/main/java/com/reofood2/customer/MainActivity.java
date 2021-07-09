package com.reofood2.customer;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private LinearLayout noInternet;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    String url = "https://reo.technofelia.com/delivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        noInternet = findViewById(R.id.noInternet);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().getAllowFileAccessFromFileURLs();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);

        webView.setWebChromeClient(new WebChromeClient()
                                   {
                                       public void onGeolocationPermissionsShowPrompt(String origin,
                                                                                      GeolocationPermissions.Callback callback) {
                                           callback.invoke(origin, true, false);
                                       }


                                   }



        );

        webView.setWebViewClient(new WebViewClient()


        );



        checkInternet();
        requestLocationPermission();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                notificationBell();
//                  runs a method every 2000ms
//       example    runThisEvery2seconds();
            }
        }, 2000);







    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }



    private void checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()|| mobile.isConnected()){
            webView.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.INVISIBLE);
            webView.loadUrl(url);
        }else{
            webView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission already granted", LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }





    }
    public void notificationBell(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://reo.technofelia.com/public/api/delivery/get-new-orders/";


 //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("response",response);

//                     Display the first 500 characters of the response string.
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error ->error.getMessage());


                 queue.add(stringRequest);





    }



}