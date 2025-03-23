import java.util.Arrays;

class Set<T extends Comparable<T>> {
    private T[] set;
    private int pojemnosc;
    private int rozmiar;

    @SuppressWarnings("unchecked")
    public Set(int pojemnosc) {
        this.pojemnosc = pojemnosc;
        this.rozmiar = 0;
        this.set = (T[]) new Comparable[pojemnosc];
    }

    public void dodajElement(T element) throws IllegalStateException {
        if (rozmiar >= pojemnosc) {
            throw new IllegalStateException("Zbiór jest pełny.");
        }
        if (szukaj(element) != -1) {
            return;
        }
        set[rozmiar++] = element;
        Arrays.sort(set, 0, rozmiar);
    }

    public int szukaj(T element) {
        for (int i = 0; i < rozmiar; i++) {
            if (set[i].compareTo(element) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void usunElement(T element) {
        int index = szukaj(element);
        if (index != -1) {
            System.arraycopy(set, index + 1, set, index, rozmiar - index - 1);
            rozmiar--;
        }
    }

    public static <T extends Comparable<T>> Set<T> dodajElementy(Set<T> zbior1, Set<T> zbior2) {
        Set<T> nowyZbior = new Set<>(zbior1.pojemnosc + zbior2.pojemnosc);
        for (int i = 0; i < zbior1.rozmiar; i++) {
            nowyZbior.dodajElement(zbior1.set[i]);
        }
        for (int i = 0; i < zbior2.rozmiar; i++) {
            nowyZbior.dodajElement(zbior2.set[i]);
        }
        return nowyZbior;
    }

    public static <T extends Comparable<T>> Set<T> odejmijElementy(Set<T> zbior1, Set<T> zbior2) {
        Set<T> nowyZbior = new Set<>(zbior1.pojemnosc);
        for (int i = 0; i < zbior1.rozmiar; i++) {
            if (zbior2.szukaj(zbior1.set[i]) == -1) {
                nowyZbior.dodajElement(zbior1.set[i]);
            }
        }
        return nowyZbior;
    }

    public static <T extends Comparable<T>> Set<T> przeciecie(Set<T> zbior1, Set<T> zbior2) {
        Set<T> nowyZbior = new Set<>(Math.min(zbior1.pojemnosc, zbior2.pojemnosc));
        for (int i = 0; i < zbior1.rozmiar; i++) {
            if (zbior2.szukaj(zbior1.set[i]) != -1) {
                nowyZbior.dodajElement(zbior1.set[i]);
            }
        }
        return nowyZbior;
    }

    @Override
    public String toString() {
        return "Zbiór: " + Arrays.toString(Arrays.copyOf(set, rozmiar)) +
                " | Rozmiar: " + rozmiar + " | Pojemność: " + pojemnosc;
    }
}

class Liczba implements Comparable<Liczba> {
    private int wartosc;

    public Liczba(int wartosc) {
        this.wartosc = wartosc;
    }

    @Override
    public int compareTo(Liczba inna) {
        return Integer.compare(this.wartosc, inna.wartosc);
    }

    @Override
    public String toString() {
        return String.valueOf(wartosc);
    }
}

class Slowo implements Comparable<Slowo> {
    private String tekst;

    public Slowo(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public int compareTo(Slowo inne) {
        return this.tekst.compareTo(inne.tekst);
    }

    @Override
    public String toString() {
        return tekst;
    }
}