package com.example.digital_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;


public class MemoryPageActivity extends AppCompatActivity {
    EditText entryTitle, entryText;
    TextView entryLocation, entryDate;
    ImageButton addButton,pdfButton;
    ImageView entryImage;
    ConstraintLayout constraintLayout;
    VideoView entryVideo;
    DBHelper dbHelper;
    Entry entry;
    double longtitude, latitude;
    Uri imageUri;
    int SELECT_PICTURE = 200;
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.d("TAG", "onLocationResult: " + location.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_page);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        defineVariables();
    }


    private void defineVariables() {
        entryDate = findViewById(R.id.editTextDate);
        entryTitle = findViewById(R.id.entryTitle);
        entryText = findViewById(R.id.entryText);
        addButton = findViewById(R.id.addButton);
        entryImage = findViewById(R.id.entryImage);
        entryLocation= findViewById(R.id.entryLocation);
        entryVideo = findViewById(R.id.entryVideo);
        pdfButton = findViewById(R.id.pdfButton);
        constraintLayout = findViewById(R.id.memoryConstraint);
        entryImage.setImageResource(R.drawable.download_icon_image_viewer_1329319935813321436_256);
        entryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertToPdf();
            }
        });
        dbHelper = new DBHelper(MemoryPageActivity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (getIntent().hasExtra("Entry")) {
            entry = (Entry) getIntent().getSerializableExtra("Entry");
            entryTitle.setText(entry.getTitle());
            entryText.setText(entry.getEnrtyText());
            entryDate.setText(entry.getDate());
            entryLocation.setText(entry.getLocation());
            if (entry.getMediaPath() != null) {
                entryImage.setImageURI(Uri.parse(entry.getMediaPath()));
            }

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEntry(entry);

                }
            });
        } else {

            //date
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy \n HH:mm");
            Calendar c = Calendar.getInstance();
            String date = sdf.format(c.getTime());
            entryDate.setText(date);

            //location
            entryLocation = findViewById(R.id.entryLocation);

            checkPermission();
            fusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    // Got last known location. In some rare situations this can be null.
                    Location location = task.getResult();
                    if (location != null) {
                        // Logic to handle location object
                        try {
                            Geocoder geocoder = new Geocoder(MemoryPageActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            entryLocation.setText(addresses.get(0).getAdminArea()+"/"+addresses.get(0).getCountryName());
                            longtitude = addresses.get(0).getLongitude();
                            latitude = addresses.get(0).getLatitude();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveEntryToDatabase(entryLocation.getText().toString());
                }
            });
        }
    }
    public void updateEntry(Entry entry) {
        if (entryTitle.getText().toString().equals("") || entryText.getText().toString().equals("")) {
            Toast.makeText(this, "Anı baslıgı ve metin kısımlarını doldurmanız gerekmektedir.", Toast.LENGTH_SHORT).show();
        } else {
            entry.setTitle(entryTitle.getText().toString());
            entry.setEnrtyText(entryText.getText().toString());
            if (imageUri != null) {
                entry.setMediaPath(imageUri.toString());
            }
            dbHelper.updateEntry(entry);
            Toast.makeText(this, "Anı Guncellendi.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void saveEntryToDatabase(String location) {
        if (entryTitle.getText().toString().equals("") || entryText.getText().toString().equals("")) {
            Toast.makeText(this, "Anı baslıgı ve metin kısımlarını doldurmanız gerekmektedir.", Toast.LENGTH_SHORT).show();
        } else {
            Entry newEntry = new Entry(entryTitle.getText().toString(), entryText.getText().toString(), entryLocation.getText().toString(), entryDate.getText().toString(), null);
            if (imageUri != null) {
                newEntry.setMediaPath(imageUri.toString());
            }
            newEntry.setLocation(location);
            newEntry.setLongtidude(longtitude);
            newEntry.setLatidude(latitude);
            newEntry.setPassword("");
            dbHelper.addEntry(newEntry);
            Toast.makeText(this, "Anı eklendi.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void addImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                imageUri = selectedImageUri;
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    entryImage.setImageURI(selectedImageUri);
                }
            }
        }
    }


    private void startLocationUpdates() {
        checkPermission();
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
    }
    private void checkWritePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
    }
    private void convertToPdf(){
        checkWritePermission();

        String path = Environment.getExternalStorageDirectory()+"/hello/";
        File file = new File(path+entryTitle.getText()+".pdf");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block e.printStackTrace(); }
            }
        }

        try{
            OutputStream outputStream= new FileOutputStream(file);
            PdfWriter writer =new PdfWriter(file);
            PdfDocument document = new PdfDocument(writer);
            Document doc = new Document(document);
            document.setDefaultPageSize(PageSize.A3);

            doc.add(new Paragraph("Title: "+entryTitle.getText()));
            doc.add(new Paragraph( "Text: "+entryText.getText()));
            doc.add(new Paragraph("" +entryDate.getText()));
            doc.add(new Paragraph(""+entryLocation.getText()));
            document.close();
            Toast.makeText(this, "Converted to PDF.", Toast.LENGTH_SHORT).show();
            Log.d("OK", "done");
        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
}