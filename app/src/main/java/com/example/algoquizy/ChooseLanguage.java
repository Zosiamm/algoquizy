package com.example.algoquizy;

//import android.content.Intent;
import static java.lang.Math.min;

import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseLanguage extends AppCompatActivity {
    ImageButton breturn, bcpp, bpy;
    TextView title;
    int number, index, result;
    boolean cmusic, csounds;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choose_language);
        breturn = findViewById(R.id.returnButton);
        breturn.setOnClickListener(this::OnClick);
        bcpp = findViewById(R.id.cppButton);
        bcpp.setOnClickListener(this::OnClick);
        bpy = findViewById(R.id.pyButton);
        bpy.setOnClickListener(this::OnClick);
        title = findViewById(R.id.title);
        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        index = intent.getIntExtra("index", 0);
        result = intent.getIntExtra("result", 0);
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                ChooseLanguage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject array = new JSONObject(myResponse);
                            String stitle = array.getJSONArray("quizzes").getJSONObject(number).getString("title");
                            title.setText(stitle);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    /*private void OnClick(View view) {
                        String resourceName = view.getResources().getResourceName(view.getId());
                        String strnumber = "";
                        for(int i = 26; i < resourceName.length(); i++){
                            strnumber += resourceName.charAt(i);
                        }
                        int number = Integer.parseInt(strnumber);
                        int index = 0;
                        Intent i = new Intent(QuestionView.this, QuestionView.class);
                        i.putExtra("number", number);
                        i.putExtra("index", index);
                        i.putExtra("result", 0);
                        startActivity(i);
                        finish();
                    }*/
                });
            }
        });
    }
    void OnClick(View view){
        if(view.getId() == breturn.getId()){
            Intent i = new Intent(ChooseLanguage.this, ChooseQuiz.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        else if(view.getId() == bcpp.getId()){
            Intent i = new Intent(ChooseLanguage.this, QuestionView.class);
            i.putExtra("number", number);
            i.putExtra("index", index);
            i.putExtra("result", result);
            i.putExtra("language", "cpp");
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        else if(view.getId() == bpy.getId()){
            Intent i = new Intent(ChooseLanguage.this, QuestionView.class);
            i.putExtra("number", number);
            i.putExtra("index", index);
            i.putExtra("result", result);
            i.putExtra("language", "py");
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
    }
}