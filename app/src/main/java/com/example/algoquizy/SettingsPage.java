package com.example.algoquizy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class SettingsPage extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ImageButton breturn;
    androidx.appcompat.widget.SwitchCompat music;
    androidx.appcompat.widget.SwitchCompat sounds;
    boolean cmusic = true, csounds;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings_page);
        Intent intent = getIntent();
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        breturn = findViewById(R.id.returnButton);
        breturn.setOnClickListener(this::OnClick);
        music = findViewById(R.id.switchmusik);
        music.setOnClickListener(this::OnClick);
        sounds = findViewById(R.id.switchsounds);
        sounds.setOnClickListener(this::OnClick);
        mediaPlayer = MediaPlayer.create(SettingsPage.this, R.raw.algoquizy_thinking_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(!cmusic){
            mediaPlayer.stop();
            music.setChecked(false);
        }
        if(!csounds){
            sounds.setChecked(false);
        }
    }
    void OnClick(View view){
        if(view.getId() == breturn.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(SettingsPage.this, MainActivity.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        if(view.getId() == music.getId()){
            cmusic = !cmusic;
            if(!cmusic){
                mediaPlayer.stop();
            }
            else{
                mediaPlayer = MediaPlayer.create(SettingsPage.this, R.raw.algoquizy_thinking_music);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
        if(view.getId() == sounds.getId()){
            csounds = !csounds;
        }
    }
}