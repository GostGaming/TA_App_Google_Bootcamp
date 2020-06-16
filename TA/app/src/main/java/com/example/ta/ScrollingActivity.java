package com.example.ta;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {

    private LinearLayout studentLayout;
//    private StudentListLayout studentListLayout;

    ArrayList<String> names = new ArrayList<String>();
    HashMap<String, Student> studentList = new HashMap<String, Student>();
    HashMap<String, String> nameToId = new HashMap<String, String>();

    private int countid = 0;
    private int editMode;

    Context context;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // how to reference an xml from java
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AssetManager assetManager = getAssets();

        Log.d("jterm", "error below!");
        context = getApplicationContext();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        studentLayout = findViewById(R.id.verticalLayout);
        // studentListLayout is never used
//        studentListLayout = new StudentListLayout(this);
//        studentLayout.addView(studentListLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Remove mode on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // set to "Remove mode" where clicking an item will remove
                editMode = 1;

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Edit mode on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // set to "Edit mode" where clicking an item will allow you to change the name
                editMode = 2;
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Normal mode on", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // set to "Normal mode" where clicking an item will "bring you to the student's page"
                editMode = 0;
            }
        });

        TextView studentListTitle = findViewById(R.id.listofstudents);
        studentListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clickity clack, edit mode is "+editMode, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            InputStream inputStream = assetManager.open("names.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String studentinfo = line.trim();
                Log.d("jterm", studentinfo);
                Student studant = createStudentFromString(studentinfo);

                // three lines below can be reduced for better readability
                String name = studant.getName();
                int id = studant.getStudentId();
                Date date = studant.getBirthday();

                names.add(name);
                studentList.put(Integer.toString(id), studant);
                nameToId.put(name, Integer.toString(id));

                TextView tx = createStudentName(name);
                studentLayout.addView(tx);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Integer.toString(id), studant.toString());

                // split check
                Log.d("jterm", "name: "+name+" id: "+id+" birthday: "+date);
            }

        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        // display names using list from get() in StudentListLayout (never used)

        final EditText addstud = new EditText(this);
        addstud.setTextSize(15);
        addstud.setHint("Add a new student here (name, id, birthday[MM/dd/yyyy])");
        addstud.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addstud.setSingleLine(true);
                    addstud.setMaxLines(1);
                    addstud.setLines(1);
                }
            }
        });

        studentLayout.addView(addstud);

        addstud.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    // take text from the editText and create a new student
                    Log.d("jterm", "enter key pressed");
                    // then clear the text from editText
                    String typed = addstud.getText().toString();
                    if(typed.isEmpty()){

                    } else {
                        Log.d("jterm", "here's what u typed: " + typed);
                        addstud.getText().clear();

                        Student studant = createStudentFromString(typed);
                        studentList.put(Integer.toString(studant.getStudentId()), studant);

                        // something wrong below
                        nameToId.put(studant.getName(), Integer.toString(studant.getId()));

                        TextView tx = createStudentName(studant.getName());
                        EditText tempEt = addstud;
                        studentLayout.removeView(addstud);
                        studentLayout.addView(tx);
                        studentLayout.addView(addstud);
                    }

                }
                return false;
            }
        });

        for(Map.Entry mapElement: studentList.entrySet()){
            String id = (String) mapElement.getKey();

        }


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

    protected void onStart(View view){
        for(int i = 0; i < names.size(); i++){
            Log.d("jterm", "is this even being done?");
        }
    }

    public TextView createStudentName(String name){
        TextView tx = new TextView(this);
        tx.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tx.setText(name);
        tx.setTextSize(20);
        tx.setGravity(Gravity.CENTER);
        final int currentid = countid++;
        tx.setId(currentid);

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You just clicked " + ((TextView) findViewById(currentid)).getText().toString() + " and editMode is "+editMode, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(editMode == 1){
                    // remove student textView from layout
                    studentLayout.removeView(findViewById(currentid));
                    // find id of student's name, remove from studentList (*** below could be null ***)
//                    String idstr = nameToId.get(((TextView) findViewById(currentid)).getText().toString());
//                    studentList.remove(idstr);
                    --countid;
                } else if(editMode == 2) {
                    TextView snatch = findViewById(currentid);
                    snatch.setVisibility(View.INVISIBLE);
                    final EditText changeName = new EditText(getApplicationContext());
                    changeName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    changeName.setSingleLine(true);
                    changeName.setMaxLines(1);
                    changeName.setLines(1);
                    changeName.setHint("What should the new name be?");

                    changeName.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                                if(!changeName.getText().toString().isEmpty()){
                                    String newName = changeName.getText().toString();
                                    TextView temp = findViewById(currentid);
                                    temp.setText(newName);

//                                    // remove (name, id) and put (newName, same id)
                                    Log.d("jterm", "whats going on here: "+temp.getText().toString());
//
//                                    int tempid = Integer.parseInt(nameToId.get(temp.getText().toString()));
//
//                                    Log.d("jterm", "error above here!");
//                                    nameToId.remove(temp.getText().toString());
//                                    nameToId.put(newName, Integer.toString(tempid));
//
//
//                                    // get student obj from id, change name in student obj
//                                    studentList.get(tempid).setName(newName);
                                }
                                findViewById(currentid).setVisibility(View.VISIBLE);
                                studentLayout.removeView(changeName);
                            }
                            return true;
                        }
                    });

                    studentLayout.addView(changeName);

                } else if (editMode == 0) {
                    // go to each student's profile created by Ashok**************
                    // possibly need to have hashmap from (name, student id) and one from (student id, Student obj)

                    Intent intent = new Intent(view.getContext(), StudentActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });
        return tx;
    }

    // helper function. turns string "name, studentID#, birthday" into a Student object
    public Student createStudentFromString(String studentinfo){
        Student newbie;
        String[] separt = studentinfo.split(", ");

        String name = separt[0];
        int id = Integer.parseInt(separt[1]);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(separt[2]);
        } catch (ParseException e) {
            Log.d("jterm", "Birthday probably formatted wrong my guy: "+separt[2]);
            e.printStackTrace();
        }

        names.add(name);
        newbie = new Student(getApplicationContext(), name, id, date);

        return newbie;
    }
}
