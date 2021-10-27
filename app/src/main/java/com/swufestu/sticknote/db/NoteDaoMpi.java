package com.swufestu.sticknote.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDaoMpi {
    public static int NOTECOUNT = 0;
    public static List<String> HEAD = new ArrayList<String>();
    public static List<Map<String,String>> LISTADAPTER = new ArrayList<Map<String,String>>();
    public static Note NOTE = new Note();
    private static final String TAG="NOTEDAO:";
    private DBHelper helper = null;
    public NoteDaoMpi(Context context){
        helper = new DBHelper(context);
    }

    public boolean insert(Note note) {
        boolean flag=false;
        SQLiteDatabase db=null;
        try {
            db=helper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("title",note.getTitle());
            values.put("note",note.getNote());
            values.put("createDate",note.getCreateDate());
            values.put("mark",note.getMark());
            db.insert("notes","",values);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return flag;
    }


    public boolean update(Note note) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", note.getTitle());
            values.put("note", note.getNote());
            values.put("createDate", note.getCreateDate());
            values.put("mark", note.getMark());
            int count = db.update("notes", values, "id = ?", new String[]{ String.valueOf(note.getId())});
            flag = count > 0 ? true : false;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(db != null){
                db.close();
            }
        }
        return flag;
    }


    public boolean delete(String id) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            int count = db.delete("notes","id = ?" , new String[]{id});
            flag = count > 0 ? true : false;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(db != null){
                db.close();
            }
        }
        return flag;
    }


    public Note query(String select, String[] selectArgs) {
        //历史记录
        SQLiteDatabase db=null;
        Note note=null;
        try {
            db=helper.getWritableDatabase();
            Cursor cursor=db.query("notes",null,select,
                    selectArgs,null,null,"createDate");
            int count = cursor.getColumnCount();
            while(cursor.moveToNext()){
                note = new Note();
                for (int i = 0; i < count; i++) {
                    String column_name = cursor.getColumnName(i);
                    @SuppressLint("Range") String column_value = cursor.getString(cursor.getColumnIndex(column_name));
                    if(column_value == null) column_value = "";
                    if(column_name.equals("id")){
                        note.setId(Integer.parseInt(column_value));
                    }
                    if(column_name.equals("title")){
                        note.setTitle(column_value);
                    }
                    if(column_name.equals("note")){
                        note.setNote(column_value);
                    }
                    if(column_name.equals("mark")){
                        note.setMark(column_value);
                    }
                    if(column_name.equals("createDate")){
                        note.setCreateDate(column_value);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return note;
    }


    public List<Map<String, String>> queryAll() {
        //放到列表中
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            Cursor cursor = db.query("notes", new String[]{"id","title","createDate"}, null, null, null, null, "createDate");
            NOTECOUNT = cursor.getCount();
            int count = cursor.getColumnCount();
            Map<String,String> note = null;
            Map<String,String> noteHead = null;
            String [] times = null;
            HEAD.clear();
            LISTADAPTER.clear();
            while(cursor.moveToNext()){
                note = new HashMap<String, String>();
                for (int i = 0; i < count; i++) {
                    String column_name = cursor.getColumnName(i);
                    @SuppressLint("Range") String column_value = cursor.getString(cursor.getColumnIndex(column_name));
                    if(column_value == null)
                        column_value = "";
                    if(column_name.equals("id")){
                        note.put("id", column_value);
                    }
                    if(column_name.equals("title")){
                        note.put("title", column_value);
                    }if(column_name.equals("createDate")){
                        times = column_value.split(" ");
                        note.put("createDate", times[times.length-1]);
                    }
                }
                if(HEAD.size() == 0){
                    noteHead = new HashMap<String, String>();
                    noteHead.put("createDate", times[0]);
                    LISTADAPTER.add(noteHead);
                    //设置Adapter中放置数据
                    HEAD.add(times[0]);
                }else if(!HEAD.get(HEAD.size()-1).equals(times[0])){
                    noteHead = new HashMap<String, String>();
                    noteHead.put("createDate", times[0]);
                    LISTADAPTER.add(noteHead);
                    HEAD.add(times[0]);
                }
                LISTADAPTER.add(note);
                HEAD.add(times[0]);
                Log.i(TAG,""+note+times);

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(db != null){
                db.close();
            }
        }
        return LISTADAPTER;
    }
}
