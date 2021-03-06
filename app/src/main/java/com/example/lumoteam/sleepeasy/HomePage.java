package com.example.lumoteam.sleepeasy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import java.util.Date;

public class HomePage extends AppCompatActivity {
    private android.widget.Button play;
    private android.widget.Button log;
    private android.widget.Button settings;
    private List<Integer> noises;
    private int isPlaying = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        String key = getApplicationContext().getString(R.string.preference_settings);
        SharedPreferences settingsPref = getApplicationContext().getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor settingsEdit = settingsPref.edit();
        String time_key = getApplicationContext().getString(R.string.play_time_min);
        if (!settingsPref.contains(time_key)) {
            settingsEdit.putInt(time_key, 60);
        }
        String duration_key = getApplicationContext().getString(R.string.sleep_duration_min);
        if (!settingsPref.contains(duration_key)) {
            settingsEdit.putInt(duration_key, 8*60);
        }
        settingsEdit.apply();

        SoundPlay.INSTANCE.init(getApplicationContext());
        NoisePickManager.INSTANCE.init(getApplicationContext(), 15);
        log = (Button) findViewById(R.id.Log);
        play = (Button) findViewById(R.id.Play);
        settings = (Button) findViewById(R.id.Settings);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long datetime = new Date().getTime();
                String key = getApplicationContext().getString(R.string.preference_getIntoBedDate);
                SharedPreferences datePref = getApplicationContext().getSharedPreferences(key, Context.MODE_PRIVATE);
                datePref.edit().putLong(key, datetime).apply();

                System.out.println(isPlaying);
                if (isPlaying == 0) {
                    isPlaying = 1;
                    noises = NoisePickManager.INSTANCE.pick(5);
                    System.out.println(noises);
                    SoundPlay.INSTANCE.play(noises);
                    play.setText("pause");
                }
                else if (isPlaying == 1){
                    isPlaying = -1;
                    SoundPlay.INSTANCE.pause();
                    play.setText("play");
                }
                else {
                    isPlaying = 1;
                    SoundPlay.INSTANCE.resume();
                    play.setText("pause");
                }

            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent intent = new Intent(HomePage.this, log.class);
                startActivity(intent);
            }

        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent intent = new Intent(HomePage.this, settings.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundPlay.INSTANCE.stop();
        getDelegate().onDestroy();
    }
}



