//공공데이터 포털의 약국 정보 API를 사용하여 XML 데이터를 가져오고, 이를 파싱하여 필요한 정보를 추출하는 클래스
package com.example.androidlogin;

import android.widget.EditText;
import android.widget.TextView; //여기까지는 이미 봤지?

import org.xmlpull.v1.XmlPullParser; //XML 데이터를 읽고 해석하는 데 사용되는 인터페이스. XML 문서를 태그 단위로 읽고 특정 태그를 찾거나 내용을 추출함
import org.xmlpull.v1.XmlPullParserFactory; //XmlPullParser 객체를 생성하는 팩토리 클래스
//XmlPullParser는 직접 객체를 생성할 수 없고, 이 팩토리 클래스를 이용해 인스턴스를 얻어야 함 <- why?

import java.io.InputStream; //바이트 단위의 입력 스트림을 나타내는 Java의 기본 인터페이스
import java.io.InputStreamReader; //InputStream의 바이트 단위를 문자(char) 단위로 읽을 수 있도록 변환하는 클래스
import java.io.UnsupportedEncodingException; //지원되지 않는 문자 인코딩을 사용하려고 할 때 발생하는 예외
import java.net.URL; //웹 주소(URL)를 다룰 수 있도록 제공하는 클래스
import java.net.URLEncoder; //문자열을 URL 형식으로 인코딩하는 데 사용

public class PharmParser {//사용자가 특정 동(행정구역)을 입력하면, 해당 지역의 약국 정보를 검색하여 약국명, 주소, 전화번호를 추출
    EditText edit; //사용자 입력을 받을(검색할 동(읍/면/동)의 이름을 입력하는) 부분
    TextView text; //결과 표시
    XmlPullParser xpp; //for XML 데이터를 파싱
    
    String key = "gyhnkvw8BuHNtPGQzXT5Nluh3Ri3hGlcpEnheMdjI1gjDbZhPSEpy05ofIMaFu2a96c%2FUX%2FzOVblYrTa%2B%2Fu%2Bjg%3D%3D"; //약국 공공데이터 서비스키
    //공공데이터 API를 사용하기 위한 서비스 키(URL 인코딩된 상태). 포털에서 제공하는 API를 사용하려면 서비스 키가 필요함

    String pharmname; //약국명
    String pharmadd; //약국 주소
    String pharmtel; //약국 전화번호

    public String getXmlData() {//API 호출하고, XML 데이터를 파싱하여 필요한 정보를 추출한 후 StringBuffer에 저장하여 반환
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();//EditText에 작성된 Text얻어오기 - 사용자가 입력한 동(지역)이름을 가져옴
        String location = null;
        try {
            location = URLEncoder.encode(str, "UTF-8"); //한글을 URL 인코딩(UTF-8)하여 API 요청에 사용 가능하도록 변환
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String queryUrl = "http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList?serviceKey="//요청 URL
                + key +"&numOfRows=100" + "&emdongNm=" + location; //동 이름으로 검색
        //http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList → 공공데이터 포털의 약국 정보 API 엔드포인트.
        //serviceKey= → 서비스 키를 포함하여 API에 접근 가능하게 함.
        //numOfRows=100 → 한 번에 최대 100개의 데이터 가져오기.
        //emdongNm= → 사용자가 입력한 동(읍/면/동) 이름으로 약국 검색.

        try {//try-catch 사용하네?
            URL url = new URL(queryUrl);
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); //new 보이지? 인스턴스 생성
            xpp = factory.newPullParser(); //이것도 new 보이지?
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기 - API 응답 데이터를 UTF-8 인코딩으로 읽어들임

            String tag;
            xpp.next(); //XML 문서의 첫 번째 데이터 읽음
            int eventType = xpp.getEventType(); //현재 XML 문서의 이벤트 타입을 가져옴. XML 문서의 각 요소(tag)를 읽기 위해 루프 사용
            pharmname="";
            pharmadd="";
            pharmtel="";
            
            while (eventType != XmlPullParser.END_DOCUMENT) {//XML 문서 끝날 때까지 END_DOCUMENT 반복
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG: //새로운 XML 태그가 열릴 때 실행
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 태그값이랑 비교
                        else if (tag.equals("addr")) {
                            xpp.next();
                            pharmadd = xpp.getText(); //<addr>주소</addr> 태그 내부의 주소 데이터를 pharmadd에 저장.
                        } else if (tag.equals("yadmNm")) {
                            buffer.append("\n");
                            xpp.next();
                            pharmname = xpp.getText(); //<yadmNm>약국 이름</yadmNm> 태그 내부의 약국명을 pharmname에 저장
                            buffer.append("약국명 : " + pharmname +"\n" + "주소 : " + pharmadd + "\n" + "전화번호 : " + pharmtel);
                            buffer.append("\n"+"_____________________________________________________");
                        } else if (tag.equals("telno")) {
                            xpp.next();
                            pharmtel = xpp.getText(); //<telno>전화번호</telno> 태그 내부의 전화번호를 pharmtel에 저장
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG: //태그가 닫힐 때 실행
                        tag = xpp.getName(); //테그 이름 얻어오기
                        if (tag.equals("item")) //<item> 태그가 닫힐 때 한 줄 띄우기
                            buffer.append("\n");// 첫번째 검색결과종료 후 줄바꿈
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환 - buffer에 누적된 약국 정보를 문자열로 변환하여 반환

    }
}

// XmlPullParser 객체를 직접 생성할 수 없는 이유
// : XmlPullParser는 ' 인터페이스(interface) '이기 때문에 직접 객체를 만들 수 없음.
// 자바에서는 인터페이스는 직접 인스턴스를 생성할 수 없고, 반드시 구현한 클래스의 객체를 생성해야함.

// 인터페이스란?
// 인터페이스는 메서드의 틀(형식)만 정의하고, 실제 동작(구현부)은 없음.
// 따라서 new XmlPullParser();처럼 직접 생성할 수 없고, 이를 구현한 클래스를 통해서 객체를 만들어야 함.
