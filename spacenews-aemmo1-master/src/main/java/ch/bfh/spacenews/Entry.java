package ch.bfh.spacenews;

public class Entry {
    private int id;
    private String title;
    private String summary;
    private String imageUrl;

    public Entry(){}

    public Entry (int id, String title, String summary, String imageUrl){
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
