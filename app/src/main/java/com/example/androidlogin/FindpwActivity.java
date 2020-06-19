package com.example.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindpwActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 작성한 이메일 값을 저장할 객체 생성
    private EditText editTextEmail;
    private String sendemail = "";

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog;

    // 뒤로가기 버튼 클릭시 로그인 화면 갱신
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(),MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = findViewById(R.id.write_email);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

    }

    public void findPw(View view)
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendemail = editTextEmail.getText().toString();

        // 이메일 유효성 검사 후 findpw 메서드 실행
        if(isValidEmail()){
            findpw(sendemail);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (sendemail.isEmpty()) {
            // 이메일 칸이 공백이면 false
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(sendemail).matches()) {
            // 이메일 형식이 불일치하면 false
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호를 찾기 위한 findpw 메서드
    private void findpw(String sendemail) {
        // 프로그래스 다이얼로그 생성하여 보여줌
        progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
        progressDialog.show();
            //비밀번호 재설정 이메일 보내기
            firebaseAuth.sendPasswordResetEmail(sendemail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // 이메일이 정상적으로 전송됐으면 "이메일을 발송하였습니다." 토스트를 보여줌
                                        Toast.makeText(FindpwActivity.this, R.string.emailsending, Toast.LENGTH_LONG).show();
                                        finish();
                                        // 이메일을 정상적으로 보낸 후 로그인 화면으로 이동
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        // 이메일이 정상적으로 전송되지 않았다면 "이메일 발송에 실패하였습니다." 토스트를 보여줌
                                        Toast.makeText(FindpwActivity.this, R.string.emailfail, Toast.LENGTH_LONG).show();
                                    }
                                    // 프로그래스 다이얼로그 사라짐
                                    progressDialog.dismiss();
                                }
                            });
    }
}



