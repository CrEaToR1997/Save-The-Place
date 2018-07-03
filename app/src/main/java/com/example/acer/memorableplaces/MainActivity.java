package com.example.acer.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ListView listView;
    public static ArrayList<String> arrayList = new ArrayList<>();
    public static ArrayList<LatLng> location = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        arrayList.add("Add places...");
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("place",p);
                startActivity(intent);
            }
        });
    }

    public void GoTo(View view){
    }
}
