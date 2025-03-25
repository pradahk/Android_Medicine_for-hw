// Android 애플리케이션에서 SQLite 데이터베이스를 정의하는 클래스
// CreateDB라는 내부 클래스 포함. (CreateDB는 알람 관련 데이터를 저장하는 데이터베이스 테이블을 생성하는 역할)
package com.example.androidlogin;
//이 클래스가 com.example.androidlogin 패키지에 속해 있다는 것.(당연함 깃헙 주소 보셈)

import android.provider.BaseColumns;
//BaseColumns를 상속받아 기본적으로 _ID 컬럼을 포함할 수 있도록함. _ID는 기본 키 역할을 하며, 행을 식별하는 고유한 값임.
//BaseColumns는 Android의 android.provider.BaseColumns 인터페이스로, SQLite 데이터베이스에서 테이블을 만들 때 기본적으로 제공되는 "_ID" 컬럼을 포함함.
public class Databases {

    public static final class CreateDB implements BaseColumns {
    //CreateDB는 BaseColumns를 구현하여 ' 자동 증가하는 _ID 필드(기본 키) '를 제공.
    //테이블의 컬럼(열) 정의
        public static final String AMPM = "ampm"; //AMPM: 오전(AM) 또는 오후(PM) 값을 저장하는 text 타입의 필드.
        public static final String HOUR = "hour"; //HOUR: 알람이 울릴 시간(시)을 저장하는 integer 타입의 필드.
        public static final String MINUTE = "minute"; //MINUTE: 알람이 울릴 분을 저장하는 integer 타입의 필드.
        public static final String DRUGTEXT = "drugtext"; //DRUGTEXT: 약의 이름이나 복용 관련 메모를 저장하는 text 타입의 필드.
        public static final String ALARMTIME = "alarmtime"; //ALARMTIME: 전체 알람 시간을 integer 값(예: 타임스탬프)으로 저장.
        public static final String TABLE_NAME ="alarmtable"; //테이블 이름 정의

        //테이블 생성 sql문
        public static final String _CREATE0= "create table if not exists "
        //SQLite 테이블을 생성하는 SQL 명령어(CREATE TABLE 구문)
        //if not exists를 사용하여 이미 테이블이 존재하면 새로 생성하지 않도록 방지
                + TABLE_NAME+ "("
                +_ID+" integer primary key autoincrement, " //기본 키로 자동 증가.
                +AMPM+" text not null, " //AM 또는 PM 값을 반드시 포함해야 함.
                +HOUR+" integer not null , " //시(hour) 값을 저장해야 함.
                +MINUTE+" integer not null , " //분(minute) 값을 저장해야 함.
                +DRUGTEXT+" text not null ," //약 복용 메모가 반드시 필요.
                +ALARMTIME+" integer not null );"; //알람 시간이 타임스탬프 또는 정수 값으로 저장.
        
        //테이블 내에서 구분하기 위해 ID값을 만들어서 행을 추가할때마다 1씩 증가하여 _ID에 입력되도록 한다.
        //HOUR와 MINNuTE은 integer, drugtext는 text로 데이터형식 설정한다.

    }
}
