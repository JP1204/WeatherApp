package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final static String URLEndpoint = "https://api.darksky.net/forecast/9e78fa234b6bef0d4cb81302992777a3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        // request location updates
        // LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // lm.requestLocationUpdate(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);


        final EditText dateEditText = (EditText) findViewById(R.id.dateEditText);
        final EditText timeEditText = (EditText) findViewById(R.id.timeEditText);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Date currentTime = Calendar.getInstance().getTime();
        dateEditText.setText(currentDateTimeString);
        timeEditText.setText(currentTime.toString());

        // get the current weather conditions when button is pushed
        Button getCurrWeatherButton = (Button) findViewById(R.id.currWeatherButton);
        getCurrWeatherButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
               Date currentTime = Calendar.getInstance().getTime();

               dateEditText.setText(currentDateTimeString);
               timeEditText.setText(currentTime.toString());

               // get current location
               Double longitude = 0.;
               Double latitude = 0.;

               try
               {
                   LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                   // Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                   Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                   if(location != null) {
                       longitude = (Double) location.getLongitude();
                       latitude = (Double) location.getLatitude();
                   }
                   else{
                       System.out.println("did not get location");
                   }
               }
               catch (SecurityException e){
                   Log.d(TAG, "Security Exception : ");
                   System.out.println("could not get location");
               }

               String longStr = Double.toString(longitude);
               String latStr = Double.toString(latitude);
               String url = URLEndpoint + latStr + "," + longStr;

               // get request
               Weather weather;
               final TextView weatherTextView = (TextView) findViewById(R.id.weatherTextView);
               OkHttpClient client = new OkHttpClient();

               Request request = new Request.Builder()
                       .url(url)
                       .build();

               client.newCall(request).enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       e.printStackTrace();
                       weatherTextView.setText("could not get location");
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       if(response.isSuccessful()){
                           final String myResponse = response.body().string();
                           MainActivity.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   // get response successful
                                   weatherTextView.setText(myResponse);
                               }
                           });
                       }
                   }
               });


/*
               DarkSky ds = new DarkSky();
               try {
                   weather = ds.getWeather(url);
               } catch(Exception e){
                   weatherTextView.setText("Could not retrieve weather");
                   return;
               }

               String weatherMessage = "Latitude = " + weather.getLatitude()
                       + "\nLongitude = " + weather.getLongitude()
                       + "\nTime Zone = " + weather.getTimezone();

               weatherTextView.setText(weatherMessage);*/

                // weatherTextView.setText("here");
           }
        });
    }
}
