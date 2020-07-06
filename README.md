스마트 모바일 프로그래밍 약쏙 최종 보고서
===================================
목차   
-----


### 1.소개
>#### 1-1 개발배경
>#### 1-2 사용한 기능
>#### 1-3 기대효과

### 2. 기능 구현
>#### 2-1 사용자 생성
>>##### 2-1-1 Firebase와 Android Studio 연동
>>##### 2-1-2 회원가입
>>##### 2-1-3 로그인
>>##### 2-1-4 구글 로그인
>>##### 2-1-5 아이디 찾기 및 비밀번호 재설정
>>##### 2-1-6 회원정보 수정
>#### 2-2 약국 찾기
>>##### 2-2-1 위치 관련 퍼미션 허용과 GPS 활성화
>>##### 2-2-2 사용자의 현재 위치 확인 
>>##### 2-2-3 현재 위치를 기반으로 주변 약국 검색
>>##### 2-2-4 약국 파싱 
>>##### 2-2-5 ‘동이름’으로 약국 검색
>### 2-3 사용자 후기 게시판
>>#### 2-3-1 firebase 연동
>>#### 2-3-2 게시물 등록  
>>#### 2-3-3 게시물 수정 및 삭제
>#### 2-5 복용시간 알림
>>##### 2-5-1 알림설정
>>##### 2-5-2 푸시알림
>>##### 2-5-3 알림삭제

<hr/>

## 1. 소개   

### 1-1 개발배경   
하루가 멀다 하고 새로운 의약품과 건강기능식품이 쏟아지고 있는 요즘, 의약품과 건강기능식품 홍수의 시대라고 해도 과언이 아니다. 수없이 출시되는 의약품과 건강기능식품을 의사처방 없이 쉽게 구입할 수 있어짐에 따라 소비자들의 딜레마 또한 피해갈 수 없다.    
약은 복용시간과 복용기간을 정확하게 지켰을 때 그 효과가 커진다. 그러나 현대인들은 바쁜 생활 속에서 복용중인 약의 복용시간 및 복용기간을 놓치기 쉽다. 때문에 사용자가 설정해놓은 약물 복용시간을 알려주고, 증상에 따른 약 추천 및 가지고 있는 약 사진과 일치하는 약의 정보를 알려줌으로 더욱 편리한 약 복용을 가능하게 하며, GPS를 통해 가까운 약국의 위치를 보여주어 약국 이용을 편리하게 한다.

### 1-2 사용한 기능   
의약품 정보 알림 서비스 어플리케이션은 약의 모양 또는 이름으로 간편하게 검색기능을 구현하여 의약품의 알맞은 복용방법과 편리한 약 검색을 위한 서비스를 제공하고, 약 복용시간 알림으로 정확한 시간에 약을 복용할 수 있도록한다. 또한 google맵과 연동을 통하여 내 주변에 약국의 위치와 지역(동)으로 검색하여 약국 위치를 알 수 있게 함으로서 모든 사람들에 의약품 정보를 언제 어디서나 편리하게 서비스를 제공 하는 것을 목표로 구성하였다. 약국과 의약품 정보를 파싱하기 위해 공공데이터를 이용하고, android studio에 데이터 저장 및 불러와 리뷰와 회원가입/로그인 알림 기능을 구현하고 또한 Google API연동을 통해 GPS를 받아와서 약국을 찾을 수 있도록 한다.

### 1-3 기대효과   
편의점에서도 약을 손쉽게 구매할 수 있는 요즘, 편의점 직원은 의약품에 대한 전공을 가지고 있는 약사가 아니어서 약에 대한 효능과 부작용을 잘 알고 있지 않다. 그렇기 때문에 약쏙 어플리케이션으로 합리적인 의약품을 선택할 수 있게 도와준다. 이와 더불어 집에 있는 의약품이 어떤 효능과 부작용을 가지고 있는지 알지 못할 때, 약의 모양과 이름을 통한 검색으로 자신에게 맞는 의약품을 선택할 수 있게 도와준다는 기대효과를 가져올 수 있다. 
<hr/>

## 2. 기능 구현

>### 2-1 사용자 생성
회원가입과 로그인 및 로그아웃을 위해 데이터를 저장해 놓을 Firebase가 필요하다.
>>#### 2-1-1 Firebase와 Android Studio 연동
1)Firebase에 개발을 진행할 프로젝트를 등록한다. Google 로그인을 사용할 예정이므로 SHA-1 정보도 저장해준다.   
2)Firebase Android 구성 파일(google-services.json)을 다운로드하여 등록한 프로젝트에 추가한다.   
3)어플에서 Firebase를 사용할 수 있도록 google-services 플러그인을 Gradle 파일에 추가힌다.
~~~java
dependencies {
    classpath 'com.google.gms:google-services:4.2.0'  // Google Services plugin
  }
}
~~~
4)모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 다음 줄을 파일 하단에 추가한다.
~~~java
apply plugin: 'com.android.application'

android {
  // ...
}
apply plugin: 'com.google.gms.google-services'  // Google Play services Gradle plugin
~~~
5)모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 핵심 Firebase SDK의 종속 항목을 추가한다.
~~~java
dependencies {
 implementation 'com.google.firebase:firebase-core:17.0.0'
 }
~~~
>>#### 2-1-2 회원가입
1)아이디로 사용할 이메일, 이름, 전화번호, 비밀번호를 입력한 후 각각의 항목에 대한 빈칸유무, 정규식 등의 유효성 검사를 진행한 후 입력한 값들이 모두 유효하면 Firebase에 저장해준다. 
~~~java
public class SignupActivity extends AppCompatActivity {
  // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    // 이메일 정규식
    public static final Pattern EMAIL_ADDRESS = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    // 전화번호 정규식
    public static final Pattern PHONE_PATTERN = Pattern.compile("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", Pattern.CASE_INSENSITIVE);
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_signup);
       
       // id가 write_email인 editText에 대한 메서드 저장
       editTextEmail = findViewById(R.id.write_email);
       // id가 signup_password인 editText에 대한 메서드 저장
       editTextPassword = findViewById(R.id.signup_password);
       // id가 check_password인 editText에 대한 메서드 저장
       editTextPasswordCheck = findViewById(R.id.check_password);
       // id가 write_name인 editText에 대한 메서드 저장
       editTextName = findViewById(R.id.write_name);
       // id가 write_phone인 editText에 대한 메서드 저장
       editTextPhone = findViewById(R.id.write_phone);
       }
