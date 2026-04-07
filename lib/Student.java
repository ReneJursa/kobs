package lib;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Student {
	
	private StringProperty vorname = new SimpleStringProperty(this, "vorname", null);
	
	private StringProperty nachname = new SimpleStringProperty(this, "nachname", null);
	
	private StringProperty kurs = new SimpleStringProperty(this, "kurs", null);
	
	private StringProperty name = new SimpleStringProperty(this, "name", null);
	
	private DoubleProperty sl_percent = new SimpleDoubleProperty(this, "sl_percent", -1);
	
	private StringProperty sl_note = new SimpleStringProperty(this, "sl_note", null);
	
	public Student(String vorname, String nachname, String kurs) {
		this.vorname.set(vorname);
		this.nachname.set(nachname);
		this.kurs.set(kurs);
		this.name.set(nachname + " " + vorname + " " + kurs);
	}
	
	private Alert alert = new Alert(AlertType.NONE);
	
	public String getVorname() {
		return vorname.get();
	}
	public String getNachname() {
		return nachname.get();
	}
	public String getKurs() {
		return kurs.get();
	}
	public String getName() {
		return name.get();
	}
	public double getSLProzent() {
		return sl_percent.get();
	}
	public DoubleProperty sl_percentProperty() {
        if (sl_percent == null) {
            sl_percent = new SimpleDoubleProperty(this, "sl_percent", 0);
        }
        return sl_percent;
    } 
	private void setSl_percent(double p) {
        this.sl_percentProperty().set(p);
    } 
	public String getSLNote() {
		return sl_note.get();
	}
	public StringProperty sl_noteProperty() {
        if (sl_note == null) {
            sl_note = new SimpleStringProperty(this, "sl_note", null);
        }
        return sl_note;
    } 
	private void setSl_note(String n) {
        this.sl_noteProperty().set(n);
    } 
	private double round(double value, int decimalPoints) {
		double d = Math.pow(10, decimalPoints);
		return Math.round(value * d) / d;
    }
	public void showErrorAlert(String str) {
		alert.setAlertType(AlertType.ERROR);
		alert.setContentText(str);
		alert.showAndWait();
	}
	
	public void calcSLNote(ArrayList<CompetenceCrit> cpCritList,
                           ArrayList<RatingMapping> ratingMappingList, ArrayList<PercentGradeMapping> percentGradeMappingList) throws Exception {
		
		ArrayList<Double> sumPfCWeList = new ArrayList<Double>();
		
		int i = 0;
		double sumCpCWe = 0.0;
		for (CompetenceCrit cC: cpCritList) {
			double cpCWe = cC.getWeight();
			sumCpCWe += cpCWe;
			double sumPfCWe = 0.0;
			for (PerformanceCrit pC: cC.getPerfCritList()) {
				double pfCWe = pC.getWeight();
				sumPfCWe += pfCWe;
			}
			sumPfCWeList.add(sumPfCWe);
			i++;
		}
		/*
		System.out.println("Summe aller Kompetenzgewichtungen: " + sumCpCWe);
		
		for (int ci = 0; ci < cpCritList.size(); ci++) {
			System.out.println("Kompetenz: " + cpCritList.get(ci).getDescription());
			System.out.println("Summe der Performanzgewichtungen: " + sumPfCWeList.get(ci));
		}
		*/
		double min_performance = 0.0;
		double max_performance = 0.0;
		double sum_we = 0.0;
		double sl = 0.0;
		
		for(int ci = 0; ci < cpCritList.size(); ci++) {
			
			double cpCWe = 0.0;
			if(0.0 < sumCpCWe) {
				cpCWe = cpCritList.get(ci).getWeight() / sumCpCWe;
			}
			//System.out.print("cpWe " + cpCWe + " pfCWe ");
			
			int pfCListSize = cpCritList.get(ci).getPerfCritList().size();
			
			double sumPfCWe = sumPfCWeList.get(ci);
			
			for(int pi = 0; pi < pfCListSize; pi++) {
				
				double pfCWe = 0.0;
				if(0.0 < sumPfCWe) {
					pfCWe = cpCritList.get(ci).getPerfCritList().get(pi).getWeight() / sumPfCWe;
				}
				//System.out.print(" " + pfCWe);
				
				double we = cpCWe * pfCWe;
				
				//System.out.print(" we " + we);
				//System.out.println();
				
				sum_we += we;
				
				int indexOfName = -1;
				for(int ni = 0; ni < cpCritList.get(ci).getPerfCritList().get(pi).getPerfOfStudList().size(); ni++) {
					String n = cpCritList.get(ci).getPerfCritList().get(pi).getPerfOfStudList().get(ni).getName();
					//System.out.println(ni + " " + n + " this "  + this.getName());
				    if(this.getName().equals(n)) {	
						indexOfName = ni;
						break;
					}
				}
				if(indexOfName == -1) {
					showErrorAlert("Fehler in Notenberechnung:\n Name " + this.getName() + " nicht in Liste gefunden");
					throw new Exception("Fehler in Notenberechnung: Name " + this.getName() + " nicht in Liste gefunden");
				}	
				
				String rating = cpCritList.get(ci).getPerfCritList().get(pi).getPerfOfStudList().get(indexOfName).getRating();
				
				int indexOfRating = -1;
				for(int ri = 0; ri < ratingMappingList.size(); ri++) {
					String r_str = ratingMappingList.get(ri).getDescription();
					//System.out.println(ri + " " + r_str + " rating "  + rating);
				    if(rating.equals(r_str)) {	
						indexOfRating = ri;
						break;
					}
				}
				
				double r;
				
				if(indexOfRating == -1) {
					showErrorAlert("Fehler in Notenberechnung:\n Bezeichnung " + rating + " nicht in Liste gefunden");
					throw new Exception("Fehler in Notenberechnung: Bezeichnung " + rating + " nicht in Liste gefunden");
				}	
				else {
					r = ratingMappingList.get(indexOfRating).getValue();
				}
				
				sl += we * r;
			}
			//System.out.println();
		}
		
		double min_performance_norm = 0.0;
		double max_performance_norm = sum_we;
		//System.out.printf("normierte Min Perf: %.6f normierte Max Perf: %.6f\n", min_performance_norm, max_performance_norm);
		
		sl += 1.0e-12; // Zur Vermeidung der Ergebnisverschlechterung durch begrenzte Rechengenauigkeit 
		double sl_norm = round(sl, 12);
		//System.out.printf("sl %.6f normiertes sl: %.6f\n", sl, sl_norm);
		
		this.setSl_note("-1");
		
		boolean gradingFlag = false;
		for(PercentGradeMapping pgM : percentGradeMappingList) {
			double loLim = pgM.getLowerLimit() / 100.0;
			double upLim = pgM.getUpperLimit() / 100.0;
			String note = pgM.getDescription();
			if (sl_norm < max_performance_norm) {
				if( (loLim * max_performance_norm <= sl_norm) && (sl_norm < upLim * max_performance_norm) ) {
					this.setSl_note(note);
					gradingFlag = true;
					break;
				}
			}
			else {
				this.setSl_note(note);
				gradingFlag = true;
				break;
			}
		}
		if (!gradingFlag) {
			showErrorAlert("Fehler bei der Notenzuordnung");
			throw new Exception("Fehler bei der Notenzuordnung");
		}
		try {
			setSl_percent(round(100.0 * sl_norm / max_performance_norm, 3));
		}
		catch (ArithmeticException e) {
			System.out.println("max_performance_norm " + max_performance_norm + " " + e.getMessage());
			showErrorAlert("Fehler in Notenberechnung: " + e.getMessage());
		}
	}
}	