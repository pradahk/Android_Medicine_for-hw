//Android 앱에서 특정 약물 정보를 상세하게 표시하는 화면. NameMainActivity를 상속받으며, 약물의 이름, 세부 정보, 이미지를 다른 화면에서 전달받아 표시
package com.example.androidlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

//이름으로 검색하기 상세페이지
public class LookupActivity extends NameMainActivity {
    TextView textView;
    TextView detailStr;
    ImageView imageView;

    String drugString;
    String str_detailStr;
    String image; //form 에서 넘어온 어댑터에서 이미지 넣어줄때 사용

    String sort = null; // form, name 중 어느 어댑터에서 넘어온 건지 구분하기 위함

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate로 이 activity가 실행될 때 .xml 레이아웃을 화면에 표시해줌
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        
        textView = findViewById(R.id.textView); //XML 레이아웃에서 TextView, ImageView를 가져와 변수에 저장
        detailStr = findViewById(R.id.detailStr);
        imageView = findViewById(R.id.image);


        //Drug라는 key값으로 NameMyAdapter에서 intent해줄때 넘겨준 값을 가져옴
        drugString = getIntent().getStringExtra("Drug");//String값으로 받아옴. 약 이름을 받아오는것
        str_detailStr = getIntent().getStringExtra("data");
        //NameMyAdapter.java파일에서 intent로 넘겨준 image를 받아와 byte배열에 저장 후 decode하여 imageview에 보여줌
        sort = getIntent().getStringExtra("sort"); //데이터가 어느 어댑터에서 왔는지 구분. sort값에 따라 데이터 처리 방식이 달라짐
        Log.e("form/sort??",sort); //sort 값을 로그에 출력하여 디버깅할 수 있도록 함


        // 이미지 넘겨주는 형식이 다르게 때문에 bitmap, string 구분하기 위해 if문 사용
        if(sort.equals("name")){
            byte[] b = getIntent().getByteArrayExtra("image"); //image 데이터가 바이트 배열(byte[]) 형태로 넘어옴
            Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length); //이를 비트맵(Bitmap) 이미지로 변환하여 imageView에 설정

            //textView와 imageView에 받아온 값들을각각 저장해줌.
            textView.setText(drugString);
            detailStr.setText(str_detailStr);
            imageView.setImageBitmap(bitmap);

        }

        else if(sort.equals("form")){
            image = getIntent().getStringExtra("image"); //image 데이터가 URL 문자열 형태로 넘어옴.
            //Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);

            Glide.with(this) //Glide 라이브러리를 사용하여 이미지를 imageView에 로드
                    .load(image)
                    .into(imageView);

            //textView와 imageView에 받아온 값들을각각 저장해줌.
            textView.setText(drugString);
            detailStr.setText(str_detailStr);
        }



    }
    @Override
    public void onBackPressed() { //뒤로가기 버튼~
        super.onBackPressed();
        finish();
    }
}
