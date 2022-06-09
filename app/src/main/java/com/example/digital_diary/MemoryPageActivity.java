package com.example.digital_diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;



public class MemoryPageActivity extends AppCompatActivity {
    EditText entryTitle, entryText;
    TextView entryLocation, entryDate;
    ImageButton addButton;
    ImageView entryImage;
    VideoView entryVideo;
    DBHelper dbHelper;
    Entry entry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_page);

        defineVariables();

    }


    private void defineVariables() {
        entryDate = findViewById(R.id.editTextDate);
        entryTitle = findViewById(R.id.entryTitle);
        entryText = findViewById(R.id.entryText);
        entryLocation = findViewById(R.id.entryLocation);
        addButton = findViewById(R.id.addButton);
        entryImage = findViewById(R.id.entryImage);
        entryVideo = findViewById(R.id.entryVideo);
        dbHelper = new DBHelper(MemoryPageActivity.this);
        if (getIntent().hasExtra("Entry")) {
            entry = (Entry) getIntent().getSerializableExtra("Entry");
            entryTitle.setText(entry.getTitle());
            entryText.setText(entry.getEnrtyText());
            entryDate.setText(entry.getDate());
            entryLocation.setText(entry.getLocation());



            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEntry(entry);
                    finish();
                }
            });
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy \n HH:mm");
            Calendar c = Calendar.getInstance();
            String date = sdf.format(c.getTime());
            entryDate.setText(date);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveEntryToDatabase();
                    finish();
                }
            });
        }

    }

    public void updateEntry(Entry entry){
        if(entryTitle.getText().toString().equals("") && entryText.getText().toString().equals("")){
            Toast.makeText(this,"Anı baslıgı ve metin kısımlarını doldurmanız gerekmektedir.",Toast.LENGTH_SHORT);
        }else{
            entry.setTitle(entryTitle.getText().toString());
            entry.setEnrtyText(entryText.getText().toString());
            entry.setMediaPath("");
            dbHelper.updateEntry(entry);
            Toast.makeText(this,"Anı Guncellendi.",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEntryToDatabase(){
        if(entryTitle.getText().toString().equals("") && entryText.getText().toString().equals("")){
            Toast.makeText(this,"Anı baslıgı ve metin kısımlarını doldurmanız gerekmektedir.",Toast.LENGTH_SHORT);
        }else{
            Entry newEntry = new  Entry(entryTitle.getText().toString(),entryText.getText().toString(),entryLocation.getText().toString(),entryDate.getText().toString(),null);
            newEntry.setMediaPath("");
            newEntry.setPassword("");
            dbHelper.addEntry(newEntry);
            Toast.makeText(this,"Anı eklendi.",Toast.LENGTH_SHORT).show();
        }

    }






}