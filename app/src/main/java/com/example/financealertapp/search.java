package com.example.financealertapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class search extends AppCompatActivity {

    CustomAdapter arrayAdapter;
    ArrayList<String> mylist;
    public static String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ListView listView = findViewById(R.id.my_list);

        mylist = new ArrayList<>();

        mylist.add("Eraser");
        mylist.add("Books");
        mylist.add("Pencil");
        mylist.add("Pen");
        mylist.add("Copy");
        mylist.add("Ruler");

//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);

        arrayAdapter = new CustomAdapter(mylist,this);

        listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText != ""){
                    new searchAPICall().execute("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + newText + "&apikey=TTFCPV9C687UNHKO");
                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    public class CustomAdapter extends BaseAdapter{

        private List<String> listOf;
        private Context context;

        public CustomAdapter(List<String> listOf, Context context) {
            this.listOf = listOf;
            this.context = context;
        }

        @Override
        public int getCount() {
            return listOf.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.activity_search_item, null);

            TextView itemText = view.findViewById(R.id.SearchItem);

            itemText.setText(listOf.get(position));

            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    result = listOf.get(position).substring(0, listOf.get(position).indexOf(':'));
                    intent.putExtra("option", result);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

            return view;
        }
    }


    public class searchAPICall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                return finalJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
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

                JSONArray finalOject = parentObject.getJSONArray("bestMatches");

                mylist.clear();

                for (int i = 0; i < finalOject.length(); i += 1){
                    JSONObject rec = finalOject.getJSONObject(i);
                    mylist.add(rec.getString("1. symbol") + ": " + rec.getString("2. name"));
                }

                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }


}
