package com.example.androidlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FragmentInfo extends Fragment {

    private InfoDialog infoDialog;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    public FragmentInfo() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Button openinfo = view.findViewById(R.id.openinfo);
       // openinfo.setOnClickListener(this);
        openinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    InfoDialog infoDialog = new InfoDialog();
                    infoDialog.show(requireActivity().getSupportFragmentManager(),"open");
                }
                else{
                    Toast.makeText(getActivity(), "로그인을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openinfo:
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.show(getActivity().getSupportFragmentManager(),"open");
                break;
        }

    }
    */
}

