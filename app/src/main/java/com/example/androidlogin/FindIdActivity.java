// Firebase Firestore에서 사용자의 이메일을 찾는 기능을 제공하는 액티비티(Activity)
// 사용자가 이름과 전화번호를 입력하면, 해당 정보와 일치하는 이메일을 찾아서 화면에 표시하는 역할
package com.example.androidlogin;

//Firebase Firestore와 관련된 라이브러리를 import하여 데이터베이스에서 사용자 정보를 검색할 수 있도록 설정
import android.app.ProgressDialog; //ProgressDialog는 로딩 중임을 사용자에게 표시하는 다이얼로그
//밑에 "처리 중입니다. 잠시만 기달려 주세요."를 보면 알 수 있음.
import android.content.Intent; //Android의 컴포넌트(액티비티, 서비스 등) 간 데이터를 주고받는 데 사용
//밑에 intent 부분을 보면, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK를 추가하여 기존 액티비티 스택을 정리하고 새로 시작함
import android.os.Bundle; //액티비티 간 데이터를 저장하고 전달하는 객체
//밑에 bundle에서 .onCreate를 보면 액티비티가 생성될 때 상태 정보를 저장하고 전달하는 데 사용
import android.util.Patterns; //정규 표현식 패턴을 제공하는 유틸리티 클래스. 이메일 주소나 웹 URL이 유효한지 확인할 때 주로 사용
//(없을?)
import android.view.View; //UI 요소의 기본 클래스. 버튼, 텍스트뷰, 에디트텍스트 등 모든 UI 요소는 View를 상속받음.
//밑에 View.VISIBLE을 사용하여 버튼의 가시성을 조정함
import android.widget.Button; //사용자가 클릭할 수 있는 버튼 정의
//버튼을 찾아서(findViewById) 클릭 이벤트를 설정하는 데 사용. clicklistener로 조정한
import android.widget.EditText; //사용자가 텍스트를 입력할 수 있는 입력 필드
//사용자가 입력한 이름과 전화번호를 가져오는 역할
import android.widget.TextView; //텍스트를 화면에 표시하는 데 사용
//textshowtext와 textshowid는 검색 결과를 사용자에게 보여주는 역할
import android.widget.Toast; //짧은 시간 동안 화면에 메시지를 띄우는 UI 요소
//(없음?)

import androidx.annotation.NonNull; //null이 될 수 없는 변수, 매개변수, 또는 반환 값을 표시하는 어노테이션(주석?)
import androidx.appcompat.app.AppCompatActivity; //Android의 기본 Activity 클래스를 확장한 버전으로, 앱이 최신 UI 및 기능과 호환되도록 돕는 역할. Android의 ' Compatibility Support Library '에 속함.

import com.google.android.gms.tasks.OnCompleteListener; //비동기 작업(예: Firestore 데이터 가져오기, Firebase 인증 등)이 완료되었을 때 실행되는 콜백 인터페이스
import com.google.android.gms.tasks.Task; //비동기 작업의 결과를 나타내는 클래스. 성공 or 실패 여
import com.google.firebase.auth.FirebaseAuth; //Firebase Authentication을 사용하여 로그인, 로그아웃, 사용자 인증을 처리하는 클래스. Google, Facebook, 이메일 등을 이용한 로그인 기능을 제공
//(없음?)
import com.google.firebase.firestore.DocumentReference; //Firestore 데이터베이스에서 특정 문서를 참조하는 객체. 특정 문서의 데이터를 읽거나 수정할 때 사용
//(없음?)
import com.google.firebase.firestore.DocumentSnapshot; //Firestore 데이터베이스의 단일 문서를 나타내는 객체. 문서의 데이터를 가져오거나 필드를 읽을 때 사용
//(없음?)
import com.google.firebase.firestore.FirebaseFirestore; //Firebase의 클라우드 데이터베이스 Firestore에 접근하는 객체. 데이터를 읽고 쓰거나 업데이트할 때 사용
//밑에 Firebase의 Firestore 데이터베이스와 연결하는 객체. instance를 생각하면 되는듯.
import com.google.firebase.firestore.QueryDocumentSnapshot; //Firestore 쿼리 결과에서 개별 문서를 나타내는 객체. 위 DocumentSnapshot과 비슷하지만 반복문에서 사
import com.google.firebase.firestore.QuerySnapshot; //Firestore에서 여러 개의 문서를 가져왔을 때, 그 결과를 나타내는 객체

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
