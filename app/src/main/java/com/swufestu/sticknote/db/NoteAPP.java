package com.swufestu.sticknote.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteAPP {
    public static List<Map<String,String>> LISTADAPTER = new ArrayList<Map<String,String>>();
    //按日期分组显示note
    public static List<String> HEAD = new ArrayList<String>();
    //一共有多少note
    public static int NOTECOUNT = 0;
    //当前note
    public static Note NOTE = new Note();
}