~~~   
##### 회원가입 버튼 클릭시
~~~java
    // onClick signup
    public void singUp(View view) {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        passwordCheck = editTextPasswordCheck.getText().toString();
        name = editTextName.getText().toString();
        phone = editTextPhone.getText().toString();

        // 유효성 검사 후 회원가입 실행
        if (isValidEmail() && isValidPasswd() && isValidPasswdcheck() && isValidName() && isValidPhone()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // 입력한 email, password, name, phone 값을 파이어스토어에 저장
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("password", password);
                                user.put("name", name);
                                user.put("phone", phone);
                                firebaseFirestore.collection("users").document(email)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // 정보 저장이 성공적으로 이루어지면 이메일 인증 메일 발송
                                                sendEmailVerification();
                                                //  이메일 인증 다이얼로그 보여줌
                                                authemailDialog.show();

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
                                Toast.makeText(SignupActivity.this, R.string.alreadyemail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
~~~
##### 유효성 검사
~~~java
   // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writeemail, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notvaildemail, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writepassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notvalidpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 체크 유효성 검사
    private boolean isValidPasswdcheck() {
        if (passwordCheck.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writecheckpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(passwordCheck)) {
            // 비밀번호와 비밀번호 확인에 입력한 값이 불일치하면 false
            Toast.makeText(SignupActivity.this, R.string.notmatchpass, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 이름 유효성 검사
    private boolean isValidName() {
        if (name.isEmpty()) {
            // 이름 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writename, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 전화번호 유효성 검사
    private boolean isValidPhone() {
        if (phone.isEmpty()) {
            // 전화번호 칸이 공백이면 false
            Toast.makeText(SignupActivity.this, R.string.writephone, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            Toast.makeText(SignupActivity.this, R.string.notvalidphone, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
~~~
<div>
<img src="https://user-images.githubusercontent.com/62936197/86549436-98e0b500-bf7a-11ea-8e1a-2906bb63d0d6.png" width="30%">   
<img src="https://user-images.githubusercontent.com/62936197/86549227-fe807180-bf79-11ea-9fbf-706f51c8ced9.png" width="60%">   
</div>

2)회원가입이 정상적으로 성공하면 입력한 이메일로 인증 메일이 전송되며, Dialog를 통해 이메일 인증이 필요함을 알려준다.   
전송된 메일을 통해 이메일 인증을 완료하지 않으면 이메일과 비밀번호 값이 일치해도 로그인에 성공할 수 없으며 이메일 인증이 완료되어야 로그인에 성공할 수 있다.   
##### 이메일 인증 다이얼로그를 작성한 java파일
~~~java
public class AuthemailDialog extends Dialog {

    // 확인 버튼 생성
    private Button positivebutton;
    // 확인 버튼의 onClickListner 생성
    private View.OnClickListener positiveListener;

    // 이메일 인증 다이얼로그
    public AuthemailDialog(@NonNull Context context, View.OnClickListener positiveListener) {
        super(context);
        this.positiveListener = positiveListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 밖의 화면은 흐리게 함
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.authemail_dialog);

        // 확인 버튼
        positivebutton = findViewById(R.id.positivebutton);
        // 클릭 리스너
        positivebutton.setOnClickListener(positiveListener);
    }
}
~~~
#####  SignUpActivity에 작성한 이메일 인증 다이얼로그
~~~java
 // 이메일 인증 다이얼로그 객체 생성
 private AuthemailDialog authemailDialog;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
          // 다이얼로그의 리스너 등록
        authemailDialog = new AuthemailDialog(this, positiveListener);
        }
~~~   
#####  이메일 인증 다이얼로그의 확인버튼 클릭시   
~~~java
    private View.OnClickListener positiveListener = new View.OnClickListener() {
        public void onClick(View v) {
            // 다이얼로그 종료
            authemailDialog.dismiss();
            // 로그인 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    };
~~~   
<img src="https://user-images.githubusercontent.com/62936197/86549388-79e22300-bf7a-11ea-8504-d6576d6257b2.png" width="30%">   
                                                                                                                       
>>#### 2-1-3 로그인
1)회원가입 시에 입력한 이메일과 비밀번호를 입력하여 로그인을 진행한다.   
<img src="https://user-images.githubusercontent.com/62936197/86550056-5324ec00-bf7c-11ea-86d6-bfb4b3b7c1d1.png" width="30%">          
2)Firebase의 Auth에 저장된 값과 비교하여 입력한 값이 일치하면 로그인이 성공되고 메뉴 화면으로 전환된다.   

~~~java                                                                                                                      
public class MainActivity extends AppCompatActivity {
                                                                                                                       
    // 비밀번호 정규식
    Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
                                                                                
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    
    // 작성한 이메일 값과 비밀번호 값을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";
     
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
        }
     
     // 이메일 로그인 메서드
     public void signInemail(View view) {
     // editText에 작성한 내용을 String으로 변환하여 객체에 저장
     email = editTextEmail.getText().toString();
     password = editTextPassword.getText().toString();
     // 유효성 검사 후 로그인 메서드 실행
     if(isValidEmail() && isValidPasswd()) {
     loginUser(email, password);
     }
     }
~~~   
##### 이메일 로그인 메서드   
~~~java
// 로그인 메서드
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                // 로그인에 성공하면 "로그인 성공" 토스트를 보여줌
                                Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                // MenuActivity로 화면 전환
                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"이메일 인증을 완료해주세요.",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 로그인에 실패하면 "로그인 실패" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
~~~   
5)로그인을 진행하지 않아도 어플을 사용할 수 있으나 게시판 이용 및 회원정보 열람은 로그인에 성공하지 못하면 이용할 수 없다.  
>>#### 2-1-4 Google 로그인
계정 생성을 통한 로그인 외에 구글 아이디를 이용한 로그인 방법을 추가하였다.   
사용자는 자신의 Google 아이디를 이용하여 어플에 로그인을 진행할 수 있으며 Google 로그인을 성공적으로 진행한 사용자는 게시판 이용 또한 가능해진다.
~~~java
public class MainActivity extends AppCompatActivity {
    // 구글 로그인 객체 생성
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Log.e("구글 로그인","버튼 클릭");
                // 클릭시 구글 로그인 메서드 실행
                signIn();
            }
        });
        }
~~~
#####  Google 로그인 메서드
~~~java
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.e("구글 로그인","메서드 실행");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("구글 로그인","result 메서드");
        // RC_SIGN_IN을 통해 로그인 확인여부 코드가 저상 전달되었다면
        if (requestCode == RC_SIGN_IN) {
            Log.e("구글 로그인","로그인 여부");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인이 성공하면, 파이어베이스에 로그인 인증 등록
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                // 구글 이용자가 확인된 사용자 정보를 파이어베이스로 넘기기
                firebaseAuthWithGoogle(account);
                Log.e("구글 로그인","구글 로그인 성공");
            } catch (ApiException ignored) {
                Log.e("구글 로그인","구글 로그인 실패");
            }
        }
    }
~~~
#####  Firebase와 Google 로그인 연결
~~~java
 // 파이어베이스와 구글 로그인 연결
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e("구글 로그인","파이어베이스랑 연결 중");
        // 파이어베이스로 받은 구글 사용자가 확인된 이용자의 값을 토큰으로 받음
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인에 성공하면 "로그인 성공" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // 로그인에 실패하면 "로그인 실패" 토스트를 보여줌
                            Toast.makeText(MainActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
~~~
>>#### 2-1-5 아이디 찾기 및 비밀번호 재설정
1)사용자가 자신의 아이디를 잊었다면 회원가입 시 사용한 이름과 전화번호를 통해 사용자의 아이디를 찾을 수 있게 한다.   
회원가입으로 인해 Firebase에 저장된 사용자의 이름, 전화번호 데이터와 아이디를 찾기 위해 Edittext에 입력한 이름, 전화번호 값이 모두 일치해야 사용자에게 이메일을 보여준다.   
~~~java
public class FindIdActivity extends AppCompatActivity {
    // 파이어베이스 인증 객체 생성
    private FirebaseFirestore firebaseFirestore;
    
    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private EditText editTextName;
    private EditText editTextPhone;
    private String sendname = "";
    private String sendphone = "";

    // 회원의 이메일을 보여줄 textview 객체 생성
    private TextView textshowid;
    private TextView textshowtext;

    // 로그인 화면으로 돌아가기 위한 버튼 객체 생성
    private Button btngotologin;

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextName = findViewById(R.id.write_name);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextPhone = findViewById(R.id.write_phone);
        // id가 tv_showid인 textview에 대한 메서드 저장
        textshowid = findViewById(R.id.tv_showid);
        // id가 tv_showtext인 textview에 대한 메서드 저장
        textshowtext = findViewById(R.id.tv_showtext);

        // 로그인 화면으로 돌아가는 버튼에 대한 메서드 저장
        btngotologin = findViewById(R.id.btn_gotologin);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        btngotologin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //로그인으로 돌아가기 버튼을 누르면 MainActivity로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
~~~
#####  아이디 찾기 버튼 클릭시
~~~java
    public void findEmail(View view)
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendname = editTextName.getText().toString();
        sendphone = editTextPhone.getText().toString();
        // findid 메서드 실행
        findid(sendname, sendphone);
    }
~~~
#####  findid 메서드
~~~java
    private void findid(final String sendname, final String sendphone) {
        // 프로그레스 디이얼로그 생성하여 보여줌
        progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
        progressDialog.show();
        // 파이어스토어 가져옴
        firebaseFirestore = FirebaseFirestore.getInstance();
        // 파이어스토어의 collection 경로를 "users"로 생성
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 파이어스토어의 "users"에서 정보를 가져오는 것이 성공하면
                        if (task.isSuccessful()) {
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
~~~
<img src="https://user-images.githubusercontent.com/62936197/86550165-a4cd7680-bf7c-11ea-9acf-818212ebd9d8.png" width="30%">   
2)비밀번호를 잊었을 경우 아이디로 사용하는 이메일을 입력하면 Firestore에 저장된 이메일 값과 비교 후, 일치하는 이메일 값이 있다면 해당하는 이메일로 비밀번호를 재설정할 수 있는 메일을 전송한다.   

~~~java
public class FindpwActivity extends AppCompatActivity {
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 작성한 이메일 값을 저장할 객체 생성
    private EditText editTextEmail;
    private String sendemail = "";

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog;
    
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
~~~
#####  비밀번호 재전송 버튼 클릭시
~~~java
    public void findPw(View view)
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendemail = editTextEmail.getText().toString();

        // 이메일 유효성 검사 후 findpw 메서드 실행
        if(isValidEmail()){
            findpw(sendemail);
        }
    }
~~~
#####  findpw 메서드
~~~java
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
                            // 버튼을 누르면 메인화면으로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            // 이메일을 정상적으로 보낸 후 로그인 화면으로 이동
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
~~~  
<img src="https://user-images.githubusercontent.com/62936197/86553548-1100a800-bf86-11ea-9f8a-858e6a56c99e.png" width="50%">     
3)사용자는 해당 메일을 통해 비밀번호를 재설정할 수 있으며 이후 재설정한 비밀번호로 로그인을 진행한다.      
<img src="https://user-images.githubusercontent.com/62936197/86553483-ced76680-bf85-11ea-86a1-750e9f48279a.png" width="30%">   

>>#### 2-1-6 회원정보 수정   
계정을 생성하여 어플에 성공적으로 로그인을 한 사용자는 ‘마이페이지’탭에서 회원정보를 수정할 수 있다. 회원정보를 열람할 수 있는 탭은 Fragment로 구성하였다.     
1)탭에 방문할 때에는 이메일을 한 번 더 입력해야 하며 입력한 값이 현재 로그인 중인 사용자의 이메일 값과 일치할 경우 회원정보 및 정보 수정을 위한 버튼을 보여준다.     
이메일 값이 일치하지 않을 경우 회원정보가 보여 지지 않으며 로그인을 하지 않은 사용자가 이 탭에 방문할 때는 ‘로그인 후 이용해주세요.’라는 Text를 보여준다.   

~~~java
public class FragmentInfo extends Fragment {
    // 파이어베이스 사용자 객체 생성
    private FirebaseUser user;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 작성한 이메일 값과 비밀번호 값을 저장할 객체 생성
    private TextView editTextEmail;
    private TextView editTextPassword;

    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private TextView editTextName;
    private TextView editTextPhone;

    // 로그인이 되어있지 않을 경우 보여주는 텍스트뷰 객체 생성
    private TextView tv_beforelogin;

    // 수정 버튼 객체 생성
    private ImageButton btn_modifypw;
    private ImageButton btn_modifypn;
    private ImageButton btn_modifyname;
    // 새로고침 버튼 객체 생성
    private ImageButton btn_refresh;

    // 새로고침 버튼 클릭시 이메일 입력 다이얼로그가 재생성되는 것을 방지하기 위하여 count라는 변수 생성하여 0을 기본값으로 설정함
    private int count=0;

    public FragmentInfo() {
    }

    // 로그인 소식을 듣고 다음단계로 넘겨주는 역할인 firebaseAuthListener을 선언을 onStart에 넣어줌
    @Override public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // firebaseAuthListener
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // 현재 로그인 중인 사용자를 가져옴
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // 로그인한 사용자가 있는 경우
                if (user != null) {
                    tv_beforelogin.setText("");
                    // 기본값인 count=0일 경우 이메일 입력 다이얼로그를 보여주고 count값을 1로 바꿔줌
                    if(count==0){
                        showDialog();
                        count =1;
                    }
                    else{
                        // 다이얼로그 인증을 끝낸 후 새로고침을 했을 경우 다이얼로그 없이 바로 수정된 회원정보를 보여줌
                        showInfo();
                    }
                }
                // 로그인한 사용자가 없는 경우
                else {
                    tv_beforelogin.setText("로그인 후 이용해주세요.");
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = view.findViewById(R.id.write_email);
        // id가 signup_password인 editText에 대한 메서드 저장
        editTextPassword = view.findViewById(R.id.signup_password);
        // id가 write_name인 editText에 대한 메서드 저장
        editTextName = view.findViewById(R.id.write_name);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextPhone = view.findViewById(R.id.write_phone);
        // id가 tv_beforeLogin인 textview에 대한 메서드 저장
        tv_beforelogin = view.findViewById(R.id.tv_beforelogin);

        // id가 modifybutton인 버튼에 대한 메서드 저장
        btn_modifypw = view.findViewById(R.id.modifybutton);
        btn_modifypn = view.findViewById(R.id.modifybutton2);
        btn_modifyname = view.findViewById(R.id.modifybutton3);

        // id가 refreshButton인 버튼에 대한 메서드 저장
        btn_refresh = view.findViewById(R.id.refreshButton);

        // 이메일 일치여부가 성공하기 전에 회원 정보를 보여줄 editText를 모두 빈칸 처리해줌
        editTextEmail.setText("");
        editTextName.setText("");
        editTextPhone.setText("");
        editTextPassword.setText("");
        tv_beforelogin.setText("로그인 후 이용해주세요.");

        // 수정 이미지 버튼과 새로고침 버튼을 GONE하여 버튼과 그 공간까지 보이지 않게 처리
        // 이메일 일치여부가 성공하여 수정할 수 있는 조건이 되면 VIDIBLE하여 보여줄 예정
        btn_modifypw.setVisibility(View.GONE);
        btn_modifypn.setVisibility(View.GONE);
        btn_modifyname.setVisibility(View.GONE);
        btn_refresh.setVisibility(View.GONE);

        // 비밀번호 수정 이미지 버튼 클릭 시
        btn_modifypw.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // modifyDialog 호출
                ModifyDialog modifyDialog = new ModifyDialog();
                modifyDialog.show(requireActivity().getSupportFragmentManager(), "tag");
            }
        });

        // 전화번호 수정 이미지 버튼 클릭 시
        btn_modifypn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // modifyPhoneDialog 호출
                ModifyPhoneDialog modifyPhoneDialog = new ModifyPhoneDialog();
                modifyPhoneDialog.show(requireActivity().getSupportFragmentManager(), "tag");
            }
        });

        // 이름 수정 이미지 버튼 클릭 시
        btn_modifyname.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // modifyNameDialog 호출
                ModifyNameDialog modifyNameDialog = new ModifyNameDialog();
                modifyNameDialog.show(requireActivity().getSupportFragmentManager(), "tag");
            }
        });

        // 새로고침 이미지 버튼 클릭 시
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // refresh 메서드 실행
                refresh();
            }
        });

        return view;
    }
