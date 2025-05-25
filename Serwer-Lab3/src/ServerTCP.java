import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    public static final int PORT = 1234;
    public static final int MAX_CLIENTS = 250;

    public static void main(String[] args) {
        // Inicjalizacja bazy danych
        DatabaseManager.initializeDatabase();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer uruchomiony na porcie " + PORT + ".");

            // Tworzymy pulę wątków
            ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowe połączenie od: " + clientSocket.getInetAddress());
                executor.execute(new ServerTCPThread(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("Błąd uruchamiania serwera: " + e.getMessage());
        }
    }
}
