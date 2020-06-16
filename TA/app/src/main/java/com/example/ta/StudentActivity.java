package com.example.ta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ta.R;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.pm.PackageManager.FEATURE_CAMERA_ANY;

public class StudentActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Student s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_layout);

        Log.d("debugme", "On Create, creating test student...");
        String formatPattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
        String dd = "07/17/1989";
        Date sd = null;

        try {
            sd = dateFormat.parse(dd);
            dd = dateFormat.format(sd);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        s = new Student(this, "Brandon Bryant", 123456, sd );

        placeImages(s.getStudentId());

        TextView studentNameView = findViewById(R.id.studentName);
        TextView studentIdView = findViewById(R.id.studentIdView);
        TextView studentDOBView = findViewById(R.id.studentDOBView);
        studentNameView.setText(s.getName());
        studentIdView.setText(String.valueOf(s.getStudentId()));
        studentDOBView.setText(s.getBirthday().toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPictureClick (View view){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Log.d("bogus", "Picture Button Clicked...");
        // only do this if there is a camera
        if (getPackageManager().hasSystemFeature(FEATURE_CAMERA_ANY)) {
            Log.d("bogus", "Creating Intent...");
            // get ready to open camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    // Needs to get current student id, so this should probably be called from student?
                    photoFile = createImageFile(s.getStudentId());
                    Log.d("bogus", "Image Created!!");
                } catch (IOException ex) {
                    Log.d("bogus", "ERROR! " + ex);
                }
                if (photoFile != null) {
                    // set path to file for some reason
                    currentPicPath = photoFile.getAbsolutePath();
                    Log.d("bogus", currentPicPath);
                    Log.d("bogus", "Getting URI... " );
                    // get file provider info stuff, important
                    Uri photoUri = FileProvider.getUriForFile(this,
                            "com.example.ta.fileprovider",
                            photoFile);

                    Log.d("bogus", "Requesting image capture...");
                    // place extra stuff at file path
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    // finally ask for image capture
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // After pic is taken, update list of pics
        placeImages(s.getStudentId());

    }

    private void placeImages(int id) {

        // Get pics into array for student
        ViewGroup picView = findViewById(R.id.studentPics);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/" + id + "/");
        File[] dirFiles = new File[0];
        try {
            dirFiles = storageDir.listFiles();
        } catch (Exception e) {
            Log.d("bogus","Error in listing files for placeImages...");
            e.printStackTrace();
        }
        // Get width of picview and seperate into 5 pieces, should create rows of 5


        for (File f : dirFiles) {
                // Create bitmap for each file

                Bitmap bmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                Log.d("bogus", "scaling image...");
                // Avoids bogus files, especially if camera crashes before pic is saved
                if (bmap != null) {
                    // scale down bmap for image before displaying, probably wrong way to do this
                    Bitmap scaled = Bitmap.createScaledBitmap(bmap, 270, 270, true);
                    // Create new imageview for each file
                    Log.d("bogus", "creating new imageview...");
                    ImageView bitImage = new ImageView(picView.getContext());
                    bitImage.setPadding(6, 6, 6, 6);
                    // attach together
                    bitImage.setImageBitmap(scaled);
                    // add the pair to the picview
                    picView.addView(bitImage);
                }
            }



    }

    String currentPicPath;
    private File createImageFile(int id) throws IOException {
        // Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = String.valueOf(id) + timeStamp;
        Log.d("bogus", "Image path: " + Environment.DIRECTORY_PICTURES);
        // get storage directory, also creates a new one if not present automatically
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/" + id + "/");
        File image = File.createTempFile(
                imageFileName,   // prefix
                ".jpg",   // format
                storageDir      // directory
        );
        // not sure if needed since it updates after every image file
        currentPicPath = image.getAbsolutePath();
        return image;
    }
}