~~~
##### 정보 수정 후 새로고침 버튼 클릭시 화면이 갱신되어 수정된 정보로 보여주는 refresh 메서드

~~~java
    // fragment 화면 갱신 메서드
    private void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }
~~~
##### 회원정보 열람 화면 호출할 이메일 입력 다이얼로그

~~~java
    private void showDialog(){
        // 입력하는 이메일 값을 저장할 변수
        final EditText edittext = new EditText(getActivity());
        // 다이얼로그 호출
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("회원정보 열람을 위해 이메일을 다시 한 번 입력해주세요.")
                .setMessage("(구글 로그인 회원은 회원정보를 제공하지 않습니다.)")
                .setView(edittext)  // 이메일을 입력하기 위한 edittext
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 로그인 중인 사용자를 불러옴
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // 다이얼로그에 입력한 이메일 값
                        final String email = edittext.getText().toString();
                        // 로그인 중인 사용자의 이메일 값을 checkmail이라는 string 변수값에 넣어줌
                        assert user != null;
                        String checkmail = user.getEmail();
                        // 다이얼로그에 입력한 이메일 값과 로그인 중인 사용자의 이메일 값 비교
                        // 같을 때
                        if(email.equals(checkmail)){
                            Toast.makeText(getActivity(),"이메일 확인 성공", Toast.LENGTH_SHORT).show();
                            // "확인" 버튼 클릭시 info 메서드 호출
                            // 입력한 이메일 값을 파이어스토어의 이메일 값과 비교해야하기 때문에 info메서드에 email값을 넣어줌
                            info(email);
                        }
                        // 입력한 값이 없이 '확인'버튼을 누를 경우
                        else if(email.getBytes().length<1){
                            Toast.makeText(getActivity(),"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        // 다이얼로그에 입력한 이메일 값과 로그인 중인 사용자의 이메일 값이 다를 때
                        else{
                            Toast.makeText(getActivity(),"이메일이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }
~~~
<img src="https://user-images.githubusercontent.com/62936197/86555561-af433c80-bf8b-11ea-942f-00ecb48f5b0c.png" width="30%">      

##### 이메일 인증 다이얼로그로 인증이 완료된 후 새로고침을 했을 때 다이얼로그 없이 보여주기 위해 회원정보가 저장된 텍스트를 바로 띄워주기 위한 showInfo 메서드 

~~~java
    private void showInfo(){
        // 현재 로그인이 되어있는 사용자를 가져옴
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // firestore의 collection 경로를  "users"로 설정
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 파이어스토어에서 데이터를 가져오는 것을 성공했을 때
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                assert user != null;
                                // 로그인 중인 사용자의 이메일과 파이어베이스의 이메일이 같을 때
                                if(user.getEmail().equals(document.getData().get("email"))) {
                                    // editText에 파이어스토에 저장된 값을 setText해줌
                                    editTextEmail.setText(document.getData().get("email").toString());
                                    editTextName.setText(document.getData().get("name").toString());
                                    editTextPhone.setText(document.getData().get("phone").toString());
                                    editTextPassword.setText(document.getData().get("password").toString());
                                    tv_beforelogin.setText("");
                                    // 수정버튼과 새로고침 버튼 VISIBLE 처리하여 보여줌
                                    btn_modifypw.setVisibility(View.VISIBLE);
                                    btn_modifypn.setVisibility(View.VISIBLE);
                                    btn_modifyname.setVisibility(View.VISIBLE);
                                    btn_refresh.setVisibility(View.VISIBLE);

                                }

                            }
                        }
                    }
                });
    }
