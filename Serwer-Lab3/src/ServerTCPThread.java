import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerTCPThread extends Thread {
    private final Socket clientSocket;

    public ServerTCPThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream in = clientSocket.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(in));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Podaj swój identyfikator (np. indeks):");
            String studentId = input.readLine();

            if (studentId == null || studentId.isBlank()) return;

            if (DatabaseManager.hasAlreadyTakenTest(studentId)) {
                out.println("Uwaga! Już podchodziłeś do testu.");
                return; // kończymy połączenie
            }

            List<Question> questions = DatabaseManager.getAllQuestions();
            int score = 0;
            List<String> answers = new ArrayList<>();

            for (Question q : questions) {
                out.println(q.getContent());
                out.println("a) " + q.getA());
                out.println("b) " + q.getB());
                out.println("c) " + q.getC());
                out.println("d) " + q.getD());

                clientSocket.setSoTimeout(15000);
                String answer = readLineWithTimeout(in);

                if (answer == null) {
                    out.println("Czas minął – brak odpowiedzi.");
                    answers.add("-");
                } else if (!answer.matches("(?i)[a-d]") || answer.length() != 1) {
                    answers.add("-");
                } else {
                    answers.add(answer.toLowerCase());
                    if (answer.trim().equalsIgnoreCase(q.getCorrectAnswer())) {
                        score++;
                    }
                }
            }

            while (answers.size() < 20) {
                answers.add("-");
            }

            DatabaseManager.saveAllAnswers(studentId, answers);
            DatabaseManager.saveResult(studentId, score);
            out.println("Wynik: " + score + " z " + questions.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLineWithTimeout(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        try {
            while ((read = in.read()) != -1) {
                if (read == '\n') break;
                if (read != '\r') buffer.write(read);
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return null;
        }
    }
}
