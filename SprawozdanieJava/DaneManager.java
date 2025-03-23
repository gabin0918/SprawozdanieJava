import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DaneManager {
    private static final String PLIK_TXT = "klienci.txt";
    private static final String PLIK_SERIALIZACJI = "klienci.dat";

    public static void zapiszDoPliku(List<Klient> klienci) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(PLIK_TXT));
        for (Klient klient : klienci) {
            out.write(klient.getDaneDoZapisu());
            out.newLine();
        }
        out.close();
    }

    public static List<Klient> odczytajZPliku() throws IOException {
        List<Klient> klienci = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(PLIK_TXT));
        String linia;
        while ((linia = in.readLine()) != null) {
            System.out.println("Odczytano z pliku: " + linia);
        }
        in.close();
        return klienci;
    }

    public static void zapiszDoPlikuBin(List<Klient> klienci) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PLIK_SERIALIZACJI));
        out.writeObject(klienci);
        out.close();
    }

    public static List<Klient> odczytajZPlikuBin() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(PLIK_SERIALIZACJI));
        List<Klient> klienci = (List<Klient>) in.readObject();
        in.close();
        return klienci;
    }
}
