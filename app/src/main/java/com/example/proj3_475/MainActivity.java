package com.example.proj3_475;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    ConnectivityCheck myCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences defPreference = PreferenceManager.getDefaultSharedPreferences(this);
        myCheck = new ConnectivityCheck(this);
        doNetworkCheck(findViewById(android.R.id.content).getRootView());
        doWirelessCheck(findViewById(android.R.id.content).getRootView());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

    public void doNetworkCheck(View view) {
        String res = myCheck.isNetworkReachable() ? "Network Reachable" : "No Network";
        Toast t = Toast.makeText(this, res, Toast.LENGTH_SHORT);
        t.show();
    }

    public void doWirelessCheck(View view) {
        String res = myCheck.isWifiReachable() ? "WiFi Reachable" : "No WiFi";
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }
}
