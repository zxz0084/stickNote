package com.swufestu.sticknote.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDaoMpi implements NoteDao{
    private DBHelper helper = null;
    public NoteDaoMpi(Context context){
        helper = new DBHelper(context);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Note query(String select, String[] selectArgs) {
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

    @Override
    public List<Map<String, String>> queryAll() {
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            Cursor cursor = db.query("notes", new String[]{"id","title","createDate"}, null, null, null, null, "createDate");
            NoteAPP.NOTECOUNT = cursor.getCount();
            int count = cursor.getColumnCount();
            //一条数据
            Map<String,String> note = null;
            //头部
            Map<String,String> noteHead = null;
            //保存当前循环的note日期
            String [] times = null;
            NoteAPP.HEAD.clear();
            NoteAPP.LISTADAPTER.clear();
            while(cursor.moveToNext()){
                note = new HashMap<String, String>();
                for (int i = 0; i < count; i++) {
                    String column_name = cursor.getColumnName(i);
                    @SuppressLint("Range") String column_value = cursor.getString(cursor.getColumnIndex(column_name));
                    if(column_value == null) column_value = "";
                    if(column_name.equals("id")){
                        note.put("id", column_value);
                    }
                    if(column_name.equals("title")){
                        note.put("title", column_value);
                    }
                    if(column_name.equals("note")){
                    }
                    if(column_name.equals("mark")){
                    }
                    if(column_name.equals("createDate")){
                        times = column_value.split(" ");
                        note.put("createDate", times[times.length-1]);
                    }
                }
                if(NoteAPP.HEAD.size() == 0){
                    noteHead = new HashMap<String, String>();
                    noteHead.put("createDate", times[0]);
                    NoteAPP.LISTADAPTER.add(noteHead);
                    NoteAPP.HEAD.add(times[0]);
                }else if(!NoteAPP.HEAD.get(NoteAPP.HEAD.size()-1).equals(times[0])){
                    noteHead = new HashMap<String, String>();
                    noteHead.put("createDate", times[0]);
                    NoteAPP.LISTADAPTER.add(noteHead);
                    NoteAPP.HEAD.add(times[0]);
                }
                NoteAPP.LISTADAPTER.add(note);
                NoteAPP.HEAD.add(times[0]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(db != null){
                db.close();
            }
        }
        return NoteAPP.LISTADAPTER;
    }
}