~~~
##### 회원정보를 보여줄 info 메서드 
~~~java
    private void info(final String string){
        // firestore의 collection 경로를  "users"로 설정
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 파이어스토어에서 데이터를 가져오는 것을 성공했을 때
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                assert user != null;
                                // 입력한 string값과 파이어베이스의 이메일 비교하여 같을 때 처리
                                if(string.equals(document.getData().get("email"))) {
                                    // editText에 파이어스토에 저장된 값을 setText해줌
                                    editTextEmail.setText(document.getData().get("email").toString());
                                    editTextName.setText(document.getData().get("name").toString());
                                    editTextPhone.setText(document.getData().get("phone").toString());
                                    editTextPassword.setText(document.getData().get("password").toString());
                                    tv_beforelogin.setText("");
                                    // 수정버튼과 새로고침 버튼 VISIBLE 처리하여 보여줌
                                    btn_modifypw.setVisibility(View.VISIBLE);
                                    btn_modifypn.setVisibility(View.VISIBLE);
                                    btn_modifyname.setVisibility(View.VISIBLE);
                                    btn_refresh.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
    }
}
~~~
<img src="https://user-images.githubusercontent.com/62936197/86555628-dc8fea80-bf8b-11ea-93d5-ce0479983e5b.png" width="30%">     
2)회원정보는 Firestore에 저장된 이메일, 이름, 전화번호, 비밀번호를 보여주되, 이메일은 수정이 불가하며 그 외의 항목 옆에 있는 수정버튼 클릭 시 Dialog로 수정할 값을 입력할 수 있다.   
수정할 값들 또한 회원가입 시와 동일하게 유효성 검사를 진행하고, 입력한 값들이 모두 유효하면 Firebase의 Auth와 Firestore에 변경한 값으로 데이터를 업데이트 해준다.   

##### 이름 수정 다이얼로그

~~~java
public class ModifyNameDialog extends DialogFragment {
    private Fragment fragment;

    // 파이어스토어에 저장된 "name"이라는 이름의 값을 pass_key라는 문자에 저장
    private static final String pass_key = "name";

    // 파이어스토어 인증 객체 생성
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 다이얼로그의 확인 버튼과 취소 버튼 객체 생성
    private Button positivebutton;
    private Button negativebutton;

    // 다이얼로그에 입력하는 name 객체 생성
    private EditText name;

    // 새롭게 저장할 name 값 객체 생성
    String newName ="";

    public ModifyNameDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify3_dialog, container, false);

        // 레이아웃에 postivebutton이라는 버튼 값과 negativebutton이라는 버튼 값 저장
        positivebutton = view.findViewById(R.id.positivebutton);
        negativebutton = view.findViewById(R.id.negativebutton);

        // 다이얼로그에 작성하는 name값 저장
        name = view.findViewById(R.id.write_name);

        // 취소 버튼 클릭시
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 다이얼로그 사라짐
                getDialog().dismiss();
            }
        });

        // 확인 버튼 클릭시
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 로그인 중인 사용자 가져오기기
               final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                if(user != null) {
                    // 다이얼로그에 작성한 name값을 string으로 처리하여 샤로운 name값인 newName에 저장
                    newName = name.getText().toString();
                    if (isValidName()) {
                        // 파이어스토어 collection 경로를 "uesrs"로 하고 로그인 중인 유저의 email을 documentID로 하는 정보를 가져옴
                        DocumentReference contact = firebaseFirestore.collection("users").document(user.getEmail());
                        // 새로운 name값으로 업데이트
                        contact.update(pass_key, newName)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // 업데이트 성공시 다이얼로그 사라짐
                                        getDialog().dismiss();
                                    }
                                });
                    }
                }
            }

        });
        return view;
    }
    // 이름 유효성 검사
    private boolean isValidName() {
        if (newName.isEmpty()) {
            // 이름 칸이 공백이면 false
            Toast.makeText(getActivity(), R.string.plzinputname, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
~~~
##### 비밀번호 수정 다이얼로그

~~~java
public class ModifyDialog extends DialogFragment {
    private Fragment fragment;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어스토어에 저장된 "password"이라는 이름의 값을 pass_key라는 문자에 저장
    private final String pass_key = "password";

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 다이얼로그에 입력하는 password 객체 생성
    private EditText password;

    // 새롭게 저장할 password 값 객체 생성
    private String newPassword ="";

    public ModifyDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_dialog, container, false);

        // 레이아웃에 postivebutton이라는 버튼 값과 negativebutton이라는 버튼 값 저장
        // 다이얼로그의 확인 버튼과 취소 버튼 객체 생성
        Button positivebutton = view.findViewById(R.id.positivebutton);
        Button negativebutton = view.findViewById(R.id.negativebutton);

        // 다이얼로그에 작성하는 password값 저장
        password = view.findViewById(R.id.signup_password);

        // 취소 버튼 클릭시
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 다이얼로그 사라짐
                getDialog().dismiss();
            }
        });

        //확인 버튼 클릭시
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 로그인 중인 사용자 가져오기
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                if(user != null) {
                    // 다이얼로그에 작성한 password값을 string으로 처리하여 새로운 password값인 newPassword에 저장
                    newPassword = password.getText().toString();
                    // 입력한 비밀번호의 유효성 검사
                    if (isValidPasswd()) {
                        // 로그인 중인 사용자의 password를 newPassword로 업데이트
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // 업데이트에 성공하면 파이어스토어에 저장된 password도 업데이트
                                            // 파이어스토어 collection의 경로를 "users"로 하고 로그인 중인 유저의 email을 documentID로 하는 정보를 가져옴
                                            DocumentReference ref = firebaseFirestore.collection("users").document(user.getEmail());
                                            // 새로운 password값으로 업데이트
                                            ref.update(pass_key, newPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // 업데이트 성공시 다이얼로그 사라짐
                                                            getDialog().dismiss();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            }
        });
        return view;
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (newPassword.isEmpty()) {
            // 비밀번호 칸이 공백이면 false
            Toast.makeText(getActivity(),R.string.plzinpytpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            // 비밀번호 형식이 불일치하면 false
            Toast.makeText(getActivity(), R.string.notvalidpassword, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
~~~
##### 전화번호 수정 다이얼로그

~~~java
public class ModifyPhoneDialog extends DialogFragment {
    private Fragment fragment;

    // 파이어스토어에 저장된 "phone"이라는 이름의 값을 pass_key라는 문자에 저장
    private static final String pass_key = "phone";

    // 파이어스토어 인증 객체 생성
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    // 전화번호 정규식
    public static final Pattern PHONE_PATTERN = Pattern.compile("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", Pattern.CASE_INSENSITIVE);

    // 다이얼로그의 확인 버튼과 취소 버튼 객체 생성
    private Button positivebutton;
    private Button negativebutton;

    // 다이얼로그에 입력하는 phone 객체 생성
    private EditText phone;

    // 새롭게 저장할 phone 값 객체 생성
    String newPhone ="";

    public ModifyPhoneDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify2_dialog, container, false);

        // 레이아웃에 postivebutton이라는 버튼 값과 negativebutton이라는 버튼 값 저장
        positivebutton = view.findViewById(R.id.positivebutton);
        negativebutton = view.findViewById(R.id.negativebutton);

        // 다이얼로그에 작성하는 phone값 저장
        phone = view.findViewById(R.id.write_phone);

        // 취소 버튼 클릭시
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 다이얼로그 사라짐
                getDialog().dismiss();
            }
        });

        // 확인 버튼 클릭시
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // 로그인 중인 사용자 가져오기
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                if(user != null) {
                    // 다이얼로그에 작성한 phone값을 string으로 처리하여 새로운 phone값인 newPhone에 저장
                    newPhone = phone.getText().toString();
                    // 전화번호 유효성 검사
                    if (isValidPhone()) {
                        // 파이어스토어 collection 경로를 "uesrs"로 하고 로그인 중인 유저의 email을 documentID로 하는 정보를 가져옴
                        DocumentReference contact = firebaseFirestore.collection("users").document(user.getEmail());
                        // 새로운 phone값으로 업데이트
                        contact.update(pass_key, newPhone)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // 업데이트 성공시 다이얼로그 사라짐
                                        getDialog().dismiss();
                                    }
                                });
                    }
                }
            }
        });
        return view;
    }
    // 전화번호 유효성 검사
    private boolean isValidPhone() {
        if (newPhone.isEmpty()) {
            // 전화번호 칸이 공백이면 false
            Toast.makeText(getActivity(), "변경할 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(newPhone).matches()) {
            // 전화번호 형식이 불일치하면 false
            Toast.makeText(getActivity(), R.string.notvalidphone, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    }
~~~
<img src="https://user-images.githubusercontent.com/62936197/86555739-2f69a200-bf8c-11ea-8c39-da34b681ac41.png" width="30%">   
*****
>### 2-2 약국 찾기
주변 약국을 찾기 위해 사용하는 지도와 주변 약국의 위치를 사용하기 위해 Google Map과 Google Place를 사용한다.   

##### Google map과 Google Place를 사용하기 위한 초기작업   
1)Google Developers Console사이트(https://console.developers.google.com/apis/dashboard)에 접속하여 새 프로젝트를 생성한다.   
<img src="https://user-images.githubusercontent.com/57400913/86548778-967d5b80-bf78-11ea-874e-6af154fa64f7.png" width="70%">

2)사용자 인증 정보 만들기 - API 키 - 생성 완료
<img src="https://user-images.githubusercontent.com/57400913/86549992-21ac2080-bf7c-11ea-8031-e9bbc339137e.png" width="70%">   


3)API 라이브러리에서 Maps SDK for Android와 Place API을 사용설정 한다.
<img src="https://user-images.githubusercontent.com/57400913/86551257-887f0900-bf7f-11ea-8d94-6ea94830123a.png" width="70%">   
          
4)Google Service를 사용하기 위해서 Google Play services 라이브러리 패키지를 설치해줘야 한다.   
안드로이드 스튜디오 - Tools - SDK Manager - SDK Tools탭 클릭 - Google Play services체크 - Apply   
<img src="https://user-images.githubusercontent.com/57400913/86551723-e06a3f80-bf80-11ea-98ca-94354ef16e20.png" width="70%"> 

