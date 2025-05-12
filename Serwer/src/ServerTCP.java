import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerTCP {
    public static final int PORT = 12345;
    public static final int MAX_CLIENTS = 250;
    public static List<Question> questions = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        loadQuestions("bazaPytan.txt");

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serwer uruchomiony na porcie " + PORT);

        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            executor.execute(new ServerTCPThread(clientSocket));
        }
    }

    private static void loadQuestions(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        List<String> qLines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;
            qLines.add(line);
            if (qLines.size() == 6) {
                String question = qLines.get(0);
                String[] options = qLines.subList(1, 5).toArray(new String[4]);
                String correct = qLines.get(5).trim();
                questions.add(new Question(question, options, correct));
                qLines.clear();
            }
        }
        reader.close();
    }
}