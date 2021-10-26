package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swufestu.sticknote.db.NoteAPP;
import com.swufestu.sticknote.db.NoteDaoMpi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends MainActivity{
    private FloatingActionButton okButton = null;
    private FloatingActionButton backButton = null;
    private EditText noteText = null;
    private EditText titleText = null;
    public static Activity addActivity = null;
    private static final  String TAG="ADD:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        okButton = findViewById(R.id.okButton);
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                Log.i(TAG,""+3);
                intent = new Intent(AddActivity.this,MainActivity.class);
                if(NoteAPP.NOTE.getId() > 0){
                    noteDao.delete(String.valueOf(NoteAPP.NOTE.getId()));
                }
                NoteAPP.NOTE.setId(0);
                NoteAPP.NOTE.setCreateDate("");
                NoteAPP.NOTE.setMark("");
                NoteAPP.NOTE.setNote("");
                NoteAPP.NOTE.setTitle("");
                startActivity(intent);
                AddActivity.addActivity.finish();
                view.setBackgroundResource(R.color.bg);
                }
                return false;
            }
        });
        okButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                Log.i(TAG,""+4);
                EditText noteText = findViewById(R.id.noteEdit);
                EditText titleText = findViewById(R.id.titleEdit);
                intent = new Intent(AddActivity.this,MainActivity.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                NoteAPP.NOTE.setCreateDate(sdf.format(new Date()));
                NoteAPP.NOTE.setNote(noteText.getText().toString().trim());
                NoteAPP.NOTE.setTitle(titleText.getText().toString().trim());
                if (NoteAPP.NOTE.getNote()==null || NoteAPP.NOTE.getNote().equals("")) {
                    Toast.makeText(AddActivity.this,"您还没有输入内容哦！",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(NoteAPP.NOTE.getTitle().equals("")){
                    NoteAPP.NOTE.setTitle("新建的笔记");
                }
                if(NoteAPP.NOTE.getId() == 0){
                    noteDao.insert(NoteAPP.NOTE);
                }else{
                    noteDao.update(NoteAPP.NOTE);
                }
                NoteAPP.NOTE = null;
                StringBuilder showCount = new StringBuilder();
                showCount.append("已经写过");
                showCount.append(NoteAPP.NOTECOUNT+1);
                showCount.append("条笔记");
                Toast.makeText(AddActivity.this,showCount,Toast.LENGTH_SHORT).show();
                startActivity(intent);
                AddActivity.addActivity.finish();
                    view.setBackgroundResource(R.color.bg);
                }
                return false;
            }
        });
        noteText = findViewById(R.id.noteEdit);
        titleText = findViewById(R.id.titleEdit);
        addActivity = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent myIntent;
            myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NoteAPP.NOTE != null) {
            noteText.setText(NoteAPP.NOTE.getNote());
            titleText.setText(NoteAPP.NOTE.getTitle());
        }
    }
}