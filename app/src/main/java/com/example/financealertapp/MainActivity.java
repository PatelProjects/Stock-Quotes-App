package com.example.financealertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


        class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

            private ArrayList<example_item> mExampleList;

            class ExampleViewHolder extends RecyclerView.ViewHolder{
                public ImageView mImageView;
                public TextView mTextView1;
                public TextView mTextView2;

                public ExampleViewHolder(@NonNull View itemView) {
                    super(itemView);
                    mImageView = itemView.findViewById(R.id.imageView);
                    mTextView1 = itemView.findViewById(R.id.textView);
                    mTextView2 = itemView.findViewById(R.id.textView2);
                }
            }

            ExampleAdapter(ArrayList<example_item> exampleList){
                this.mExampleList = exampleList;
            }

            @NonNull
            @Override
            public ExampleAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
                ExampleAdapter.ExampleViewHolder evh = new ExampleAdapter.ExampleViewHolder(v);
                return evh;
            }

            @Override
            public void onBindViewHolder(@NonNull ExampleAdapter.ExampleViewHolder holder, int position) {
                example_item currentItem = mExampleList.get(position);
                holder.mImageView.setImageResource(currentItem.getmImageResource());
                holder.mTextView1.setText(currentItem.getmText1());
                holder.mTextView2.setText(currentItem.getmText2());
            }

            @Override
            public int getItemCount() {
                return mExampleList.size();
            }
        }

        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
