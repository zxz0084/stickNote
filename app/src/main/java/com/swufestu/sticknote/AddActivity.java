package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;

import com.swufestu.sticknote.db.NoteAPP;

public class AddActivity extends MainActivity {
    private ImageButton okButton = null;
    private ImageButton backButton = null;
    private EditText noteText = null;
    private EditText titleText = null;
    public static Activity addActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        okButton = findViewById(R.id.okButton);
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);
        okButton.setOnTouchListener(this);
        noteText = findViewById(R.id.noteEdit);
        titleText = findViewById(R.id.titleEdit);
        addActivity = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent myIntent;
            myIntent = new Intent(this, QueryActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return false;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (NoteAPP.NOTE != null) {
            noteText.setText(NoteAPP.NOTE.getNote());
            titleText.setText(NoteAPP.NOTE.getTitle());
        }
    }
}