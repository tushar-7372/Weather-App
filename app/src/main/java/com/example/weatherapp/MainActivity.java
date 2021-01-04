package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* DownloadTask task = new DownloadTask();
        try {
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=London&appid=6df27dd455658fbb0b56309821c185fb");
            Log.i("Result", "json processing");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "cant process json");
        }*/

        editText=findViewById(R.id.editText);
        resultTextView=findViewById(R.id.resultTextView);
    }

    public void getWeather(View view)
    {

//        editText.getText().toString();
        DownloadTask task = new DownloadTask();
        try {
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=6df27dd455658fbb0b56309821c185fb");
            Log.i("Result", "json processing");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "cant process json");
        }

        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Could not find City", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.i("json",s);
//            Log.i("Hi","Inside on Post Execute");
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather info",weatherInfo);

                JSONArray array=new JSONArray(weatherInfo);

                String message="";
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonPart=array.getJSONObject(i);

                    String main=jsonPart.getString("main");
                    String des=jsonPart.getString("description");

                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));

                    if(!main.equals("") && !des.equals(""))
                    {
                        message+=main + " : " + des;
                    }
                }

                if(!message.equals(""))
                {
                    resultTextView.setText(message);
                }
                else{
                    /*message="City not found";
                    resultTextView.setText(message);*/
                    Toast.makeText(getApplicationContext(), "Could not find City", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find City", Toast.LENGTH_SHORT).show();
            }

        }

    }
}