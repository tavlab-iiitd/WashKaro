package inspire2connect.inspire2connect;

import android.util.Log;

public class Story_Details implements Comparable<Story_Details> {
    private String title;
    private String story;
    private int number;
    private String date;
    private Double similarity;
    private String url;
    private String who_url;
    private String who_article_text;
    private String who_summary;
    private int number_of_relevant_votes;
    private int number_of_irrelevant_votes;

    public Story_Details() {
    }

    public Story_Details(String title, String story, int number, String url, String who_url, String who_article_text, String who_summary,
                         int number_of_relevant_votes, int number_of_irrelevant_votes, String date, Double similarity) {
        this.title = title;
        this.story = story;
        this.url = url;
        this.number = number;
        this.who_url = who_url;
        this.who_article_text = who_article_text;
        this.who_summary = who_summary;
        this.number_of_relevant_votes = number_of_relevant_votes;
        this.number_of_irrelevant_votes = number_of_irrelevant_votes;
        this.date = date;
        this.similarity = similarity;

    }

    @Override
    public int compareTo(Story_Details s) {

        if (this.getSimilarity() > s.getSimilarity())
            return 0;
        else
            return 1;
    }

    public String getTitle() {
        return title;
    }

    public String getStory() {
        return story;
    }

    public String getWho_url() {
        return who_url;
    }

    public String getWho_article_text() {
        return who_article_text;
    }

    public String getWho_summary() {
        return who_summary;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber_of_relevant_votes() {
        return number_of_relevant_votes;
    }

    public int getNumber_of_irrelevant_votes() {
        return number_of_irrelevant_votes;
    }

    public String getDate() {
        return date;
    }

    public Double getSimilarity() {
        return similarity;
    }

}