5)AndroidManifest.xml파일의 <application>태그의 하위 요소로 추가한다.   
  2)에서 발급받은 API키를 추가한다.
~~~jave
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="발급받은 API키"/>
~~~   

6)Gradle Scripts - Module app의 build.gradle에 라이브러리를 사용하기 위해 추가한다.   
추가 후 - Sync Now 클릭
~~~java
dependencies {
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
    implementation 'noman.placesapi:placesAPI:1.1.3'
    }
   ~~~   
   
7)MapMainActivity.java를 만들고 지도를 생성해준다.
~~~java
package com.example.androidlogin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapMainActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {
    PharmParser parser = new PharmParser();
    String data;
    private GoogleMap mMap;
    private Marker currentMarker = null;
    Button handle_btn;
    EditText edit;
    String getedit; //약국 동이름으로 검색하는 edittext

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 1000; //권한 설정을 한 activity에 request값으로 받아올 변수 설정
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션 정의
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    Location mCurrentLocatiion;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서 View가 필요
    List<Marker> previous_marker = null; //google place에서 얻어온 약국 마커 표시

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.map_activity_main);

        previous_marker = new ArrayList<Marker>();

        handle_btn = (Button) findViewById(R.id.handle);
        edit = (EditText) findViewById(R.id.edit);

        //약국 찾기 버튼 눌렀을때(마커 생성)
        Button button = (Button)findViewById(R.id.pharm_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceInformaiton(currentPosition);
            }
        });

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parser.edit = (EditText)findViewById(R.id.edit);
        parser.text = (TextView)findViewById(R.id.result);

        // 홈으로 이동하는 버튼 객체 생성
        ImageButton btn_home = findViewById(R.id.gohome);

        // 홈 버튼 onclicklistener 생성
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 메인화면으로 이동
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        });

    }
~~~   

>>#### 2-2-1 위치 관련 퍼미션 허용과 GPS 활성화   
##### 위치 관련 퍼미션 허용 여부 검사   
~~~java
@Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 퍼미션 허용됐는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                // 퍼미션을 허용했다면 위치 업데이트를 시작
                startLocationUpdates();
            }

            else {

                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료함
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    //사용자가 퍼미션을 거부했을때 뜨는 메시지
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                }else {

                    //사용자가 "다시 묻지 않음"을 누르고 퍼미션을 거부하면 설정-앱 정보에서 퍼미션을 허용해야 사용 가능함을 알림
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 활성화하려면 설정-앱 정보 에서 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }
 ~~~   
 
##### GPS활성화 여부 검사 
~~~java
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapMainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
 ~~~   

 1)처음 실행하면 현재 위치를 찾는데 시간이 걸리기 때문에 초기 위치를 서울역으로 지정해준다.   
 ##### 지도 실행시 초기 위치로 
 ~~~java
 public void setDefaultLocation() {

        //디폴트 위치, 서울역으로 지정
        LatLng DEFAULT_LOCATION = new LatLng(37.553321, 126.972627); //서울역의 위도, 경도
        String markerTitle = "서울역";

        if (currentMarker != null) currentMarker.remove();


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15); //zoom 설정 1~21까지
        mMap.moveCamera(cameraUpdate);

    }
~~~

 2)위치 퍼미션과 GPS 활성화 여부를 검사한 후에 모두 활성화 되어있다면 위치 업데이트를 시작한다.    
 ##### 위치 업데이트    
 ~~~java
  @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            startLocationUpdates(); // 이미 퍼미션 가지고 있다면 위치 업데이트 시작
        }
        else {  //퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요.

            // 사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                //요청을 진행하기 전에 사용자에게 접근 권한이 필요함을 알림
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //사용자게에 퍼미션 요청을 함 요청 결과는 onRequestPermissionResult에서 수신
                        ActivityCompat.requestPermissions(MapMainActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();

            } else {
                //사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 함
                //요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
        //결과 맵에 띄우기
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d( TAG, "onMapClick :");
            }
        });
    }
 ~~~ 
 
<div>
<img src="https://user-images.githubusercontent.com/57400913/86553887-f5e26800-bf86-11ea-9c12-fc412c5b63fc.png" width="30%">       
<img src="https://user-images.githubusercontent.com/57400913/86553418-a8193000-bf85-11ea-8c6d-da8c8a4f3d24.png" width="30%">
<img src="https://user-images.githubusercontent.com/57400913/86553428-b1a29800-bf85-11ea-9750-3571cd1ebdb2.png" width="30%">
</div>   
   
   
>>#### 2-2-2 사용자의 현재 위치 확인
##### GPS서비스 상태 파악 후 현재 위치 업데이트 
~~~java
 private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();//gps비활성화 상태일때 서비스 세팅 메서드 호출
        }
        else {
            //gps활성화 되어있으면 퍼미션 확인
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }
    }
~~~   
##### Geocoder로 GPS를 주소로 변환   
~~~java
 public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }
 ~~~   
    
##### 현재 위치에 마커 생성하기   
~~~java
public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currentMarker = mMap.addMarker(markerOptions);
    }
~~~

