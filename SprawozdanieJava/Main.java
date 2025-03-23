import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Tworzenie seansu
        Seans seans1 = new Seans("Avatar 2", "2025-03-25", "19:00", 12, 5, 10);
        Seans seans2 = new Seans("Batman", "2025-03-26", "20:00", 16, 5, 10);

        // Tworzenie klientów
        List<Klient> klienci = new ArrayList<>();
        List<String> miejsca1 = List.of("A1", "A2");
        List<String> miejsca2 = List.of("B3");

        if (seans1.rezerwujMiejsce("A1") && seans1.rezerwujMiejsce("A2")) {
            klienci.add(new Klient("Kowalski", "Jan", "jan.kowalski@email.com", "123456789", seans1, miejsca1));
        }

        if (seans2.rezerwujMiejsce("B3")) {
            klienci.add(new Klient("Nowak", "Anna", "anna.nowak@email.com", "987654321", seans2, miejsca2));
        }

        // Zapis do pliku tekstowego
        try {
            DaneManager.zapiszDoPliku(klienci);
            System.out.println("Dane zapisane do pliku tekstowego.");
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }

        // Zapis binarny (serializacja)
        try {
            DaneManager.zapiszDoPlikuBin(klienci);
            System.out.println("Dane zapisane w formie binarnej.");
        } catch (IOException e) {
            System.out.println("Błąd serializacji: " + e.getMessage());
        }

        // Odczyt binarny
        try {
            List<Klient> wczytani = DaneManager.odczytajZPlikuBin();
            System.out.println("Wczytani klienci:");
            wczytani.forEach(System.out::println);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Błąd odczytu binarnego: " + e.getMessage());
        }
    }
}
