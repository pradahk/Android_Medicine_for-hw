package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.security.MessageDigest;
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
        // id가 write_name인 editText에 대한 메서드 저장
        editTextName = findViewById(R.id.write_name);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextPhone = findViewById(R.id.write_phone);

    }

    // onClick signup
    public void singUp(View view) {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        name = editTextName.getText().toString();
        phone = editTextPhone.getText().toString();

        // 유효성 검사 후 회원가입 메서드 실행
        if(isValidEmail() && isValidPasswd() && isValidName() && isValidPhone()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("password", password);
                                user.put("name", name);
                                user.put("phone", phone);
                                firebaseFirestore.collection("users").document(name)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // 회원가입에 성공하면 "회원가입 성공" 토스트를 보여줌
                                                Toast.makeText(SignupActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                                                // 회원가입에 성공하면 로그인 레이아웃으로 이동
                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // 회원가입에 실패하면 "회원가입 실패" 토스트를 보여줌
                                                Toast.makeText(SignupActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(SignupActivity.this,"회원가입 양식을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 칸이 공백이면 false
            Toast.makeText(SignupActivity.this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, "이메일 형식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, "비밀번호 형식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 이름 유효성 검사
    private boolean isValidName() {
        if (name.isEmpty()) {
            // 이름 칸이 공백이면 false
            Toast.makeText(SignupActivity.this,"이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 전화번호 유효성 검사
    private boolean isValidPhone() {
        if (phone.isEmpty()) {
            // 전화번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this,"전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }






}


