package com.example.digital_diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EntriesListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton addEntryButton,mapButton;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_list);
        dbHelper= new DBHelper(EntriesListActivity.this);
        defineVariables();

    }

    @Override
    protected void onResume() {
        super.onResume();
        defineVariables();
    }

    public void defineVariables(){
        recyclerView= (RecyclerView) findViewById(R.id.entryListView);
        addEntryButton = (ImageButton) findViewById(R.id.addEntryButton);
        mapButton =  (ImageButton)findViewById(R.id.mapButton);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Directed to Memory Page...",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),MemoryPageActivity.class);
                startActivity(intent);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Entry> entries = new ArrayList<Entry>();
                entries= dbHelper.getAllEntriesWitHLocation();
                Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("entries",entries);
                startActivity(intent);
            }
        });

        EntryAdapter adapter=new EntryAdapter(dbHelper.getAllEntries());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}