package lib;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

@SuppressWarnings("unchecked")
// Klasse eines Kriteriums der Kompetenz, die eine Liste mehrerer Performanz-Kriterien enthält 
public class CompetenceCrit extends Criterion {
	
	// Liste aller Performanz-Kriterien eines Kriteriums der Kompetenz
	private ArrayList<PerformanceCrit> perfCritList;

	public CompetenceCrit(String description, double weight) {
		super(description, weight);
		this.perfCritList = new ArrayList<PerformanceCrit>();
	}
	public void addPerfCrit(String description, double weight) {
		PerformanceCrit pC = new PerformanceCrit(description, weight);
		perfCritList.add(pC); 
	}
	public ArrayList<PerformanceCrit> getPerfCritList() {
		return this.perfCritList;
	}
	public TableColumn<PerformanceCrit, String> getCpCritDcCol() {
		TableColumn<PerformanceCrit, String> cpCritDcCol = new TableColumn<>(this.getDescription());
		return cpCritDcCol;
	}
	public TableColumn<PerformanceCrit, String> getPfCritDcCol() {
		TableColumn<PerformanceCrit, String> pfCritDcCol = new TableColumn<>("Performanz");
		pfCritDcCol.setId("description");
		pfCritDcCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		pfCritDcCol.setCellFactory(TextFieldTableCell.<PerformanceCrit>forTableColumn());
		
		return pfCritDcCol;
	}
	public TableColumn<PerformanceCrit, Double> getPfCritWeCol() {
		TableColumn<PerformanceCrit, Double> pfCritWeCol = new TableColumn<>("Gewich-\ntung");
		pfCritWeCol.setId("weight");
		pfCritWeCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		
		pfCritWeCol.setCellFactory(TextFieldTableCell.<PerformanceCrit, Double>forTableColumn(new DoubleStringConverter() {
		   
			@Override
			public String toString(Double value) {
				return value == null ? "" : value.toString();
			}
			@Override
			public Double fromString(String value) {
				return (value == null) ? null : Double.parseDouble(value.replace(',','.'));
			}
		}));
		return pfCritWeCol;
	}
	
	@Override
	public String toString() {
		return ("Kompetenz-Kriterium \"" + this.getDescription() + "\" mit der Gewichtung: " + this.getWeight());
	}	
}
