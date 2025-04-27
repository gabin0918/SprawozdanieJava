import java.util.*;
import java.util.concurrent.*;


class Task<T> extends FutureTask<T> {
    private final String taskName;

    public Task(Callable<T> callable, String taskName) {
        super(callable);
        this.taskName = taskName;
    }

    @Override
    protected void done() {
        System.out.println("[Listener] Zadanie '" + taskName + "' zakończone.");
        try {
            if (isCancelled()) {
                System.out.println("[Listener] Zadanie '" + taskName + "' zostało anulowane.");
            } else {
                System.out.println("[Listener] Wynik zadania '" + taskName + "': " + get());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("[Listener] Błąd w zadaniu '" + taskName + "': " + e.getMessage());
        }
    }

    public String getTaskName() {
        return taskName;
    }
}

public class TaskManagerInteractive {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<Task<?>> tasks = new ArrayList<>();
    private int taskCounter = 1;
    private final Scanner scanner = new Scanner(System.in);

    public <T> void submitTask(Callable<T> callable, String taskName) {
        Task<T> task = new Task<>(callable, taskName);
        tasks.add(task);
        executor.submit(task);
        System.out.println("Dodano zadanie: " + taskName);
    }

    public void listTasks() {
        System.out.println("\nLista zadań:");
        for (int i = 0; i < tasks.size(); i++) {
            Task<?> task = tasks.get(i);
            System.out.println(i + ": " + task.getTaskName() +
                    " | Done: " + task.isDone() +
                    " | Cancelled: " + task.isCancelled());
        }
    }

    public void cancelTask() {
        System.out.print("Podaj indeks zadania do anulowania: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < tasks.size()) {
            boolean success = tasks.get(index).cancel(true);
            System.out.println(success ? "Zadanie anulowane." : "Nie udało się anulować zadania.");
        } else {
            System.out.println("Nieprawidłowy indeks.");
        }
    }

    public void getResult() {
        System.out.print("Podaj indeks zadania do pobrania wyniku: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < tasks.size()) {
            Task<?> task = tasks.get(index);
            if (task.isDone() && !task.isCancelled()) {
                try {
                    System.out.println("Wynik zadania: " + task.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Błąd przy pobieraniu wyniku: " + e.getMessage());
                }
            } else {
                System.out.println("Zadanie jeszcze nie zakończone lub zostało anulowane.");
            }
        } else {
            System.out.println("Nieprawidłowy indeks.");
        }
    }

    public void runMenu() {
        int choice;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Dodaj nowe zadanie");
            System.out.println("2. Wyświetl listę zadań");
            System.out.println("3. Anuluj zadanie");
            System.out.println("4. Pokaż wynik zadania");
            System.out.println("0. Wyjście");
            System.out.print("Wybierz opcję: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // wyczyść Enter

            switch (choice) {
                case 1 -> addNewTask();
                case 2 -> listTasks();
                case 3 -> cancelTask();
                case 4 -> getResult();
                case 0 -> System.out.println("Zamykanie programu...");
                default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
            }
        } while (choice != 0);

        shutdown();
    }

    private void addNewTask() {
        System.out.print("Podaj czas trwania zadania w sekundach: ");
        int seconds = scanner.nextInt();
        String taskName = "Zadanie " + (taskCounter++);

        submitTask(() -> {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                System.out.println("Zadanie '" + taskName + "' zostało przerwane.");
                throw e;
            }
            return "Wykonane po " + seconds + " sekundach";
        }, taskName);
    }

    public void shutdown() {
        executor.shutdown();
        System.out.println("Wszystkie zadania zakończone.");
    }

    public static void main(String[] args) {
        TaskManagerInteractive manager = new TaskManagerInteractive();
        manager.runMenu();
    }
}
