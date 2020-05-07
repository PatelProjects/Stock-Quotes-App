package com.example.financealertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static com.example.financealertapp.search.result;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> stocksString;
    private ArrayList<example_item> exampleList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private example_item tempExampleItem;

    private static Queue<String> APIs;
    private static int APIcounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stocksString = new ArrayList<>();
        APIs = new LinkedList<>();

        populateAPIs();

        try {
            FileInputStream fis = null;
            fis = openFileInput("alice.csv");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
            
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                stocksString.add((String) jsonArray.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        createExampleList();
        buildRecyclerView();


    }

    private void populateAPIs() {
        APIs.add("TTFCPV9C687UNHKO");
        APIs.add("T4ZADFK2F3N7661O");
        APIs.add("M2OBDY72H4K1P3C9");
        APIs.add("C77RO63XNNI28VKU");

        APIcounter = 5;
    }

    public static void checkChangeAPI(){

        Log.d("MMMMMMMM", Integer.toString(APIcounter));

        APIcounter -= 1;



        if (APIcounter == 1){
            changeAPI();
        }
    }

    private static void changeAPI(){

        Log.d("LLLLLLLLL", APIs.peek());

        APIs.add(APIs.poll());
        APIcounter = 5;

        Log.d("LLLLLLLLL", APIs.peek());
    }

    @Override
    protected void onStop() {

        FileOutputStream fos = null;
        
        File internalStorageDir = getFilesDir();
        File alice = new File(internalStorageDir, "alice.csv");

        stocksString.clear();

        for (int i = 0; i < exampleList.size(); i += 1){
            stocksString.add(i, exampleList.get(i).getmText1());
        }

        try {
            fos = new FileOutputStream(alice, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            JSONArray jsArray = new JSONArray(stocksString);
            fos.write(jsArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("option");
                    Log.d("res", newText);
                    new makeInitialList().execute("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                            + newText + "&apikey=" + APIs.peek(), "newinsert");
                }
                break;
            }
        }
    }



    public void insertItem(int position, example_item example_item){
        exampleList.add(position, example_item);
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position){
        exampleList.remove(position);

        mAdapter.notifyItemRemoved(position);
    }

    public void createExampleList(){
        exampleList = new ArrayList<>();

        for (String stockName: stocksString) {
            new makeInitialList().execute("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stockName + "&apikey=" + APIs.peek(), "insert");
        }
    }

    public  class makeInitialList extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {

            Log.d("ZZZZZZZZZZZZZZZZZ", urls[0]);

            checkChangeAPI();

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                String type = urls[1];
                String param = null;

                if (urls.length >= 3){
                    param = urls[2];
                }

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

                ArrayList<String> ret = new ArrayList<String>();
                ret.add(finalJson);
                ret.add(type);
                ret.add(param);

                return ret;

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
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);

            try {

                JSONObject parentObject = new JSONObject(s.get(0));
                Log.d("resss", s.get(0));

                JSONObject finalOject = parentObject.getJSONObject("Global Quote");

                String symbol = finalOject.getString("01. symbol");
                String price = finalOject.getString("05. price");
                String changeNumber = finalOject.getString("09. change");
                String changePercentage = finalOject.getString("10. change percent");

                int image = 0;

                if (Double.parseDouble(changeNumber) > 0){
                    image = R.drawable.ic_up_arrow;
                }
                else if (Double.parseDouble(changeNumber) < 0){
                    image = R.drawable.ic_down_arrow;
                }

                tempExampleItem = new example_item(image, symbol, price, changeNumber, changePercentage);

                if(s.get(1) == "newinsert"){
                    insertItem(0, tempExampleItem);
//                    stocksString.add(0, tempExampleItem.getmText1());
                }
                else if(s.get(1) == "insert"){
                    insertItem(0, tempExampleItem);
                }
                else if(s.get(1) == "update"){
                    exampleList.set(Integer.parseInt(s.get(2)), tempExampleItem);
                    mAdapter.notifyItemChanged(Integer.parseInt(s.get(2)));
                }

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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
            case R.id.addNew:

                Intent intent = new Intent(this, search.class);
                startActivityForResult(intent, 1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;

        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case (ItemTouchHelper.LEFT):
                    removeItem(position);
                    break;

                case (ItemTouchHelper.RIGHT):
                    break;
            }
        }
    };


}
