package com.example.algoquizy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public ImageButton bsettings;
    public ImageButton bstart;
    ImageView logo;
    MediaPlayer mediaPlayer;
    boolean music, csounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        music = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        bsettings = findViewById(R.id.settingsButton);
        bsettings.setOnClickListener(this::OnClick);
        bstart = findViewById(R.id.startButton);
        bstart.setOnClickListener(this::OnClick);
        logo = findViewById(R.id.logoImage);
        logo.setImageDrawable(getDrawable(R.drawable.logo));
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.algoquizy_thinking_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(!music){
            mediaPlayer.stop();
        }
    }
    void OnClick(View view){
        if(view.getId()==bsettings.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(MainActivity.this, SettingsPage.class);
            i.putExtra("music", music);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        if(view.getId()==bstart.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(MainActivity.this, ChooseQuiz.class);
            i.putExtra("music", music);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
    }
}