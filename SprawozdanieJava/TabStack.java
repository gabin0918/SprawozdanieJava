public class TabStack {
    private String[] stack = new String[20];
    private int size = 0;

    public String pop() {
        if (size == 0) {
            throw new RuntimeException("Błąd: stos jest pusty.");
        }
        size--;
        return stack[size];
    }

    public void push(String a) {
        if (size >= stack.length) {
            throw new RuntimeException("Błąd: stos jest pełny.");
        }
        stack[size] = a;
        size++;
    }

    public int getSize() {
        return size;
    }

    public String showValue(int i) {
        if (i < size) {
            return stack[i];
        } else {
            throw new RuntimeException("Błąd: indeks poza zakresem stosu.");
        }
    }
}
