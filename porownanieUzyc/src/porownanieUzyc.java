import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class porownanieUzyc {
    private static volatile boolean warunekSpelniony = false;

    public static void main(String[] args) {
        System.out.println("--- Przyklad z petla while ---");
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                warunekSpelniony = true;
                System.out.println("Warunek zostal spelniony!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        while (!warunekSpelniony) {
        }
        System.out.println("Kontynuuje po spelnieniu warunku.\n");

        System.out.println("--- Przyklad z CountDownLatch ---");
        int liczbaWatkow = 3;
        CountDownLatch zatrzask = new CountDownLatch(liczbaWatkow);

        for (int i = 0; i < liczbaWatkow; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " zaczyna prace.");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " zakonczyl prace.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    zatrzask.countDown();
                }
            }).start();
        }

        try {
            zatrzask.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Wszystkie watki zakonczyly dzialanie.\n");

        System.out.println("--- Przyklad z CyclicBarrier ---");
        CyclicBarrier bariera = new CyclicBarrier(liczbaWatkow, () -> System.out.println("Wszystkie watki dotarly do bariery!"));

        for (int i = 0; i < liczbaWatkow; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " czeka na bariere.");
                    bariera.await();
                    System.out.println(Thread.currentThread().getName() + " przekroczyl bariere.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
