package lib;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

public class PerformanceOfStudent {
	
	private final StringProperty vorname = new SimpleStringProperty(this, "vorname", null);
	private final StringProperty nachname = new SimpleStringProperty(this, "nachname", null);
	private final StringProperty kurs = new SimpleStringProperty(this, "kurs", null);
	private final StringProperty name = new SimpleStringProperty(this, "name", null);
	private StringProperty rating = new SimpleStringProperty(this, "rating", null);
	
	public PerformanceOfStudent(String vorname, String nachname, String kurs, String rating) {
		this.vorname.set(vorname);
		this.nachname.set(nachname);
		this.kurs.set(kurs);
		this.name.set(this.getNachname() + " " + this.getVorname() + " " + this.getKurs());
		this.rating.set(rating);
	}
	
	public final String getVorname() {
		return vorname.get();
	}
	public final void setVorname(String vorname) {
		vornameProperty().set(vorname);
	}
	public final StringProperty vornameProperty() {
		return vorname;
	}
	public final String getNachname() {
		return nachname.get();
	}
	public final void setNachname(String nachname) {
		nachnameProperty().set(nachname);
	}
	public final StringProperty nachnameProperty() {
		return nachname;
	}
	public final String getName() {
		return name.get();
	}
	public final String getKurs() {
		return kurs.get();
	}
	public final void setKurs(String kurs) {
		kursProperty().set(kurs);
	}
	public final StringProperty kursProperty() {
		return kurs;
	}
	public void setRating(String r) {
        ratingProperty().set(r);
    } 
	public StringProperty ratingProperty() {
        return rating;
    } 
    public String getRating() {
		return rating.get();
    } 
}