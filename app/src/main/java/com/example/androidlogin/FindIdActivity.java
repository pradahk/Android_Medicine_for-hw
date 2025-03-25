// Firebase Firestore에서 사용자의 이메일을 찾는 기능을 제공하는 액티비티(Activity)
// 사용자가 이름과 전화번호를 입력하면, 해당 정보와 일치하는 이메일을 찾아서 화면에 표시하는 역할
package com.example.androidlogin;

//Firebase Firestore와 관련된 라이브러리를 import하여 데이터베이스에서 사용자 정보를 검색할 수 있도록 설정
import android.app.ProgressDialog; //ProgressDialog는 로딩 중임을 사용자에게 표시하는 다이얼로그
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindIdActivity extends AppCompatActivity {
//AppCompatActivity를 상속받아 Android의 기본 액티비티 기능을 사용

    // 파이어베이스 인증 객체 생성
    private FirebaseFirestore firebaseFirestore; //Firebase Firestore 객체 → 사용자 정보 검색에 활용

    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private EditText editTextName; //사용자가 입력하는 이름과 전화번호 필드
    private EditText editTextPhone;

    private String sendname = ""; //입력된 이름과 전화번호를 저장할 변수
    private String sendphone = "";

    // 회원의 이메일을 보여줄 textview 객체 생성
    private TextView textshowid; //찾은 이메일을 화면에 표시할 TextView
    private TextView textshowtext;

    // 로그인 화면으로 돌아가기 위한 버튼 객체 생성
    private Button btngotologin; //이메일을 찾은 후 로그인 화면으로 이동하는 버튼

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog; //데이터 로딩 중임을 표시하는 프로그레스 다이얼로그

    // 뒤로가기 버튼 클릭시 로그인 화면 갱신
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(getApplication(),MainActivity.class));
    }
    //사용자가 뒤로 가기 버튼을 누르면 이전 화면으로 이동
    //주석 처리된 startActivity() 코드가 실행되면 뒤로 가기 시 로그인 화면으로 이동하도록 설정 가능.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);
    //액티비티가 실행될 때 activity_findid.xml 레이아웃을 설정

        //XML 레이아웃에 정의된 UI 요소를 찾아서 변수에 저장
        editTextName = findViewById(R.id.write_name); // id가 write_email인 editText에 대한 메서드 저장
        editTextPhone = findViewById(R.id.write_phone); // id가 write_phone인 editText에 대한 메서드 저장
        textshowid = findViewById(R.id.tv_showid); // id가 tv_showid인 textview에 대한 메서드 저장
        textshowtext = findViewById(R.id.tv_showtext); // id가 tv_showtext인 textview에 대한 메서드 저장

        // 로그인 화면으로 돌아가는 버튼에 대한 메서드 저장
        btngotologin = findViewById(R.id.btn_gotologin); 

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        btngotologin.setOnClickListener(new View.OnClickListener(){//' 로그인 화면으로 가기 ' 버튼을 클릭하면 로그인 화면(MainActivity)으로 이동
            @Override
            public void onClick(View v){
                //로그인으로 돌아가기 버튼을 누르면 MainActivity로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); //finish()를 호출하여 현재 액티비티를 종료
            }
        });

    }

    public void findEmail(View view) //사용자가 입력한 이름과 전화번호를 문자열로 변환하여 저장
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendname = editTextName.getText().toString();
        sendphone = editTextPhone.getText().toString();
        // findid 메서드 실행
        findid(sendname, sendphone); //findid() 메서드를 호출하여 입력된 정보와 일치하는 이메일을 찾음
    }

    // 아이디 찾기 메서드
    private void findid(final String sendname, final String sendphone) {
        progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요..."); // 프로그레스 디이얼로그 생성하여 보여줌
        progressDialog.show();
        //로딩 중임을 표시하는 다이얼로그를 실행
        
        // 파이어스토어 가져옴
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users") // 파이어스토어의 collection 경로를 "users"로 생성
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { // 파이어스토어의 "users"에서 정보를 가져오는 것이 성공하면
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // doument의 id와 입력한 이름이 같고 저장된 휴대폰번호와 입력한 휴대폰 번호가 같으면 입력한 이름의 저장된 이메일을 보여줌
                                if (sendname.equals(document.getData().get("name")) && sendphone.equals(document.getData().get("phone"))) {
                                    textshowtext.setText("회원님의 이메일은 다음과 같습니다.");
                                    textshowid.setText(document.getData().get("email").toString());
                                    // 로그인 화면으로 가는 버튼을 VISIBLE 처리하여 보여줌
                                    btngotologin.setVisibility(View.VISIBLE);
                                    break;
                                }
                                // 입력한 정보와 파이어베이스에 저장된 정보가 다르면 일치하는 회원정보가 없다는 텍스트를 보여줌
                                else {
                                    textshowtext.setText("일치하는 회원정보가 없습니다.");
                                    textshowid.setText("");
                                }
                            }
                        }
                        // 프로그래스 다이얼로그 사라짐
                        progressDialog.dismiss();
                    }
                });

    }


}
