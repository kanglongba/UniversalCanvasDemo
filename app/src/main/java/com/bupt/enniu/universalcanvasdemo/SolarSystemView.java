package com.bupt.enniu.universalcanvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

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

    public static int time_gap = 60;   //绘制每一帧的时间间隔 ms
    int earth_time = 30 * (1000 / time_gap);  //在地球绕太阳公转一圈花费30s的情况下,计算地球公转一圈所需要的time_gap的总数
    int moon_time = earth_time * 30 / 365;  //在地球绕太阳公转一圈花费30s的情况下,计算月球绕地球公转一圈所需要的time_gap的总数

    int earth_start = 0;  //地球开始的位置,角度
    int moon_start = 0;  //月球开始的位置,角度

    //太阳的圆心,太阳是保持不变的
    int sun_x, sun_y;

    //地球的圆心,地球围着太阳转
    float earth_x, earth_y;

    //月球的圆心,月亮围着地球转
    float moon_x, moon_y;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //计算太阳的圆心坐标
        //把分配内存的操作放在onDraw外面,否则可能会引起频繁的内存分配和释放,造成内存抖动
        sun_x = width / 2;
        sun_y = height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //太阳
        canvas.drawCircle(sun_x, sun_y, radius_sun, paintSun);
        canvas.drawCircle(sun_x, sun_y, radiusTrack_earth, paintTrack_earth);

        earth_start = earth_start % earth_time;
        moon_start = moon_start % moon_time;



        //地球
        earth_x = sun_x + radiusTrack_earth * (float) Math.cos(Math.PI / 180 * earth_start * (360f / earth_time));
        earth_y = sun_y + radiusTrack_earth * (float) Math.sin(Math.PI / 180 * earth_start * (360f / earth_time));

        canvas.drawCircle(earth_x, earth_y, radius_earth, paintEarth);
        canvas.drawCircle(earth_x, earth_y, radiusTrack_moon, paintTrack_moon);



        //月亮
        moon_x = earth_x + radiusTrack_moon * (float) Math.cos(Math.PI / 180 * moon_start * (360f / moon_time));
        moon_y = earth_y + radiusTrack_moon * (float) Math.sin(Math.PI / 180 * moon_start * (360f / moon_time));

        canvas.drawCircle(moon_x, moon_y, radius_moon, paintMoon);

        earth_start++;
        moon_start++;

        //按预先设定的重复绘制,产生动画效果
        //(1)这种方式,会产生大量的垃圾,占用很多内存,造成频繁GC,从而使系统丢帧,影响用户体验
        /*
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                postDelayed(this, time_gap);

            }
        }, time_gap);
        */

        //(2)这种方式,不会引起丢帧,但是运行久了,OS会抛出错误,造成程序崩溃
        /*
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
            }
        },time_gap);
        */
    }
}
