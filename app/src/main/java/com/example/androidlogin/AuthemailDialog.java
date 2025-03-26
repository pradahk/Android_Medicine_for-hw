//이메일 인증을 위한 다이얼로그를 정의한 클래스. 사용자가 이메일 인증을 할 때, 이 다이얼로그를 띄우는 역할임.
package com.example.androidlogin;

import android.app.Dialog; //사용자와 상호작용할 수 있는 작은 창을 띄우는 데 사용
import android.content.Context; //안드로이드의 시스템 서비스에 접근할 수 있게 해주는 객체. 다이얼로그를 생성할 때 필요
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager; //다이얼로그의 속성(밑에 화면 흐리게 하기)을 설정하는 데 사용
import android.widget.Button;

import androidx.annotation.NonNull;

public class AuthemailDialog extends Dialog { //Dialog를 상속받아 이메일 인증을 위한 다이얼로그를 생성하고 동작을 정의
//Dialog는 안드로이드에서 사용자 인터페이스 상에서 팝업 형태로 표시되는 UI 요소
    
    // 확인 버튼 생성
    private Button positivebutton;
    // 확인 버튼의 onClickListner 생성
    private View.OnClickListener positiveListener;

    // 이메일 인증 다이얼로그
    public AuthemailDialog(@NonNull Context context, View.OnClickListener positiveListener) {
        //Context context: 다이얼로그가 생성될 위치와 관련된 컨텍스트.
        //View.OnClickListener positiveListener: 다이얼로그에서 '확인' 버튼을 클릭했을 때 실행될 리스너.
        super(context);
        this.positiveListener = positiveListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 밖의 화면은 흐리게 함
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND; //<-밖 화면 흐리
        layoutParams.dimAmount = 0.8f; //흐림 정도 설
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.authemail_dialog); //다이얼로그의 레이아웃을 설정하는 코드. 이 레이아웃은 XML 파일로 다이얼로그 UI를 구성함

        // 확인 버튼
        positivebutton = findViewById(R.id.positivebutton); //XML 레이아웃에서 정의된 버튼의 ID
        // 클릭 리스너
        positivebutton.setOnClickListener(positiveListener); //positiveListener로 전달된 클릭 리스너를 설정하여, 사용자가 버튼을 클릭할 때 실행될 동작 정의
    }
}
