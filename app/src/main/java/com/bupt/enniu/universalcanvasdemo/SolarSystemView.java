package com.bupt.enniu.universalcanvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by enniu on 15/11/23.
 */
public class SolarSystemView extends View {

    Paint paintSun;
    Paint paintEarth;
    Paint paintMoon;

    int radius_sun = 80;   //radius of sun
    int radius_earth = 40;  //radius of earth
    int radius_moon = 20;   //radius of moon

    Paint paintTrack_earth;
    Paint paintTrack_moon;

    int radiusTrack_earth = 300;  //radius of earth track
    int radiusTrack_moon = 100;     //radius of moon track

    final int time_gap = 100;   //绘制每一帧的时间间隔 ms
    int earth_time = 60 * (1000 / time_gap);  //在地球绕太阳公转一圈花费60s的情况下,计算地球公转一圈所需要的time_gap的总数
    int moon_time = earth_time * 30 / 365;  //在地球绕太阳公转一圈花费60s的情况下,计算月球绕地球公转一圈所需要的time_gap的总数

    int earth_start = 0;  //地球开始的位置
    int moon_start = 0;  //月球开始的位置

    public SolarSystemView(Context context) {
        super(context);
        init();
    }

    void init() {
        paintSun = new Paint();
        paintSun.setColor(Color.RED);
        paintSun.setStyle(Paint.Style.FILL);
        paintSun.setStrokeWidth(3);
        paintSun.setAntiAlias(true);

        paintEarth = new Paint();
        paintEarth.setColor(Color.BLUE);
        paintEarth.setStyle(Paint.Style.FILL);
        paintEarth.setStrokeWidth(3);
        paintEarth.setAntiAlias(true);

        paintMoon = new Paint();
        paintMoon.setColor(Color.YELLOW);
        paintMoon.setStyle(Paint.Style.FILL);
        paintMoon.setStrokeWidth(3);
        paintMoon.setAntiAlias(true);

        paintTrack_earth = new Paint();
        paintTrack_earth.setColor(Color.BLUE);
        paintTrack_earth.setStyle(Paint.Style.STROKE);
        paintTrack_earth.setStrokeWidth(3);
        paintTrack_earth.setAntiAlias(true);

        paintTrack_moon = new Paint();
        paintTrack_moon.setColor(Color.YELLOW);
        paintTrack_moon.setStyle(Paint.Style.STROKE);
        paintTrack_moon.setStrokeWidth(3);
        paintTrack_moon.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //太阳的圆心,太阳是恒定的,不变
        int sun_x = width / 2;
        int sun_y = height / 2;
        //太阳
        canvas.drawCircle(sun_x, sun_y, radius_sun, paintSun);
        canvas.drawCircle(sun_x, sun_y, radiusTrack_earth, paintTrack_earth);

        earth_start = earth_start % earth_time;
        moon_start = moon_start % moon_time;

        //地球的圆心,地球围着太阳转
        float earth_x, earth_y;


        earth_x = sun_x + radiusTrack_earth * (float) Math.cos(Math.PI / 180 * earth_start * (360f / earth_time));
        earth_y = sun_y + radiusTrack_earth * (float) Math.sin(Math.PI / 180 * earth_start * (360f / earth_time));

        canvas.drawCircle(earth_x, earth_y, radius_earth, paintEarth);
        canvas.drawCircle(earth_x, earth_y, radiusTrack_moon, paintTrack_moon);


        //月球的圆心,月亮围着地球转
        float moon_x, moon_y;

        moon_x = earth_x + radiusTrack_moon * (float) Math.cos(Math.PI / 180 * moon_start * (360f / moon_time));
        moon_y = earth_y + radiusTrack_moon * (float) Math.sin(Math.PI / 180 * moon_start * (360f / moon_time));

        canvas.drawCircle(moon_x, moon_y, radius_moon, paintMoon);

        earth_start++;
        moon_start++;

        //按预先设定的重复绘制,产生动画效果
        //这种方式,会产生大量的垃圾,占用很多内存,造成频繁GC,从而使系统卡顿
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                postDelayed(this, time_gap);

            }
        }, time_gap);
    }
}
