package com.example.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FormMainActivity extends AppCompatActivity implements Button.OnClickListener {
    private static final String TAG = "Ma";
    private ProgressDialog progressDialog; //로딩중 progressDialog

    // 각각의 카테고리에서 최종적으로 선택한 것 저장
    private String choosecolor = null; // 선택한 색상 저장
    private String chooseshape = null; // 선택한 모양 저장
    private String choosetype = null; // 선택한 제형 저장
    private String searchmarkfront = null; // 식별자 검색 저장(앞)
    private String searchmarkback = null; // 식별자 검색 저장(뒤)

    //색상 버튼과 관련
    Button[] colorBtn = new Button[16]; //색상 버튼 배열
    Button result_colorbtn; //버튼의 id값 저장
    private String colorbtn_id; //버튼의 id값
    private String thiscolor; // 비교할 색상 값

    //모양 버튼과 관련
    Button[] shapeBtn = new Button[11]; //모양 버튼 배열
    Button result_shapebtn; //버튼의 id값 저장
    private String shapebtn_id; //버튼의 id값
    private String thisshape; // 비교할 색상 값

    //제형 버튼과 관련
    Button[] typeBtn = new Button[4]; //모양 버튼 배열
    Button result_typebtn; //버튼의 id값 저장
    private String typebtn_id; //버튼의 id값
    private String thistype; // 비교할 색상 값



    TextView textcolor, textshape, texttype;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity_main);


        //색상 버튼 눌린것 텍스트뷰로 띄워줄것
        textcolor = (TextView) findViewById(R.id.choosecolor);
        textshape = (TextView) findViewById(R.id.chooseshape);
        texttype = (TextView) findViewById(R.id.choosetype);

        //로딩중 progressdialog
        progressDialog = new ProgressDialog(this);

        thiscolor = textcolor.getText().toString();
        thisshape = textshape.getText().toString();
        thistype = texttype.getText().toString();

        Log.e("지금 색:", thiscolor);

        //색상, 모양, 제형 버튼 이벤트 실행
        settingColorbtn();
        settingShapebtn();
        settingTypebtn();

    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//


    //색상 버튼 이벤트
    public void settingColorbtn(){
        for(int i=0; i <colorBtn.length; i++){
            colorbtn_id = "color_btn" + (i+1); //버튼 아이디값 저장
            colorBtn[i] = findViewById(getResources().getIdentifier(colorbtn_id, "id",getPackageName())); //버튼 초기화

        }

        for(Button buttonId : colorBtn){
            buttonId.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    result_colorbtn = findViewById(v.getId());
                    result_colorbtn.setBackgroundResource(R.drawable.choose_btton); //해당아이디 버튼의 배경색을 바꿈
                    result_colorbtn.setTextColor(Color.WHITE);
                    choosecolor = result_colorbtn.getText().toString(); //선택 색상을 저장

                    //////여기서 for문으로 thiscolor랑 result.getText.toString()비교해서 배경색 다시 바꿔주기
                    Log.e("다음 클릭 후 : ", thiscolor);

                    for(int j=0; j<colorBtn.length; j++){
                        if(!colorBtn[j].getText().toString().equals(choosecolor)) {
                                    colorBtn[j].setBackgroundResource(R.drawable.basic_button);
                                    colorBtn[j].setTextColor(Color.BLACK);
                        }if(colorBtn[j].getText().toString().equals(thiscolor)){
                            colorBtn[j].setBackgroundResource(R.drawable.basic_button);
                            colorBtn[j].setTextColor(Color.BLACK);
                        }
                    }

                    thiscolor = textcolor.getText().toString();

                }
            });
        }

    }

    // 모양 버튼 이벤트
    public void settingShapebtn(){
        for(int i=0; i <shapeBtn.length; i++){
            shapebtn_id = "shape_btn" + (i+1); //버튼 아이디값 저장
            shapeBtn[i] = findViewById(getResources().getIdentifier(shapebtn_id, "id",getPackageName()));
        }

        for(Button buttonId : shapeBtn){
            buttonId.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    result_shapebtn = findViewById(v.getId());
                    result_shapebtn.setBackgroundResource(R.drawable.choose_btton); //해당아이디 버튼의 배경색을 하양으로 바꿈
                    result_shapebtn.setTextColor(Color.WHITE);
                    chooseshape = result_shapebtn.getText().toString();


                    Log.e("다음 클릭 후 : ", thisshape);

                    for(int j=0; j<shapeBtn.length; j++){
                        if(!shapeBtn[j].getText().toString().equals(chooseshape)) {
                            shapeBtn[j].setBackgroundResource(R.drawable.basic_button);
                            shapeBtn[j].setTextColor(Color.BLACK);
                        }if(shapeBtn[j].getText().toString().equals(thisshape)){
                            shapeBtn[j].setBackgroundResource(R.drawable.basic_button);
                            shapeBtn[j].setTextColor(Color.BLACK);
                        }
                    }

                    //  textcolor.setText(result.getText()); // 선택 색상을 보여줄 textview

                    thisshape = textshape.getText().toString();
                }
            });
        }
    }

    // 제형 버튼 이벤트
    public void settingTypebtn(){
        for(int i=0; i <typeBtn.length; i++){
            typebtn_id = "type_btn" + (i+1); //버튼 아이디값 저장
            typeBtn[i] = findViewById(getResources().getIdentifier(typebtn_id, "id",getPackageName())); //초기화
        }

        for(Button buttonId : typeBtn){
            buttonId.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    result_typebtn = findViewById(v.getId());
                    result_typebtn.setBackgroundResource(R.drawable.choose_btton); //해당아이디 버튼의 배경색을 하양으로 바꿈
                    result_typebtn.setTextColor(Color.WHITE);
                    choosetype = result_typebtn.getText().toString();

                    if(choosetype.contains("정")){
                        choosetype = "나정, 필름코팅정, 서방정, 저작정, 구강붕해정, 장용성필름코팅정, 다층정";
                    }else if(choosetype.contains("경질")){
                        choosetype = "경질캡슐제|산제, 경질캡슐제|과립제, 경질캡슐제|장용성과립제";
                    }else if(choosetype.contains("연질")){
                        choosetype ="연질캡슐제|현탁상, 연질캡슐제|액상";
                    } else if(choosetype.contains("기타")){
                        choosetype = "껌제";
                    }

                    //texttype.setText(choosetype);

                    Log.e("choosetype ?????", choosetype);
                    Log.e("다음 클릭 후 : ", thistype);

                    for(int j=0; j<typeBtn.length; j++){

                        if(typeBtn[j].getText().toString().contains("정")){
                            if(!choosetype.contains("정")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                            if(thisshape.contains("정")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                        }else if(typeBtn[j].getText().toString().contains("경질")){
                            if(!choosetype.contains("경질")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                            if(thisshape.contains("경질")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                        }else if(typeBtn[j].getText().toString().contains("연질")){
                            if(!choosetype.contains("연질")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                            if(thisshape.contains("연질")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                        }else {
                            if(!choosetype.contains("껌제")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                            if(thisshape.contains("껌제")) {
                                typeBtn[j].setBackgroundResource(R.drawable.basic_button);
                                typeBtn[j].setTextColor(Color.BLACK);
                            }
                        }

                    }

                    //  textcolor.setText(result.getText()); // 선택 색상을 보여줄 textview

                    thistype = texttype.getText().toString();
                }
            });
        }
    }




    //색상, 모양, 제형 버튼 클릭 함수
    @Override
    public void onClick(View v) {

    }


    //식별자 앞 edittext값 초기화, 저장
    public void takeMarkfront(){
        EditText markfront = (EditText) findViewById(R.id.mark_front);
        searchmarkfront = markfront.getText().toString();
        if(searchmarkfront.length() == 0){
            searchmarkfront = null; // 입력된 값이 없을때 '-'로 저장
        }else {
            searchmarkfront=this.searchmarkfront;
        }
    }

    //식별자 뒤 edittext값 초기화, 저장
    public void takeMarkBack(){
        EditText markback = (EditText) findViewById(R.id.mark_Back);
        searchmarkback = markback.getText().toString();
        if(searchmarkback.length() == 0){
            searchmarkback = null;
        }else{
            searchmarkback = this.searchmarkback;
        }
    }


    //검색 결과 버튼
    public void click_result(View view) {

      //  progressDialog.setMessage("로딩중입니다.");
       // progressDialog.show();

       //takeMarkfront(); // 식별자 앞 edit에 입력한 텍스트값 가져오기
       //takeMarkBack();

        Intent intent = new Intent(getApplicationContext(), FormSearchActivity.class);

        intent.putExtra("choosecolor",choosecolor);
        intent.putExtra("chooseshape",chooseshape);
        intent.putExtra("choosetype",choosetype);
        //intent.putExtra("searchmarkfront",searchmarkfront);
        //intent.putExtra("searchmarkback", searchmarkback);

        startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        //progressDialog.dismiss();
    }

    //식별자 검색 결과 버튼
    public void click_markresult(View view) {
        progressDialog.setMessage("로딩중입니다.");
        progressDialog.show();

        takeMarkfront(); // 식별자 앞 edit에 입력한 텍스트값 가져오기
        takeMarkBack();

        Intent intent = new Intent(getApplicationContext(), FormSearchActivity.class);
        intent.putExtra("searchmarkfront",searchmarkfront);
        intent.putExtra("searchmarkback", searchmarkback);


        startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        //progressDialog.dismiss();
    }


    //초기화 버튼
    public void click_research(View view) {
        choosecolor = null;
        chooseshape = null;
        choosetype = null;
       // textcolor.setText("");
       // textshape.setText("");
        Toast myToast = Toast.makeText(this.getApplicationContext(),"선택이 초기화 되었습니다.", Toast.LENGTH_SHORT);
        myToast.show();

        for(int i=0; i <colorBtn.length; i++){
            colorBtn[i].setBackgroundColor(Color.WHITE);
            colorBtn[i].setBackgroundResource(R.drawable.basic_button);
            colorBtn[i].setTextColor(Color.BLACK);
        }
        for(int i=0; i <shapeBtn.length; i++){
            shapeBtn[i].setBackgroundColor(Color.WHITE);
            shapeBtn[i].setBackgroundResource(R.drawable.basic_button);
            shapeBtn[i].setTextColor(Color.BLACK);
        }
        for(int i=0; i <typeBtn.length; i++){
            typeBtn[i].setBackgroundColor(Color.WHITE);
            typeBtn[i].setBackgroundResource(R.drawable.basic_button);
            typeBtn[i].setTextColor(Color.BLACK);
        }

    }
}