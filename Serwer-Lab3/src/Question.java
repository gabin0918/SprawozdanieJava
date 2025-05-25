public class Question {
    private int id;
    private String content;
    private String a, b, c, d;
    private String correctAnswer;

    public Question(int id, String content, String a, String b, String c, String d, String correctAnswer) {
        this.id = id;
        this.content = content;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String toString() {
        return content + "\n" +
                "a) " + a + "\n" +
                "b) " + b + "\n" +
                "c) " + c + "\n" +
                "d) " + d + "\n";
    }
}
