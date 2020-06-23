package com.example.androidlogin;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import listener.OnAlarmListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentAlarm extends Fragment {

    private static final String TAG = "FragmentAlarm";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private MyAdapter myAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<AlarmInfo> alarmList;
    private EditText editText;
    private TimePicker timePicker;
    private int hour,minute;
    private AlarmManager alarmManager;
    String id;

    public FragmentAlarm(){
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm,container,false);
        editText = (EditText)view.findViewById(R.id.editText);
        timePicker = (TimePicker)view.findViewById(R.id.timepicker);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        alarmUpdate();
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myStartActivity(SettingAlarm.class);
        }
    };

    //DB에 저장된 게시물 보여주는 코드. 배열로 해서 apater로 넘겨줌.
    private void alarmUpdate(){
        firebaseFirestore.collection("AlarmDemo").orderBy("hour", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            alarmList = new ArrayList<>();
                            alarmList.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                alarmList.add(new AlarmInfo(
                                        document.getData().get("hour").toString(),
                                        document.getData().get("minute").toString(),
                                        document.getData().get("drugtext").toString(),
                                        document.getData().get("ampm").toString(),
                                        document.getId()
                                ));
                            }
                            myAdapter = new MyAdapter(getActivity(), alarmList);
                            myAdapter.setOnAlarmListener(onAlarmListener);
                            recyclerView.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();
                        }else {
                            Log.d(TAG, "Error : ",task.getException());
                        }
                    }
                });
    }

    OnAlarmListener onAlarmListener = new OnAlarmListener() {//인터페이스인 OnPostListener를 가져와서 구현해줌
        @Override
        public void onDelete(final int position) {//MainAdapter에 넘겨주기 위한 메서드 작성

            id = alarmList.get(position).getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
            firebaseFirestore.collection("AlarmDemo").document(id).delete()//그 id에 맞는 값들을 지워줌
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {//성공시
                            alarmUpdate();

                            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                            // intent.putExtra("cancelId", alarmList.get(position).getHour()+alarmList.get(position).getMinute());
                            //cancelId = intent.getStringExtra("cancelId");
                            //if (cancelId != null){
                            Log.e("cancel : ","cancel");
                            //alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                            // Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                            PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(),
                                    Integer.parseInt(alarmList.get(position).getHour()+alarmList.get(position).getMinute()),intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pIntent);
                            pIntent.cancel();
                            Toast.makeText(getActivity(), "알림이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("ERROR : ","ERROR");

                            Log.e("DB삭제 성공 : ","성공");

                        }
                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {//실패시
                    startToast("게시글 삭제에 실패하였습니다.");
                }
            });
        }
        @Override
        public void onModify(int position) {//여기서 수정하면 writepostActivity를 켜서 수정해주는코드
            // myStartActivity(SettingAlarm.class,alarmList.get(position));
            Intent intent = new Intent(getActivity(), SettingAlarm.class);
            //intent.putExtra("drug",firedrugtext);
            intent.putExtra("modifyId", alarmList.get(position).getHour()+alarmList.get(position).getMinute());
            intent.putExtra("alarmInfo",alarmList.get(position));
            startActivity(intent);
        }
    };
    private void myStartActivity(Class c) {//게시물을 추가하는 경우 WritePostActivity 화면으로 넘겨주는 코드
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 1);

    }

    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
    }

}
