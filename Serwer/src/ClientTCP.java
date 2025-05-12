import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientTCP {
    private static final int TIME_LIMIT = 45000; // sekundy
    private static final String HOST = "localhost";
    private static final int PORT = 12345;


    public static void main(String[] args) {
        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.print("Podaj numer albumu: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                username = "Nieznany numer albumu";
            }
            out.println(username);

            System.out.println("Wpisz q, aby przerwać test.");

            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("Wynik:")) {
                    System.out.println(line);
                    break;
                }

                System.out.println(line); // pytanie lub opcje

                if (line.startsWith("d)")) {
                    System.out.print("Wpisz a,b,c lub d: ");
                    String answer = null;

                    long startTime = System.currentTimeMillis();
                    while ((System.currentTimeMillis() - startTime) < TIME_LIMIT ) {
                        try {
                            if (System.in.available() > 0) {
                                answer = scanner.nextLine().trim().toUpperCase();
                                break;
                            }
                        } catch (IOException e) {
                            System.err.println("Błąd podczas sprawdzania wejścia: " + e.getMessage());
                            break;
                        }
                    }

                    if (answer == null || answer.isEmpty()) {
                        answer = "Nie udzielono odpowiedzi.";
                        System.out.println("45 sekund minęło, brak odpowiedzi.");
                    }

                    out.println(answer);
                    if (answer.equalsIgnoreCase("q")) break;
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd połączenia: " + e.getMessage());
        }
    }
}