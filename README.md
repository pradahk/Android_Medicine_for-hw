[^scala]
import android.app.Dialog; //사용자와 상호작용할 수 있는 작은 창을 띄우는 데 사용
import android.content.Context; //안드로이드의 시스템 서비스에 접근할 수 있게 해주는 객체. 다이얼로그를 생성할 때 필요
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager; //다이얼로그의 속성(밑에 화면 흐리게 하기)을 설정하는 데 사용
import android.widget.Button;
import androidx.annotation.NonNull;

//Firebase Firestore와 관련된 라이브러리를 import하여 데이터베이스에서 사용자 정보를 검색할 수 있도록 설정
import android.app.ProgressDialog; //ProgressDialog는 로딩 중임을 사용자에게 표시하는 다이얼로그
//밑에 "처리 중입니다. 잠시만 기달려 주세요."를 보면 알 수 있음.
import android.content.Intent; //Android의 컴포넌트(액티비티, 서비스 등) 간 데이터를 주고받는 데 사용
//밑에 intent 부분을 보면, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK를 추가하여 기존 액티비티 스택을 정리하고 새로 시작함
import android.os.Bundle; //액티비티 간 데이터를 저장하고 전달하는 객체
//밑에 bundle에서 .onCreate를 보면 액티비티가 생성될 때 상태 정보를 저장하고 전달하는 데 사용
import android.util.Patterns; //정규 표현식 패턴을 제공하는 유틸리티 클래스. 이메일 주소나 웹 URL이 유효한지 확인할 때 주로 사용
//(없을?)
import android.view.View; //UI 요소의 기본 클래스. 버튼, 텍스트뷰, 에디트텍스트 등 모든 UI 요소는 View를 상속받음.
//밑에 View.VISIBLE을 사용하여 버튼의 가시성을 조정함
import android.widget.Button; //사용자가 클릭할 수 있는 버튼 정의
//버튼을 찾아서(findViewById) 클릭 이벤트를 설정하는 데 사용. clicklistener로 조정한
import android.widget.EditText; //사용자가 텍스트를 입력할 수 있는 입력 필드
//사용자가 입력한 이름과 전화번호를 가져오는 역할
import android.widget.TextView; //텍스트를 화면에 표시하는 데 사용
//textshowtext와 textshowid는 검색 결과를 사용자에게 보여주는 역할
import android.widget.Toast; //짧은 시간 동안 화면에 메시지를 띄우는 UI 요소
//(없음?)
import androidx.annotation.NonNull; //null이 될 수 없는 변수, 매개변수, 또는 반환 값을 표시하는 어노테이션(주석?)
import androidx.appcompat.app.AppCompatActivity; //Android의 기본 Activity 클래스를 확장한 버전으로, 앱이 최신 UI 및 기능과 호환되도록 돕는 역할. Android의 ' Compatibility Support Library '에 속함.
import com.google.android.gms.tasks.OnCompleteListener; //비동기 작업(예: Firestore 데이터 가져오기, Firebase 인증 등)이 완료되었을 때 실행되는 콜백 인터페이스
import com.google.android.gms.tasks.Task; //비동기 작업의 결과를 나타내는 클래스. 성공 or 실패 여
import com.google.firebase.auth.FirebaseAuth; //Firebase Authentication을 사용하여 로그인, 로그아웃, 사용자 인증을 처리하는 클래스. Google, Facebook, 이메일 등을 이용한 로그인 기능을 제공
//(없음?)
import com.google.firebase.firestore.DocumentReference; //Firestore 데이터베이스에서 특정 문서를 참조하는 객체. 특정 문서의 데이터를 읽거나 수정할 때 사용
//(없음?)
import com.google.firebase.firestore.DocumentSnapshot; //Firestore 데이터베이스의 단일 문서를 나타내는 객체. 문서의 데이터를 가져오거나 필드를 읽을 때 사용
//(없음?)
import com.google.firebase.firestore.FirebaseFirestore; //Firebase의 클라우드 데이터베이스 Firestore에 접근하는 객체. 데이터를 읽고 쓰거나 업데이트할 때 사용
//밑에 Firebase의 Firestore 데이터베이스와 연결하는 객체. instance를 생각하면 되는듯.
import com.google.firebase.firestore.QueryDocumentSnapshot; //Firestore 쿼리 결과에서 개별 문서를 나타내는 객체. 위 DocumentSnapshot과 비슷하지만 반복문에서 사
import com.google.firebase.firestore.QuerySnapshot; //Firestore에서 여러 개의 문서를 가져왔을 때, 그 결과를 나타내는 객체
