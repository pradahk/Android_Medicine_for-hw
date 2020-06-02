package com.example.androidlogin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class InfoDialog extends DialogFragment implements View.OnClickListener {

    private Fragment fragment;

    private View.OnClickListener positiveListener;

    public InfoDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.info_dialog, container, false);
        view.findViewById(R.id.positivebutton).setOnClickListener(this);
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);

        return view;
    }

    // 다이얼로그 종료 메서드
    private void dismissDialog() {
        this.dismiss();
    }

    // "확인"버튼 클릭시 다이얼로그 종료
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positivebutton:
                dismissDialog();
                break;
        }


}

}
