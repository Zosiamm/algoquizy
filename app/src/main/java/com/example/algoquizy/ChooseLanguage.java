package com.example.algoquizy;

//import android.content.Intent;
import static java.lang.Math.min;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
//import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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
        JSONObject array = null;
        try {
            array = new JSONObject(loadContentFromFile(ChooseLanguage.this, "db.json"));
            String stitle = array.getJSONArray("quizzes").getJSONObject(number).getString("title");
            title.setText(stitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public static InputStream loadInputStreamFromAssetFile(Context context, String fileName){
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadContentFromFile(Context context, String path){
        String content = null;
        try {
            InputStream is = loadInputStreamFromAssetFile(context, path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }
}