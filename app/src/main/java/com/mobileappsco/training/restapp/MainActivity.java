package com.mobileappsco.training.restapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb1;
    TextView tv_msg1, tv_result;
    EditText et_name, et_season;
    Button btn_search;
    RequestAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb1= (ProgressBar) findViewById(R.id.pb1);
        tv_msg1 = (TextView) findViewById(R.id.tv_msg1);
        et_name = (EditText) findViewById(R.id.et_name);
        et_season = (EditText) findViewById(R.id.et_season);
        btn_search = (Button) findViewById(R.id.btn_search);
        tv_result = (TextView) findViewById(R.id.tv_result);
        pb1.setVisibility(View.GONE);
    }

    public void performSearch(View view) {
        Log.d("MYTAG", "MainActivity.performSearch()");
        String name = et_name.getText().toString();
        String season = et_season.getText().toString();

        if (name.length()<1 && season.length()<1) {
            Toast.makeText(MainActivity.this, "Provide both, name and season number", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://www.omdbapi.com/?";
            try {
                url+= "&t=" + URLEncoder.encode(name, "UTF-8") +
                        "&Season=" + URLEncoder.encode(season, "UTF-8");
                myAsyncTask = new RequestAsyncTask();
                myAsyncTask.execute(url);
                Log.d("MYTAG", "MainActivity.performSearch() task launched "+url);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MYTAG", "ERROR: " + e.getMessage());

            }
        }
    }

    public class RequestAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pb1.setMax(100);
            pb1.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MYTAG", "RequestAsyncTask.onPreExecute()");
            pb1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("MYTAG", "RequestAsyncTask.onPreExecute()");
            tv_result.setText(result);
            pb1.setVisibility(View.GONE);
        }

    }

}
