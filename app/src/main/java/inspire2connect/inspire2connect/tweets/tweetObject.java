package inspire2connect.inspire2connect.tweets;

public class tweetObject {
    public String text;
    public String source;
    String position;

    public tweetObject(String text, String source, String position) {
        this.text = text;
        this.source = source;
        this.position = position;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public String getPosition() {
        return position;
    }



}

