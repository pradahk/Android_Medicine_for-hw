package com.example.androidlogin;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 작성한 이메일 값과 비밀번호 값을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextPassword;

    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private EditText editTextName;
    private EditText editTextPhone;

    private String email = "";
    private String password = "";
    private String name = "";
    private String phone = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = findViewById(R.id.write_email);
        // id가 signup_password인 editText에 대한 메서드 저장
        editTextPassword = findViewById(R.id.signup_password);
        //
        editTextName = findViewById(R.id.write_name);
        editTextPhone = findViewById(R.id.write_phone);

    }

    // onClick signup
    public void singUp(View view) {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        // 유효성 검사 후 회원가입 메서드 실행
        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 칸이 공백이면 false
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식이 불일치하면 false
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식이 불일치하면 false
            return false;
        } else {
            return true;
        }
    }


    // 회원가입 메서드
    private void createUser(final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user != null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(FirebaseID.documentId,user.getUid());
                                userMap.put(FirebaseID.email, editTextEmail.getText().toString());
                                userMap.put(FirebaseID.password, editTextPassword.getText().toString());
                                firebaseFirestore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                Toast.makeText(SignupActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            // 회원가입에 성공하면 "회원가입 성공" 토스트를 보여줌
                            Toast.makeText(SignupActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            // 회원가입에 성공하면 로그인 레이아웃으로 이동
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
                            // 회원가입에 실패하면 "회원가입 실패" 토스트를 보여줌
                            Toast.makeText(SignupActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}


