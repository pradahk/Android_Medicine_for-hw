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
>>##### 2-1-4 아이디 찾기 및 비밀번호 재설정
>>##### 2-1-5 구글 로그인
>>##### 2-1-6 회원정보 수정
>#### 2-2 약국 찾기
>>##### 2-1-1 위치 관련 퍼미션 허용과 GPS 활성화


<hr/>

## 1. 소개

### 1-1 개발배경

### 1-2 사용한 기능

### 1-3 기대효과

<hr/>

## 2. 기능 구현

### 2-1 사용자 생성
회원가입과 로그인 및 로그아웃을 위해 데이터를 저장해 놓을 Firebase가 필요하다.
>#### 2-1-1 Firebase와 Android Studio 연동
Firebase에 개발을 진행할 프로젝트를 등록한다. Google 로그인을 사용할 예정이므로 SHA-1 정보도 저장해준다.   
이후 Firebase Android 구성 파일(google-services.json)을 다운로드하여 등록한 프로젝트에 추가한다.   
앱에서 Firebase 제품을 사용할 수 있도록 google-services 플러그인을 Gradle 파일에 추가힌다.
<pre>
<code>
dependencies {
    classpath 'com.google.gms:google-services:4.2.0'  // Google Services plugin
  }
}
</code>
</pre>
모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 다음 줄을 파일 하단에 추가한다.
<pre>
<code>
apply plugin: 'com.android.application'

android {
  // ...
}
apply plugin: 'com.google.gms.google-services'  // Google Play services Gradle plugin
</code>
</pre>
모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 핵심 Firebase SDK의 종속 항목을 추가한다.
<pre>
<code>
dependencies {
 implementation 'com.google.firebase:firebase-core:17.0.0'
 }
</code>
</pre>
>#### 2-1-2 회원가입
아이디로 사용할 이메일, 이름, 전화번호, 비밀번호를 입력한 후 각각의 항목에 대한 빈칸유무, 정규식 등의 유효성 검사를 진행한 후 입력한 값들이 모두 유효하면 Firebase에 저장해준다.
<pre>
<code>
public class MainActivity extends AppCompatActivity {
    // 비밀번호 정규식
    Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // 작성한 이메일 값과 비밀번호 값과 이름 값을 저장할 객체 생성
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

</code>
</pre>   
<img src="https://user-images.githubusercontent.com/62936197/86549436-98e0b500-bf7a-11ea-8e1a-2906bb63d0d6.png" width="40%">
<img src="https://user-images.githubusercontent.com/62936197/86549227-fe807180-bf79-11ea-9fbf-706f51c8ced9.png" width="70%">   
회원가입이 정상적으로 성공하면 입력한 이메일로 인증 메일이 전송되며, Dialog를 통해 이메일 인증이 필요함을 알려준다.   
전송된 메일을 통해 이메일 인증을 완료하지 않으면 이메일과 비밀번호 값이 일치해도 로그인에 성공할 수 없으며 이메일 인증이 완료되어야 로그인에 성공할 수 있다.      
<img src="https://user-images.githubusercontent.com/62936197/86549388-79e22300-bf7a-11ea-8504-d6576d6257b2.png" width="70%>   
>#### 2-1-3 로그인
회원가입 시에 입력한 이메일과 비밀번호를 입력하여 로그인을 진행한다.     
Firebase의 Auth에 저장된 값과 비교하여 입력한 값이 일치하면 로그인이 성공되고 메뉴 화면으로 전환된다.   
로그인을 진행하지 않아도 어플을 사용할 수 있으나 게시판 이용 및 회원정보 열람은 로그인에 성공하지 못하면 이용할 수 없다.   
<img src="https://user-images.githubusercontent.com/62936197/86550056-5324ec00-bf7c-11ea-86d6-bfb4b3b7c1d1.png" width="70%>   
>#### 2-1-4 아이디 찾기 및 비밀번호 재설정
사용자가 자신의 아이디를 잊었다면 회원가입 시 사용한 이름과 전화번호를 통해 사용자의 아이디를 찾을 수 있게 한다.   
회원가입으로 인해 Firebase에 저장된 사용자의 이름, 전화번호 데이터와 아이디를 찾기 위해 Edittext에 입력한 이름, 전화번호 값이 모두 일치해야 사용자에게 이메일을 보여준다.   
또한 비밀번호를 잊었을 경우 아이디로 사용하는 이메일을 입력하면 Firestore에 저장된 이메일 값과 비교 후, 일치하는 이메일 값이 있다면 해당하는 이메일로 비밀번호를 재설정할 수 있는 메일을 전송한다. 사용자는 해당 메일을 통해 비밀번호를 재설정할 수 있으며 이후 재설정한 비밀번호로 로그인을 진행한다.   
<img src="https://user-images.githubusercontent.com/62936197/86550165-a4cd7680-bf7c-11ea-9acf-818212ebd9d8.png" width="70%>   
