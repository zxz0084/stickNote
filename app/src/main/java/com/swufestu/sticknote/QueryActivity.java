package com.swufestu.sticknote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.swufestu.sticknote.db.NoteAPP;

import java.util.ArrayList;

public class QueryActivity extends MainActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private ImageButton delete = null;
    // 添加
    private ImageButton add = null;
    //删除成功按钮
    private ImageButton deleteOk = null;
    //返回按钮
    private ImageButton back = null;
    // listview
    //private CardView listView=null;
    private ListView listView = null;
    // private SimpleAdapter adapter = null;
    public static Activity queryActivity = null;
    // 自定义适配器继承BaseAdapter
    private NormalAdapter adapter = null;
    // 自定义编辑适配器
    private DeleteAdapter deleteAdapter = null;
    // list总数提示组件
    private TextView listCount = null;
    // list总数提示
    private StringBuilder showCount = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RecyclerView byId = (RecyclerView)findViewById(R.id.Rece);
        //byId.setHasFixedSize(true);
        //byId.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        listView=findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
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
                //添加和删除按钮显示
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                searchNormal();
            }
        });
        add = findViewById(R.id.addButton);
        add.setOnTouchListener(this);
        listCount = (TextView) findViewById(R.id.listCount);
        queryActivity = this;
    }
    private void searchNormal() {
        noteDao.queryAll();
        adapter = new NormalAdapter();
        // adapter = new SimpleAdapter(this,
        // NoteApp.LISTADAPTER,R.layout.listview, new String []
        // {"createDate","id","title"},new int[]{R.id.createDate, R.id.id,
        // R.id.title });
        listView.setAdapter(adapter);
        showCount = new StringBuilder();
        showCount.append("亲，已经写过");
        showCount.append(NoteAPP.NOTECOUNT);
        showCount.append("条笔记");
        listCount.setText(showCount);
    }

    private void toDeleteCheck() {
        searchDelete();
        //返回号显示
        back.setVisibility(View.VISIBLE);
        //对号显示
        deleteOk.setVisibility(View.VISIBLE);
        //添加和删除按钮隐藏
        delete.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        this.finish();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        searchNormal();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
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
        searchNormal();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        LinearLayout frame = (LinearLayout)view;
        String noteID = ((TextView)frame.getChildAt(0)).getText().toString();
        if(frame.getChildCount() == 3){
            NoteAPP.NOTE = noteDao.query("id=?", new String[]{noteID});
            Intent intent = new Intent(this,AddActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("note", note);
//			intent.putExtras(bundle);
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
                        // TODO Auto-generated method stub
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
        // 头部
        TextView listHead = null;
        //checkbox
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
            // TODO Auto-generated method stub
            return NoteAPP.LISTADAPTER.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
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
                convertView = inflater.inflate(R.layout.deletelistview, null);
                viewHolder.id = (TextView) convertView
                        .findViewById(R.id.id);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.createDate);
                viewHolder.checkNote = (CheckBox) convertView
                        .findViewById(R.id.checkNote);
            } else {
                convertView = inflater.inflate(R.layout.showhint, null);
                viewHolder.listHead = (TextView) convertView
                        .findViewById(R.id.listHead);
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
                // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            return NoteAPP.LISTADAPTER.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
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
                viewHolder.id = (TextView) convertView
                        .findViewById(R.id.id);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.createDate);
            } else {
                convertView = inflater.inflate(R.layout.showhint, null);
                viewHolder.listHead = (TextView) convertView
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





