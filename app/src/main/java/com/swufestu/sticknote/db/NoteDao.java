package com.swufestu.sticknote.db;

import java.util.List;
import java.util.Map;

public interface NoteDao {
    public boolean insert(Note note);
    public boolean update(Note note);
    public boolean delete(String id);
    public Note query(String select,String [] selectArgs);
    public List<Map<String,String>> queryAll();
}
