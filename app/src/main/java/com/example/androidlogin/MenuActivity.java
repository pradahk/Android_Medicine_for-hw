package com.example.androidlogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // 파이어베이스 사용자 객체 생성
    private FirebaseUser user;

    // 프래그먼트 객체 생성
    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    // 탭 버튼 객체 생성
    private Button bt_tab1, bt_tab2, bt_tab3;

    // 로그인 및 로그아웃 버튼 객체 생성
    private Button singout_btn;
    private Button signin_btn;

    @Override public void onStart() {
        super.onStart();
        // 파이어베이스에 로그인 중인지 판단
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }


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

        // 어느 프레그먼트를 처음으로 프레임레이아웃에 띄울 것인지를 결정
        callFragment(FRAGMENT2);

        // 레이아웃의 signOutbutton에 로그아웃 버튼 저장
        singout_btn = findViewById(R.id.signOutbutton);

        // 레이아웃의 signInbutton에 로그인 버튼 저장
        signin_btn = findViewById(R.id.signInbutton);

        // 로그인 버튼 클릭시
        signin_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 로그인을 할 수 있는 MainActivity로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼 클릭시
        singout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 show
                show();
            }
        });

        // 로그인 중인 사용자가 있는 지 판단
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // 로그인한 사용자가 있는 경우
                if (user != null) {
                    // 로그아웃을 할 수 있게 로그아웃 버튼을 보여줌
                    singout_btn.setVisibility(View.VISIBLE);
                    // 로그인 중이라면 로그인 버튼은 필요하지 않으므로 로그인 버튼은 GONE 처리하여 버튼 영역까지 보이지 않게 처리
                    signin_btn.setVisibility(View.GONE);
                }
                // 로그인한 사용자가 없는 경우
                else {
                    // 로그아웃 중이라면 로그아웃 버튼은 필요하지 않으므로 로그아웃 버튼은 GONE 처리하여 버튼 영역까지 보이지 않게 처리
                  singout_btn.setVisibility(View.GONE);
                  // 로그인을 할 수 있게 로그인 버튼을 보여줌
                  signin_btn.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    // 프래그먼트 클릭시
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 첫번째 tab 클릭시
            case R.id.bt_tab1 :
                // FRAGMENT 1 보여줌
                callFragment(FRAGMENT1);
                break;
                // 두번째 tab 클릭시
            case R.id.bt_tab2 :
                // FRAGMENT 2 보여줌
                callFragment(FRAGMENT2);
                break;
                // 세번째 tab 클릭시
            case R.id.bt_tab3 :
                // FRAGMENT 3 보여줌
                callFragment(FRAGMENT3);
                break;

        }
    }

    private void callFragment(int frament_no){
        // 프래그먼트 사용을 위해 transaction 정의
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
        // Alert 다이얼로그 객체 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 생성한 다이얼로그을 보여줌
        builder.create().show();
        // 다이얼로그 셋팅
        builder.setTitle("로그아웃");
        builder.setMessage("정말 로그아웃을 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "예" 클릭시 로그아웃
                        FirebaseAuth.getInstance().signOut();
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



