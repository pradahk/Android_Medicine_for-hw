package com.example.androidlogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 로그아웃 버튼 객체 생성
        Button singout_btn = findViewById(R.id.signOutbutton);

        // 로그아웃 버튼 onclicklistener 생성
        singout_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 다이얼로그 show
                show();
            }
        });
    }

    // 로그아웃 확인 다이얼로그 show 메서드
    void show(){
        // alert 다이얼로그 객체
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create().show();
        builder.setTitle("로그아웃");
        builder.setMessage("정말 로그아웃을 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Toast.makeText(MenuActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
        builder.show();
    }
}
