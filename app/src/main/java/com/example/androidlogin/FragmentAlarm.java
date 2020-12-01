package com.example.androidlogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class FragmentAlarm extends Fragment {
    private static final String TAG="FragmentAlarm";

    private SQLiteDatabase database;
    private AlarmDbHelper dbHelper;
    public static final int REQUEST_CODE =1000;
    private AlarmAdapter mAdapter;
    public String cancelId;
    SetAlarm setAlarm = new SetAlarm();
    AlarmReceiver alarmReceiver = new AlarmReceiver();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_alarm);

        /*
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FragmentAlarm.this, SetAlarm.class),
                        REQUEST_CODE);
            }
        });



        final ListView listView = findViewById(R.id.alarmView);

        final Cursor cursor = getAlarmCursor();
        mAdapter = new AlarmAdapter(this,cursor);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final long deleted = id;


                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentAlarm.this);
                builder.setTitle("Alarm delete");
                builder.setMessage("알림을 삭제 하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = AlarmDbHelper.getInstance(FragmentAlarm.this).getWritableDatabase();
                        int deletedCount = db.delete(Databases.CreateDB.TABLE_NAME,
                                Databases.CreateDB._ID + "=" + deleted, null);

                        if(deletedCount ==0){
                            Toast.makeText(FragmentAlarm.this, "알림삭제 오류", Toast.LENGTH_SHORT).show();

                        }else{
                            mAdapter.swapCursor(getAlarmCursor());
                            Toast.makeText(FragmentAlarm.this,"알림을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            //setAlarm.cancelAlarm(getApplicationContext(),);
                            //Log.e("alarmtime 확인: ", cursor.getString(5));
                            //alarmReceiver.notificationManager.cancel(Integer.parseInt(cursor.getString(cursor.getPosition())));
                            //Log.e("alarmtime 확인: ", cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.ALARMTIME)));
                            //setAlarm.cancelAlarm(getApplicationContext(), Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.ALARMTIME))));

                        }

                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();

                return true;

            }
        });
        //listView.deferNotifyDataSetChanged();
         */
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alarm_activity_main, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SetAlarm.class),
                        REQUEST_CODE);
            }
        });



        final ListView listView =  (ListView) view.findViewById(R.id.alarmlist);

        final Cursor cursor = getAlarmCursor();
        mAdapter = new AlarmAdapter(getContext(),cursor);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final long deleted = id;


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Alarm delete");
                builder.setMessage("알림을 삭제 하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = AlarmDbHelper.getInstance(getActivity()).getWritableDatabase();
                        int deletedCount = db.delete(Databases.CreateDB.TABLE_NAME,
                                Databases.CreateDB._ID + "=" + deleted, null);

                        if(deletedCount ==0){
                            Toast.makeText(getActivity(), "알림삭제 오류", Toast.LENGTH_SHORT).show();

                        }else{
                            mAdapter.swapCursor(getAlarmCursor());
                            Toast.makeText(getActivity(),"알림을 삭제했습니다.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();

                return true;

            }
        });

        return view;
    }



    private Cursor getAlarmCursor(){
        dbHelper = AlarmDbHelper.getInstance(getContext());
        return dbHelper.getReadableDatabase()
                .query(Databases.CreateDB.TABLE_NAME, null,null,null,null,null,null);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            mAdapter.swapCursor(getAlarmCursor());
        }
    }

    private static class AlarmAdapter extends CursorAdapter{

        public AlarmAdapter(Context context, Cursor c) {
            super(context, c,false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context)
                    .inflate(R.layout.item_alarm, parent, false);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView ampmtext = view.findViewById(R.id.ampmText);
            ampmtext.setText(cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.AMPM)));

            TextView hourtext = view.findViewById(R.id.hourText);
            hourtext.setText(cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.HOUR)));

            TextView colon = view.findViewById(R.id.colonText);
            colon.setText(":");

            TextView minutetext = view.findViewById(R.id.minuteText);
            minutetext.setText(cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.MINUTE)));

            TextView drug = view.findViewById(R.id.drug_text);
            drug.setText(cursor.getString(cursor.getColumnIndexOrThrow(Databases.CreateDB.DRUGTEXT)));

        }
    }

}