<div>
<img src="https://user-images.githubusercontent.com/57400913/86555113-79ea1f00-bf8a-11ea-8ae5-dce9c8927e97.png" width="30%">       
<img src="https://user-images.githubusercontent.com/57400913/86555039-40b1af00-bf8a-11ea-9188-8bb074153239.png" width="30%">
</div>   
   
   
>>#### 2-2-3 현재 위치를 기반으로 주변 약국 검색   
##### PlacesListener 인터페이스 구현   
MainActivity에서 PlacesListener를 구현해주고 인터페이스가 요구하는 메서드 4개를 추가한다.    
1)PlacesListener 인터페이스 구현
~~~java
public class MapMainActivity extends AppCompatActivity implements OnMapReadyCallback,   
ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {
~~~
     
2)PlacesListener 인터페이스가 요구하는 메서드 4개 Override로 추가
~~~java
    @Override
    public void onPlacesFailure(PlacesException e) { }

    @Override
    public void onPlacesStart() { }

    @Override //구글 플레이스에서 가져온 정보 지도에 표시하기
    public void onPlacesSuccess(final List<Place> places) { }

    @Override
    public void onPlacesFinished() { }
~~~

##### 주변 약국 마커 생성하기   
~~~java
public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    String markerSnippet = getCurrentAddress(latLng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);

                    //약국 마커 아이콘 바꾸기
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker6);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    Marker item = mMap.addMarker(markerOptions);

                    previous_marker.add(item);
                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });

    }
~~~   
      
##### 현재 위치를 기반으로 주변 약국 찾기   
radius의 값으로 반경 2500m로 설정해주었기 때문에 현재 위치를 기반으로 2500m 근처에 있는 약국을 찾는다.   
내 주변 약국 찾기 버튼을 누를때마다 마커가 클리어되고 새롭게 찾아진 주변 약국 마커가 생성된다.   
~~~java
    public void showPlaceInformaiton(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MapMainActivity.this)
                .key("발급받은 자신의 API 키")
                .latlng(location.latitude, location.longitude)//현재 위치
                .radius(2500)// 반경
                .type(PlaceType.PHARMACY) //약국
                .build()
                .execute();
    }
~~~
<div>
<img src="https://user-images.githubusercontent.com/57400913/86556093-40ff7980-bf8d-11ea-96c3-e2160409c832.png" width="30%">       
<img src="https://user-images.githubusercontent.com/57400913/86556107-4fe62c00-bf8d-11ea-8e5f-bf161c52c0d4.png" width="30%">
</div>   
   

>>#### 2-2-4 약국 파싱
1) 
)공공데이터로 XML형태로 제공하는 전국 약국 정보를 파싱하기 위한 PharmParser.java 파일 만들기




>>#### 2-2-5 ‘동이름’으로 약국 검색   

##### 검색 버튼 눌렀을 때
~~~java
public void mOnClick(View v){
        //edit 검색 후 키보드 숨기기
        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(parser.edit.getWindowToken(),0);
        getedit = edit.getText().toString();
        if(getedit.getBytes().length <= 0)
        {
            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } // edittext에 아무것도 입력하지 않고 검색 버튼을 눌렀을 경우
        
        else{
            handle_btn.setText("검색 결과를 보려면 위로 슬라이딩 해주세요.");
            switch (v.getId()){
                case R.id.serach_btn :
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            data = parser.getXmlData();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    parser.text.setText(data);
                                }
                            });
                        }
                    }).start();
                    break;

            }

        }

    }
 ~~~
<div>
<img src="https://user-images.githubusercontent.com/57400913/86556685-fe3ea100-bf8e-11ea-97dc-850dda76d829.png" width="30%">
<img src="https://user-images.githubusercontent.com/57400913/86557019-eae00580-bf8f-11ea-8d11-b519cdf41e36.png" width="30%">       
</div>
*****
>### 2-3 사용자 후기 게시판
>#### 2-3-1 firebase 연동
firebase와 연동하는 방법은 회원가입 부분에서 설명한 방법과 동일하다.      
>#### 2-3-2 게시물 등록    
recyclerView와 cardView를 이용하여 목록에서 firebase에 저장된 데이터들을 보여줄 것이다.    
##### 1)getter와 setter를 정의해주는 ReviewPostInfo.java 파일 생성하기    
외부에서 접근할 때 객체의 무결성을 보장하기 위해 getter와 setter를 정의하였다  
게시글을 입력했을 때, 게시글의 제목과 내용, 등록한 날짜를 정의해준다.   
~~~java
import java.io.Serializable;
import java.util.Date;
//getter와 setter를 정의해주는 코드
public class ReviewPostInfo implements Serializable {
    //intent에서 putExtra로 보내주기 위해 implements Serializable가 사용됨.
    private String title;
    private String contents;
    private Date createdAt;
    private String id;
   public ReviewPostInfo(String title, String Contents, Date createdAt) {
        this.title = title;
        this.contents = Contents;
        this.createdAt = createdAt;
    }
    //get set 메서드 이용
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
~~~
##### 2)게시물을 입력할 ReviewWriteActivity.java 파일 생성    
위에서 정의해준 getter와 setter를 이용하여 사용자가 입력한 값을 ReviewPostInfo.java의 setter에 저장해준다.   
~~~java
//text 업데이트를 위한 코드
    private void contentsUpdate() {
        //titleEditText과 contentEditText의 값을 받아서 string값으로 받아옴
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentEditText)).getText().toString();
        final Date date = reviewPostInfo2 ==null? new Date() : reviewPostInfo2.getCreatedAt();//날짜가 존재하지 않으면 현재 날짜를 불러오고, 수정 시 날짜가 존재하니까 그때는 그 날짜 그대로 유지시켜줌
        //제목과 글이 둘 다 입력 되었을 때 실행됨
        if(title.length() > 0 && contents.length()>0){
            loadrLayout.setVisibility(View.VISIBLE);
            ReviewPostInfo reviewPostInfo = new ReviewPostInfo(title, contents, date);
            uploader(reviewPostInfo);//값들이 postinfo로 들어와 uploader 메서드로 들어감
        }else {//그렇지 않으면 게시글을 입력해달라는 toast가 띄워짐
            startToast("게시글을 입력해주세요");
        }
    }
~~~
입력한 게시물의 내용을 setter를 이용하여 firebase에 저장해준다    
~~~java
 private void uploader(ReviewPostInfo reviewPostInfo){
        firebaseFirestore = FirebaseFirestore.getInstance();

        //값이 null이면 앞에것을 반환.->게시물 등록 시 사용됨. null이 아니면 뒤에것을 반환
        final DocumentReference documentReference = reviewPostInfo2 ==null? firebaseFirestore.collection("posts").document()
                :firebaseFirestore.collection("posts").document(reviewPostInfo2.getId());
        //게시물에서 입력한 텍스트들을 받아와서 database의 document부분에 넣어줌.
        documentReference.set(reviewPostInfo.getPostInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//성공시
                        Log.d(TAG,"id");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override// 실패 시
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error",e);
                    }
                });
    }
~~~

##### 3)firebase에 저장된 게시물을 목록으로 보여주기 위해 ReviewMainAdapter.java에 adapter를 정의해준다   
이 adpater는 recyclerView와 cardView를 이용한다.    
~~~java
//Review페이지의 처음 창을 보여주는 MainAdapter
public class ReviewMainAdapter extends RecyclerView.Adapter<ReviewMainAdapter.MainViewHolder>{
    private ArrayList<ReviewPostInfo> mDataset;
    private Activity activity;
    private String email;
    TextView textEmail;
    CardView cardView1;

