package com.swufestu.sticknote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends MainActivity{
    private FloatingActionButton okButton = null;
    private FloatingActionButton backButton = null;
    private EditText noteText = null;
    private EditText titleText = null;
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
                if(noteDao.NOTE.getId() > 0){
                    noteDao.delete(String.valueOf(noteDao.NOTE.getId()));
                }
                noteDao.NOTE.setId(0);
                noteDao.NOTE.setCreateDate("");
                noteDao.NOTE.setMark("");
                noteDao.NOTE.setNote("");
                noteDao.NOTE.setTitle("");
                startActivity(intent);
                AddActivity.this.finish();
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
                noteDao.NOTE.setCreateDate(sdf.format(new Date()));
                noteDao.NOTE.setNote(noteText.getText().toString().trim());
                noteDao.NOTE.setTitle(titleText.getText().toString().trim());
                if (noteDao.NOTE.getNote()==null || noteDao.NOTE.getNote().equals("")) {
                    Toast.makeText(AddActivity.this,"您还没有输入内容！",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(noteDao.NOTE.getTitle().equals("")){
                    noteDao.NOTE.setTitle("新建的笔记");
                }
                if(noteDao.NOTE.getId() == 0){
                    noteDao.insert(noteDao.NOTE);
                }else{
                    noteDao.update(noteDao.NOTE);
                }
                noteDao.NOTE = null;
                StringBuilder showCount = new StringBuilder();
                showCount.append("已经写过");
                showCount.append(noteDao.NOTECOUNT+1);
                showCount.append("条笔记");
                Toast.makeText(AddActivity.this,showCount,Toast.LENGTH_SHORT).show();
                startActivity(intent);
                AddActivity.this.finish();
                    view.setBackgroundResource(R.color.bg);
                }
                return false;
            }
        });
        noteText = findViewById(R.id.noteEdit);
        titleText = findViewById(R.id.titleEdit);
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
            noteText.setText(noteDao.NOTE.getNote());
            titleText.setText(noteDao.NOTE.getTitle());
    }
}