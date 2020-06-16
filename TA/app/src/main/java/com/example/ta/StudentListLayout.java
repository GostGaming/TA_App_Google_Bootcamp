package com.example.ta;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Stack;
import java.util.TreeSet;

public class StudentListLayout extends RelativeLayout {
    TreeSet<View> studentList = new TreeSet();


    public StudentListLayout(Context context){ super(context); }

    // create new textView, add it to studentList
    public void add(View name){
        studentList.add(name);
        this.addView(name);
    }

    public View remove(){
        // how would remove work? remove last kid asked, have button to remove?
        if(empty())
            //don't remove
            ;

        return null;
    }

    public boolean empty(){
        return studentList.isEmpty();
    }

    public TreeSet<View> get() {
        return studentList;
    }

}
