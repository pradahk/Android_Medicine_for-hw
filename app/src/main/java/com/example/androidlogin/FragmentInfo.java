package com.example.androidlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FragmentInfo extends Fragment {

    // 회원정보 열람을 위한 다이얼로그 객체 생성
    private InfoDialog infoDialog;

    // 작성한 이메일을 저장할 객체
    private String email = "";

    private FirebaseUser user;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // 작성한 이메일 값과 비밀번호 값을 저장할 객체 생성
    private TextView editTextEmail;
    private TextView editTextPassword;


    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private TextView editTextName;
    private TextView editTextPhone;

    public FragmentInfo() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // 로그인한 사용자가 있는 경우
                if (user != null) {
                    InfoDialog infoDialog = new InfoDialog();
                    infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_NoTitleBar);
                    infoDialog.show(requireActivity().getSupportFragmentManager(), "open");


                }
                // 로그인한 사용자가 없는 경우
                else {
                    Toast.makeText(getActivity(), "로그인을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show();
                    editTextEmail.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.INVISIBLE);
                    editTextPhone.setVisibility(View.INVISIBLE);
                    editTextPassword.setVisibility(View.INVISIBLE);


                }
            }
        };


    }
    @Override public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextEmail = view.findViewById(R.id.write_email);
        // id가 signup_password인 editText에 대한 메서드 저장
        editTextPassword = view.findViewById(R.id.signup_password);
        // id가 write_name인 editText에 대한 메서드 저장
        editTextName = view.findViewById(R.id.write_name);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextPhone = view.findViewById(R.id.write_phone);

        // info 메서드 실행
        info();

        return view;
    }

    // info 메서드
    private void info(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                // 입력한 이메일과 firestore에 저장된 이메일이 같을 경우 회원정보를 Text에 보여줌
                                if(user.getEmail().equals(document.getData().get("email"))) {
                                    editTextEmail.setText(document.getData().get("email").toString());
                                    editTextName.setText(document.getData().get("name").toString());
                                    editTextPhone.setText(document.getData().get("phone").toString());
                                    editTextPassword.setText(document.getData().get("password").toString());
                                    break;
                                }

                            }
                        }
                    }
                });

    }


    }


