package com.example.androidlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentMainMenu extends Fragment {

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // 파이어스토어 인증 객체 생성
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private ImageButton btn_image;
    private ImageButton btn_map;
    private ImageButton btn_review;

    public FragmentMainMenu(){
    }

    // 파이어베이스 인증을 onStart에 넣어줌
    @Override public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);

        btn_image = view.findViewById(R.id.ImageButton);
        btn_map = view.findViewById(R.id.mapButton);
        btn_review = view.findViewById(R.id.reviewButton);

        btn_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),NameMainActivity.class);
                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),MapMainActivity.class);
                startActivity(intent);
            }
        });

        btn_review.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(),ReviewMainActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }


}
