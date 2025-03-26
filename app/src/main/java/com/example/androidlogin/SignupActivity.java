//회원가입(SignUp) 화면을 구현한 코드로, 사용자가 입력한 정보(이메일, 비밀번호, 이름, 전화번호 등)를 firebase를 이용하여 회원가입하도록 함.
//또한, 회원가입 후 이메일 인증을 위한 다이얼로그를 띄우는 부분도 포함함.
package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; 

import com.google.android.gms.tasks.OnCompleteListener; //여기까지 import는 id찾기에서 다 봤지?
import com.google.android.gms.tasks.OnFailureListener; //Firebase 작업이 실패했을 때 호출되는 콜백
import com.google.android.gms.tasks.OnSuccessListener; //이건 성공했을때
import com.google.android.gms.tasks.Task; //이것도 봤지?
import com.google.firebase.auth.AuthResult; //Firebase 인증 관련 작업에서 인증 결과를 담고 있는 객체. 사용자가 로그인하거나 회원가입을 완료하면, 성공 여부 또는 인증된 사용자의 정보를 담아둠.
import com.google.firebase.auth.FirebaseAuth; //이것도 봤지?
import com.google.firebase.auth.FirebaseUser; //Firebase Authentication을 통해 인증된 사용자의 정보를 나타내는 클래스. 현재 로그인한 사용자의 이메일, UID 같은 정보를 제공하며, 이메일 인증을 보내거나, 사용자 데이터를 업데이트할 때 사용할 수 있음.
import com.google.firebase.firestore.FirebaseFirestore; //이것도 봤지?

import java.util.HashMap; //키와 값의 쌍으로 데이터를 저장하는 자료구조. 사용자의 이메일, 비밀번호, 이름, 전화번호 등을 HashMap에 저장하고 Firestore에 전송
import java.util.Map; //HashMap과 같은 자료구조. HashMap은 Map의 구현체이므로 Map을 사용하여 다른 구현체도 사용 가능. 
//밑에서 Firestore에 데이터를 저장할 때 Map<String, Object> 타입을 사용
import java.util.regex.Pattern; //정규식을 처리하는 데 사용.
//밑에서 유효성 검사할 때 사용되는듯.

import static com.example.androidlogin.FirebaseID.user;

public class SignupActivity extends AppCompatActivity {

