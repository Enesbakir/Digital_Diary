package com.example.digital_diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EntriesListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton addEntryButton;
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
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Succesfully Logined",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),MemoryPageActivity.class);
                startActivity(intent);
            }
        });
        EntryAdapter adapter=new EntryAdapter(dbHelper.getAllEntries());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}