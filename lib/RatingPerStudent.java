package lib;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("unchecked")
public class RatingPerStudent {
	
	// Performanz-Kriterium
	private StringProperty performance = new SimpleStringProperty(this, "performance", null);
	
	// Bewertung zu dem Performanz-Kriterium
	private StringProperty rating = new SimpleStringProperty(this, "rating", null);
	
	public RatingPerStudent(String performance, String rating) {
		this.performance.set(performance);
		this.rating.set(rating);
	}
	
	public RatingPerStudent(String performance, StringProperty rating) {
		this.performance.set(performance);
		this.rating = rating;
	}
	
	private Alert alert = new Alert(AlertType.NONE);
	
	public StringProperty performanceProperty() {
		return performance;
	}
	public void setPerformance(String p) {
		performanceProperty().set(p);
	}
	public String getPerformance() {
		return performance.get();
	}
	public StringProperty ratingProperty() {
		return rating;
	}
	public void setRating(String r) {
		ratingProperty().set(r);
	}
	public String getRating() {
		return rating.get();
	}
	
	public void showErrorAlert(String str) {
		alert.setAlertType(AlertType.ERROR);
		alert.setContentText(str);
		alert.showAndWait();
	}
}	
