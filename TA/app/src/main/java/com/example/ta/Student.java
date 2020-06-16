package com.example.ta;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.valueOf;

public class Student extends AppCompatImageView {
    private int id;
    private String name;
    private Date birthday;

    public Student(Context context, String name, int id, Date birthday){
        super(context);
        this.name = name;
        this.id = id;
        this.birthday = birthday;

    }

    public String getName(){
        return name;
    }

    public String setName(String newName){
        String oldName = this.name;
        this.name = newName;
        return oldName;
    }


    public int getStudentId(){
        return id;
    }

    public Date getBirthday(){
        return birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        String strname = this.name;
        int strid = this.id;
        SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
        String strdate = spf.format(this.birthday);

        return strname+", "+strid+", "+strdate;
    }
}
