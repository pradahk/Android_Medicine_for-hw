package com.example.androidlogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    // 바텀 네비게이션 뷰
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentMainMenu fragmentMainMenu;
    private FragmentInfo fragmentInfo;
    private FragmentAlram fragmentAlram;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuItem:
                        setFrag(0);
                        break;
                    case R.id.infoItem:
                        setFrag(1);
                        break;
                    case R.id.alramItem:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        fragmentMainMenu = new FragmentMainMenu();
        fragmentInfo = new FragmentInfo();
        fragmentAlram = new FragmentAlram();
        // 첫 fragment 화면 지정
        setFrag(0);

        // 로그아웃 버튼 객체 생성
        Button singout_btn = findViewById(R.id.signOutbutton);

        // 로그아웃 버튼 onclicklistener 생성
        singout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 show
                show();
            }
        });
    }

    // fragment 교체가 일어나는 실행문
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, fragmentMainMenu);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, fragmentInfo);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fragmentAlram);
                ft.commit();
                break;
        }
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
                        // "예" 클릭시 로그아웃
                        firebaseAuth.signOut();
                        Toast.makeText(MenuActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "아니오" 클릭시 다이얼로그 취소
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}



