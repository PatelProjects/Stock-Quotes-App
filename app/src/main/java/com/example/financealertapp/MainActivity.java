package com.example.financealertapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<example_item> exampleList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       createExampleList();
       buildRecyclerView();

       buttonInsert = findViewById(R.id.button_insert);
       buttonRemove = findViewById(R.id.button_remove);
       editTextInsert = findViewById(R.id.edittext_insert);
       editTextRemove = findViewById(R.id.edittext_remove);

       buttonInsert.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               int position = Integer.parseInt(editTextInsert.getText().toString());
               insertItem(position);
           }
       });

        buttonRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });

    }

    public void insertItem(int position){
        exampleList.add(position, new example_item(R.drawable.ic_android, "New", "New"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position){
        exampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void createExampleList(){
        exampleList = new ArrayList<>();
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
        exampleList.add(new example_item(R.drawable.ic_android, "Line 1", "Line 2"));
    }


    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }




}
