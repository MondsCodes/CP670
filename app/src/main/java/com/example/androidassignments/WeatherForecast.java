package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherForecast extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    private ImageView weatherImage;
    private TextView cityName;
    private TextView currentTemperature;
    private TextView minTemperature;
    private TextView maxTemperature;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImage = findViewById(R.id.weatherImage);
        cityName = findViewById(R.id.cityName);
        currentTemperature = findViewById(R.id.currentTemperature);
        minTemperature = findViewById(R.id.minTemperature);
        maxTemperature = findViewById(R.id.maxTemperature);
        progressBar = findViewById(R.id.weatherProgressBar);

        progressBar.setVisibility(View.VISIBLE);

        String city = getIntent().getStringExtra("city");
        if (city == null) {
            city = "Ottawa";
        }
        cityName.setText(city);

        Log.i(ACTIVITY_NAME, "Querying weather for " + city);
        new ForecastQuery().execute(city);
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private String iconName;
        private Bitmap weatherPicture;

        @Override
        protected String doInBackground(String... args) {
            String city = (args.length > 0 && args[0] != null) ? args[0] : "Ottawa";
            HttpURLConnection conn = null;
            try {
                String queryURL = "http://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(city, "UTF-8")
                        + ",ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&mode=xml&units=metric";
                Log.i(ACTIVITY_NAME, "Query URL: " + queryURL);
                URL url = new URL(queryURL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();
                        if ("temperature".equals(tagName)) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if ("weather".equals(tagName)) {
                            iconName = parser.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = parser.next();
                }
                inputStream.close();

                Log.i(ACTIVITY_NAME, "Parsed XML: current=" + currentTemp
                        + " min=" + minTemp + " max=" + maxTemp + " icon=" + iconName);

                if (iconName != null) {
                    String imageFileName = iconName + ".png";
                    Log.i(ACTIVITY_NAME, "Looking for image file: " + imageFileName);

                    if (fileExistance(imageFileName)) {
                        Log.i(ACTIVITY_NAME, "Found image " + imageFileName
                                + " locally, no download needed");
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(imageFileName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        weatherPicture = BitmapFactory.decodeStream(fis);
                    } else {
                        String imageURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                        Log.i(ACTIVITY_NAME, "Image " + imageFileName
                                + " not found locally, downloading from " + imageURL);
                        Bitmap image = HTTPUtils.getImage(imageURL);
                        if (image != null) {
                            weatherPicture = image;
                            FileOutputStream outputStream =
                                    openFileOutput(imageFileName, Context.MODE_PRIVATE);
                            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Saved image " + imageFileName + " to local storage");
                        }
                    }
                }
                publishProgress(100);
            } catch (IOException | XmlPullParserException e) {
                Log.e(ACTIVITY_NAME, "Error retrieving forecast", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return "Forecast finished";
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (currentTemp != null) {
                currentTemperature.setText(getString(R.string.current_temperature, currentTemp));
            }
            if (minTemp != null) {
                minTemperature.setText(getString(R.string.min_temperature, minTemp));
            }
            if (maxTemp != null) {
                maxTemperature.setText(getString(R.string.max_temperature, maxTemp));
            }
            if (weatherPicture != null) {
                weatherImage.setImageBitmap(weatherPicture);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
