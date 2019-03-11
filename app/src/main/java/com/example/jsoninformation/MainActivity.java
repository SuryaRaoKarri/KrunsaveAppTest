package com.example.jsoninformation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    String Userlocation1 = "";
      String result = "";

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlconnection = null;
            try {
                result = "";
                url = new URL(urls[0]);

                urlconnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlconnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result +=current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override // Called when the doInbackground method is finised and can't updated in the UI
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("Web Content here = ", result);

        }
    }

    public void ButtonClick(View view) throws JSONException {
        DownloadTask task = new DownloadTask();
        task.execute("http://10.0.2.2:5000/api/values");
        Toast.makeText(this,result, Toast.LENGTH_LONG).show();
        Toast.makeText(this, Userlocation1, Toast.LENGTH_LONG).show();
       // JSONObject jsonObject = new JSONObject(result);
        //JSONArray arr = new JSONArray(jsonObject.getString("data"));
        //JSONObject onepart1 = arr.getJSONObject(0);
        //Toast.makeText(this,onepart1.getString("id"), Toast.LENGTH_LONG).show();
        //Toast.makeText(this,result, Toast.LENGTH_LONG).show();

    }

    //Add Permision Maps

    LocationManager locationManager;
    LocationListener locationListiner;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListiner);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       DownloadTask task = new DownloadTask();
        task.execute("http://10.0.2.2:5000/api/values");

        //Location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListiner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("User Current Location:", location.toString());
                Userlocation1 = location.toString();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission already
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListiner);

        }
    }
}
