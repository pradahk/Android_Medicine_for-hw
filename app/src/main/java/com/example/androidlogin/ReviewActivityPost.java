package com.example.androidlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

import listener.OnPostListener;

//게시글화면 만들기. 해당하는 게시글을 클릭하면 자세히 볼 수 있는 화면이 나옴
public class ReviewActivityPost extends ReviewMainActivity {
    private ReviewPostInfo reviewPostInfo;
    private ReviewPostAdapter reviewPostAdapter;


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

        buttonClick();
    }
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

        Button modify2 = findViewById(R.id.modify2);
        Button delete2 = findViewById(R.id.delete2);

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

    private void myStartActivity (Class c, ReviewPostInfo reviewPostInfo){//intent를 이용하여 id 값을 전달해줄것임.
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", reviewPostInfo);//앞에는 key값, 뒤에는 실제 값
        //id값을 보내주면 WritePostActivity에서 받아서 쓸것임
        startActivity(intent);
    }
    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
