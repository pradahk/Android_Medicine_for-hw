//앱 시작할 때 로딩 화면 보여주고, 이후에 다른 화면(여기서는 MenuActivity)으로 자동으로 넘어가는 화면 전환을 구현한 것
package com.example.androidlogin;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends MainActivity {//loading화면 -> main화면으로 넘어갈때.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //bundle import한거에서 호출된 oncreate 이제 눈에 잘 보이져?

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        //새로운 Intent 객체 생성. Intent는 다른 액티비티를 시작하거나 서비스를 실행할 때 사용됨.
        //getApplicationContext()는 현재 애플리케이션의 컨텍스트 가져옴. MenuActivity.class는 전환하려는 대상 화면.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //Intent.FLAG_ACTIVITY_NO_HISTORY 플래그를 추가하는 이유는, SplashActivity가 MenuActivity로 이동한 뒤 SplashActivity가 스택에서 제거되도록.
        //따라서 사용자가 뒤로 가기 버튼을 눌러도 SplashActivity로 돌아가지 않음.
        startActivity(intent);
        //지정된 Intent에 따라 새로운 액티비티 시작. 여기서는 MenuActivity로 이동
        finish();
        //finish()를 호출함으로써, SplashActivity는 더 이상 액티비티 스택에 존재하지 않아 뒤로 가기 버튼을 눌러도 SplashActivity로 안돌아감.
    }
}
