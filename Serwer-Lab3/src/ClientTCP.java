import java.io.*;
import java.net.Socket;

public class ClientTCP {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String line;

            while ((line = in.readLine()) != null) {
                // Zapytanie o identyfikator
                if (line.toLowerCase().contains("identyfikator")) {
                    System.out.println(line);
                    System.out.print(">> ");
                    String response = userInput.readLine();
                    out.println(response);
                    continue;
                }

                // Jeśli już podchodził do testu — zakończ program
                if (line.toLowerCase().contains("podchodziłeś")) {
                    System.out.println(line);
                    System.out.println("Zamykanie klienta...");
                    break;
                }

                // Komunikaty systemowe
                if (line.startsWith("Czas minął") || line.startsWith("Wynik:") || line.startsWith("Niepoprawna odpowiedź")) {
                    System.out.println(line);
                    continue;
                }

                // Pytanie + odpowiedzi
                if (!line.trim().isEmpty()) {
                    StringBuilder questionBlock = new StringBuilder();
                    questionBlock.append(line).append("\n");

                    for (int i = 0; i < 4; i++) {
                        String option = in.readLine();
                        while (option != null && (option.startsWith("Czas minął") || option.startsWith("Wynik:") || option.trim().isEmpty())) {
                            option = in.readLine();
                        }
                        if (option != null) {
                            questionBlock.append(option).append("\n");
                        }
                    }

                    System.out.println(questionBlock.toString().trim());
                    System.out.print(">> Masz 10 sekund na odpowiedź: ");

                    String response = "-";
                    boolean odpowiedzWpisana = false;
                    long start = System.currentTimeMillis();
                    while (System.currentTimeMillis() - start < 10000) {
                        if (System.in.available() > 0) {
                            response = userInput.readLine().trim().toLowerCase();
                            odpowiedzWpisana = true;

                            if (!response.matches("[a-d]") || response.length() != 1) {
                                System.out.println("\nNie rozpoznano odpowiedzi. Zapisano jako '-'");
                                response = "-";
                            }
                            break;
                        }
                        Thread.sleep(50);
                    }

                    if (response.equals("-") && !odpowiedzWpisana) {
                        System.out.println("\nCzas minął – wysłano pustą odpowiedź.");
                    }

                    out.println(response);
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }
}
