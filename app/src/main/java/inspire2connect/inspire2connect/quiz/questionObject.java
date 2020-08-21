package inspire2connect.inspire2connect.quiz;

public class questionObject implements Comparable<questionObject>{
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


    public questionObject(String question, String option1, String option2, String option3, String option4, int answer, String explanation, String correct_attempts, String total_attempts, String id,int key) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.explanation = explanation;
        this.correct_attempts = correct_attempts;
        this.total_attempts = total_attempts;
        this.id = id;
        this.key = key ;

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

    public int getAnswer(){
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

    public String getId(){
        return id;
    }

    public int getKey() {
        return key;
    }

    @Override
    public int compareTo(questionObject comparestu) {
        float compareRatio= Integer.valueOf(((questionObject)comparestu).getCorrect_attempts ())/Integer.valueOf(((questionObject)comparestu).getTotal_attempts ());

        return Float.compare(compareRatio,  Integer.valueOf(this.total_attempts)/Integer.valueOf( this.total_attempts ) );

    }
}
