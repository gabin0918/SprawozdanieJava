import java.io.*;
import java.net.Socket;

class ServerTCPThread extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public ServerTCPThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String username;
        try {
            // Odczytujemy nazwę użytkownika
            username = in.readLine();
            if (username == null || username.isEmpty()) {
                username = "Nieznany numer albumu";
            }
            System.out.println("Nr " + username + " rozpoczął test.");

            int score = 0;
            StringBuilder answers = new StringBuilder();
            answers.append("Nr: ").append(username).append("\n");

            for (Question question : ServerTCP.questions) {
                // Wysyłamy pytanie i opcje
                out.write(question.getQuestion() + "\n");
                for (String option : question.getOptions()) {
                    out.write(option + "\n");
                }
                out.flush();

                String answer = in.readLine(); // Oczekujemy odpowiedzi od klienta
                if (answer == null || answer.equalsIgnoreCase("q")) {
                    // Użytkownik przerwał test
                    break;
                }

                answers.append(answer).append("\n");

                // Sprawdzamy odpowiedź, jeśli jest niepusta i porównujemy z poprawną
                if (answer != null && !answer.trim().isEmpty()) {
                    if (answer.equalsIgnoreCase(question.getCorrect())) {
                        score++;
                    }
                }
            }

            // Wysyłamy wynik
            out.write("Wynik: " + score + "/" + ServerTCP.questions.size() + "\n");
            out.flush();
            System.out.println("Nr " + username + " zakończył test.");

            // Zapisujemy odpowiedzi do pliku
            synchronized (ServerTCPThread.class) {
                try (BufferedWriter answerWriter = new BufferedWriter(new FileWriter("bazaOdpowiedzi.txt", true))) {
                    answerWriter.write("=== Odpowiedzi: " + username + " ===\n");
                    answerWriter.write(answers.toString());
                    answerWriter.write("=== Koniec odpowiedzi ===\n\n");
                }

                try (BufferedWriter resultWriter = new BufferedWriter(new FileWriter("wyniki.txt", true))) {
                    resultWriter.write("Nr " + username + " wynik: " + score + " z " + ServerTCP.questions.size() + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd w komunikacji z klientem: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Błąd przy zamykaniu socketu: " + e);
            }
        }
    }
}