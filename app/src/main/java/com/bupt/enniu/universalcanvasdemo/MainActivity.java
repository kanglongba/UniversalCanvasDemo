package com.bupt.enniu.universalcanvasdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(new SolarSystemView(MainActivity.this));


        final SolarSystemView solarSystemView = new SolarSystemView(MainActivity.this);

        setContentView(solarSystemView);

        //将重复操绘制太阳系的任务放在这里,可以解决放在onDraw里时的丢帧问题.
        solarSystemView.postDelayed(new Runnable() {
            @Override
            public void run() {
                solarSystemView.invalidate();
                solarSystemView.postDelayed(this, SolarSystemView.time_gap);

            }
        }, SolarSystemView.time_gap);

    }
}
