package com.example.androidlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentInfo extends Fragment implements View.OnClickListener {

    private InfoDialog infoDialog;


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
        openinfo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openinfo:
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.show(getActivity().getSupportFragmentManager(),"open");
                break;

        }

    }
}

