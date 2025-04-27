import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

class Task implements Runnable {
    private final int id;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private String result = null;
    private final Object lock = new Object();

    public Task(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            int workTime = 2000 * id;
            int step = 500;

            for (int elapsed = 0; elapsed < workTime; elapsed += step) {
                if (cancelled.get()) {
                    System.out.println("[INFO] Zadanie #" + id + " zostalo zatrzymane.");
                    return;
                }
                Thread.sleep(step);
            }

            synchronized (lock) {
                result = "Wynik zadania #" + id;
                finished.set(true);
            }

            System.out.println("[INFO] Zadanie #" + id + " wykonane poprawnie.");
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Watek zadania #" + id + " przerwany.");
        }
    }

    public void stop() {
        cancelled.set(true);
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public boolean isFinished() {
        return finished.get();
    }

    public String getResult() {
        synchronized (lock) {
            return result;
        }
    }

    public int getId() {
        return id;
    }
}

public class TaskManager {
    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Task task = new Task(i + 1);
            Thread thread = new Thread(task);
            tasks.add(task);
            threads.add(thread);
            thread.start();
        }

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENEDZER ZADAN ===");
            System.out.println("1. Pokaz status wszystkich zadan");
            System.out.println("2. Pobierz wynik zadania");
            System.out.println("3. Anuluj zadanie");
            System.out.println("4. Wyjscie");

            System.out.print("Wybierz opcje: ");
            String option = input.nextLine();

            switch (option) {
                case "1" -> {
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        System.out.println("Zadanie " + (i + 1) +
                                " | Zakonczone: " + t.isFinished() +
                                " | Anulowane: " + t.isCancelled());
                    }
                }
                case "2" -> {
                    System.out.print("Podaj numer zadania: ");
                    int nr = Integer.parseInt(input.nextLine()) - 1;
                    if (nr >= 0 && nr < tasks.size()) {
                        Task t = tasks.get(nr);
                        if (t.isFinished() && !t.isCancelled()) {
                            System.out.println("Wynik: " + t.getResult());
                        } else {
                            System.out.println("Zadanie nadal trwa lub zostalo anulowane.");
                        }
                    } else {
                        System.out.println("Nieprawidlowy numer zadania.");
                    }
                }
                case "3" -> {
                    System.out.print("Podaj numer zadania do anulowania: ");
                    int nr = Integer.parseInt(input.nextLine()) - 1;
                    if (nr >= 0 && nr < tasks.size()) {
                        Task t = tasks.get(nr);
                        t.stop();
                        System.out.println("Zadanie #" + (nr + 1) + " zostalo anulowane.");
                    } else {
                        System.out.println("Nieprawidlowy numer zadania.");
                    }
                }
                case "4" -> {
                    running = false;
                    for (Task t : tasks) {
                        t.stop();
                    }
                    System.out.println("Zamykanie programu...");
                }
                default -> System.out.println("Nieprawidlowa opcja.");
            }
        }
    }
}
