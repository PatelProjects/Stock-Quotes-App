package com.example.financealertapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<example_item> exampleList = new ArrayList<>();
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

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



}
