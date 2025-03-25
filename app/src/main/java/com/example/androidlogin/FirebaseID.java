// Firebase 데이터베이스에서 사용할 키(필드명)를 정의하는 클래스
package com.example.androidlogin;

public class FirebaseID {
    //static 변수를 선언하여 공통적으로 사용할 키 이름을 저장
    public static String user = "user";
    public static String email = "email";
    public static String name = "name";
    //public static String으로 선언하여 클래스 이름을 통해 직접 접근 가능 (FirebaseID.user).
    //Firebase에 데이터를 저장할 때 사용할 키 값을 미리 정의.

}
