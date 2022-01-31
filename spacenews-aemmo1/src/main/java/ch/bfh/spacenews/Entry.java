package ch.bfh.spacenews;

public class Entry {
    private int id;
    private String title;
    private String summary;
    private String imageUrl;
    private String link;

    public Entry(){}

    public Entry (int id, String title, String summary, String imageUrl, String link){
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.link = link;
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

    public String getLink() {
        return link;
    }
}
