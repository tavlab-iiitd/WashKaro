package inspire2connect.inspire2connect.mythGuidelineUpdates;

public class guidelinesObject {
    String title;
    String content;
    String position;
    String source;

    public guidelinesObject(String title, String content, String position, String source) {
        this.title = title;
        this.content = content;
        this.position = position;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


}
