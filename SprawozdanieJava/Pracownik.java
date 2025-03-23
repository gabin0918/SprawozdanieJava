import java.io.Serializable;
 
public class Pracownik implements Serializable {
	private static final long serialVersionUID = -7887612267521882048L;
        String imie;
        String nazwisko;
        String email;
 
        public Pracownik(String imie, String nazwisko, String email) {
            this.imie = imie;
            this.nazwisko = nazwisko;
            this.email = email;
        }
 
	@Override
	public String toString() {
		return "Pracownik [imie=" + imie + ", nazwisko=" + nazwisko
					+ ", email=" + email + "]";
	}
 
}