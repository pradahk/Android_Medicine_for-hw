package com.example.androidlogin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RecomMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<FormDrug> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recom_list);

    }

    //증상에 맞는 약 json파일에서 검색
    public void recomJson() {
        try {
            InputStream is = getAssets().open("recom.json"); //assests파일에 저장된 druglist_final.json 파일 열기
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("recomlist"); //json파일에서 의약품리스트의 배열명, jsonArray로 저장

            list = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}