    //RecyclerView와 cardView를 이용하여 등록된 리뷰를 전체 리스트로 출력할것임. 그것을 위한 정의
    static class MainViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        MainViewHolder(Activity activity, CardView v, ReviewPostInfo reviewPostInfo) {
            super(v);
            cardView = v;
        }
    }
    //viewType이 계속 0만 주기 때문에 사용하려면 override해야함.
    @Override
    public int getItemViewType(int position){
        return position;
    }

    //배열로 들어온 데이터들을 불러오는 작업.
    ReviewMainAdapter(Activity activity, ArrayList<ReviewPostInfo> mDataset) {//생성자. 초기화해줌
        this.mDataset = mDataset;
        this.activity = activity;
    }

    @NonNull
    @Override//RecyclerView와 cardView를 만들어주는 작업. 보이는 부분만 load함.
    public ReviewMainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout을 view객체로 만들기 위해 layoutInflater를 이용한다.
        final CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(activity, cardView, mDataset.get(viewType));//cardview가 하나하나 돌때, position값을 알기위해 viewType을 넣어 만듬.
        //Log.e("로그: ","로그: "+viewType);

        cardView.setOnClickListener(new View.OnClickListener() {//하나의 카드뷰를 클릭 시 intent로 해당하는 값을 ReviewActivityPost로넘겨줌.
            @Override
            public void onClick(View view) {
                //postInfo 데이터를 보내줘야 데이터를 가지고 레이아웃에 그려줌.
                Intent intent = new Intent(activity, ReviewActivityPost.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));//앞에는 key값, 뒤에는 실제 값
                //postInfo의 이름으로 intent를 보내 PostActivity에서 받아서 쓸수있게함
                activity.startActivity(intent);
            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        //item_post에 실제 db들의 값들을 넣어주는 작업.

        //CardView에 firebase에 저장된 title값 넣어주기
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());

        //게시물을 추가한 날짜 넣어주기
        TextView createTextView = cardView.findViewById(R.id.createdTextView);
        createTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        //contents값 넣어주기
        final TextView contentsTextView = cardView.findViewById(R.id.contentsTextView);
        contentsTextView.setText(mDataset.get(position).getContents());

        textEmail = cardView.findViewById(R.id.textView2);
        textEmail.setText(mDataset.get(position).getEmail());
    }

    @Override //자동 override됨. 데이터들의 수를 세줌.
    public int getItemCount() {
        return (mDataset != null ? mDataset.size() : 0);
    }
}
~~~

##### 4)ReviewMainActivity.java에서 firebase에 저장된 데이터들을 위에서 정의된 adapter를 이용하여 넣어준다.    
Review의 목록을 보여줄 java 파일인 ReviewMainActivity.java 파일에 recyclerView를 정의해준다     
~~~java
public class ReviewMainActivity extends AppCompatActivity {
    private  ArrayList<ReviewPostInfo> postList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ReviewMainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이 파일에서는 review_activity_main.xml창을 보여줄것임.
        setContentView(R.layout.review_activity_main);
        fragmentMainMenu = new FragmentMainMenu();
        
        //recyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView);//recyclerViewid연결
        recyclerView.setHasFixedSize(true);//recylerView 기존 성능 강화
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReviewMainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        postUpdate();
        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);//게시글 추가 버튼을 클릭 시
    }
    //게시글 추가 버튼을 클릭할 때 처리하는 기능
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {//게시글 추가 버튼을 눌렀을 때 ReviewWriteActivity.java로 넘겨줌
            if (view.getId() == R.id.floatingActionButton) {
                myStartActivity(ReviewWriteActivity.class);
            }
         }
    };

 //실제 게시물을 보여주고 업데이트 해주는 코드
    public void postUpdate(){
        firebaseFirestore.collection("posts").orderBy("createdAt", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            postList = new ArrayList<>();//arraylist에 받아온값들을 다 넣어주어 보여줌
                            postList.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId()+" => "+document.getData());
                                postList.add(new ReviewPostInfo(//데이터를 다 가져와 postList배열에 넣어줌.
                                        document.getData().get("title").toString(),
                                        document.getData().get("contents").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId(),
                                        document.getData().get("user").toString()
                                        //firebaseFirestore.collection("users").document().getId()//user의 email값을 가져옴.
                                ));//각 post들을 구분할 수 있게 하기 위해 post의id값을 얻어옴
                            }
                            //MainAdaper에서 넘겨줌.
                            mainAdapter = new ReviewMainAdapter(ReviewMainActivity.this, postList);
                            mainAdapter.setOnPostListener(onPostListener);//onPostListener를 넘겨주면 MainAdapter에서도 쓸수있음.
                            recyclerView.setAdapter(mainAdapter);
                            mainAdapter.notifyDataSetChanged();
                        }else {
                            Log.d(TAG, "Error : ",task.getException());
                        }

                    }
                });
}
~~~
 
##### 5)게시글 자세히보기 기능    
등록된 게시물은 다음 코드를 이용하여 내용의 일부만 보여주도록 구현하였다.    
~~~java
<TextView
    android:id="@+id/titleTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:textColor="#000000"
    android:textSize="15sp"
    android:textStyle="bold"
    tools:text="@string/itemPostTitle"
    android:maxLines="1"
    android:ellipsize="end"/>
~~~
따라서 게시글의 자세히보기 기능을 구현하였다.   
이는 해당 게시물을 클릭하면 게시글을 전체볼 수 있는 창으로 넘어가도록 구현한 것이다.   
먼저 adapter에서 해당하는 게시물을 클릭했을 때, intent를 이용하여 값들을 넘겨주었다.   
~~~java
@NonNull
    @Override//RecyclerView와 cardView를 만들어주는 작업. 보이는 부분만 load함.
    public ReviewMainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout을 view객체로 만들기 위해 layoutInflater를 이용한다.
        final CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(activity, cardView, mDataset.get(viewType));//cardview가 하나하나 돌때, position값을 알기위해 viewType을 넣어 만듬.
        //Log.e("로그: ","로그: "+viewType);

        cardView.setOnClickListener(new View.OnClickListener() {//하나의 카드뷰를 클릭 시 intent로 해당하는 값을 ReviewActivityPost로넘겨줌.
            @Override
            public void onClick(View view) {
                //postInfo 데이터를 보내줘야 데이터를 가지고 레이아웃에 그려줌.
                Intent intent = new Intent(activity, ReviewActivityPost.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));//앞에는 key값, 뒤에는 실제 값
                //postInfo의 이름으로 intent를 보내 PostActivity에서 받아서 쓸수있게함
                activity.startActivity(intent);
            }
        });
~~~
intent를 이용하여 넘겨준 값들을 ReviewActivityPost.java 파일에서 받아서 띄워주는 것이다.
~~~java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //이 java파일에서는 activity_write_post창을 보여줄것임.
        setContentView(R.layout.review_activity_post);

        reviewPostInfo = (ReviewPostInfo) getIntent().getSerializableExtra("postInfo");
        //title 넣어주기
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(reviewPostInfo.getTitle());

        //게시물을 추가한 날짜 넣어주기
        TextView createTextView = findViewById(R.id.createdTextView);
        createTextView.setText(new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(reviewPostInfo.getCreatedAt()));//local시간을 생성하여 넣어줄것임.
        //contents 넣어주기
        final TextView contentsTextView = findViewById(R.id.contentsTextView);
        contentsTextView.setText(reviewPostInfo.getContents());
        
        reviewPostAdapter = new ReviewPostAdapter(this);
        reviewPostAdapter.setOnPostListener(onPostListener);//ReviewPostAdapter에 연결해줌.
    }
~~~
<div>
<img src="https://user-images.githubusercontent.com/57400849/86554916-e1539f00-bf89-11ea-965e-1b2bd067ae3a.png" width="70%"> 
<img src="https://user-images.githubusercontent.com/57400849/86554958-01835e00-bf8a-11ea-8329-65fb59a8c7db.png">  
</div>

 




>#### 2-3-3 게시물 수정 및 삭제
게시글의 수정 및 삭제 기능은 popup메뉴를 이용한 방법과 버튼을 이용한 방법. 이 두가지를 이용하여 기능 이용이 가능하도록 구현하였다.
게시물의 업데이트를 위해 interface를 이용하는 것이다.


#### 1.수정 및 삭제 방법 설명   
먼저 기능 구현을 설명하기 전, 공통적인 수정 및 삭제 방법을 설명한다.   
##### 1)수정기능   
우선 수정버튼을 클릭하면, ReviewWriteActivity.java파일로 넘어가게 된다. 이때, 이 java파일에서는 해당하는 게시물의 저장되어있는 값들을 가져와
보여주며 원래의 게시글을 수정할 수 있게 도와준다.   
~~~java
 private void postInit (){//수정버튼을 눌렀을 때 그 전의 값들을 넣어주는 역할을 함
        if(reviewPostInfo2 !=null){
            titleEditText.setText(reviewPostInfo2.getTitle());
            contentEditText.setText(reviewPostInfo2.getContents());
        }
    }
~~~
이 메서드를 이용하여 uploader메서드의 아래의 코드에서 firebase에서의 수정도 가능하게 한다.   
~~~java
 //값이 null이면 앞에것을 반환.->게시물 등록 시 사용됨. null이 아니면 뒤에것을 반환->수정버튼 이용 시 사용됨
        final DocumentReference documentReference = reviewPostInfo2 ==null? firebaseFirestore.collection("posts").document()
                :firebaseFirestore.collection("posts").document(reviewPostInfo2.getId());
