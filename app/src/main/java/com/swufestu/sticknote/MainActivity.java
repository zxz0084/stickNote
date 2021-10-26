package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.swufestu.sticknote.db.Note;
import com.swufestu.sticknote.db.NoteAPP;
import com.swufestu.sticknote.db.NoteDaoMpi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public NoteDaoMpi noteDao = null;
    //保证noteDao是所有文件的全局变量，简化传参操作
    Intent intent=null;
    private final static  String TAG="Main:";
    private StringBuilder showCount = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        noteDao = new NoteDaoMpi(this);
        Log.i(TAG,""+noteDao);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            int tag=Integer.parseInt(view.getTag().toString());
            Intent intent=null;
            Log.i(TAG,""+tag);
            switch (tag){
                case 1:
                    Log.i(TAG,""+1);
                    break;
                case 2:
                    Log.i(TAG,""+2);
                    intent=new Intent(this,AddActivity.class);
                    NoteAPP.NOTE=new Note();
                    QueryActivity.queryActivity.finish();
                    startActivity(intent);
                    break;
                case 3:
                    Log.i(TAG,""+3);
                    intent = new Intent(this,QueryActivity.class);
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
                    break;
                case 4:
                    Log.i(TAG,""+4);
                    EditText noteText = findViewById(R.id.noteEdit);
                    EditText titleText = findViewById(R.id.titleEdit);
                    intent = new Intent(this,QueryActivity.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    NoteAPP.NOTE.setCreateDate(sdf.format(new Date()));
                    NoteAPP.NOTE.setNote(noteText.getText().toString().trim());
                    NoteAPP.NOTE.setTitle(titleText.getText().toString().trim());
                    if (NoteAPP.NOTE.getNote()==null || NoteAPP.NOTE.getNote().equals("")) {
                        Toast.makeText(this,"您还没有输入内容哦！",Toast.LENGTH_SHORT).show();
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
                    showCount = new StringBuilder();
                    showCount.append("已经写过");
                    showCount.append(NoteAPP.NOTECOUNT+1);
                    showCount.append("条笔记");
                    Toast.makeText(this,showCount,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    AddActivity.addActivity.finish();
                    break;
            }
            view.setBackgroundResource(R.color.bg);
        }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            view.setBackgroundResource(R.color.bg);
        }
        return false;
    }
}