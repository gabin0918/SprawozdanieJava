public class Question {
    private final String question;
    private final String[] options;
    private final String correct;

    public Question(String question, String[] options, String correct) {
        this.question = question;
        this.options = options;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrect() {
        return correct;
    }
}
