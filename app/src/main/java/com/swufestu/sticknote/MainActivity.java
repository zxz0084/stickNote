package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swufestu.sticknote.db.Note;
import com.swufestu.sticknote.db.NoteAPP;
import com.swufestu.sticknote.db.NoteDaoMpi;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    //删除按钮、添加按钮、删除成功按钮、返回按钮
    //继承Main的目的是为了保证使用公共note，简化通过intent传参和获取参数的问题
    private static final String TAG="Query";
    Intent intent=null;
    public NoteDaoMpi noteDao = null;
    private FloatingActionButton delete = null;
    private FloatingActionButton add = null;
    private FloatingActionButton deleteOk = null;
    private FloatingActionButton back = null;
    //private CardView listView=null;
    private ListView listView = null;
    public static Activity queryActivity = null;
    private NormalAdapter adapter = null;
    private DeleteAdapter deleteAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteDao = new NoteDaoMpi(this);
        Log.i(TAG,""+noteDao);
        //RecyclerView byId = (RecyclerView)findViewById(R.id.Rece);
        //byId.setHasFixedSize(true);
        //byId.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        listView=findViewById(R.id.listView);
        listView.setOnItemClickListener(this);//OnItemClick
        back=findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backDeleCheck();
            }
        });
        delete=findViewById(R.id.delete);
        delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                toDeleteCheck();
                return false;
            }
        });

        deleteOk=findViewById(R.id.deleteok);
        deleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAdapter.deleteItems();
                deleteOk.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                searchAll();
            }
        });
        add = findViewById(R.id.addButton);
        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "" + 2);
                    intent = new Intent(MainActivity.this, AddActivity.class);
                    NoteAPP.NOTE = new Note();
                    MainActivity.this.finish();
                    startActivity(intent);
                    view.setBackgroundResource(R.color.bg);
                }
                return false;
            }
        });

    }


    private void searchAll() {
        noteDao.queryAll();
        adapter = new NormalAdapter();
        listView.setAdapter(adapter);
    }

    private void toDeleteCheck() {
        searchDelete();
        back.setVisibility(View.VISIBLE);
        deleteOk.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toDeleteCheck();
        return false;
    }

    private void searchDelete() {
        deleteAdapter = new DeleteAdapter();
        listView.setAdapter(deleteAdapter);
    }

    private void backDeleCheck() {
        deleteOk.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        //添加和删除按钮显示
        delete.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        searchAll();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        LinearLayout frame = (LinearLayout)view;
        String noteID = ((TextView)frame.getChildAt(0)).getText().toString();
        Log.i(TAG,""+frame.getChildCount());
        if(frame.getChildCount() == 3){
            NoteAPP.NOTE = noteDao.query("id=?", new String[]{noteID});
            Intent intent = new Intent(this,AddActivity.class);
            this.finish();
            startActivity(intent);
        }else if(frame.getChildCount() == 4){
            deleteAdapter.toggle(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    class ListHolder {
        TextView id = null;
        TextView title = null;
        TextView createDate = null;
        TextView listHead = null;
        CheckBox checkNote = null;
    }

    class DeleteAdapter extends BaseAdapter {
        LayoutInflater inflater;
        boolean[] itemStatus = new boolean[NoteAPP.LISTADAPTER.size()];

        //获取所有删除项ID
        public void deleteItems(){
            for (int i = 0; i < itemStatus.length; i++) {
                if(itemStatus[i]){
                    noteDao.delete(NoteAPP.LISTADAPTER.get(i).get("id"));
                }
            }
        }
        //设置选中状态
        public void toggle(int position){
            if(itemStatus[position]){
                itemStatus[position] = false;
            }else{
                itemStatus[position] = true;
            }
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return NoteAPP.LISTADAPTER.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListHolder viewHolder = null;
            // 分组显示
            if (inflater == null) {
                inflater = (LayoutInflater) queryActivity
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
            }
            viewHolder = new ListHolder();
            // 上一个时间点
            // 与上一个时间比较
            String time = NoteAPP.HEAD.get(position);
            String create = (NoteAPP.LISTADAPTER.get(position)
                    .get("createDate"));
            if (!time.equals(create)) {
                convertView = inflater.inflate(R.layout.deletelistview, null);
                viewHolder.id = convertView.findViewById(R.id.id);
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.createDate = convertView.findViewById(R.id.createDate);
                viewHolder.checkNote = convertView.findViewById(R.id.checkNote);
            } else {
                convertView = inflater.inflate(R.layout.showhint, null);
                viewHolder.listHead =  convertView.findViewById(R.id.listHead);
            }
            if (viewHolder.id != null) {
                viewHolder.id.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "id"));
                viewHolder.title.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "title"));
                viewHolder.createDate.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
                viewHolder.checkNote.setOnCheckedChangeListener(new CheckChange(position));
                viewHolder.checkNote.setChecked(itemStatus[position]);
            } else {
                viewHolder.listHead.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
            }
            return convertView;
        }

        //定义一个监听
        class CheckChange implements CompoundButton.OnCheckedChangeListener {
            int position;

            public CheckChange(int position){
                this.position = position;
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    itemStatus[position] = true;
                }else{
                    itemStatus[position] = false;
                }
            }

        }
    }
    class NormalAdapter extends BaseAdapter {
        LayoutInflater inflater;

        @Override
        public int getCount() {
            return NoteAPP.LISTADAPTER.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListHolder viewHolder = null;
            // 分组模式
            if (inflater == null) {
                inflater = (LayoutInflater) queryActivity
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
            }
            viewHolder = new ListHolder();
            // 上一个时间点
            // 与上一个时间比较
            String time = NoteAPP.HEAD.get(position);
            String create = (NoteAPP.LISTADAPTER.get(position)
                    .get("createDate"));
            if (!time.equals(create)) {
                convertView = inflater.inflate(R.layout.listview, null);
                viewHolder.id =  convertView
                        .findViewById(R.id.id);
                viewHolder.title =  convertView
                        .findViewById(R.id.title);
                viewHolder.createDate =convertView
                        .findViewById(R.id.createDate);
            } else {
                convertView = inflater.inflate(R.layout.showhint, null);
                viewHolder.listHead =  convertView
                        .findViewById(R.id.listHead);
            }
            if (viewHolder.id != null) {
                viewHolder.id.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "id"));
                viewHolder.title.setText(NoteAPP.LISTADAPTER.get(position).get(
                        "title"));
                viewHolder.createDate.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
            } else {
                viewHolder.listHead.setText(NoteAPP.LISTADAPTER.get(position)
                        .get("createDate"));
            }
            return convertView;
        }
    }
}





