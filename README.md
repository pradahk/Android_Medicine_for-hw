# 스마트 모바일 프로그래밍 최종 보고서
## Description
>개발배경   
* 하루가 멀다 하고 새로운 의약품과 건강기능식품이 쏟아지고 있는 요즘, 의약품과 건강기능식품 홍수의 시대라고 해도 과언이 아니다. 수없이 출시되는 의약품과 건강기능식품을 의사처방 없이 쉽게 구입할 수 있어짐에 따라 소비자들의 딜레마 또한 피해갈 수 없다.    
약은 복용시간과 복용기간을 정확하게 지켰을 때 그 효과가 커진다. 그러나 현대인들은 바쁜 생활 속에서 복용중인 약의 복용시간 및 복용기간을 놓치기 쉽다. 때문에 사용자가 설정해놓은 약물 복용시간을 알려주고, 증상에 따른 약 추천 및 가지고 있는 약 사진과 일치하는 약의 정보를 알려줌으로 더욱 편리한 약 복용을 가능하게 하며, GPS를 통해 가까운 약국의 위치를 보여주어 약국 이용을 편리하게 한다.
> 사용한 기능   
* 의약품 정보 알림 서비스 어플리케이션은 약의 모양 또는 이름으로 간편하게 검색기능을 구현하여 의약품의 알맞은 복용방법과 편리한 약 검색을 위한 서비스를 제공하고, 약 복용시간 알림으로 정확한 시간에 약을 복용할 수 있도록한다. 또한 google맵과 연동을 통하여 내 주변에 약국의 위치와 지역(동)으로 검색하여 약국 위치를 알 수 있게 함으로서 모든 사람들에 의약품 정보를 언제 어디서나 편리하게 서비스를 제공 하는 것을 목표로 구성하였다. 약국과 의약품 정보를 파싱하기 위해 공공데이터를 이용하고, android studio에 데이터 저장 및 불러와 리뷰와 회원가입/로그인 알림 기능을 구현하고 또한 Google API연동을 통해 GPS를 받아와서 약국을 찾을 수 있도록 한다.
>기대효과   
* 편의점에서도 약을 손쉽게 구매할 수 있는 요즘, 편의점 직원은 의약품에 대한 전공을 가지고 있는 약사가 아니어서 약에 대한 효능과 부작용을 잘 알고 있지 않다. 그렇기 때문에 약쏙 어플리케이션으로 합리적인 의약품을 선택할 수 있게 도와준다. 이와 더불어 집에 있는 의약품이 어떤 효능과 부작용을 가지고 있는지 알지 못할 때, 약의 모양과 이름을 통한 검색으로 자신에게 맞는 의약품을 선택할 수 있게 도와준다는 기대효과를 가져올 수 있다. 
> 작품 구성도
  <img src="https://user-images.githubusercontent.com/62936197/152375666-cea45f83-aa6a-44d9-b9e5-ec167242f8c2.png" width="550" height="150">
  
## Prerequisite
* 회원가입, 로그인 및 로그아웃, 리뷰 작성, 알람을 위해 데이터를 저장할 Firebase와 Android Studio를 연동한다.
    1. Firebase에 개발을 진행할 프로젝트를 등록한다. Google 로그인을 사용할 예정이므로 SHA-1 정보도 저장해준다.  
    2. Firebase Android 구성 파일(google-services.json)을 다운로드하여 등록한 프로젝트에 추가한다.   
    3. google-services 플러그인을 Gradle 파일에 추가한다. 
        ```java
        dependencies {
            classpath 'com.google.gms:google-services:4.2.0'  // Google Services plugin
          }
        }
        ```
    4. 모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 다음 줄을 파일 하단에 추가한다.
        ```java
        apply plugin: 'com.android.application'

        android {
           ...
        }
        apply plugin: 'com.google.gms.google-services'  // Google Play services Gradle plugin
        ```
    5. 모듈(앱 수준) Gradle 파일(일반적으로 app/build.gradle)에서 핵심 Firebase SDK의 종속 항목을 추가한다.
        ```java
        dependencies {
         implementation 'com.google.firebase:firebase-core:17.0.0'
         }
        ```
* 위치를 받아오기 위해 Google map과 Google Place를 사용할 수 있도록 설정한다.
    1. Google Developers Console사이트(https://console.developers.google.com/apis/dashboard)에 접속하여 새 프로젝트를 생성한다.    
    2. 사용자 인증 정보 만들기 - API 키 - 생성 완료
    3. API 라이브러리에서 Maps SDK for Android와 Place API을 사용설정 한다.
        <img src="https://user-images.githubusercontent.com/57400913/86551257-887f0900-bf7f-11ea-8d94-6ea94830123a.png" width="550" height="150"> 
    4. Google Service를 사용하기 위해서 Google Play services 라이브러리 패키지를 설치한다. <br>
       안드로이드 스튜디오 - Tools - SDK Manager - SDK Tools탭 클릭 - Google Play services체크 - Apply 
       <img src="https://user-images.githubusercontent.com/57400913/86551723-e06a3f80-bf80-11ea-98ca-94354ef16e20.png" width="550" height="180"> 
    5. AndroidManifest.xml파일의 <application>태그의 하위 요소로 발급받은 API키를 추가한다.   
       ```jave
       <meta-data
                   android:name="com.google.android.geo.API_KEY"
                   android:value="발급받은 API키"/>
       ```   

    6. Gradle Scripts - Module app의 build.gradle에 라이브러리를 사용하기 위해 추가한다.   
       ```java
       dependencies {
           implementation 'com.google.android.gms:play-services-maps:17.0.0'
           implementation 'com.google.android.gms:play-services-location:17.0.0'
           implementation 'noman.placesapi:placesAPI:1.1.3'
           }
       ```  
 * 공공데이터를 이용하여 XML형태로 제공하는 전국 약국 정보를 파싱하기 위해 키 값을 받아온다.
    1. 공공데이터 키 값 받기 <br>
        <img src="https://user-images.githubusercontent.com/62935657/86558576-bcb0f480-bf94-11ea-88e1-6cdb04e78f6a.png" width="550" height="250"> <br>
    2. 활용신청을 눌러서 키값을 받는다.
 * 의약품의 모양으로 식별하는 기능을 위해 '의약품 낱알식별' 파일을 추가한다.
    1. '의약품안전나라'사이트에서 csv형식으로 제공하는 '의약품 낱알식별' 파일을 다운로드 받는다. <br>
        <img src="https://user-images.githubusercontent.com/57400913/86558535-9c813580-bf94-11ea-8dac-a6032270ccf8.png" width="550" height="250">   
    2. csv형식을 안드로이드 스튜디오에서 사용하기 좋게 json형식으로 변환한다. <br>

        <img src="https://user-images.githubusercontent.com/57400913/86558548-a2771680-bf94-11ea-9fb8-a8ce03f54e16.png" width="450" height="150"> 　
        <img src="https://user-images.githubusercontent.com/57400913/86558552-a4d97080-bf94-11ea-89b7-8f1752c71524.png" width="450" height="150">

    3. app폴더 아래에 assets폴더를 생성한 후에 json으로 변환한 파일을 넣는다. <br>
        <img src="https://user-images.githubusercontent.com/57400913/86558778-4234a480-bf95-11ea-82fb-facc8f9ec789.png" width="550" height="250">
