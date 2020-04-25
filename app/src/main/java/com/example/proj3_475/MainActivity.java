package com.example.proj3_475;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    ConnectivityCheck myCheck;
    String info_choice = "CNU - Defender";
    String URL = "https://www.pcs.cnu.edu/~kperkins/pets/";


    ViewPager2 viewpager2;
    RecyclerViewAdapter adapt;


    private ImageView petImageview;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        viewpager2 = findViewById(R.id.vp2);
        adapt = new RecyclerViewAdapter(this);
        viewpager2.setAdapter(adapt);

//        petImageview = findViewById(R.id.imgView);
//        errorTextView = findViewById(R.id.errorText);

        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        a.registerOnSharedPreferenceChangeListener(this);
        getPrefValues(a);

        myCheck = new ConnectivityCheck(this);


//        runDownloadJSON();
////        runDownloadImage("p0.png");
        doNetworkCheck(findViewById(android.R.id.content).getRootView());
        //doWirelessCheck(findViewById(android.R.id.content).getRootView());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getPrefValues(PreferenceManager.getDefaultSharedPreferences((Context) this));
        if (key.equals("website")) {
            Toast.makeText(getApplicationContext(), "URL is " + info_choice, Toast.LENGTH_SHORT).show();
            if (!myCheck.isNetworkReachable()) {
                petImageview.setImageResource(R.drawable.error404);
                errorTextView.setText("Network unreachable! Turn on network to see cuter pets!");
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                // spinner.setVisibility(View.VISIBLE);
                Log.w("Preferences Changed", "trying to download JSON");
                //runDownloadJSON();
              //  runDownloadImage("p0.png");
            }
        }
    }

    private void getPrefValues(SharedPreferences settings) {
        this.info_choice = settings.getString("website", "CNU - Defender");
        this.URL = settings.getString("website", "https://www.pcs.cnu.edu/~kperkins/pets/");
        adapt.info_choice = this.info_choice;
        adapt.URL = this.URL;
    }


//    public void runDownloadImage(String imageFile) {
//        String fullImageURL = URL + imageFile;
//        new DownloadIMG().execute(new String[]{fullImageURL});
//    }

//    public void runDownloadJSON() {
//        new DownloadJSON().execute(new String[]{getString(R.string.json)});
//    }

    //inflate settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //settings selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        info_choice = a.getString("website", "CNU - Defender");
        URL = a.getString("website", "https://www.pcs.cnu.edu/~kperkins/pets/");
    }

    public void doNetworkCheck(View view) {
//        ImageView img = (ImageView) findViewById(R.id.vp2);
        //      img.setImageResource(R.drawable.error404);
        String res = myCheck.isNetworkReachable() ? "Network Reachable" : "No Network";
       // Toast t = Toast.makeText(this, res, Toast.LENGTH_SHORT);
      //  t.show();
    }

    public void doWirelessCheck(View view) {
        String res = myCheck.isWifiReachable() ? "WiFi Reachable" : "No WiFi";
       // Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }


}



