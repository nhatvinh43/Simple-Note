package com.example.a1712914;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {
    public static final int MODE_ADD = 1;
    private RecyclerView recyclerView;
    public static ArrayList<Note> arrNote = new ArrayList<>();
    private StaggeredGridViewAdapter adapter;

    public void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("note", MODE_PRIVATE);
        Gson gson = new Gson();
        String readData = sharedPreferences.getString("Note", "");
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        arrNote = gson.fromJson(readData, type);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();

        adapter = new StaggeredGridViewAdapter(MainActivity.this, this, arrNote);
        recyclerView = findViewById(R.id.gv_note);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);



        SharedPreferences sharedPreferences = getSharedPreferences("note", MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                getData();
                adapter.itemList = arrNote;
                adapter.filteredItemList = arrNote;
                adapter.notifyDataSetChanged();
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });



        FloatingActionButton floatingActionButton = findViewById(R.id.addNote);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
                editIntent.putExtra("MODE", MODE_ADD);
                startActivity(editIntent);
            }
            });
        }

        @Override
        public void onResume()
        {
            super.onResume();
            getData();
            if(arrNote!=null)
            {
                adapter.itemList = arrNote;
                adapter.filteredItemList = arrNote;
                adapter.notifyDataSetChanged();
            }
        }

}
