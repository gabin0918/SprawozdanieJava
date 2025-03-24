import java.util.Scanner;

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ONP onp = new ONP();

    System.out.println("Podaj wyrażenie w ONP zakończone '=':");
    String rownanie = scanner.nextLine();

    String wynik = onp.obliczOnp(rownanie);
    System.out.println("Wynik: " + wynik);

    scanner.close();
}