package com.example.weatherapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText city;
    private Button search;
    private TextView weather;
    private String url;
    @NonNull
    private String getWeatherData(String MyURL) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(MyURL);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            httpurlconnection.disconnect();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        String temperature = null;

        try {
            JSONObject jsonObject = new JSONObject(result.toString());
            JSONObject mainObject = jsonObject.getJSONObject("main");
            double temp = mainObject.getDouble("temp");


            temp = temp - 273.15;
            DecimalFormat df = new DecimalFormat("#.00");
            temp = Double.parseDouble(df.format(temp));
            temperature = String.valueOf(temp);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Temperature:" + temperature + "°C";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            search = (Button) findViewById(R.id.button);
            city = (EditText) findViewById(R.id.editTextText);
            weather = (TextView) findViewById(R.id.textView2);

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city = ((EditText) findViewById(R.id.editTextText)).getText().toString();
                    if (city.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=7a23d3bef5a3626e182f20bc4b3544ed";
                        String result = getWeatherData(url);
                        weather.setText(result);
                    }



                }
            });
            return insets;
        });
    }


}

