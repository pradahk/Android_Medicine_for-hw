package com.example.androidlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // 비밀번호 정규식
    Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 작성한 이메일 값과 비밀번호 값과 이름 값을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;

    private String email = "";
    private String password = "";
    private String name = "";

    // 구글 로그인 객체 생성
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = findViewById(R.id.et_eamil);
        // id가 signup_password인 editText에 대한 메서드 저장
        editTextPassword = findViewById(R.id.et_password);

        // 회원가입 버튼 객체 생성
        Button signup_btn = findViewById(R.id.btn_signUp);
        // 비밀번호 재설정 버튼 객체 생성
        Button findpw_btn = findViewById(R.id.btn_findpw);
        // 이메일 찾기 버튼 객체 생성
        Button findid_btn = findViewById(R.id.btn_findid);

        // 구글 로그인 버튼 객체 생성
        signInButton = findViewById(R.id.signInButton);


        // 회원가입 버튼 onclicklistener 생성
        signup_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 회원가입 버튼을 누르면 회원가입 레이아웃으로 이동
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        // 비밀번호 재설정 버튼 onclicklistener 생성
        findpw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 재설정 버튼을 누르면 비밀번호 찾기 레이아웃으로 이동
                Intent intent = new Intent(getApplicationContext(),FindpwActivity.class);
                startActivity(intent);
            }
        });

        // 이메일 찾기 버튼 onclicklistener 생성
        findid_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 이메일 찾기 버튼을 누르면 이메일 찾기 레이아웃으로 이동
                 Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
                 startActivity(intent);
        }
        });

        // 구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 구글 로그인 버튼  onClicklistener 생성
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 클릭시 구글 로그인 메서드 실행
                signIn();
            }
        });

    }

    // 구글 로그인 메서드
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    // 파이어베이스와 구글 로그인 연결
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인에 성공하면 "로그인 성공" 토스트를 보여줌
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인에 실패하면 "로그인 실패" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 이메일 로그인
    public void signIn(View view) {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        // 유효성 검사 후 로그인 메서드 실행
        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
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
            // 비밀번호 칸이 공백이면 fasle
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식이 불일치하면 false
            return false;
        } else {
            return true;
        }
    }

    // 로그인 메서드
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인에 성공하면 "로그인 성공" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인에 실패하면 "로그인 실패" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}
