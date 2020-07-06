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
아이디로 사용할 이메일, 이름, 전화번호, 비밀번호를 입력한 후 각각의 항목에 대한 빈칸유무, 정규식 등의 유효성 검사를 진행한 후 입력한 값들이 모두 유효하면 Firebase의 Auth와 Firestore에 저장해준다.
<img src="https://user-images.githubusercontent.com/62936197/86549436-98e0b500-bf7a-11ea-8e1a-2906bb63d0d6.png" width="40%">
<img src="https://user-images.githubusercontent.com/62936197/86549227-fe807180-bf79-11ea-9fbf-706f51c8ced9.png" width="70%">   
회원가입이 정상적으로 성공하면 입력한 이메일로 인증 메일이 전송되며, Dialog를 통해 이메일 인증이 필요함을 알려준다.   
전송된 메일을 통해 이메일 인증을 완료하지 않으면 이메일과 비밀번호 값이 일치해도 로그인에 성공할 수 없으며 이메일 인증이 완료되어야 로그인에 성공할 수 있다.
<img src="https://user-images.githubusercontent.com/62936197/86549388-79e22300-bf7a-11ea-8504-d6576d6257b2.png" wioth="70%>   
