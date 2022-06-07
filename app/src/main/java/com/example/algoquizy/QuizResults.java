package com.example.algoquizy;

//import android.content.Intent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizResults extends AppCompatActivity {
    Button bforwardresult;
    TextView res;
    int result, count;
    boolean cmusic, csounds;
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quiz_results);
        bforwardresult = findViewById(R.id.forwardresultButton);
        res = findViewById(R.id.result);
        bforwardresult.setOnClickListener(this::OnClick);
        Intent intent = getIntent();
        result = intent.getIntExtra("result", 0);
        count = intent.getIntExtra("count", 0);
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        String text = String.valueOf(result) + "/" + String.valueOf(count);
        res.setText(text);
    }
    void OnClick(View view){
        if(view.getId()==bforwardresult.getId()){
            Intent i = new Intent(QuizResults.this, ChooseQuiz.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
    }
}