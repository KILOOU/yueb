package com.example.administrator.excelrwrite;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


import jxl.Sheet;
import jxl.Workbook;
import jxl.demo.Write;
import jxl.read.biff.BiffException;


public class MainActivity extends AppCompatActivity {
    Workbook history_book;
    private Handler handler;
    private int j=0;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
            setContentView(R.layout.activity_main);
        } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
            setContentView(R.layout.test_land);}

        try {
            history_book = Workbook.getWorkbook(getAssets().open("XXX.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        new TimeThread().start();
        final ImageView nImageView = findViewById(R.id.iv2);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1 & j <=60) {
                    Sheet sheet = history_book.getSheet(0);
                    String content = sheet.getCell(0, j).getContents();
                    j++;
                    i++;
                    TextView txt = findViewById(R.id.textView);
                    txt.setText("(" + i + ")" + " " + content);
                    Bitmap nBitmap = QRCodeUtil.createQRCodeBitmap(String.valueOf("（" + i + ")" + content), 1000, 1000);
                    nImageView.setImageBitmap(nBitmap);
                    saveBitmap(nBitmap,String.valueOf(j));

                }

                }

        };
    }
    public void saveBitmap(Bitmap bitmap,String fileName) {
        // 首先保存图片
        String time =  new SimpleDateFormat("yyyy_MM_dd").format(new Date().getTime());
        File appDir1 = new File(Environment.getExternalStorageDirectory(), "二维码");
        File appDir=new File(appDir1,time);
        final ImageView mImageView = findViewById(R.id.iv1);
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(String.valueOf(appDir),100,100);
        mImageView.setImageBitmap(mBitmap);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
       fileName +=  ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }}
        public class TimeThread extends Thread {
    //重写run方法
    @Override
    public void run() {
        super.run();
        // do-while  一 什么什么 就
        do {
            try {
                //每隔一秒 发送一次消息
                Thread.sleep(6000);
                Message msg = new Message();
                //消息内容 为MSG_ONE
                msg.what = 1;
                //发送
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }
    }

}









