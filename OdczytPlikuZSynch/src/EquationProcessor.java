import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class EquationProcessor {
    private static final String PLIK = "equations.txt";
    private static final Lock BLOKADA = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        List<String> wiersze = Files.readAllLines(Paths.get(PLIK));
        ExecutorService czytnik = Executors.newFixedThreadPool(wiersze.size());
        ExecutorService licznik = Executors.newFixedThreadPool(wiersze.size());

        for (int indeks = 0; indeks < wiersze.size(); indeks++) {
            int nrLinii = indeks;

            Callable<String> zadanieCzytajace = () -> {
                String rownanie;
                BLOKADA.lock();
                try {
                    List<String> aktualneWiersze = Files.readAllLines(Paths.get(PLIK));
                    rownanie = aktualneWiersze.get(nrLinii);
                } finally {
                    BLOKADA.unlock();
                }
                return rownanie;
            };

            Future<String> przyszlyTekst = czytnik.submit(zadanieCzytajace);

            FutureTask<Void> zadanieLiczace = new FutureTask<>(() -> {
                String rownanie = przyszlyTekst.get();
                String oczyszczone = rownanie.replace("=", "").trim();
                List<String> onp = zamienNaONP(oczyszczone);
                double wynik = policzONP(onp);

                BLOKADA.lock();
                try {
                    List<String> aktualneWiersze = Files.readAllLines(Paths.get(PLIK));
                    String nowaLinia = aktualneWiersze.get(nrLinii) + " " + wynik;
                    aktualneWiersze.set(nrLinii, nowaLinia);
                    Files.write(Paths.get(PLIK), aktualneWiersze);
                } finally {
                    BLOKADA.unlock();
                }
                return null;
            }) {
                @Override
                protected void done() {
                    System.out.println("Wynik obliczony dla linii " + (nrLinii + 1));
                }
            };

            licznik.submit(zadanieLiczace);
        }

        czytnik.shutdown();
        licznik.shutdown();
    }

    private static List<String> zamienNaONP(String infix) {
        List<String> wyjscie = new ArrayList<>();
        Stack<String> stos = new Stack<>();
        StringTokenizer tokeny = new StringTokenizer(infix, "+-*/^() ", true);
        while (tokeny.hasMoreTokens()) {
            String token = tokeny.nextToken().trim();
            if (token.isEmpty()) continue;
            if (czyLiczba(token)) {
                wyjscie.add(token);
            } else if (czyOperator(token)) {
                while (!stos.isEmpty() && priorytet(stos.peek()) >= priorytet(token)) {
                    wyjscie.add(stos.pop());
                }
                stos.push(token);
            } else if (token.equals("(")) {
                stos.push(token);
            } else if (token.equals(")")) {
                while (!stos.isEmpty() && !stos.peek().equals("(")) {
                    wyjscie.add(stos.pop());
                }
                stos.pop();
            }
        }
        while (!stos.isEmpty()) {
            wyjscie.add(stos.pop());
        }
        return wyjscie;
    }

    private static double policzONP(List<String> onp) {
        Stack<Double> stos = new Stack<>();
        for (String token : onp) {
            if (czyLiczba(token)) {
                stos.push(Double.parseDouble(token));
            } else {
                double b = stos.pop();
                double a = stos.pop();
                switch (token) {
                    case "+" -> stos.push(a + b);
                    case "-" -> stos.push(a - b);
                    case "*" -> stos.push(a * b);
                    case "/" -> stos.push(a / b);
                    case "^" -> stos.push(Math.pow(a, b));
                }
            }
        }
        return stos.pop();
    }

    private static boolean czyLiczba(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean czyOperator(String token) {
        return "+-*/^".contains(token);
    }

    private static int priorytet(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }
}
