//공공데이터 포털의 약국 정보 API를 사용하여 XML 데이터를 가져오고, 이를 파싱하여 필요한 정보를 추출하는 클래스
package com.example.androidlogin;

import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

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

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();
            pharmname="";
            pharmadd="";
            pharmtel="";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기

                        if (tag.equals("item")) ;// 첫번째 태그값이랑 비교
                        else if (tag.equals("addr")) {
                            xpp.next();
                            pharmadd = xpp.getText();
                        } else if (tag.equals("yadmNm")) {
                            buffer.append("\n");
                            xpp.next();
                            pharmname = xpp.getText();
                            buffer.append("약국명 : " + pharmname +"\n" + "주소 : " + pharmadd + "\n" + "전화번호 : " + pharmtel);
                            buffer.append("\n"+"_____________________________________________________");
                        } else if (tag.equals("telno")) {
                            xpp.next();
                            pharmtel = xpp.getText();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //테그 이름 얻어오기
                        if (tag.equals("item"))
                            buffer.append("\n");// 첫번째 검색결과종료 후 줄바꿈
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }
}