~~~
<div>
<img src="https://user-images.githubusercontent.com/57400849/86555488-786d2680-bf8b-11ea-865c-e833ca339498.png" width="70%"> 
<img src="https://user-images.githubusercontent.com/57400849/86555300-f5e46700-bf8a-11ea-92ee-52cdbd07c024.png" width="20%">
</div>   


##### 2)삭제기능   
삭제하고싶은 게시물에서 popup메뉴의 삭제버튼이나 게시물의 삭제버튼을 누르게 되면 해당하는 게시물의 position값을 얻어와 해당 게시물을
firebase와 리뷰 목록에서 삭제 가능하도록 구현하였다.    
이를 구현하기 위해 OnPostListener.java 파일에 수정 및 삭제 기능의 listener를 이용하였다.   
수정 및 삭제시, 게시물 업데이트를 위해 interface를 이용하는 것이다. 이 메서드 하나로 수정 및 삭제 기능을 구현가능하도록 한다.    
~~~java
public interface OnPostListener {
    void onDelete(int position);
    void onModify(int position);
}
~~~
위의 listener를 정의해주기 위해 ReviewMainActivity.java파일에 추가해준다.   
~~~java
 OnPostListener onPostListener = new OnPostListener() {//인터페이스인 OnPostListener를 가져와서 구현해줌
        @Override
        public void onDelete(int position) {//MainAdapter에 넘겨주기 위한 메서드 작성

            String id = postList.get(position).getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
            firebaseFirestore.collection("posts").document(id).delete()//그 id에 맞는 값들을 지워줌
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {//성공시
                            startToast("게시글을 삭제하였습니다.");
                            postUpdate();//새로고침을 위해 이 이벤트를 mainActivity에서 알아야함.->listener를 만들어줘야함
                        }
                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {//실패시
                    startToast("게시글 삭제에 실패하였습니다.");
                }
            });
        }

        @Override
        public void onModify(int position) {//여기서 수정하면 writepostActivity를 켜서 수정해주는코드
            myStartActivity(ReviewWriteActivity.class,postList.get(position));
        }
    };
~~~
<img src="https://user-images.githubusercontent.com/57400849/86555531-99ce1280-bf8b-11ea-93a3-742ee9cd725a.png" width="70%">   

##### 2. popup메뉴를 이용하여 수정 및 삭제 기능을 구현하였다.
##### 1)각각의 adapter에서 구현이 가능하도록 해야하기 때문에 ReviewMainAdapter.java에서 추가적으로 popup메서드를 구현하였다.   
~~~java
 //popup메뉴를 만들기 위한 메서드. view로 받아오기 위해 activity사용함. 여기서 popup메뉴는 수정 삭제가 내려오는 메뉴임.
    public void showPopup(View v, final int position) {//android studio에서 제공하는 팝업 메뉴 표시 기능
        //db값을 갖고오고, 선택된 post값을 알아오기 위해 사용함. view와 위치값(position)을 갖고와서 사용하기. 하나의 postID를 알아야함.
        //postID를 알아야 그 post를 삭제할수있음.->postInfo.java수정

        //수정,삭제의 popup메뉴를 보여주는 버튼을 cardview로 정의함.
        // 버튼을 클릭시 popup메뉴를 보여주는 코드임.

        PopupMenu popup = new PopupMenu(activity,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override//popup메뉴 내의 삭제버튼, 수정버튼을 눌렀을 때 삭제,수정기능 구현
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.modify ://modify버튼을 눌렀을 때
                        onPostListener.onModify(position);//인터페이스의 onModify를 이용
                        return true;
                    case R.id.delete://delete버튼을 눌렀을 때
                        // 게시글 삭제를 위한 메서드. db에서도 삭제함.
                        onPostListener.onDelete(position);//인터페이스의 onDelete를 이용
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();//inflater를 이용하여 view화 시킴
        inflater.inflate(R.menu.post, popup.getMenu());//popup메뉴를 보여줌.
        popup.show();
    }
~~~
##### 2)이 showPopup() 메서드를 onCreateViewHolder 메서드에 추가적으로 정의해준다.    
~~~java
@NonNull
    @Override//RecyclerView와 cardView를 만들어주는 작업. 보이는 부분만 load함.
    public ReviewMainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout을 view객체로 만들기 위해 layoutInflater를 이용한다.
        final CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(activity, cardView, mDataset.get(viewType));//cardview가 하나하나 돌때, position값을 알기위해 viewType을 넣어 만듬.
        //Log.e("로그: ","로그: "+viewType);
        cardView.setOnClickListener(new View.OnClickListener() {//하나의 카드뷰를 클릭 시 intent로 해당하는 값을 ReviewActivityPost로넘겨줌.
            @Override
            public void onClick(View view) {
                //postInfo 데이터를 보내줘야 데이터를 가지고 레이아웃에 그려줌.
                Intent intent = new Intent(activity, ReviewActivityPost.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));//앞에는 key값, 뒤에는 실제 값
                //postInfo의 이름으로 intent를 보내 PostActivity에서 받아서 쓸수있게함
                activity.startActivity(intent);
            }
        });
        //수정,삭제의 popup메뉴를 보여주는 버튼을 cardview로 정의함.
        // 버튼을 클릭시 popup메뉴를 보여주는 코드임.
        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, mainViewHolder.getAdapterPosition());
            }
        });
        return mainViewHolder;
    }
~~~
따라서 각각의 adapter에서 수정 및 삭제 popup 메뉴가 생성되도록 구현하도록 한다.    

<img src="https://user-images.githubusercontent.com/57400849/86555610-ce41ce80-bf8b-11ea-8e6d-b97148d80912.png" width="20%">   


##### 3. 게시물을 자세히 보기 클릭시, 그 안에서도 수정 및 삭제 기능이 가능하도록 구현하였다.    
##### 1)먼저 게시글 삭제 기능을 ReviewPostAdapter.java파일에 따로 구현해주었다. 이는 메서드의 유연한 사용을 위함이다.   
~~~java
public void postDelete(ReviewPostInfo reviewPostInfo){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();//FirebaseFirestore 초기화해주는 코드
        String id = reviewPostInfo.getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
        firebaseFirestore.collection("posts").document(id).delete()//그 id에 맞는 값들을 지워줌
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//성공시
                        startToast("게시글을 삭제하였습니다.");//삭제시 삭제 토스트를 띄워줌
                        //끝났는줄 알고 post 업데이트를 해줘야 하니까 리스너 필요
                        onPostListener.onDelete(1);
                    }
                }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {//실패시 실패 토스트를 띄워줌
                startToast("게시글 삭제에 실패하였습니다."); }
        });
    }

    public void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }
};
~~~
##### 2)ReviewActivityPost,java파일에 추가적으로 정의해준다.   
이때도 onPostListener의 메서드를 불러온다.
~~~java
OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(int position) {
            Log.e("로그", "삭제 성공");
        }

        @Override
        public void onModify(int position) {
            Log.e("로그", "수정 성공");
        }
    };
    //수정버튼, 삭제버튼 클릭 시 실행해주는 메서드를 구현함
    private void buttonClick(){
        //dialog를 이용해서 삭제를 재 확인 해주는 메서드를 구현함.
        final AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        //삭제버튼 클릭 시
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oDialog.setMessage("삭제하시겠습니까?")//Dialog로 듸워줌
                        .setTitle("알림")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener()//아니오 클릭 시
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Log.i("Dialog", "취소");
                                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener()//예 버튼 클릭 시
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //실제 삭제 기능을 수행하는 코드.
                                reviewPostAdapter.postDelete(reviewPostInfo); //reviewPostInfo의 postDelete메서드를 이용해 삭제시킴
                                myStartActivity(ReviewMainActivity.class, reviewPostInfo);//intent로 ReviewMainActivity로 넘겨줌
                                finish();
                            }
                        })
                        .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                        .show();
            }
        });
        //수정버튼 클릭 시
        modify2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(ReviewWriteActivity.class, reviewPostInfo);//바로 ReviewWriteActivity창으로 넘겨줌. 다시 게시물을 작성할 수 있는 창으로 넘겨주는것임.
            }
        });
    }
~~~
<img src="https://user-images.githubusercontent.com/57400849/86555662-f3364180-bf8b-11ea-938d-246628adb58e.png" width="20%">    


