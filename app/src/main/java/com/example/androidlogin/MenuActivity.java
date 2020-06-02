package com.example.androidlogin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 프래그먼트 객체 생성
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    // 탭 버튼 객체 생성
    private Button bt_tab1, bt_tab2, bt_tab3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       // 위젯에 대한 참조
        bt_tab1 = findViewById(R.id.bt_tab1);
        bt_tab2 = findViewById(R.id.bt_tab2);
        bt_tab3 = findViewById(R.id.bt_tab3);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);
        bt_tab3.setOnClickListener(this);

        // 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 결정
        callFragment(FRAGMENT1);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :
                callFragment(FRAGMENT1);
                break;

            case R.id.bt_tab2 :
                callFragment(FRAGMENT2);
                break;

            case R.id.bt_tab3 :
                callFragment(FRAGMENT3);
                break;
        }
    }

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '회원정보'프래그먼트 호출
                FragmentInfo fragmentInfo = new FragmentInfo();
                transaction.replace(R.id.fragment_container, fragmentInfo);
                transaction.commit();
                break;

            case 2:
                // '메인메뉴'프래그먼트 호출
                FragmentMainMenu fragmentMainMenu = new FragmentMainMenu();
                transaction.replace(R.id.fragment_container, fragmentMainMenu);
                transaction.commit();
                break;

            case 3:
                // '알람'프래그먼트 호출
                FragmentAlarm fragmentAlarm = new FragmentAlarm();
                transaction.replace(R.id.fragment_container, fragmentAlarm);
                transaction.commit();
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



