package com.example.administrator.aynctasktest;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView news;
    private ImageView nImageView;
    String resultData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news = findViewById(R.id.news);
        nImageView = findViewById(R.id.iv2);
       final String url = new String("http://192.168.43.63:8080/timeNotifier_war/t?length=10");
        Timer timer=new Timer();//实例化Timer类
        timer.schedule(new TimerTask(){
            public void run(){
                new HttpGetTask().execute(url);
               }},0,1000);//五百毫秒
    }


    private class HttpGetTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL urls = new URL(params[0]);
                HttpURLConnection urlconn = (HttpURLConnection) urls.openConnection();
                InputStreamReader in = new InputStreamReader(urlconn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                String inputline = buffer.readLine();
              if ((inputline != null)) {
                    resultData = inputline;
                    System.out.println(resultData);
                }
                in.close();
                urlconn.disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return resultData;
        }

        @Override
        protected void onPostExecute(String resultData) {
            super.onPostExecute(resultData);
            news.setText(resultData);
            Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(resultData, 480, 480);
            nImageView.setImageBitmap(mBitmap);
        }
    }
}

