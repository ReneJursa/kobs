package lib;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.geometry.Pos;

@SuppressWarnings("unchecked")
// Klasse eines Kriteriums der Performanz, das beschreibt wie gut ein Kriterium der Kompetenz erfüllt wird
public class PerformanceCrit extends Criterion {
	
	private boolean initRatingFlag = false;
	
	// Liste der Bewertungen aller Kollegiaten zu einem Performanz-Kriterium eines Kriteriums der Kompetenz
	private ArrayList<PerformanceOfStudent> perfOfStudList;
	
	private Alert alert = new Alert(AlertType.NONE);
	
	public PerformanceCrit(String description, double weight) {
		super(description, weight);
		this.perfOfStudList = new ArrayList<PerformanceOfStudent>();
	}
	public void setIinitRatingFlag(boolean initRatingFlag) {
		this.initRatingFlag = initRatingFlag;
	}
	public boolean getIinitRatingFlag() {
		return initRatingFlag;
	}
	public void addPerfOfStud(String vorname, String nachname, String kurs, String rating) {
		PerformanceOfStudent pOfStud = new PerformanceOfStudent(vorname, nachname, kurs, rating);
		perfOfStudList.add(pOfStud);
		initRatingFlag = true;
		String name = nachname + " " + vorname + " " + kurs;
	}
	public ArrayList<PerformanceOfStudent> getPerfOfStudList() {
		return this.perfOfStudList;
	}
	public void clearPerfOfStudList() {
	    perfOfStudList.clear();
	}
	public void showInformationAlert(String str) {
		alert.setAlertType(AlertType.INFORMATION);

		alert.setContentText(str);

		alert.showAndWait();
	}
	public TableColumn<PerformanceOfStudent, String> getPfOfStudVnCol() {
		TableColumn<PerformanceOfStudent, String> pfOfStudVnCol = new TableColumn<>("Vorname");
		pfOfStudVnCol.setId("vorname");
		pfOfStudVnCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		return pfOfStudVnCol;
	}
	public TableColumn<PerformanceOfStudent, String> getPfOfStudNnCol() {
		TableColumn<PerformanceOfStudent, String> pfOfStudNnCol = new TableColumn<>("Nachname");
		pfOfStudNnCol.setId("nachname");
		pfOfStudNnCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		return pfOfStudNnCol;
	}
	public TableColumn<PerformanceOfStudent, String> getPfOfStudKsCol() {
		TableColumn<PerformanceOfStudent, String> pfOfStudKsCol = new TableColumn<>("Kurs");
		pfOfStudKsCol.setId("kurs");
		pfOfStudKsCol.setCellValueFactory(new PropertyValueFactory<>("kurs"));
		return pfOfStudKsCol;
	}
	public TableColumn<PerformanceOfStudent, String> getPfOfStudRtCol() {
		TableColumn<PerformanceOfStudent, String> pfOfStudRtCol = new TableColumn<>("Bewertung");
		pfOfStudRtCol.setId("rating");
		pfOfStudRtCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
		pfOfStudRtCol.setCellFactory(TextFieldTableCell.<PerformanceOfStudent>forTableColumn());
		return pfOfStudRtCol;
	}
	
	@Override
	public String toString() {
		return ("Performanz-Kriterium \"" + this.getDescription() + "\" mit der Gewichtung: " + this.getWeight());
	}
}