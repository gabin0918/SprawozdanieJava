import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void initializeDatabase() {
        // 1. Utwórz bazę danych, jeśli nie istnieje
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS quiz");


        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu bazy danych:");
            e.printStackTrace();
        }

        // 2. Połącz się z bazą quiz i twórz tabele
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Tabela z pytaniami
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS questions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "content TEXT," +
                    "a TEXT, b TEXT, c TEXT, d TEXT," +
                    "correct_answer CHAR(1))");

            // Tabela z odpowiedziami (question1–20)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS answers (" +
                    "student_id VARCHAR(50) PRIMARY KEY," +
                    "question1 CHAR(1)," +
                    "question2 CHAR(1)," +
                    "question3 CHAR(1)," +
                    "question4 CHAR(1)," +
                    "question5 CHAR(1)," +
                    "question6 CHAR(1)," +
                    "question7 CHAR(1)," +
                    "question8 CHAR(1)," +
                    "question9 CHAR(1)," +
                    "question10 CHAR(1)," +
                    "question11 CHAR(1)," +
                    "question12 CHAR(1)," +
                    "question13 CHAR(1)," +
                    "question14 CHAR(1)," +
                    "question15 CHAR(1)," +
                    "question16 CHAR(1)," +
                    "question17 CHAR(1)," +
                    "question18 CHAR(1)," +
                    "question19 CHAR(1)," +
                    "question20 CHAR(1))");

            // Tabela z wynikami
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS results (" +
                    "student_id VARCHAR(50) PRIMARY KEY," +
                    "score INT)");

            // Wczytaj pytania z pliku, jeśli tabela jest pusta
            if (getAllQuestions().isEmpty()) {
                loadQuestionsFromFile("bazaPytan.txt");

            }

        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabel:");
            e.printStackTrace();
        }
    }

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getString("a"),
                        rs.getString("b"),
                        rs.getString("c"),
                        rs.getString("d"),
                        rs.getString("correct_answer")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static void insertQuestion(Question q) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO questions (content, a, b, c, d, correct_answer) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, q.getContent());
            ps.setString(2, q.getA());
            ps.setString(3, q.getB());
            ps.setString(4, q.getC());
            ps.setString(5, q.getD());
            ps.setString(6, q.getCorrectAnswer());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadQuestionsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String content;
            while ((content = br.readLine()) != null) {
                String a = br.readLine();
                String b = br.readLine();
                String c = br.readLine();
                String d = br.readLine();
                String correct = br.readLine();

                if (content != null && a != null && b != null && c != null && d != null && correct != null) {
                    insertQuestion(new Question(0,
                            content,
                            a.substring(3).trim(),
                            b.substring(3).trim(),
                            c.substring(3).trim(),
                            d.substring(3).trim(),
                            correct.trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku z pytaniami: " + e.getMessage());
        }
    }

    public static void saveAllAnswers(String studentId, List<String> answers) {
        if (answers.size() != 20) {

            return;
        }

        String sql = "REPLACE INTO answers (" +
                "student_id, question1, question2, question3, question4, question5, " +
                "question6, question7, question8, question9, question10, question11, " +
                "question12, question13, question14, question15, question16, question17, " +
                "question18, question19, question20) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            for (int i = 0; i < 20; i++) {
                ps.setString(i + 2, answers.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveResult(String studentId, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "REPLACE INTO results (student_id, score) VALUES (?, ?)")) {

            ps.setString(1, studentId);
            ps.setInt(2, score);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasAlreadyTakenTest(String studentId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM answers WHERE student_id = ?")) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
