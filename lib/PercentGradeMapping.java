package lib;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("unchecked")
public class PercentGradeMapping {
	
	// Bezeichnung
	private StringProperty description =
			 new SimpleStringProperty(this, "description", null);
	
	// lower limit
	private DoubleProperty lowerLimit = new SimpleDoubleProperty(this, "lowerLimit", 0);
	
	// upper limit
	private DoubleProperty upperLimit = new SimpleDoubleProperty(this, "upperLimit", 0);
	
	public PercentGradeMapping(String description, double lowerLimit, double upperLimit) {
		this.description.set(description);
		this.lowerLimit.set(lowerLimit);
		this.upperLimit.set(upperLimit);
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
	public DoubleProperty lowerLimitProperty() {
		return lowerLimit;
	}
	public void setLowerLimit(double v) {
		lowerLimitProperty().set(v);
	}
	public double getLowerLimit() {
		return lowerLimit.get();
	}
	public DoubleProperty upperLimitProperty() {
		return upperLimit;
	}
	public void setUpperLimit(double v) {
		upperLimitProperty().set(v);
	}
	public double getUpperLimit() {
		return upperLimit.get();
	}
	
	public void showErrorAlert(String str) {
		alert.setAlertType(AlertType.ERROR);
		alert.setContentText(str);
		alert.showAndWait();
	}
}	
