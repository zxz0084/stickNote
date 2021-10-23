package com.swufestu.sticknote.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteAPP {
    //适配器用到的列表
    public static List<Map<String,String>> LISTADAPTER = new ArrayList<Map<String,String>>();
    //需要显示的分组头
    public static List<String> HEAD = new ArrayList<String>();
    //多少条数据
    public static int NOTECOUNT = 0;
    //当前操作的note
    public static Note NOTE = new Note();
}
