package com.example.androidlogin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText editTextpassword;

    private String pass = "";
    private Button btninfo;
    private Button positivebutton;
    private Button negativebutton;

    public InfoDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.info_dialog, container, false);

        //Dialog dialog = getDialog();
       // dialog.setCanceledOnTouchOutside(false);

        editTextpassword = view.findViewById(R.id.inputpassword);

        positivebutton = view.findViewById(R.id.positivebutton);
        negativebutton = view.findViewById(R.id.negativebutton);

        negativebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        positivebutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pass = editTextpassword.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String checkpass = user.getEmail();
                    if (pass.equals(checkpass)) {
                        Toast.makeText(getActivity(), "비밀번호 확인 성공", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();

                    }
                    else{
                        Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        return view;

}

}
