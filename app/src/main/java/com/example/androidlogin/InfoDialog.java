package com.example.androidlogin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InfoDialog extends DialogFragment  {

    private Fragment fragment;

    // 파이어베이스 인증 객체 생성
    private FirebaseFirestore firebaseFirestore;
    private View.OnClickListener positiveListener, negativeListener;

    // 입력한 이메일을 저장할 객체
    private String writeemail = "";

    // 확인 버튼과 취소 버튼 객체 생성
    private Button positivebutton;
    private Button negativebutton;

    private EditText editTextemail;

    public InfoDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.info_dialog, container, false);

        editTextemail = view.findViewById(R.id.inputemail);

        positivebutton = view.findViewById(R.id.positivebutton);
        negativebutton = view.findViewById(R.id.negativebutton);

        // 취소 버튼 클릭시 다이얼로그 사라짐
        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // 확인 버튼 클릭시 이메일 값 비교
        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                writeemail = editTextemail.getText().toString();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // 로그인 상태일 경우
                if (user != null) {
                    String checkmail = user.getEmail();
                    // 작성한 이메일과 로그인한 유저의 이메일이 같을 경우
                    if (writeemail.equals(checkmail)) {
                        Toast.makeText(getActivity(), "이메일 확인 성공", Toast.LENGTH_SHORT).show();
                        // 다이얼로그 사라짐
                        getDialog().dismiss();
                    }
                    else{
                        Toast.makeText(getActivity(), "이메일 확인 실패", Toast.LENGTH_SHORT).show();

                    }
                }
                }


        });


        return view;

}

}
