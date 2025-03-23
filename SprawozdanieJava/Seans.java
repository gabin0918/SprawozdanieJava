import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Seans implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String tytul;
    private String dzien;
    private String godzina;
    private int ograniczenieWiekowe;
    private Map<Character, Map<Integer, Boolean>> miejsca; // np. {'A': {1: false, 2: true}, 'B': {3: false}}

    public Seans(String tytul, String dzien, String godzina, int ograniczenieWiekowe, int liczbaRzedow, int miejscaWRzedzie) {
        this.tytul = tytul;
        this.dzien = dzien;
        this.godzina = godzina;
        this.ograniczenieWiekowe = ograniczenieWiekowe;
        this.miejsca = new HashMap<>();
        
        for (char rzad = 'A'; rzad < 'A' + liczbaRzedow; rzad++) {
            miejsca.put(rzad, new HashMap<>());
            for (int miejsce = 1; miejsce <= miejscaWRzedzie; miejsce++) {
                miejsca.get(rzad).put(miejsce, false); // false - miejsce wolne
            }
        }
    }

    public boolean rezerwujMiejsce(String miejsce) {
        char rzad = miejsce.charAt(0);
        int numer = Integer.parseInt(miejsce.substring(1));
        
        if (miejsca.containsKey(rzad) && miejsca.get(rzad).containsKey(numer) && !miejsca.get(rzad).get(numer)) {
            miejsca.get(rzad).put(numer, true); // Rezerwacja
            return true;
        }
        return false;
    }

    public String getTytul() {
        return tytul;
    }

    @Override
    public String toString() {
        return "Seans: " + tytul + ", " + dzien + " " + godzina + ", Ograniczenie: " + ograniczenieWiekowe + "+";
    }
}
