package com.example.proj3_475;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    ConnectivityCheck myCheck;
    String info_choice;
    List<pet> pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupSimpleSpinner();


        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        a.registerOnSharedPreferenceChangeListener(this);
        getPrefValues(a);

        myCheck = new ConnectivityCheck(this);

        doNetworkCheck(findViewById(android.R.id.content).getRootView());
        doWirelessCheck(findViewById(android.R.id.content).getRootView());
    }

    Spinner spinner;

    private void setupSimpleSpinner() {
        //create a data adapter to fill above spinner with choices
        //R.array.numbers is arraylist in strings.xml
        //R.layout.spinner_item_simple is just a textview
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.info_choice, R.layout.spinner_item_simple);

        //get a reference to the spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //bind the spinner to the datasource managed by adapter
        if (spinner != null)
            spinner.setAdapter(adapter);
        //respond when spinner clicked
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                if (arg0.getChildAt(SELECTED_ITEM) != null) {
                    ((TextView) arg0.getChildAt(SELECTED_ITEM)).setTextColor(Color.WHITE);
                    Toast.makeText(MainActivity.this, (String) arg0.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getPrefValues(PreferenceManager.getDefaultSharedPreferences((Context) this));
    }

    private void getPrefValues(SharedPreferences settings) {
        this.info_choice = settings.getString("website", "CNU - Defender");
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        SharedPreferences a = PreferenceManager.getDefaultSharedPreferences(this);
        info_choice = a.getString("website", "CNU - Defender");

    }

    public void doNetworkCheck(View view) {
        ImageView img = (ImageView) findViewById(R.id.imgView);
        img.setImageResource(R.drawable.error404);
        String res = myCheck.isNetworkReachable() ? "Network Reachable" : "No Network";
        Toast t = Toast.makeText(this, res, Toast.LENGTH_SHORT);
        t.show();
    }

    public void doWirelessCheck(View view) {
        String res = myCheck.isWifiReachable() ? "WiFi Reachable" : "No WiFi";
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }


}

class pet {
    public String name;
    public String url;
}
