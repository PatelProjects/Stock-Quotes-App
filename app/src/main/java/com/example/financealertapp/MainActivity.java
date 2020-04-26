package com.example.financealertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        exampleList.add(position, new example_item(R.drawable.ic_android, "New", "New", "8", "5"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position){
        exampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void createExampleList(){
        exampleList = new ArrayList<>();

        new makeInitialList().execute("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=BNS.TO&apikey=TTFCPV9C687UNHKO");

    }


    public  class makeInitialList extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                return finalJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }

                try {
                    if (reader != null){
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject parentObject = new JSONObject(s);

                JSONObject finalOject = parentObject.getJSONObject("Global Quote");

                String symbol = finalOject.getString("01. symbol");
                String price = finalOject.getString("05. price");
                String changeNumber = finalOject.getString("09. change");
                String changePercentage = finalOject.getString("10. change percent");

                exampleList.add(new example_item(R.drawable.ic_uparrow, symbol, price, changeNumber, changePercentage));
                mAdapter.notifyItemInserted(exampleList.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
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
                public TextView change;


                public ExampleViewHolder(@NonNull View itemView) {
                    super(itemView);
                    mImageView = itemView.findViewById(R.id.imageView);
                    mTextView1 = itemView.findViewById(R.id.textView);
                    mTextView2 = itemView.findViewById(R.id.textView2);
                    change = itemView.findViewById(R.id.changeNumber);

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
                holder.change.setText(currentItem.getchangeNumber() + "(" + currentItem.getChangePercentage() + ")");
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
