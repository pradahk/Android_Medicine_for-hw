import android.app.Dialog; //사용자와 상호작용할 수 있는 작은 창을 띄우는 데 사용

import android.content.Context; //안드로이드의 시스템 서비스에 접근할 수 있게 해주는 객체. 다이얼로그를 생성할 때 필요

import android.view.WindowManager; //다이얼로그의 속성(밑에 화면 흐리게 하기)을 설정하는 데 사용

import android.app.ProgressDialog; //ProgressDialog는 로딩 중임을 사용자에게 표시하는 다이얼로그

import android.content.Intent; //Android의 컴포넌트(액티비티, 서비스 등) 간 데이터를 주고받는 데 사용

import android.os.Bundle; //액티비티 간 데이터를 저장하고 전달하는 객체

import android.util.Patterns; //정규 표현식 패턴을 제공하는 유틸리티 클래스. 이메일 주소나 웹 URL이 유효한지 확인할 때 주로 사용

import android.view.View; //UI 요소의 기본 클래스. 버튼, 텍스트뷰, 에디트텍스트 등 모든 UI 요소는 View를 상속받음.

import android.widget.Button; //사용자가 클릭할 수 있는 버튼 정의

import android.widget.EditText; //사용자가 텍스트를 입력할 수 있는 입력 필드

import android.widget.Text; //텍스트를 화면에 표시하는 데 사용

import android.widget.Toast; //짧은 시간 동안 화면에 메시지를 띄우는 UI 요소

import androidx.annotation.NonNull; //null이 될 수 없는 변수, 매개변수, 또는 반환 값을 표시하는 어노테이션(주석?)

import androidx.appcompat.app.AppCompatActivity; //Android의 기본 Activity 클래스를 확장한 버전으로, 앱이 최신 UI 및 기능과 호환되도록 돕는 역할. Android의 ' Compatibility Support Library '에 속함.

import com.google.android.gms.tasks.OnCompleteListener; //비동기 작업(예: Firestore 데이터 가져오기, Firebase 인증 등)이 완료되었을 때 실행되는 콜백 인터페이스

import com.google.android.gms.tasks.Task; //비동기 작업의 결과를 나타내는 클래스. 성공 or 실패 여부

import com.google.firebase.auth.FirebaseAuth; //Firebase Authentication을 사용하여 로그인, 로그아웃, 사용자 인증을 처리하는 클래스. Google, Facebook, 이메일 등을 이용한 로그인 기능을 제공

import com.google.firebase.firestore.DocumentReference; //Firestore 데이터베이스에서 특정 문서를 참조하는 객체. 특정 문서의 데이터를 읽거나 수정할 때 사용

import com.google.firebase.firestore.DocumentSnapshot; //Firestore 데이터베이스의 단일 문서를 나타내는 객체. 문서의 데이터를 가져오거나 필드를 읽을 때 사용

import com.google.firebase.firestore.FirebaseFirestore; //Firebase의 클라우드 데이터베이스 Firestore에 접근하는 객체. 데이터를 읽고 쓰거나 업데이트할 때 사용

import com.google.firebase.firestore.QueryDocumentSnapshot; //Firestore 쿼리 결과에서 개별 문서를 나타내는 객체. 위 DocumentSnapshot과 비슷하지만 반복문에서 사용

import com.google.firebase.firestore.QuerySnapshot; //Firestore에서 여러 개의 문서를 가져왔을 때, 그 결과를 나타내는 객체

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.util.Log;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.remote.EspressoRemoteMessage;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;

import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser; //XML 데이터를 읽고 해석하는 데 사용되는 인터페이스. XML 문서를 태그 단위로 읽고 특정 태그를 찾거나 내용을 추출함
import org.xmlpull.v1.XmlPullParserFactory; //XmlPullParser 객체를 생성하는 팩토리 클래스
//XmlPullParser는 직접 객체를 생성할 수 없고, 이 팩토리 클래스를 이용해 인스턴스를 얻어야 함 <- why?

import java.io.InputStream; //바이트 단위의 입력 스트림을 나타내는 Java의 기본 인터페이스
import java.io.InputStreamReader; //InputStream의 바이트 단위를 문자(char) 단위로 읽을 수 있도록 변환하는 클래스
import java.io.UnsupportedEncodingException; //지원되지 않는 문자 인코딩을 사용하려고 할 때 발생하는 예외
import java.net.URL; //웹 주소(URL)를 다룰 수 있도록 제공하는 클래스
import java.net.URLEncoder; //문자열을 URL 형식으로 인코딩하는 데 사용

import android.view.LayoutInflater;

import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity; 

import com.google.android.gms.tasks.OnCompleteListener; //여기까지 import는 id찾기에서 다 봤지?
import com.google.android.gms.tasks.OnFailureListener; //Firebase 작업이 실패했을 때 호출되는 콜백
import com.google.android.gms.tasks.OnSuccessListener; //이건 성공했을때
import com.google.android.gms.tasks.Task; //이것도 봤지?
import com.google.firebase.auth.AuthResult; //Firebase 인증 관련 작업에서 인증 결과를 담고 있는 객체. 사용자가 로그인하거나 회원가입을 완료하면, 성공 여부 또는 인증된 사용자의 정보를 담아둠.
import com.google.firebase.auth.FirebaseAuth; //이것도 봤지?
import com.google.firebase.auth.FirebaseUser; //Firebase Authentication을 통해 인증된 사용자의 정보를 나타내는 클래스. 현재 로그인한 사용자의 이메일, UID 같은 정보를 제공하며, 이메일 인증을 보내거나, 사용자 데이터를 업데이트할 때 사용할 수 있음.
import com.google.firebase.firestore.FirebaseFirestore; //이것도 봤지?

import java.util.HashMap; //키와 값의 쌍으로 데이터를 저장하는 자료구조. 사용자의 이메일, 비밀번호, 이름, 전화번호 등을 HashMap에 저장하고 Firestore에 전송
import java.util.Map; //HashMap과 같은 자료구조. HashMap은 Map의 구현체이므로 Map을 사용하여 다른 구현체도 사용 가능. 
//밑에서 Firestore에 데이터를 저장할 때 Map<String, Object> 타입을 사용
import java.util.regex.Pattern; //정규식을 처리하는 데 사용.
//밑에서 유효성 검사할 때 사용되는듯.

import static com.example.androidlogin.FirebaseID.user;