    // 뒤로가기 버튼 클릭시 로그인 화면 갱신
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(),MainActivity.class));
    }

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$"); //4~16자 사이의 영문자, 숫자, 그리고 특수문자만 포함된 비밀번호를 검증
    //^: 문자열의 시작. 문자열의 처음부터 검사.
    //[a-zA-Z0-9!@.#$%^&*?_~]: 허용되는 문자 정의. 
    //{4,16}: 비밀번호의 길이는 최소 4자 이상, 최대 16자 이하.
    //$: 문자열의 끝. 문자열의 끝까지 검사.
    
    // 이메일 정규식
    public static final Pattern EMAIL_ADDRESS = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //[A-Z0-9._%+-]+: 이메일의 사용자 이름 부분. 대소문자 알파벳 (A-Z), 숫자 (0-9), 점 (.), 밑줄 (_), 퍼센트 (%), 더하기 (+), 하이픈 (-) 문자를 하나 이상 포함할 수 있음.
    //[A-Z0-9.-]+: 이메일의 도메인 이름 부분. 대소문자 알파벳 (A-Z), 숫자 (0-9), 점 (.), 하이픈 (-) 문자를 하나 이상 포함할 수 있음.
    //\\.: 점(.)을 의미함. 점은 정규식에서 특별한 의미이므로 \\로 이스케이프 처리 해줌.
    //[A-Z]{2,6}: 도메인의 최상위 도메인(TLD)부분을 의미함. 예를 들어, com, org, net 등과 같은 2~6자의 알파벳 구성.
    //Pattern.CASE_INSENSITIVE: 대소문자를 구분하지 않도록 설정
    
    // 전화번호 정규식
    public static final Pattern PHONE_PATTERN = Pattern.compile("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", Pattern.CASE_INSENSITIVE); //대한민국 전화번호 형식 점검
    //01: 전화번호가 01로 시작해야 한다는 규칙.
    //(?:0|1|[6-9]): 01 다음에 오는 숫자는 0, 1, 또는 6~9 사이의 숫자.
    //(?:\\d{3}|\\d{4}): 그 다음에 3자리 또는 4자리 숫자가 올 수 있음.
    //\\d{4}: 마지막으로 4자리 숫자가 와야함.
    
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //Firebase Authentication을 이용하여 사용자 인증을 처리
    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance(); //Firebase Firestore를 사용하여 사용자 정보를 데이터베이스에 저장

    // 작성한 이메일 값과 비밀번호 값을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextPassword;

    // 비밀번호 확인에 작성한 값을 저장할 객체 생성
    private EditText editTextPasswordCheck;

    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private EditText editTextName;
    private EditText editTextPhone;

    // 회원정보에 저장할 값 객체 생성
    private String email = "";
    private String password = "";
    private String passwordCheck = "";
    private String name = "";
    private String phone = "";

    // 이메일 인증 다이얼로그 객체 생성
    private AuthemailDialog authemailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //UI 요소 초기화. 각 EditText는 사용자가 입력한 이메일, 비밀번호, 비밀번호 확인, 이름, 전화번호 등을 받기 위한 UI 요소
        editTextEmail = findViewById(R.id.write_email); // id가 write_email인 editText에 대한 메서드 저장
        editTextPassword = findViewById(R.id.signup_password); // id가 signup_password인 editText에 대한 메서드 저장
        editTextPasswordCheck = findViewById(R.id.check_password); // id가 check_password인 editText에 대한 메서드 저장
        editTextName = findViewById(R.id.write_name); // id가 write_name인 editText에 대한 메서드 저장
        editTextPhone = findViewById(R.id.write_phone); // id가 write_phone인 editText에 대한 메서드 저장

        //사용자에게 이메일 인증을 안내하는 다이얼로그
        authemailDialog = new AuthemailDialog(this, positiveListener); // 다이얼로그의 리스너 등록

    }

    // 이메일 인증 다이얼로그의 확인버튼 클릭시
    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            // 다이얼로그 종료
            authemailDialog.dismiss();
            // 로그인 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    };

    // onClick signup - 회원가입 버튼 클릭 시 실행되는 singUp 메서드
    public void singUp(View view) {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        passwordCheck = editTextPasswordCheck.getText().toString();
        name = editTextName.getText().toString();
        phone = editTextPhone.getText().toString(); //가져온 변수를 각각 email, password, passwordCheck, name, phone 변수에 저장함.

        // 유효성 검사 후 회원가입 실행
        if (isValidEmail() && isValidPasswd() && isValidPasswdcheck() && isValidName() && isValidPhone()) { //유효성 검사 통과해서 true면
            firebaseAuth.createUserWithEmailAndPassword(email, password) //Firebase Authentication으로 이메일과 비밀번호로 사용자 계정 생성
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //회원가입 요청이 완료되면 addOnCompleteListener가 호출되어 task.isSuccessful()로 성공 여부 확인
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 입력한 email, password, name, phone 값을 파이어스토어에 저장
                                Map<String, Object> user = new HashMap<>(); //사용자 정보를 담을 맵 생성, 이메일-비밀번호-이름-전화번호 값을 맵에 추가
                                user.put("email", email);
                                user.put("password", password);
                                user.put("name", name);
                                user.put("phone", phone);
                                firebaseFirestore.collection("users").document(email)
                                        .set(user) //Firestore의 users 컬렉션에 사용자 이메일을 문서 이름으로 하여 저장
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {//저장 성공하면 onSuccess호출, 이메일 인증 메일을 발송하기 위한 sendEmailVerification() 메서드 호출+다이얼로그 띄움.
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // 정보 저장이 성공적으로 이루어지면 이메일 인증 메일 발송
                                                sendEmailVerification(); //Firebase의 sendEmailVerification() 메서드?
                                                //  이메일 인증 다이얼로그 보여줌
                                                authemailDialog.show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {//실패가 발생하면 onFailure 리스너 호출
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // 회원가입에 실패하면 "회원가입 실패" 토스트를 보여줌
                                                Toast.makeText(SignupActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else { //이미 회원 존재해도 토스트로 알려줌
                                Toast.makeText(SignupActivity.this, R.string.alreadyemail, Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }


    // 이메일 인증 메서드 - 이메일 인증 메일 발송
    public void sendEmailVerification() {
        FirebaseAuth auth = FirebaseAuth.getInstance(); // 파이어베이스 인승 객체 가져옴
        FirebaseUser user = auth.getCurrentUser(); // 현재 사용자 가져옴
        // 사용자가 있을 때
        assert user != null; //assert는 user객체가 null이 아님을 보장. 만약 user가 null이면, 예외가 발생하여 앱이 강제종료. for 디버깅
        user.sendEmailVerification() //user.sendEmailVerification()은 Firebase에서 제공하는 메서드. 로그인된 사용자에게 이메일 인증 요청
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {//인증 메일 전송되었다고 사용자한테 토스트 보여줌.
                            Toast.makeText(SignupActivity.this, R.string.sendverifyemail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //boolean 어쩌구는 다 정규식 기반 유효성 검사인듯
    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {// 이메일 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writeemail, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {// 이메일 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notvaildemail, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {// 비밀번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writepassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {// 비밀번호 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notvalidpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 체크 유효성 검사
    private boolean isValidPasswdcheck() {
        if (passwordCheck.isEmpty()) {// 비밀번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writecheckpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(passwordCheck)) {// 비밀번호와 비밀번호 확인에 입력한 값이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notmatchpass, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 이름 유효성 검사
    private boolean isValidName() {
        if (name.isEmpty()) {// 이름 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writename, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 전화번호 유효성 검사
    private boolean isValidPhone() {
        if (phone.isEmpty()) {// 전화번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writephone, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            Toast.makeText(SignupActivity.this, R.string.notvalidphone, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
