package com.example.a1712914;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    public static final int MODE_EDIT = 2;
    public static final int MODE_ADD = 1;
    public static final int RESULT_UNCHANGED = 0;
    public static final int RESULT_CHANGED = 1;
    public static final int POSITION_NONE = -1;

    public static int mode=0;
    public static int position = -1;
    public static int status=-1;
    public static int align;

    private static  ArrayList<Note> arrNote = new ArrayList<Note>();

    public void setData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("note",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String data = gson.toJson(arrNote);
        editor.putString("Note",data);
        editor.commit();
    }
    public void getData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("note", MODE_PRIVATE);
        Gson gson = new Gson();
        String readData = sharedPreferences.getString("Note", "");
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        arrNote = gson.fromJson(readData, type);
    }
    public void deleteItem()
    {
        if(mode==MODE_ADD)
        {
            finish();
        }
        else
        {
            arrNote.remove(position);
            setData();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getData();

        Intent intent = getIntent();
        mode = intent.getIntExtra("MODE",0);

        if(mode==MODE_EDIT) //Edit mode -> Get and display item's content on screen
        {
            position = intent.getIntExtra("POSITION",0);
            align = intent.getIntExtra("ALIGN",1);
            EditText title= findViewById(R.id.edit_title);
            EditText tags= findViewById(R.id.edit_tags);
            EditText content= findViewById(R.id.edit_content);
            TextView date = findViewById(R.id.edit_date);

            Note item = arrNote.get(position);
            title.setText(item.getTitle());
            tags.setText(item.getTags());
            content.setText(item.getContent());
            date.setText(item.getDate());

            switch(align){
                case 1:
                {
                    content.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                    title.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                    date.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                    break;
                }
                case 2:
                {
                    content.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                    title.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                    date.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                    break;
                }
                case 3:
                {
                    content.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                    title.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                    date.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                    break;
                }
            }

        }

        ImageButton left = findViewById(R.id.align_left);
        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                align = 1;
                EditText content= findViewById(R.id.edit_content);
                EditText title= findViewById(R.id.edit_title);
                TextView date = findViewById(R.id.edit_date);

                content.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                content.setGravity(Gravity.LEFT);
                title.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                title.setGravity(Gravity.LEFT);
                date.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_START);
                date.setGravity(Gravity.LEFT);
            }
        });

        ImageButton right = findViewById(R.id.align_right);
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                align = 2;
                EditText content= findViewById(R.id.edit_content);
                EditText title= findViewById(R.id.edit_title);
                TextView date = findViewById(R.id.edit_date);

                content.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                content.setGravity(Gravity.END);
                title.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                title.setGravity(Gravity.END);
                date.setTextAlignment(content.TEXT_ALIGNMENT_VIEW_END);
                date.setGravity(Gravity.END);
            }
        });

        ImageButton center = findViewById(R.id.align_center);
        center.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                align = 3;
                EditText content= findViewById(R.id.edit_content);
                EditText title= findViewById(R.id.edit_title);
                TextView date = findViewById(R.id.edit_date);

                content.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                content.setGravity(Gravity.CENTER);
                title.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                title.setGravity(Gravity.CENTER);
                date.setTextAlignment(content.TEXT_ALIGNMENT_CENTER);
                date.setGravity(Gravity.CENTER);
            }
        });

        ImageButton button3 = findViewById(R.id.delete);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                deleteItem();
            }
        });
        ImageButton button2 = findViewById(R.id.cancel);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        ImageButton button1 = findViewById(R.id.ok);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TextView title = findViewById(R.id.edit_title);
                TextView tags = findViewById(R.id.edit_tags);
                TextView content = findViewById(R.id.edit_content);
                TextView date = findViewById(R.id.edit_date);

                if(TextUtils.isEmpty(title.getText()) && TextUtils.isEmpty(tags.getText()) && TextUtils.isEmpty(content.getText()) && TextUtils.isEmpty(date.getText()))
                {
                    status = RESULT_UNCHANGED;
                }

                if(status==RESULT_UNCHANGED){
                    finish();
                }
                else {

                    Note newNote = new Note("", "", "", "");
                    newNote.setTitle(title.getText().toString());
                    newNote.setTags(tags.getText().toString());
                    newNote.setContent(content.getText().toString());

                    newNote.align = align;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
                    Date dateNow = new Date();
                    newNote.setDate(formatter.format(dateNow).toString());

                    if(mode==MODE_EDIT)
                    {
                        arrNote.remove(position);
                        arrNote.add(0, newNote);

                    }
                    else
                    {
                        if(arrNote==null)
                        {
                            arrNote= new ArrayList<Note>();
                        }
                        arrNote.add(0,newNote);
                    }

                    setData();
                    finish();
                }

            }
        });
    }
}
