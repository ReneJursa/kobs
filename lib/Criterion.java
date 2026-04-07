package lib;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

// abstrakte Basis-Klasse eines Kriteriums, das aus einer Beschreibung und einer Gewichtung dieses Kriteriums besteht
public abstract class Criterion {
	
	// Beschreibung des Kriteriums
	private StringProperty description =
			 new SimpleStringProperty(this, "description", null);
	
	// Gewichtung des Kriteriums
	private DoubleProperty weight = new SimpleDoubleProperty(this, "weight", 0);
	
	public Criterion(String description, double weight) {
		this.description.set(description);
		this.weight.set(weight);
	}
	public StringProperty descriptionProperty() {
		return description;
	}
	public void setDescription(String d) {
		descriptionProperty().set(d);
	}
	public String getDescription() {
		return description.get();
	}
	public DoubleProperty weightProperty() {
        if (weight == null) {
            weight = new SimpleDoubleProperty(this, "weight", 0);
        }
        return weight;
    } 
	public void setWeight(double w) {
        this.weightProperty().set(w);
    } 
	public double getWeight() {
        if (weight != null)
            return weight.get();
        return 0;
    } 
    
	public abstract String toString();
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Criterion)) return false;
		Criterion c = (Criterion) o;
		return description.equals(c.description);
	}
}	
