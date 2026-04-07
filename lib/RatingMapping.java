package lib;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("unchecked")
public class RatingMapping {
	
	// Bezeichnung
	private StringProperty description =
			 new SimpleStringProperty(this, "description", null);
	
	// Wert der Bezeichnung
	private DoubleProperty value = new SimpleDoubleProperty(this, "value", 0);
	
	public RatingMapping(String description, double value) {
		this.description.set(description);
		this.value.set(value);
	}
	
	private Alert alert = new Alert(AlertType.NONE);
	
	public StringProperty descriptionProperty() {
		return description;
	}
	public void setDescription(String d) {
		descriptionProperty().set(d);
	}
	public String getDescription() {
		return description.get();
	}
	public DoubleProperty valueProperty() {
		return value;
	}
	public void setValue(double v) {
		valueProperty().set(v);
	}
	public double getValue() {
		return value.get();
	}
	
	public void showErrorAlert(String str) {
		alert.setAlertType(AlertType.ERROR);
		alert.setContentText(str);
		alert.showAndWait();
	}
}	
