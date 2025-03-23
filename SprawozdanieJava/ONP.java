public class ONP {
    private TabStack stack = new TabStack();

    public String obliczOnp(String rownanie) {
        if (!rownanie.endsWith("=")) {
            return "Błąd: Brak '=' na końcu równania.";
        }

        stack = new TabStack();
        String wynik = "";
        Double a, b;

        for (int i = 0; i < rownanie.length(); i++) {
            char znak = rownanie.charAt(i);
            if (Character.isDigit(znak)) {
                wynik += znak;
                if (i + 1 >= rownanie.length() || !Character.isDigit(rownanie.charAt(i + 1))) {
                    stack.push(wynik);
                    wynik = "";
                }
            } else if (znak == '=') {
                return stack.pop();
            } else if (znak != ' ') {
                try {
                    switch (znak) {
                        case '+':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            stack.push(Double.toString(a + b));
                            break;
                        case '-':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            stack.push(Double.toString(a - b));
                            break;
                        case '*':
                        case 'x':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            stack.push(Double.toString(a * b));
                            break;
                        case '/':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            if (b == 0) {
                                throw new ArithmeticException("Błąd: Dzielenie przez zero.");
                            }
                            stack.push(Double.toString(a / b));
                            break;
                        case '^':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            stack.push(Double.toString(Math.pow(a, b)));
                            break;
                        case 'r': 
                            a = Double.parseDouble(stack.pop());
                            if (a < 0) {
                                throw new ArithmeticException("Błąd: Pierwiastek z liczby ujemnej.");
                            }
                            stack.push(Double.toString(Math.sqrt(a)));
                            break;
                        case '%':
                            b = Double.parseDouble(stack.pop());
                            a = Double.parseDouble(stack.pop());
                            stack.push(Double.toString(a % b));
                            break;
                        case '!': 
                            a = Double.parseDouble(stack.pop());
                            if (a < 0 || a != Math.floor(a)) {
                                throw new ArithmeticException("Błąd: Silnia tylko dla liczb całkowitych >= 0.");
                            }
                            stack.push(Double.toString(factorial((int) Math.round(a))));
                            break;
                        default:
                            throw new IllegalArgumentException("Błąd: Nieznany operator " + znak);
                    }
                } catch (RuntimeException e) {
                    return e.getMessage();
                }
            }
        }
        return "Błąd: Niepoprawne równanie.";
    }

    private int factorial(int n) {
        if (n == 0 || n == 1) return 1;
        return n * factorial(n - 1);
    }
}
