package inspire2connect.inspire2connect.quiz;

public class questionObject implements Comparable<questionObject> {
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public int answer;
    public String explanation;
    public String correct_attempts;
    public String total_attempts;
    public String id;
    public int key;

    public String source;


    public questionObject(String question, String option1, String option2, String option3, String option4, int answer, String explanation, String correct_attempts, String total_attempts, String id, int key, String source) {

        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.explanation = explanation;
        this.correct_attempts = correct_attempts;
        if (correct_attempts.equals("")) this.correct_attempts = "0";
        this.total_attempts = total_attempts;
        if (total_attempts.equals("")) this.total_attempts = "0";
        this.id = id;
        if (id.equals("")) this.id = "0";
        this.key = key;

        this.source = source;

    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getCorrect_attempts() {
        return correct_attempts;
    }

    public String getTotal_attempts() {
        return total_attempts;
    }

    public String getId() {
        return id;
    }

    public int getKey() {
        return key;
    }


    public String getSource(){return source;}


    @Override
    public int compareTo(questionObject toCompare) {

        float ratio;

        if (Integer.parseInt(toCompare.getTotal_attempts()) > 0) {
            ratio = Float.parseFloat(toCompare.getCorrect_attempts());
        } else {
            ratio = Float.parseFloat(toCompare.getCorrect_attempts()) / Float.parseFloat(toCompare.getTotal_attempts());
        }

        float myRatio;

        if (Integer.parseInt(toCompare.getTotal_attempts()) > 0) {
            myRatio = Float.parseFloat(this.getCorrect_attempts());
        } else {
            myRatio = Float.parseFloat(this.getCorrect_attempts()) / Float.parseFloat(this.getTotal_attempts());
        }

        return Float.compare(ratio, myRatio);

    }
}
