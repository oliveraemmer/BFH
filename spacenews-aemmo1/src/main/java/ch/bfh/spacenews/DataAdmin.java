package ch.bfh.spacenews;

import java.util.ArrayList;
import java.util.List;

public class DataAdmin {
    private static final DataAdmin instance = new DataAdmin();
    private final List<Entry> entries = new ArrayList<>();
    private String type;

    public static DataAdmin getInstance() {
        return instance;
    }

    private DataAdmin() {}

    public Entry addEntry(int id, String title, String summary, String imageUrl, String link){
        Entry e = new Entry(id, title, summary, imageUrl, link);
        entries.add(e);
        return e;
    }

    public List<Entry> getEntries(){
        return entries;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
