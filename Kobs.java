import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import static javafx.stage.FileChooser.ExtensionFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TabPane;
import static javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.SelectionMode;
import static javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.stage.Screen;
import javafx.geometry.VPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.URISyntaxException;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.PageSize;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.Chunk;
import org.openpdf.text.Paragraph;
import org.openpdf.text.HeaderFooter;
import org.openpdf.text.Rectangle;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.LineSeparator;

import lib.Criterion;
import lib.CompetenceCrit;
import lib.PerformanceCrit;
import lib.PerformanceOfStudent;
import lib.RatingMapping;
import lib.RatingPerStudent;
import lib.PercentGradeMapping;
import lib.Student;
import lib.AuthorTab;
import lib.LicenseTab;
import lib.Info;
import lib.DescriptionTab;
import lib.DocumentationTab;
import lib.LocalDateStringConverter;

@SuppressWarnings("unchecked")
public class Kobs extends Application {
	
	private final String kobsVersionStr = "KOBS 1.3.1";
	
	private String styleAnchorPaneStr = "-fx-background-color: snow;";
	
	private String studFile_str = "";
	
	private String studAbsoluteFile_str = "";
	
	private String prefix_outputFiles = ""; 
	
	private String initialDir = "";
	
	private final String initDirFile_str = "initialDirectory.txt";
	
	private String initialDirPath_str = "";
	
	private final String initPDFTextFile_str = "initialPDFText.txt";
	
	private String initialPDFTextPath_str = "";
	
	private final String initPercentGradeMappingFile_str = "initialPercentGradeMapping.csv";
	
	private String initialPercentGradeMappingPath_str = "";
	
	private final String initRatingMappingFile_str = "initialRatingMapping.csv";
	
	private String initialRatingMappingPath_str = "";
	
	private String titleIndividualPDF = "";
	
	private String headIndividualPDF = "";
	
	private String placeIndividualPDF = "";
	
	private LocalDateStringConverter locDateStrConv;
	
	private String date_str = "";
	
	private boolean date_set = false;
	
	private boolean initialDirSelected = false;
	
	private boolean studList_imported = false;
	
	private boolean ratingMappingList_imported = false;
	
	private boolean percentGradeMappingList_imported = false;
	
	private boolean csvFilesSaved = false;
	
	private boolean pdfFilesSaved = false;
	
	private boolean performanceOfStud_initialized = false;
	
	private int actual_ci = 0;
	private int actual_pi = 0;
	
	private int actual_name_i = 0;
	
	private final TextField textField = new TextField();
	
	// Liste aller Kompetenz-Kriterien 
	public ArrayList<CompetenceCrit> cpCritList = new ArrayList<CompetenceCrit>();
	
	// Tabelle für die Kompetenz-Kriterien
	TableView<CompetenceCrit> tableCompetenceCrit = new TableView<>();
	
	// Liste der Tabellen für die Performanz-Kriterien zu den einzelnen Kompetenz-Kriterien 
	public ArrayList<TableView<PerformanceCrit>>  tablePfCritList = new ArrayList<TableView<PerformanceCrit>>();
	
	// Liste der Tabellen für die Bewertung zu den Performanz-Kriterien der einzelnen Kompetenz-Kriterien 
	public ArrayList<TableView<RatingPerStudent>>  ratingPerStudentTableList = new ArrayList<TableView<RatingPerStudent>>();
	
	// Liste aller Studenten
	public ArrayList<Student> studList = new ArrayList<Student>();
	
	// Liste der Bewertungsskala
	public ArrayList<RatingMapping> ratingMappingList = new ArrayList<RatingMapping>();
	
	// Sortierte Liste der Bewertungsskala
	public ArrayList<RatingMapping> ratingMappingList_sorted = new ArrayList<RatingMapping>();
	
	// Liste der Prozent Note Zuordnung
	public ArrayList<PercentGradeMapping> percentGradeMappingList = new ArrayList<PercentGradeMapping>();
	
	// Tabelle für die Bewertungsskala
	TableView<RatingMapping> tableRatingMapping = new TableView<>();
	
	// Tabelle für die Prozent Note Zuordnung
	TableView<PercentGradeMapping> tablePercentGradeMapping = new TableView<>();
	
	private Stage mainStage;
	private Stage infoStage;
	private Stage lastOpenMainStage = null;
	private Stage compStage;
	private Stage lastOpenCompStage = null;
	private Stage perfStage;
	private Stage lastOpenPerfStage = null;
	private Stage resultStage;
	private Stage lastOpenResultStage = null;
	private Stage headStage;
	private Stage lastOpenHeadStage = null;
	private Stage ratingPerStudentStage;
	private Stage lastOpenRatingPerStudentStage = null;
	private Stage ratingPerCompetenceStage;
	private Stage lastOpenRatingPerCompetenceStage = null;
	private Stage ratMapStage;
	private Stage lastOpenRatMapStage = null;
	private Stage percentGradeMapStage;
	private Stage lastOpenPercentGradeMapStage = null;
	private Stage readyStage;
	
	private Alert alert = new Alert(AlertType.NONE);
	
	public void showErrorAlert(String str) {
		alert.setAlertType(AlertType.ERROR);
		alert.setContentText(str);
		alert.showAndWait();
	}
	public void showInformationAlert(String str) {
		alert.setAlertType(AlertType.INFORMATION);
		alert.setContentText(str);
		alert.showAndWait();
	}
	public void showWarningAlert(String str) {
		alert.setAlertType(AlertType.WARNING);
		alert.setContentText(str);
		alert.showAndWait();
	}
	public void clearFields(TextField descriptionField, TextField weightField) {
		descriptionField.setText(null);
		weightField.setText(null);
	}
	
	public String replaceToUniCode(String str) {
		str = str.replace("Ä","\u00C4");
		str = str.replace("ä","\u00E4");
		str = str.replace("Ö","\u00D6");
		str = str.replace("ö","\u00F6");
		str = str.replace("Ü","\u00DC");
		str = str.replace("ü","\u00FC");
		str = str.replace("ß","\u00DF");
		str = str.replace("à","\u00E0");
		str = str.replace("á","\u00E1");
		str = str.replace("è","\u00E8");
		str = str.replace("é","\u00E9");
		str = str.replace("ì","\u00EC");
		str = str.replace("í","\u00ED");
		str = str.replace("ò","\u00F2");
		str = str.replace("ó","\u00F3");
		str = str.replace("ù","\u00F9");
		str = str.replace("ú","\u00FA");
		return str;
	}
	
	private double round(double value, int decimalPoints) {
		double d = Math.pow(10, decimalPoints);
		return Math.round(value * d) / d;
    }
	
	public void addCompCrit(Stage stage, TextField descriptionField, TextField weightField) {
		String cp_crit = replaceToUniCode(descriptionField.getText());
		String cp_crit_we_str = weightField.getText().replace(',','.');
		try {
			double cp_crit_we = Double.parseDouble(cp_crit_we_str);
			if(0.0 <= cp_crit_we) {
				if(cp_crit.length() == 0) {
					showWarningAlert("Beschreibung des Kompetenz-Kriteriums fehlt.");
				}
				CompetenceCrit cC = new CompetenceCrit(cp_crit, cp_crit_we);
				tableCompetenceCrit.getItems().add(cC);
				cpCritList.add(cC);
				clearFields(descriptionField, weightField);
				//perfStage.close();
				if(perfStage.isShowing()) {
					createPerfTables(stage);
				}
				if(ratingPerCompetenceStage.isShowing()) {
					ratingPerCompetenceStage.close();
				}
				if(ratingPerStudentStage.isShowing()) {
					//editRatingPerStudentTable(stage, actual_name_i);
					ratingPerStudentStage.close();
				}
				if(resultStage.isShowing()) {
					resultStage.close();
				}
			}
			else {
				showErrorAlert("Die Gewichtung " + cp_crit_we + " liegt ausserhalb des erlaubten Bereichs.\n" +
							   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");	
			}
		} catch (NumberFormatException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr die Gewichtung eingegeben:\n" + e);
			weightField.setText(null);
		} catch (NullPointerException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr die Gewichtung eingegeben:\n" + e);
			weightField.setText(null);		
		}
	}

	public GridPane getNewCompCritPane(Stage stage) {
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		
		Label loadLabel = new Label("Einlesen von Kompetenzen\nund Performanzen aus CSV-Datei:");
		loadLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		Button loadBtn = new Button();
		loadBtn.setText("Datei \u00F6ffnen");
		loadBtn.setOnAction(event -> readCriteria(stage));
		
		pane.add(loadLabel, 0, 0);
		pane.add(loadBtn, 1, 0);
		
		TextField descriptionField = new TextField();
		TextField weightField = new TextField();
		Label cpLabel = new Label("Einf\u00FCgen einer Kompetenz:");
		cpLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(2, cpLabel, descriptionField);
		
		Label weLabel = new Label("Einf\u00FCgen einer Gewichtung:");
		weLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(3, weLabel, weightField);
		
		Button addBtn = new Button("Einf\u00FCgen");
		addBtn.setOnAction(event -> addCompCrit(stage, descriptionField, weightField));
		
		pane.add(addBtn, 1, 4);
		
		return pane;
	}
	
	public void deleteCompCrit(Stage stage) {	
		
		TableViewSelectionModel<CompetenceCrit> tsm = tableCompetenceCrit.getSelectionModel();
		if (tsm.isEmpty()) {
			//System.out.println("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			showInformationAlert("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}
		
		ObservableList<Integer> list = tsm.getSelectedIndices();
		Integer[] selectedIndices = new Integer[list.size()];
		selectedIndices = list.toArray(selectedIndices);

		Arrays.sort(selectedIndices);
		
		for(int i = selectedIndices.length - 1; i >= 0; i--) {
			tsm.clearSelection(selectedIndices[i].intValue());
			tableCompetenceCrit.getItems().remove(selectedIndices[i].intValue());
			cpCritList.remove(selectedIndices[i].intValue());
			if(ratingPerCompetenceStage.isShowing() && actual_ci == selectedIndices[i].intValue()) {
				ratingPerCompetenceStage.close();
			}	
		}
		
		if(perfStage.isShowing() && !cpCritList.isEmpty()) {
			createPerfTables(stage);
		}	
		
		if(ratingPerStudentStage.isShowing() && !cpCritList.isEmpty()) {
			editRatingPerStudentTable(stage, actual_name_i);
		}
		
		if(resultStage.isShowing() && !cpCritList.isEmpty() && studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				try {
					st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
				} catch (Exception e) {
					System.out.println("Fehler in deleteCompCrit bei Notenberechnung von " + name + " " + e.getMessage());
				}
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		}
		if(cpCritList.isEmpty()) {
			perfStage.close();
			resultStage.close();
			ratingPerStudentStage.close();
			ratingPerCompetenceStage.close();
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
		}
	}
	
	private void createCompTable(Stage stage) {
		
		tableCompetenceCrit.getColumns().clear();
		
		ObservableList<CompetenceCrit> observableCpCritList =
			FXCollections.<CompetenceCrit>observableArrayList(cpCritList);
		
		tableCompetenceCrit.getSelectionModel().setCellSelectionEnabled(true);
		
		tableCompetenceCrit.setEditable(true);
		
		TableViewSelectionModel<CompetenceCrit> tableCompetenceCritSelMod = tableCompetenceCrit.getSelectionModel();
		tableCompetenceCritSelMod.setSelectionMode(SelectionMode.MULTIPLE);
		
		TableColumn<CompetenceCrit, String> cpCritDcCol = new TableColumn<>("Kompetenz");
		cpCritDcCol.setMinWidth(150);
		TableColumn<CompetenceCrit, Double> cpCritWeCol = new TableColumn<>("Gewichtung");
		cpCritWeCol.setMinWidth(150);
		
		String descriptStr = "description";
		String weightStr = "weight";
		
		cpCritDcCol.setId(descriptStr);
		cpCritWeCol.setId(weightStr);
		
		PropertyValueFactory<CompetenceCrit, String> cpCritDcCellValueFactory =
			new PropertyValueFactory<>(descriptStr);
			
		cpCritDcCol.setCellValueFactory(cpCritDcCellValueFactory);
		
		cpCritDcCol.setCellFactory(TextFieldTableCell.<CompetenceCrit>forTableColumn());
		
		cpCritDcCol.setOnEditCommit((CellEditEvent<CompetenceCrit, String> t) -> {
			((CompetenceCrit) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
					).setDescription(t.getNewValue());
			if(perfStage.isShowing()) {
				createPerfTables(stage);
			}	
			if(ratingPerStudentStage.isShowing()) {
				editRatingPerStudentTable(stage, actual_name_i);
			}
			if(ratingPerCompetenceStage.isShowing()) {
				editRatingPerCompetenceTable(stage, actual_ci, actual_pi);
			}
        });
		
		
		PropertyValueFactory<CompetenceCrit, Double> cpCritWeCellValueFactory =
			new PropertyValueFactory<>(weightStr);
			
		cpCritWeCol.setCellValueFactory(cpCritWeCellValueFactory);
		
		//cpCritWeCol.setCellFactory(TextFieldTableCell.<CompetenceCrit, Double>forTableColumn(new DoubleStringConverter()));
		cpCritWeCol.setCellFactory(TextFieldTableCell.<CompetenceCrit, Double>forTableColumn(new DoubleStringConverter() {
		   
			@Override
			public String toString(Double value) {
				return value == null ? "" : value.toString();
			}
			@Override
			public Double fromString(String value) {
				return (value == null) ? null : Double.parseDouble(value.replace(',','.'));
			}
		}));
		
		cpCritWeCol.setOnEditCommit((CellEditEvent<CompetenceCrit, Double> t) -> {
            
			Double newVal = 0.0;
			Double oldVal = 0.0;
			try { 
				newVal = t.getNewValue();
				
				int rowId = t.getTablePosition().getRow();
				
				oldVal = t.getOldValue();
				if(0.0 <= newVal) {
					((CompetenceCrit) t.getTableView().getItems().get(rowId)).setWeight(newVal);
				} 
				else {
					showErrorAlert("Der eingef\u00FCgte Wert " + newVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
								   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");
					t.getTableView().refresh();
					((CompetenceCrit) t.getTableView().getItems().get(rowId)).setWeight(oldVal);
				}
			} catch (NumberFormatException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			} catch (NullPointerException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			} 
			
			if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
				for (Student st: studList) {
					String vorname = st.getVorname();
					String nachname = st.getNachname();
					String kurs = st.getKurs();
					String name = nachname + " " + vorname + " " + kurs;
					try {
						st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.out.println("Fehler in createCompTable bei Notenberechnung von " + name + " " + e.getMessage());
					}
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}	
        });
		
		tableCompetenceCrit.getColumns().addAll(cpCritDcCol, cpCritWeCol);
		tableCompetenceCrit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		tableCompetenceCrit.setOnKeyPressed(event -> {
            TablePosition<CompetenceCrit, ?> pos = tableCompetenceCrit.getFocusModel().getFocusedCell() ;
            //if (pos != null && event.getCode().isLetterKey()) {
			if (pos != null) {
                tableCompetenceCrit.edit(pos.getRow(), pos.getTableColumn());
            }
        });
		
		tableCompetenceCrit.setItems(observableCpCritList);
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
		                  "-fx-border-width: 2;" +
		                  "-fx-border-insets: 5;" + 
		                  "-fx-border-radius: 5;" + 
						  "-fx-border-color: blue;";
						 
		VBox rootCpCritTable = new VBox(tableCompetenceCrit);
		rootCpCritTable.setStyle(styleStr);
		rootCpCritTable.setPrefWidth(350);
		rootCpCritTable.setLayoutX(0);
		rootCpCritTable.setLayoutY(0);
		
		GridPane newCompCritPane  = getNewCompCritPane(stage);
		gridPane.add(newCompCritPane, 0, 0);
		
		gridPane.add(rootCpCritTable, 0, 1);
		
		Button deleteBtn = new Button("Zeilen l\u00F6schen");
		deleteBtn.setOnAction(event -> deleteCompCrit(stage));  
		gridPane.add(deleteBtn, 0, 2);
		
		Button nextBtn = new Button();
		nextBtn.setText("Weiter");
		nextBtn.setOnAction(event -> createPerfTables(stage));
		
		gridPane.add(nextBtn, 1, 3);
		
		GridPane.setHgrow(rootCpCritTable, Priority.ALWAYS);
		GridPane.setVgrow(rootCpCritTable, Priority.ALWAYS);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(490);
		anchorPane.setPrefHeight(750);
		
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		Scene scene = new Scene(anchorPane, 480, 640);
		//ScrollPane sp = new ScrollPane(anchorPane);
		//Scene scene = new Scene(sp, 480, 640);
		compStage.setScene(scene);
		compStage.setTitle("Kompetenztabelle - KOBS");
		
		if(this.lastOpenCompStage == null) {
			compStage.setX(300);
			compStage.setY(20);
		}
		else {
			compStage.setX(this.lastOpenCompStage.getX());
			compStage.setY(this.lastOpenCompStage.getY());
		}
		compStage.setWidth(510);
		compStage.setHeight(770);
		compStage.show();
		compStage.toFront();
		this.lastOpenCompStage = compStage;
	}
	
	public void addPerfCrit(Stage stage, int k, TextField descriptionField, TextField weightField) {
		String pf_crit = replaceToUniCode(descriptionField.getText());
		String pf_crit_we_str = weightField.getText().replace(',','.');
		try {
			double pf_crit_we = Double.parseDouble(pf_crit_we_str);
			if(0.0 <= pf_crit_we) {
				if(pf_crit.length() == 0) {
					showWarningAlert("Beschreibung des Performanz-Kriteriums fehlt.");
				}
				PerformanceCrit pC = new PerformanceCrit(pf_crit, pf_crit_we);
				tablePfCritList.get(k).getItems().add(pC);
				cpCritList.get(k).getPerfCritList().add(pC);
				
				for (PerformanceCrit pC2: cpCritList.get(k).getPerfCritList()) {
					//System.out.write(String.format("   %s ", pC2.getDescription());
					if (pC2.getDescription().equals(pf_crit)) {
						for (Student st: studList) {
							pC2.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), getInitRating());
						}
					}
				}
				//System.out.write(String.format("\n");
				
				clearFields(descriptionField, weightField);
				
				if(ratingPerStudentStage.isShowing()) {
					editRatingPerStudentTable(stage, actual_name_i);
				}
				if(ratingPerCompetenceStage.isShowing() ) {
					editRatingPerCompetenceTable(stage, actual_ci, actual_pi);
				}
				if(resultStage.isShowing() && studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
					
					for (Student st: studList) {
						String vorname = st.getVorname();
						String nachname = st.getNachname();
						String kurs = st.getKurs();
						String name = nachname + " " + vorname + " " + kurs;
						try {
							st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in addPerfCrit bei Notenberechnung von " + name + " " + e.getMessage());
						}
					}
					csvFilesSaved = false;
					pdfFilesSaved = false;
				}
			}
			else {
				showErrorAlert("Die Gewichtung " + pf_crit_we + " liegt ausserhalb des erlaubten Bereichs.\n" +
							   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");	
			}
		} catch (NumberFormatException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr die Gewichtung eingegeben:\n" + e);
			weightField.setText(null);
		} catch (NullPointerException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr die Gewichtung eingegeben:\n" + e);
			weightField.setText(null);		
		}
	}

	public GridPane getNewPerfCritPane(Stage stage, int k) {
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(5);
		TextField descriptionField = new TextField();
		TextField weightField = new TextField();
		Label cpLabel = new Label("Performanz:");
		cpLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.add(cpLabel, 1, 0);
		pane.add(descriptionField, 1, 1);
		Label weLabel = new Label("Gewichtung:");
		weLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.add(weLabel, 1, 2);
		pane.add(weightField, 1, 3);
		
		Button addBtn = new Button("Einf\u00FCgen");
		addBtn.setOnAction(event -> addPerfCrit(stage, k, descriptionField, weightField));
		
		pane.add(addBtn, 1, 4);
		
		return pane;
	}
	
	public void deletePerfCrit(Stage stage, int k) {	
		TableViewSelectionModel<PerformanceCrit> tsm = tablePfCritList.get(k).getSelectionModel();
		if (tsm.isEmpty()) {
			//System.out.println("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			showInformationAlert("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}
		
		ObservableList<Integer> list = tsm.getSelectedIndices();
		Integer[] selectedIndices = new Integer[list.size()];
		selectedIndices = list.toArray(selectedIndices);

		Arrays.sort(selectedIndices);
		
		for(int i = selectedIndices.length - 1; i >= 0; i--) {
			tsm.clearSelection(selectedIndices[i].intValue());
			tablePfCritList.get(k).getItems().remove(selectedIndices[i].intValue());
			cpCritList.get(k).getPerfCritList().remove(selectedIndices[i].intValue());
			//System.out.println("vorher k " + k + " sel ind " + selectedIndices[i].intValue() + " actual_ci: " + actual_ci + " actual_pi: " + actual_pi);
			if(actual_ci == k && selectedIndices[i].intValue() <= actual_pi && actual_pi > 0) {
				actual_pi -= 1;
				if(ratingPerCompetenceStage.isShowing()) {
					editRatingPerCompetenceTable(stage, actual_ci, actual_pi);
				}
				
			}
			else if(actual_ci == k && selectedIndices[i].intValue() <= actual_pi && actual_ci > 0) {
				actual_ci -= 1;
				actual_pi = cpCritList.get(actual_ci).getPerfCritList().size() - 1;
				if(ratingPerCompetenceStage.isShowing()) {
					editRatingPerCompetenceTable(stage, actual_ci, actual_pi);
				}
			}
			else if(actual_ci == k && k == 0 && selectedIndices[i].intValue() == 0) {
				ratingPerCompetenceStage.close();
			}
			//System.out.println("nachher k " + k + " sel ind " + selectedIndices[i].intValue() + " actual_ci: " + actual_ci + " actual_pi: " + actual_pi);
		}
		if((resultStage.isShowing() || ratingPerStudentStage.isShowing()) && cpCritList.get(k).getPerfCritList().isEmpty()) {
			resultStage.close();
			ratingPerStudentStage.close();
			//showInformationAlert("F\u00FCr die Kompetenz " + cpCritList.get(k).getDescription() + 
			//	                 "\nwurden noch keine Performanz-Kriterien festgelegt.");
		}
		if(ratingPerStudentStage.isShowing() && !cpCritList.get(k).getPerfCritList().isEmpty()) {
			editRatingPerStudentTable(stage, actual_name_i);
		}
		if(resultStage.isShowing() && studList_imported && !cpCritList.get(k).getPerfCritList().isEmpty() && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				try {
					st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
				} catch (Exception e) {
					System.err.println("Fehler in deletePerfCrit bei Notenberechnung von " + name + " " + e.getMessage());
				}
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		}
	}
	
	private void createPerfTables(Stage stage) {
		
		if(cpCritList.isEmpty()) {
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
			if(perfStage.isShowing()) {
				perfStage.close();
			}
			createCompTable(stage);
			return;
		}
		
		tablePfCritList.clear();
		
		for(int k = 0; k < cpCritList.size(); k++) {
			
			TableColumn<PerformanceCrit, String> pfCritDcCol = cpCritList.get(k).getPfCritDcCol();
			TableColumn<PerformanceCrit, Double> pfCritWeCol = cpCritList.get(k).getPfCritWeCol();
			
			ObservableList<PerformanceCrit> observablePfCritList =
				FXCollections.<PerformanceCrit>observableArrayList(cpCritList.get(k).getPerfCritList());
			
			tablePfCritList.add(new TableView<PerformanceCrit>(observablePfCritList));	
			
			TableColumn<PerformanceCrit, String> nameCpCritDeCol = cpCritList.get(k).getCpCritDcCol();
			
			pfCritDcCol.setOnEditCommit((CellEditEvent<PerformanceCrit, String> t) -> {
				
				((PerformanceCrit) t.getTableView().getItems().get(
					t.getTablePosition().getRow())
					).setDescription(t.getNewValue());
				
				if(ratingPerStudentStage.isShowing()) {
					editRatingPerStudentTable(stage, actual_name_i);
				}
				if(ratingPerCompetenceStage.isShowing()) {
					editRatingPerCompetenceTable(stage, actual_ci, actual_pi);
				}
			});
			
			pfCritWeCol.setOnEditCommit( (CellEditEvent<PerformanceCrit, Double> t) -> {
				Double newVal = 0.0;
				Double oldVal = 0.0;
				try {
					newVal = t.getNewValue();
					int rowId = t.getTablePosition().getRow();
					oldVal = t.getOldValue();
					if(0.0 <= newVal) {
						((PerformanceCrit) t.getTableView().getItems().get(rowId)).setWeight(newVal);
					}
					else {
						showErrorAlert("Der eingef\u00FCgte Wert " + newVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
									   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");
						t.getTableView().refresh();
						((PerformanceCrit) t.getTableView().getItems().get(rowId)).setWeight(oldVal);
					}
				} catch (NumberFormatException e) {
					showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
				} catch (NullPointerException e) {
					showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
				}
				if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
					for (Student st: studList) {
						String vorname = st.getVorname();
						String nachname = st.getNachname();
						String kurs = st.getKurs();
						String name = nachname + " " + vorname + " " + kurs;
						try {
							st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in createPerfTables bei Notenberechnung von " + name + " " + e.getMessage());
						}
					}
					csvFilesSaved = false;
					pdfFilesSaved = false;
				}	
			});
			
			nameCpCritDeCol.getColumns().addAll(pfCritDcCol, pfCritWeCol);
			
			tablePfCritList.get(k).getColumns().addAll(nameCpCritDeCol);
			tablePfCritList.get(k).setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
			
			tablePfCritList.get(k).getSelectionModel().setCellSelectionEnabled(true);
			tablePfCritList.get(k).setEditable(true);
			
			final int k2 = k;
			tablePfCritList.get(k).setOnKeyPressed(event -> {
				TablePosition<PerformanceCrit, ?> pos = tablePfCritList.get(k2).getFocusModel().getFocusedCell() ;
				//if (pos != null && event.getCode().isLetterKey()) {
				if (pos != null) {
					tablePfCritList.get(k2).edit(pos.getRow(), pos.getTableColumn());
				}
			});
			TableViewSelectionModel<PerformanceCrit> tsm = tablePfCritList.get(k).getSelectionModel();
			tsm.setSelectionMode(SelectionMode.MULTIPLE);
			
			//tablePfCritList.get(k).setItems(observablePfCritList);
		}
		
		String styleStr = "-fx-font-size: 14px;" + 
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
		                  "-fx-border-width: 2;" +
		                  "-fx-border-insets: 5;" + 
		                  "-fx-border-radius: 5;" + 
		                  "-fx-border-color: blue;";
		
		AnchorPane root = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		int pfCritTableWidth = 250;
		int pfCritTableOffset = 0;
		
		int k = 0;
		for(k = 0; k < cpCritList.size(); k++) {
			VBox rootPfCritTable = new VBox(tablePfCritList.get(k));
			rootPfCritTable.setStyle(styleStr);
			rootPfCritTable.setPrefWidth(pfCritTableWidth);
			
			GridPane newPerfCritPane  = getNewPerfCritPane(stage, k);
			gridPane.add(newPerfCritPane, k, 0);
			
			rootPfCritTable.setLayoutX(0);
			rootPfCritTable.setLayoutY(0);
			gridPane.add(rootPfCritTable, k, 1);
			
			Button deleteBtn = new Button("Zeilen l\u00F6schen");
			final int k2 = k; 
			deleteBtn.setOnAction(event -> deletePerfCrit(stage, k2));  
			gridPane.add(deleteBtn, k, 2);
		}
		
		Button nextBtn = new Button();
		nextBtn.setText("Weiter");
		nextBtn.setOnAction(event -> createInputTables(stage));
		
		gridPane.add(nextBtn, k, 4);
		
		Button backBtn = new Button();
		backBtn.setText("Zur\u00FCck");
	    backBtn.setOnAction(event -> createCompTable(stage));
		
		gridPane.add(backBtn, 0, 4);
		
		root.getChildren().add(gridPane);
		int pWidth = pfCritTableWidth * cpCritList.size() + 120;
		root.setPrefWidth(pWidth);
		root.setPrefHeight(600);
		
		root.setStyle(styleAnchorPaneStr);
		
		AnchorPane.setLeftAnchor(gridPane, 10.);
		AnchorPane.setTopAnchor(gridPane, 10.);
		
		ScrollPane sp = new ScrollPane(root);
		Scene scene = new Scene(sp, 1000, 750);
		
		perfStage.setScene(scene);
		perfStage.setTitle("Performanztabellen - KOBS");
		
		if(this.lastOpenPerfStage == null) {
			perfStage.setX(100);
			perfStage.setY(50);
		}
		else {
			perfStage.setX(this.lastOpenPerfStage.getX());
			perfStage.setY(this.lastOpenPerfStage.getY());
		}
		perfStage.setWidth(pWidth + 20);
		perfStage.setHeight(770);
		perfStage.show();
		perfStage.toFront();
		this.lastOpenPerfStage = perfStage;
	}
	
	private void createInputTables(Stage stage) {
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane pane = new GridPane();
		pane.setHgap(50);
		pane.setVgap(10);
		Label infoLabel = new Label("\n\u00DCber KOBS");
		infoLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(infoLabel, 0, 0);
		
		Button infoBtn = new Button();
		infoBtn.setText("\u00DCber");
		infoBtn.setOnAction(event -> about());
		pane.add(infoBtn, 0, 1);
		
		Label dirLabel = new Label("Ordner f\u00FCr Dateien ausw\u00E4hlen");
		dirLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(dirLabel, 0, 2);
		
		Button dirBtn = new Button();
		dirBtn.setText("Ordner w\u00E4hlen");
		dirBtn.setOnAction(event -> selectDir(stage));
		pane.add(dirBtn, 0, 3);
		
		Label loadPGMapLabel = new Label("\nProzent-Noten-Zuordnung");
		loadPGMapLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(loadPGMapLabel, 0, 4);
		
		Button loadPGMapBtn = new Button();
		loadPGMapBtn.setText("Weiter");
		loadPGMapBtn.setOnAction(event -> createPercentGradeMappingTable(stage));
		pane.add(loadPGMapBtn, 0, 5);
		
		Label loadMapLabel = new Label("\nKompetenz-Bewertungsskala");
		loadMapLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(loadMapLabel, 0, 6);
		
		Button loadMapBtn = new Button();
		loadMapBtn.setText("Weiter");
		loadMapBtn.setOnAction(event -> createRatingMappingTable(stage));
		pane.add(loadMapBtn, 0, 7);
		
		Label loadLabel = new Label("\u00D6ffnen einer Kursliste als CSV-Datei");
		loadLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(loadLabel, 0, 8);
		
		Button loadBtn = new Button();
		loadBtn.setText("Datei \u00F6ffnen");
		loadBtn.setOnAction(event -> readStudData(stage));
		pane.add(loadBtn, 0, 9);
		
		Label compLabel = new Label("Zeige Kompetenztabelle");
		compLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(compLabel, 0, 10);
		
		Button compBtn = new Button();
		compBtn.setText("Zeige");
		compBtn.setOnAction(event -> createCompTable(stage));
		pane.add(compBtn, 0, 11);
		
		Label perfLabel = new Label("Zeige Performanztabellen");
		perfLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(perfLabel, 0, 12);
		
		Button perfBtn = new Button();
		perfBtn.setText("Zeige");
		perfBtn.setOnAction(event -> createPerfTables(stage));
		pane.add(perfBtn, 0, 13);
		
		Label loadRatingLabel = new Label("\u00D6ffnen einer Bewertungstabelle\nals CSV-Datei");
		loadRatingLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(loadRatingLabel, 1, 0);
		
		Button loadRatingBtn = new Button();
		loadRatingBtn.setText("Datei \u00F6ffnen");
		loadRatingBtn.setOnAction(event -> readRatingData(stage));
		pane.add(loadRatingBtn, 1, 1);
		
		Label ratLabel = new Label("\nBewertung pro Kompetenz");
		ratLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(ratLabel, 1, 4);
		Button nextBtn = new Button();
		nextBtn.setText("Weiter");
		int ci = 0;
		int pi = 0;
		nextBtn.setOnAction(event -> editRatingPerCompetenceTable(stage, ci, pi));
		pane.add(nextBtn, 1, 5);
		
		Label ratLabel2 = new Label("\nBewertung pro Person");
		ratLabel2.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(ratLabel2, 1, 6);
		
		Button nextBtn2 = new Button();
		nextBtn2.setText("Weiter");
		int ni = 0;
		nextBtn2.setOnAction(event -> editRatingPerStudentTable(stage, ni));
		pane.add(nextBtn2, 1, 7);
		
		Label calcLabel = new Label("Berechne Noten");
		calcLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(calcLabel, 1, 12);
		
		Button calcBtn = new Button();
		calcBtn.setText("Berechne Noten");
		calcBtn.setOnAction(event -> calcGrades(stage));
		pane.add(calcBtn, 1, 13);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(pane);
		anchorPane.setPrefWidth(640);
		anchorPane.setPrefHeight(670);
		AnchorPane.setLeftAnchor(pane, 30.);
		AnchorPane.setTopAnchor(pane, 30.);
		
		Scene scene = new Scene(anchorPane, 620, 670);
		
		mainStage.setScene(scene);
		mainStage.setTitle("KOBS");
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		
		if(this.lastOpenMainStage == null) {
			mainStage.setX(20);
			mainStage.setY(80);
		}
		else {
			mainStage.setX(this.lastOpenMainStage.getX());
			mainStage.setY(this.lastOpenMainStage.getY());
		}
		mainStage.setWidth(640);
		mainStage.setHeight(670);
		mainStage.show();
		mainStage.toFront();
		this.lastOpenMainStage = mainStage;
	}
	
	private void about() {
		
		Text kobsVersionText = new Text(kobsVersionStr);
		kobsVersionText.setFont(javafx.scene.text.Font.font("Helvetica", FontWeight.BOLD, 24));
		
		kobsVersionText.setTextOrigin(VPos.TOP);
		DescriptionTab descTab = new DescriptionTab("Info");
		AuthorTab authorTab = new AuthorTab("Autoren");
		LicenseTab licenseTab = new LicenseTab("Lizenz");
		DocumentationTab documentationTab = new DocumentationTab("Dokumentation");

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		tabPane.getTabs().addAll(descTab, authorTab, licenseTab, documentationTab);
		//tabPane.getTabs().addAll(descTab, authorTab, licenseTab);

		HBox hBox = new HBox(tabPane);	
		hBox.setStyle("-fx-padding: 10;");
		
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
		gridPane.add(kobsVersionText, 0, 0);
		
		gridPane.add(hBox, 0, 1);
		
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setStyle(styleAnchorPaneStr);
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(770);
		anchorPane.setPrefHeight(650);
		
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		ScrollPane sp = new ScrollPane(anchorPane);
		//System.out.println("Vvalue: " + sp.getVvalue());
		sp.setVvalue(-50.0);
		Scene scene = new Scene(sp, 770, 650);
		
		infoStage.setScene(scene);
		infoStage.setTitle("\u00DCber - KOBS");
		infoStage.show();
		infoStage.toFront();
	}
	
	private void readInitDir() {
		
		FileReader fileReader = null;
		BufferedReader in = null;
		try {
			fileReader = new FileReader(initialDirPath_str);
			in = new BufferedReader(fileReader);
			String line;
			if((line = in.readLine()) != null) {
				initialDir = line;
			}		
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		try {
			if (in != null) {
				in.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}		
	}
	
	private void selectDir(Stage stage) {
		try {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("W\u00E4hle einen Ordner aus");
			
			readInitDir();
			if(initialDir != null && !initialDir.equals("")) {
				directoryChooser.setInitialDirectory(new File(initialDir));
			}
			File selectedDirectory = directoryChooser.showDialog(stage);
			if(selectedDirectory != null) {
				initialDir = selectedDirectory.getAbsolutePath();
			}
			//System.out.println("Ordner: " + initialDir);
		} catch (IllegalArgumentException | NullPointerException ex) {
			System.err.println(ex); 
		}
		
		if (initialDir != null) {
			FileWriter fileWriter = null;
			BufferedWriter out = null;
			try {
				fileWriter = new FileWriter(initialDirPath_str);
				out = new BufferedWriter(fileWriter); 
				//System.out.println("in Ordner: " + initialDir);
				out.write(initialDir);
				out.newLine();
				initialDirSelected = true;
			} catch (FileNotFoundException ex) {
				System.err.println(ex);
			} catch (IOException ex) {
				System.err.println(ex);
			}
			try {
				if (out != null) {
					out.close();
				}
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
	
	private String getInitRating() {
		String initRating = "";
		if(!ratingMappingList.isEmpty()) {
			
			ratingMappingList_sorted = new ArrayList<RatingMapping>(ratingMappingList);
			
			Comparator<RatingMapping> byValue = Comparator.comparing(RatingMapping::getValue);
			Collections.sort(ratingMappingList_sorted, byValue);
			
			int initIndex = (int)(ratingMappingList_sorted.size())/2;
			initRating = ratingMappingList_sorted.get(initIndex).getDescription();
		}
		else {
			if(resultStage.isShowing()) {
				resultStage.close();
			}
		}
		return initRating;
	}
	
	public void addRatMap(Stage stage, TextField descriptionField, TextField valueField) {
		
		if(cpCritList.isEmpty()) {
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
			createCompTable(stage);
			return;
		}
		for (CompetenceCrit cC: cpCritList) {
			if(cC.getPerfCritList().isEmpty()) {
				showInformationAlert("F\u00FCr die Kompetenz " + cC.getDescription() + 
				               "\nwurden noch keine Performanz-Kriterien festgelegt.");
				createPerfTables(stage);
				return;
			}
		}
		String desc = replaceToUniCode(descriptionField.getText());
		String value_str = valueField.getText().replace(',','.');
		try {
			double value = Double.parseDouble(value_str);
			if(desc.length() == 0) {
				showWarningAlert("Beschreibung des Werts fehlt.");
			}
			RatingMapping rM = new RatingMapping(desc, value);
			String newRM = rM.getDescription();
			double newRMVal = rM.getValue();
			int indexOfRating = -1;
			for(RatingMapping rm: ratingMappingList) {
				String r_str = rm.getDescription();
				if(newRM.equals(r_str)) {	
					showWarningAlert("Die eingef\u00FCgte Bewertung " + newRM + " gibt es schon in der Bewertungsskala.");
				}
			}
			if(0.0 <= newRMVal && newRMVal <= 1.0) {
				tableRatingMapping.getItems().add(rM);
				ratingMappingList.add(rM);
			} 
			else {
				showErrorAlert("Der eingef\u00FCgte Wert " + newRMVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
				               "Bitte nur Werte von 0 bis 1 eingeben.");
			}
			clearFields(descriptionField, valueField);
			
		} catch (NumberFormatException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			valueField.setText(null);
		} catch (NullPointerException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			valueField.setText(null);
		} 
		
		if(!ratingMappingList.isEmpty() && studList_imported && performanceOfStud_initialized == false) {  	
			String initRating = this.getInitRating();
			for (CompetenceCrit cC: cpCritList) {
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					pC.clearPerfOfStudList();
					for (Student st: studList) {
						pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
					}
				}
			}
			performanceOfStud_initialized = true;
		}
	}

	public GridPane getNewRatMapPane(Stage stage) {
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		
		Label loadLabel = new Label("Einlesen einer Bewertungsskala\naus einer CSV-Datei:");
		loadLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		Button loadBtn = new Button();
		loadBtn.setText("Datei \u00F6ffnen");
		loadBtn.setOnAction(event -> readRatingMappingData(stage));
		
		pane.add(loadLabel, 0, 0);
		pane.add(loadBtn, 1, 0);
		
		TextField descriptionField = new TextField();
		TextField valueField = new TextField();
		Label descLabel = new Label("Einf\u00FCgen einer Bezeichnung:");
		descLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(2, descLabel, descriptionField);
		
		Label valueLabel = new Label("Einf\u00FCgen eines Werts:");
		valueLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(3, valueLabel, valueField);
		
		Button addBtn = new Button("Einf\u00FCgen");
		addBtn.setOnAction(event -> addRatMap(stage, descriptionField, valueField));
		
		pane.add(addBtn, 1, 4);
		
		return pane;
	}
	
	public void deleteRatMap(Stage stage) {	
		
		TableViewSelectionModel<RatingMapping> tsm = tableRatingMapping.getSelectionModel();
		
		if (tsm.isEmpty()) {
			showInformationAlert("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}
		
		ObservableList<Integer> list = tsm.getSelectedIndices();
		Integer[] selectedIndices = new Integer[list.size()];
		selectedIndices = list.toArray(selectedIndices);

		Arrays.sort(selectedIndices);
		
		for(int i = selectedIndices.length - 1; i >= 0; i--) {
			tsm.clearSelection(selectedIndices[i].intValue());
			
			String delete_rating = ((RatingMapping) tableRatingMapping.getItems().get(selectedIndices[i].intValue())).getDescription();
			
			tableRatingMapping.getItems().remove(selectedIndices[i].intValue());
			
			//System.out.println("delete_rating: " + delete_rating);
			
			int indexOfRating = -1;
			for(int ri = 0; ri < ratingMappingList.size(); ri++) {
				String r_str = ratingMappingList.get(ri).getDescription();
				if(delete_rating.equals(r_str)) {	
					indexOfRating = ri;
					//System.out.println("L\u00F6sche " + ri + " " + r_str + " von Bewertungsskala");
					break;
				}
			}
			ratingMappingList.remove(indexOfRating);
			
			if(performanceOfStud_initialized) {
				String initRating = this.getInitRating();
				
				showInformationAlert(delete_rating + " wurde aus der Bewertungsskala gel\u00F6scht.\n" +
									 "In den Bewertungstabellen wird die Bewertung " + delete_rating +
									 "\ndurch den initialen Wert " + initRating + " ersetzt.");
			
				for (int si = 0; si < studList.size(); si++) {
					String vorname = studList.get(si).getVorname();
					String nachname = studList.get(si).getNachname();
					//System.out.println(vorname + " " + nachname);
					
					for (CompetenceCrit cC: cpCritList) {
						for (PerformanceCrit pC: cC.getPerfCritList()) {
							if(!pC.getPerfOfStudList().isEmpty()) {
								if(delete_rating.equals(pC.getPerfOfStudList().get(si).getRating())) {
									
									pC.getPerfOfStudList().get(si).setRating(initRating); 
									//System.out.println("Comp: " + cC.getDescription() + " Perf: " + 
									//					pC.getDescription() + " setze initiales Rating: " + initRating );
								}
							}		
						}
					}
					if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty()) {
						try {
							studList.get(si).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in deleteRatMap bei Notenberechnung von " + vorname + " " + nachname + "\n" + e.getMessage());
						}
					}					
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}
		}
		if(ratingMappingList.isEmpty()) {
			if(ratingPerCompetenceStage.isShowing()) {
				ratingPerCompetenceStage.close();
			}	
			if(ratingPerStudentStage.isShowing()) {
				ratingPerStudentStage.close();
			}
			if(resultStage.isShowing()) {
				resultStage.close();
			}	
			for (CompetenceCrit cC: cpCritList) {
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					pC.clearPerfOfStudList();
					pC.setIinitRatingFlag(false);
				}
			}
			performanceOfStud_initialized = false;
		}
	}
	
	private void createRatingMappingTable(Stage stage) {
		
		tableRatingMapping.getColumns().clear();
		
		ObservableList<RatingMapping> observableRatingMappingList =
			FXCollections.<RatingMapping>observableArrayList(ratingMappingList);
		
		tableRatingMapping.getSelectionModel().setCellSelectionEnabled(true);
		
		tableRatingMapping.setEditable(true);
		
		TableViewSelectionModel<RatingMapping> tableRatingMappingSelMod = tableRatingMapping.getSelectionModel();
		tableRatingMappingSelMod.setSelectionMode(SelectionMode.MULTIPLE);
		
		TableColumn<RatingMapping, String> descCol = new TableColumn<>("Bezeichnung");
		descCol.setMinWidth(100);
		TableColumn<RatingMapping, Double> valueCol = new TableColumn<>("Wert");
		valueCol.setMinWidth(100);
		
		String descriptStr = "description";
		String valueStr = "value";
		
		descCol.setId(descriptStr);
		valueCol.setId(valueStr);
		
		PropertyValueFactory<RatingMapping, String> descCellValueFactory =
			new PropertyValueFactory<>(descriptStr);
			
		descCol.setCellValueFactory(descCellValueFactory);
		
		descCol.setCellFactory(TextFieldTableCell.<RatingMapping>forTableColumn());
		
		descCol.setOnEditCommit(event -> {
		    TableColumn.CellEditEvent evn = 
				(TableColumn.CellEditEvent<RatingMapping, String>) event;
			int rowId = evn.getTablePosition().getRow();
			int colId = evn.getTablePosition().getColumn();
			String oldVal = evn.getOldValue().toString();
			//System.out.println("In Zeile : " + rowId + " steht der alte Wert " + oldVal);
			
			String delete_rating = oldVal;
			
			String newVal = evn.getNewValue().toString();
			//System.out.println("Erhalte in Zeile : " + rowId + " den neuen Wert " + newVal);
			
			int indexOfRating = -1;
			for(RatingMapping rm: ratingMappingList) {
				String r_str = rm.getDescription();
				if(newVal.equals(r_str)) {	
					showWarningAlert("Die ge\u00E4nderte Bewertung " + newVal + " gibt es schon in der Bewertungsskala.");
				}
			}
			
			((RatingMapping) evn.getTableView().getItems().get(rowId)).setDescription(newVal);
			
			if(performanceOfStud_initialized) {
				String initRating = this.getInitRating();
				
				showInformationAlert(delete_rating + " wurde in der Bewertungsskala ersetzt.\n" +
									 "In den Bewertungstabellen wird die Bewertung " + delete_rating +
									 "\ndurch den initialen Wert " + initRating + " ersetzt.");
			
				for (int si = 0; si < studList.size(); si++) {
					String vorname = studList.get(si).getVorname();
					String nachname = studList.get(si).getNachname();
					//System.out.println(vorname + " " + nachname);
					
					for (CompetenceCrit cC: cpCritList) {
						for (PerformanceCrit pC: cC.getPerfCritList()) {
							if(!pC.getPerfOfStudList().isEmpty()) {
								if(delete_rating.equals(pC.getPerfOfStudList().get(si).getRating())) {
									
									pC.getPerfOfStudList().get(si).setRating(initRating); 
									//System.out.println("Comp: " + cC.getDescription() + " Perf: " + 
									//					pC.getDescription() + " setze initiales Rating: " + initRating );
								}
							}		
						}
					}
					if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty()) {
						try {
							studList.get(si).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in createRatingMappingTable bei Notenberechnung von " + vorname + " " + nachname + "\n" + e.getMessage());
						}
					}					
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}
		});
		
		PropertyValueFactory<RatingMapping, Double> valueCellValueFactory =
			new PropertyValueFactory<>(valueStr);
			
		valueCol.setCellValueFactory(valueCellValueFactory);
		
		//valueCol.setCellFactory(TextFieldTableCell.<RatingMapping, Double>forTableColumn(new DoubleStringConverter()));
		valueCol.setCellFactory(TextFieldTableCell.<RatingMapping, Double>forTableColumn(new DoubleStringConverter() {
		   
			@Override
			public String toString(Double value) {
				return value == null ? "" : value.toString();
			}
			@Override
			public Double fromString(String value) {
				return (value == null) ? null : Double.parseDouble(value.replace(',','.'));
			}
		}));
		
		valueCol.setOnEditCommit((CellEditEvent<RatingMapping, Double> t) -> {
            
			try {
				Double newVal = t.getNewValue();
				int rowId = t.getTablePosition().getRow();
				//int colId = t.getTablePosition().getColumn();
				Double oldVal = t.getOldValue();
				if(0.0 <= newVal && newVal <= 1.0) {
					((RatingMapping) t.getTableView().getItems().get(rowId)).setValue(newVal);
				} 
				else {
					showErrorAlert("Der eingef\u00FCgte Wert " + newVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
								   "Bitte nur Werte von 0 bis 1 eingeben.");
					t.getTableView().refresh();
					((RatingMapping) t.getTableView().getItems().get(rowId)).setValue(oldVal);
				}
			} catch (NumberFormatException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			} catch (NullPointerException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);			  
			} 	
			if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
				for (Student st: studList) {
					String vorname = st.getVorname();
					String nachname = st.getNachname();
					String kurs = st.getKurs();
					String name = nachname + " " + vorname + " " + kurs;
					try {
						st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.err.println("Fehler in createRatingMappingTable bei Notenberechnung von " + name + " " + e.getMessage());
					}
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}	
        });
		
		tableRatingMapping.getColumns().addAll(descCol, valueCol);
		tableRatingMapping.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		tableRatingMapping.setItems(observableRatingMappingList);
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
		                  "-fx-border-width: 2;" +
		                  "-fx-border-insets: 5;" + 
		                  "-fx-border-radius: 5;" + 
		                  "-fx-border-color: blue;";
						 
		VBox rootRatMapTable = new VBox(tableRatingMapping);
		rootRatMapTable.setStyle(styleStr);
		rootRatMapTable.setPrefWidth(100);
		rootRatMapTable.setPrefHeight(300);
		rootRatMapTable.setLayoutX(0);
		rootRatMapTable.setLayoutY(0);
		
		GridPane newRatMapPane  = getNewRatMapPane(stage);
		gridPane.add(newRatMapPane, 0, 0);
		
		gridPane.add(rootRatMapTable, 0, 1);
		
		Button deleteBtn = new Button("Zeilen l\u00F6schen");
		deleteBtn.setOnAction(event -> deleteRatMap(stage));  
		gridPane.add(deleteBtn, 0, 2);
		
		GridPane.setHgrow(rootRatMapTable, Priority.ALWAYS);
		GridPane.setVgrow(rootRatMapTable, Priority.ALWAYS);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(450);
		anchorPane.setPrefHeight(580);
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		Scene scene = new Scene(anchorPane, 450, 580);
		ratMapStage.setScene(scene);
		ratMapStage.setTitle("Kompetenz-Bewertungsskala - KOBS");
		
		if(this.lastOpenRatMapStage == null) {
			ratMapStage.setX(630);
			ratMapStage.setY(20);
		}
		else {
			ratMapStage.setX(this.lastOpenRatMapStage.getX());
			ratMapStage.setY(this.lastOpenRatMapStage.getY());
		}
		ratMapStage.setWidth(470);
		ratMapStage.setHeight(600);
		ratMapStage.show();
		ratMapStage.toFront();
		this.lastOpenRatMapStage = ratMapStage;
	}
	
	public void addPerGraMap(Stage stage, TextField descriptionField, TextField loLimField, TextField upLimField) {
		
		String desc = replaceToUniCode(descriptionField.getText());
		String loLim_str = loLimField.getText().replace(',','.');
		String upLim_str = upLimField.getText().replace(',','.');
		try {
			double loLim = Double.parseDouble(loLim_str);
			double upLim = Double.parseDouble(upLim_str);
			if(desc.length() == 0) {
				showWarningAlert("Note fehlt.");
			}
			PercentGradeMapping pgM = new PercentGradeMapping(desc, loLim, upLim);
			String newPGM = pgM.getDescription();
			double newPGLoLim = pgM.getLowerLimit();
			double newPGUpLim = pgM.getUpperLimit();
			int indexOfRating = -1;
			for(PercentGradeMapping rm: percentGradeMappingList) {
				String r_str = rm.getDescription();
				if(newPGM.equals(r_str)) {	
					showWarningAlert("Die eingef\u00FCgte Note " + newPGM + " gibt es schon in der Prozent-Noten-Zuordnung.");
				}
			}
			if(0.0 <= newPGLoLim && newPGLoLim <= 100.0 && 0.0 <= newPGUpLim && newPGUpLim <= 100.0) {
				if (!(newPGLoLim > newPGUpLim)) { 
					tablePercentGradeMapping.getItems().add(pgM);
					percentGradeMappingList.add(pgM);
					if(performanceOfStud_initialized) {
						for (int si = 0; si < studList.size(); si++) {
							String vorname = studList.get(si).getVorname();
							String nachname = studList.get(si).getNachname();
							if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty()) {
								try {
									studList.get(si).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
								} catch (Exception e) {
									System.err.println("Fehler in addPerGraMap bei Notenberechnung von " + vorname + " " + nachname + "\n" + e.getMessage());
								}
							}					
						}
						csvFilesSaved = false;
						pdfFilesSaved = false;
					}
				}
				else {
					showErrorAlert("Die untere Grenze ist gr\u00F6\u00DFer als die obere Grenze.");
				}
			} 
			else {
				showErrorAlert("Die eingef\u00FCgten Werte liegen ausserhalb des erlaubten Bereichs.\n" +
				               "Bitte nur Werte von 0 bis 100 eingeben.");
			}
			descriptionField.setText(null);
			loLimField.setText(null);
			upLimField.setText(null);
		} catch (NumberFormatException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			loLimField.setText(null);
			upLimField.setText(null);
		} catch (NullPointerException e) {
			showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			loLimField.setText(null);
			upLimField.setText(null);
		} 
	}

	public GridPane getNewPerGraMapPane(Stage stage) {
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		
		Label loadLabel = new Label("Einlesen einer Prozent-Noten-Zuordnung\naus einer CSV-Datei:");
		loadLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		Button loadBtn = new Button();
		loadBtn.setText("Datei \u00F6ffnen");
		loadBtn.setOnAction(event -> readPercentGradeMappingData(stage));
		
		pane.add(loadLabel, 0, 0);
		pane.add(loadBtn, 1, 0);
		
		TextField descriptionField = new TextField();
		TextField loLimField = new TextField();
		TextField upLimField = new TextField();
		Label descLabel = new Label("Einf\u00FCgen einer Note:");
		descLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(2, descLabel, descriptionField);
		
		Label valueLabel = new Label("Einf\u00FCgen der unteren\nund oberen Grenze in \u0025:");
		valueLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 14px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: black");
		
		pane.addRow(3, valueLabel, loLimField, upLimField);
		
		Button addBtn = new Button("Einf\u00FCgen");
		addBtn.setOnAction(event -> addPerGraMap(stage, descriptionField, loLimField, upLimField));
		
		pane.add(addBtn, 1, 4);
		
		return pane;
	}
	
	public void deletePerGraMap(Stage stage) {	
		
		TableViewSelectionModel<PercentGradeMapping> tsm = tablePercentGradeMapping.getSelectionModel();
		
		if (tsm.isEmpty()) {
			showInformationAlert("Zeile zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}
		
		ObservableList<Integer> list = tsm.getSelectedIndices();
		Integer[] selectedIndices = new Integer[list.size()];
		selectedIndices = list.toArray(selectedIndices);

		Arrays.sort(selectedIndices);
		
		for(int i = selectedIndices.length - 1; i >= 0; i--) {
			tsm.clearSelection(selectedIndices[i].intValue());
			
			String delete_rating = ((PercentGradeMapping) tablePercentGradeMapping.getItems().get(selectedIndices[i].intValue())).getDescription();
			
			tablePercentGradeMapping.getItems().remove(selectedIndices[i].intValue());
			
			//System.out.println("delete_rating: " + delete_rating);
			
			int indexOfRating = -1;
			for(int ri = 0; ri < percentGradeMappingList.size(); ri++) {
				String r_str = percentGradeMappingList.get(ri).getDescription();
				if(delete_rating.equals(r_str)) {	
					indexOfRating = ri;
					//System.out.println("L\u00F6sche " + ri + " " + r_str + " von Bewertungsskala");
					break;
				}
			}
			percentGradeMappingList.remove(indexOfRating);
			
			if(performanceOfStud_initialized) {
				for (int si = 0; si < studList.size(); si++) {
					String vorname = studList.get(si).getVorname();
					String nachname = studList.get(si).getNachname();
					//System.out.println(vorname + " " + nachname);
					
					if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty()) {
						try {
							studList.get(si).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in deletePerGraMap bei Notenberechnung von " + vorname + " " + nachname + "\n" + e.getMessage());
						}
					}					
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}
		}
		if(percentGradeMappingList.isEmpty()) {
			if(resultStage.isShowing()) {
				resultStage.close();
			}	
		}
	}
	
	private void createPercentGradeMappingTable(Stage stage) {
		
		tablePercentGradeMapping.getColumns().clear();
		
		ObservableList<PercentGradeMapping> observablePercentGradeMappingList =
			FXCollections.<PercentGradeMapping>observableArrayList(percentGradeMappingList);
		
		tablePercentGradeMapping.getSelectionModel().setCellSelectionEnabled(true);
		
		tablePercentGradeMapping.setEditable(true);
		
		TableViewSelectionModel<PercentGradeMapping> tablePercentGradeMappingSelMod = tablePercentGradeMapping.getSelectionModel();
		tablePercentGradeMappingSelMod.setSelectionMode(SelectionMode.MULTIPLE);
		
		TableColumn<PercentGradeMapping, Double> loLimCol = new TableColumn<>("Untere Grenze in \u0025");
		loLimCol.setMinWidth(100);
		TableColumn<PercentGradeMapping, Double> upLimCol = new TableColumn<>("Obere Grenze in \u0025");
		upLimCol.setMinWidth(100);
		TableColumn<PercentGradeMapping, String> descCol = new TableColumn<>("Note");
		descCol.setMinWidth(100);	
		String descriptStr = "description";
		String loLimStr = "lowerLimit";
		String upLimStr = "upperLimit";
		
		descCol.setId(descriptStr);
		loLimCol.setId(loLimStr);
		upLimCol.setId(upLimStr);
		
		PropertyValueFactory<PercentGradeMapping, String> descCellValueFactory =
			new PropertyValueFactory<>(descriptStr);
			
		descCol.setCellValueFactory(descCellValueFactory);
		
		descCol.setCellFactory(TextFieldTableCell.<PercentGradeMapping>forTableColumn());
		
		descCol.setOnEditCommit(event -> {
		    TableColumn.CellEditEvent evn = 
				(TableColumn.CellEditEvent<PercentGradeMapping, String>) event;
			int rowId = evn.getTablePosition().getRow();
			int colId = evn.getTablePosition().getColumn();
			String oldVal = evn.getOldValue().toString();
			//System.out.println("In Zeile : " + rowId + " steht der alte Wert " + oldVal);
			
			String newVal = evn.getNewValue().toString();
			//System.out.println("Erhalte in Zeile : " + rowId + " den neuen Wert " + newVal);
			
			int indexOfRating = -1;
			for(PercentGradeMapping rm: percentGradeMappingList) {
				String r_str = rm.getDescription();
				if(newVal.equals(r_str)) {	
					showWarningAlert("Die ge\u00E4nderte Note " + newVal + " gibt es schon in der Prozent Noten Zuordnung.");
				}
			}
			
			((PercentGradeMapping) evn.getTableView().getItems().get(rowId)).setDescription(newVal);
			
			if(performanceOfStud_initialized) {
				
				showInformationAlert(oldVal + " wurde in der Prozent Noten Zuordnung durch " + newVal + " ersetzt.");
			
				for (int si = 0; si < studList.size(); si++) {
					String vorname = studList.get(si).getVorname();
					String nachname = studList.get(si).getNachname();
					//System.out.println(vorname + " " + nachname);
					
					if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty()) {
						try {
							studList.get(si).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
						} catch (Exception e) {
							System.err.println("Fehler in createPercentGradeMappingTable bei Notenberechnung von " + vorname + " " + nachname + "\n" + e.getMessage());
						}
					}					
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}
		});
		
		PropertyValueFactory<PercentGradeMapping, Double> loLimCellValueFactory =
			new PropertyValueFactory<>(loLimStr);
			
		loLimCol.setCellValueFactory(loLimCellValueFactory);
		
		//valueCol.setCellFactory(TextFieldTableCell.<RatingMapping, Double>forTableColumn(new DoubleStringConverter()));
		loLimCol.setCellFactory(TextFieldTableCell.<PercentGradeMapping, Double>forTableColumn(new DoubleStringConverter() {
		   
			@Override
			public String toString(Double value) {
				return value == null ? "" : value.toString();
			}
			@Override
			public Double fromString(String value) {
				return (value == null) ? null : Double.parseDouble(value.replace(',','.'));
			}
		}));
		
		loLimCol.setOnEditCommit((CellEditEvent<PercentGradeMapping, Double> t) -> {
            
			try {
				Double newVal = t.getNewValue();
				int rowId = t.getTablePosition().getRow();
				//int colId = t.getTablePosition().getColumn();
				Double oldVal = t.getOldValue();
				if(0.0 <= newVal && newVal <= 100.0) {
					((PercentGradeMapping) t.getTableView().getItems().get(rowId)).setLowerLimit(newVal);
				} 
				else {
					showErrorAlert("Der eingef\u00FCgte Wert " + newVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
								   "Bitte nur Werte von 0 bis 100 eingeben.");
					t.getTableView().refresh();
					((PercentGradeMapping) t.getTableView().getItems().get(rowId)).setLowerLimit(oldVal);
				}
			} catch (NumberFormatException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			} catch (NullPointerException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);			  
			} 	
			if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
				for (Student st: studList) {
					String vorname = st.getVorname();
					String nachname = st.getNachname();
					String kurs = st.getKurs();
					String name = nachname + " " + vorname + " " + kurs;
					try {
						st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.err.println("Fehler in createPercentGradeMappingTable bei Notenberechnung von " + name + " " + e.getMessage());
					}
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}	
        });
		
		PropertyValueFactory<PercentGradeMapping, Double> upLimCellValueFactory =
			new PropertyValueFactory<>(upLimStr);
			
		upLimCol.setCellValueFactory(upLimCellValueFactory);
		
		//valueCol.setCellFactory(TextFieldTableCell.<RatingMapping, Double>forTableColumn(new DoubleStringConverter()));
		upLimCol.setCellFactory(TextFieldTableCell.<PercentGradeMapping, Double>forTableColumn(new DoubleStringConverter() {
		   
			@Override
			public String toString(Double value) {
				return value == null ? "" : value.toString();
			}
			@Override
			public Double fromString(String value) {
				return (value == null) ? null : Double.parseDouble(value.replace(',','.'));
			}
		}));
		
		upLimCol.setOnEditCommit((CellEditEvent<PercentGradeMapping, Double> t) -> {
            
			try {
				Double newVal = t.getNewValue();
				int rowId = t.getTablePosition().getRow();
				//int colId = t.getTablePosition().getColumn();
				Double oldVal = t.getOldValue();
				if(0.0 <= newVal && newVal <= 100.0) {
					((PercentGradeMapping) t.getTableView().getItems().get(rowId)).setUpperLimit(newVal);
				} 
				else {
					showErrorAlert("Der eingef\u00FCgte Wert " + newVal + " liegt ausserhalb des erlaubten Bereichs.\n" +
								   "Bitte nur Werte von 0 bis 100 eingeben.");
					t.getTableView().refresh();
					((PercentGradeMapping) t.getTableView().getItems().get(rowId)).setUpperLimit(oldVal);
				}
			} catch (NumberFormatException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);
			} catch (NullPointerException e) {
				showErrorAlert("Keine Dezimalbruchzahl f\u00FCr den Wert eingegeben:\n" + e);			  
			} 	
			if(studList_imported && !ratingMappingList.isEmpty() && !percentGradeMappingList.isEmpty() && performanceOfStud_initialized) {
				for (Student st: studList) {
					String vorname = st.getVorname();
					String nachname = st.getNachname();
					String kurs = st.getKurs();
					String name = nachname + " " + vorname + " " + kurs;
					try {
						st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.err.println("Fehler in createPercentGradeMappingTable bei Notenberechnung von " + name + " " + e.getMessage());
					}
				}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}	
        });
		
		tablePercentGradeMapping.getColumns().addAll(loLimCol, upLimCol, descCol);
		tablePercentGradeMapping.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		tablePercentGradeMapping.setItems(observablePercentGradeMappingList);
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
		                  "-fx-border-width: 2;" +
		                  "-fx-border-insets: 5;" + 
		                  "-fx-border-radius: 5;" + 
		                  "-fx-border-color: blue;";
						 
		VBox rootPercentGradeMapTable = new VBox(tablePercentGradeMapping);
		rootPercentGradeMapTable.setStyle(styleStr);
		rootPercentGradeMapTable.setPrefWidth(100);
		rootPercentGradeMapTable.setPrefHeight(300);
		rootPercentGradeMapTable.setLayoutX(0);
		rootPercentGradeMapTable.setLayoutY(0);
		
		GridPane newPerGraMapPane  = getNewPerGraMapPane(stage);
		gridPane.add(newPerGraMapPane, 0, 0);
		
		gridPane.add(rootPercentGradeMapTable, 0, 1);
		
		Button deleteBtn = new Button("Zeilen l\u00F6schen");
		deleteBtn.setOnAction(event -> deletePerGraMap(stage));  
		gridPane.add(deleteBtn, 0, 2);
		
		GridPane.setHgrow(rootPercentGradeMapTable, Priority.ALWAYS);
		GridPane.setVgrow(rootPercentGradeMapTable, Priority.ALWAYS);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(650);
		anchorPane.setPrefHeight(600);
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		Scene scene = new Scene(anchorPane, 650, 600);
		percentGradeMapStage.setScene(scene);
		percentGradeMapStage.setTitle("Prozent-Noten-Zuordnung - KOBS");
		
		if(this.lastOpenPercentGradeMapStage == null) {
			percentGradeMapStage.setX(630);
			percentGradeMapStage.setY(20);
		}
		else {
			percentGradeMapStage.setX(this.lastOpenPercentGradeMapStage.getX());
			percentGradeMapStage.setY(this.lastOpenPercentGradeMapStage.getY());
		}
		percentGradeMapStage.setWidth(650);
		percentGradeMapStage.setHeight(620);
		percentGradeMapStage.show();
		percentGradeMapStage.toFront();
		this.lastOpenPercentGradeMapStage = percentGradeMapStage;
	}
	
	private void readStudData(Stage stage) {
		
		if(ratingPerCompetenceStage.isShowing()) {
			ratingPerCompetenceStage.close();
		}	
		if(ratingPerStudentStage.isShowing()) {
			ratingPerStudentStage.close();
		}
		if(resultStage.isShowing()) { 
			resultStage.close();
		}
		if(cpCritList.isEmpty()) {
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
			createCompTable(stage);
			return;
		}
		if(ratingMappingList.isEmpty()) {
			showInformationAlert("Es wurde noch keine Kompetenz-Bewertungsskala festgelegt.");
			createRatingMappingTable(stage);
			return;
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		if(initialDir != null && !initialDir.equals("")) {
			fileChooser.setInitialDirectory(new File(initialDir));
		}
		File studFile = fileChooser.showOpenDialog(stage);
		if (studFile == null) {
			showWarningAlert("Keine Datei ausgew\u00E4hlt oder Datei nicht lesbar");  
			return;
		}
		studFile_str = studFile.getName();
		studAbsoluteFile_str = studFile.getAbsolutePath();
		//System.out.println("Kurslistendatei: " + studFile_str);
		
		this.prefix_outputFiles = studFile_str.substring(0, studFile_str.indexOf("csv")-1);
		//System.out.println(prefix_outputFiles);
		
		try {
			Scanner inp_stud = new Scanner(studFile,"UTF-8");
			
			String name = "";
			
			String row = inp_stud.nextLine();
			
			studList.clear();
			boolean all_rows_valid = true;
			int nr_rows = 0;
			while(inp_stud.hasNext()) { 			
				row = inp_stud.nextLine();
				int idx1 = row.indexOf(";");
				int idx2 = row.indexOf(";", idx1 + 1);
				int idx3 = row.indexOf(";", idx2 + 1);
				//System.out.println(row);
				String s[] = row.split(";");
				if(idx1 != -1 && idx2 != -1 && idx3 != -1 && s.length > 3) {
					int number = Integer.parseInt(s[0]);
					String vorname = s[1];
					String nachname = s[2];
					String kurs = s[3];
					name = nachname + " " + vorname + " " + kurs;
					studList.add(new Student(vorname, nachname, kurs));
				}
				else {
					System.out.println("Die Person\n" + row + "\nwurde nicht importiert.");
					showWarningAlert("Die Person\n" + row + "\nwurde nicht importiert.");
					all_rows_valid = false;
				}
				nr_rows++;
			}
			inp_stud.close();
			if(all_rows_valid && studList.size() == nr_rows) {
				showInformationAlert("Die Kursliste " + studFile_str + "\nwurde vollst\u00E4ndig importiert.");
				studList_imported = true;
			}
			else if(all_rows_valid == false && studList.size() > 0) {
				showWarningAlert("Die Kursliste " + studFile_str + "\nwurde nur unvollst\u00E4ndig importiert.");
				studList_imported = true;
			}
			else {
				showErrorAlert("Die Kursliste " + studFile_str + "\nwurde nicht importiert.");
				studList_imported = false;
			}
			/*
			System.out.println("Liste der Studenten:");
			Iterator<Student> st_it = studList.iterator();
			while (st_it.hasNext()) {
				Student st = st_it.next();
				System.out.printf("%s %s\n", st.getVorname(), st.getNachname());
			}
			*/
			//System.out.println("Einf\u00FCgen der initialen Bewertung:");
			String initRating = this.getInitRating();
			//System.out.println(initRating);
			showInformationAlert("F\u00FCr alle Personen wird in den Bewertungstabellen\n" + 
			                     "als initiale Kompetenz-Bewertung " + initRating + " eingetragen.");
			for (CompetenceCrit cC: cpCritList) {
				//System.out.printf("   %s ", cC.getDescription());
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					//System.out.printf("   %s ", pC.getDescription());
					pC.clearPerfOfStudList();
					for (Student st: studList) {
						pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
					}
				}
				//System.out.printf("\n");
			}
			performanceOfStud_initialized = true;
			csvFilesSaved = false;
			pdfFilesSaved = false;
			
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			studList_imported = false;
			showErrorAlert("Die Kursliste " + studFile_str + " konnte nicht eingelesen werden\n" + ex);
		} catch (NumberFormatException ex) {
			System.err.println(ex);
			studList_imported = false;
			showErrorAlert("Die Kursliste " + studFile_str + " konnte nicht eingelesen werden\n" + ex);
		}	
		
		return;
	}

	private void readRatingMappingData(Stage stage) {
		
		ratingMappingList.clear();
		tableRatingMapping.getItems().clear();
		
		if(ratingPerCompetenceStage.isShowing()) {
			ratingPerCompetenceStage.close();
		}	
		if(ratingPerStudentStage.isShowing()) {
			ratingPerStudentStage.close();
		}
		if(resultStage.isShowing()) {
			resultStage.close();
		}	
		for (CompetenceCrit cC: cpCritList) {
			for (PerformanceCrit pC: cC.getPerfCritList()) {
				pC.clearPerfOfStudList();
				pC.setIinitRatingFlag(false);
			}
		}
		performanceOfStud_initialized = false;
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		
		readInitDir();
		if(initialDir != null && !initialDir.equals("")) {
			fileChooser.setInitialDirectory(new File(initialDir));
		}
		File mapFile = fileChooser.showOpenDialog(stage);
		if (mapFile == null) {
			showWarningAlert("Keine Datei ausgew\u00E4hlt oder Datei nicht lesbar");  
			return;
		}
		String mapFile_str = mapFile.getName();
		//System.out.println("Datei der Bewertungsskala: " + mapFile_str);
		
		try {
			Scanner inp_map = new Scanner(mapFile,"UTF-8");
			
			String row = inp_map.nextLine();
			
			boolean all_rows_valid = true;
			int nr_rows = 0;
			while(inp_map.hasNext()) { 			
				row = inp_map.nextLine();
				int idx1 = row.indexOf(";");
				//System.out.println(row);
				String s[] = row.split(";");
				
				if(idx1 != -1 && s.length > 1) {
					String c = replaceToUniCode(s[0]);
					String v_str = s[1].replace(',','.');
					//System.out.println(row);
					double v = Double.parseDouble(v_str);
					if( 0.0 <= v && v <= 1.0 ) {
						//System.out.println("c: " + c + " v: " + v);
						RatingMapping rM = new RatingMapping(c, v);
						tableRatingMapping.getItems().add(rM);
						ratingMappingList.add(rM);
					}
					else {
						System.out.println("Skalenwert " + row + " wurde nicht importiert.");
						showWarningAlert("Skalenwert " + row + " wurde nicht importiert.");
						all_rows_valid = false;
					}
				}
				else {
					System.out.println("Skalenwert " + row + " wurde nicht importiert.");
					showWarningAlert("Skalenwert " + row + " wurde nicht importiert.");
					all_rows_valid = false;
				}
				nr_rows++;
			}
			inp_map.close();
			if(all_rows_valid && ratingMappingList.size() == nr_rows) {
				showInformationAlert("Die Kompetenz-Bewertungsskala " + mapFile_str + "\nwurde vollst\u00E4ndig importiert.");
				ratingMappingList_imported = true;
				
				if(!studList.isEmpty()) {
					String initRating = this.getInitRating();
					showInformationAlert("F\u00FCr alle Personen wird in den Bewertungstabellen\n" + 
										 "als initiale Kompetenz-Bewertung " + initRating + " eingetragen.");
					for (CompetenceCrit cC: cpCritList) {
						for (PerformanceCrit pC: cC.getPerfCritList()) {
							pC.clearPerfOfStudList();
							for (Student st: studList) {
								pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
							}
						}
					}
					performanceOfStud_initialized = true;
				}
			}
			else if(all_rows_valid == false && ratingMappingList.size() > 0) {
				showWarningAlert("Die Kompetenz-Bewertungsskala " + mapFile_str + "\nwurde nur unvollst\u00E4ndig importiert.");
				ratingMappingList_imported = true;
				if(!studList.isEmpty()) {
					String initRating = this.getInitRating();
					showInformationAlert("F\u00FCr alle Personen wird in den Bewertungstabellen\n" + 
										 "als initiale Kompetenz-Bewertung " + initRating + " eingetragen.");
					for (CompetenceCrit cC: cpCritList) {
						for (PerformanceCrit pC: cC.getPerfCritList()) {
							pC.clearPerfOfStudList();
							for (Student st: studList) {
								pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
							}
						}
					}
					performanceOfStud_initialized = true;
				}
			}
			else {
				showErrorAlert("Die Kompetenz-Bewertungsskala " + mapFile_str + "\nwurde nicht importiert.");
				ratingMappingList_imported = false;
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			ratingMappingList_imported = false;
			showErrorAlert("Die Kompetenz-Bewertungsskala " + mapFile_str + " konnte nicht eingelesen werden\n" + ex);
		} catch (NumberFormatException ex) {
			System.err.println(ex);
			ratingMappingList_imported = false;
			showErrorAlert("Die Kompetenz-Bewertungsskala " + mapFile_str + " konnte nicht eingelesen werden\n" + ex);
		}	
		return;
	}
	
	private void readPercentGradeMappingData(Stage stage) {
		
		percentGradeMappingList.clear();
		tablePercentGradeMapping.getItems().clear();
		
		if(resultStage.isShowing()) {
			resultStage.close();
		}	
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		
		readInitDir();
		if(initialDir != null && !initialDir.equals("")) {
			fileChooser.setInitialDirectory(new File(initialDir));
		}
		File mapFile = fileChooser.showOpenDialog(stage);
		if (mapFile == null) {
			showWarningAlert("Keine Datei ausgew\u00E4hlt oder Datei nicht lesbar");  
			return;
		}
		String mapFile_str = mapFile.getName();
		//System.out.println("Datei der Prozent-Note-Zuordnung: " + mapFile_str);
		
		try {
			Scanner inp_map = new Scanner(mapFile,"UTF-8");
			
			String row = inp_map.nextLine();
			
			boolean all_rows_valid = true;
			int nr_rows = 0;
			while(inp_map.hasNext()) { 			
				row = inp_map.nextLine();
				int idx1 = row.indexOf(";");
				int idx2 = row.indexOf(";", idx1 + 1);
				//System.out.println(row);
				String s[] = row.split(";");
				
				if(idx1 != -1 && idx2 != -1 && s.length > 2) {
					String loLim_str = s[0].replace(',','.');
					String upLim_str = s[1].replace(',','.');
					String n = replaceToUniCode(s[2]);
					//System.out.println(row);
					double loLim = Double.parseDouble(loLim_str);
					double upLim = Double.parseDouble(upLim_str);
					if( 0.0 <= loLim && loLim <= 100.0 && 0.0 <= upLim && upLim <= 100.0 && loLim <= upLim ) {
						//System.out.println("loLim: " + loLim + " upLim: " + upLim + " Note: " + n);
						PercentGradeMapping pgM = new PercentGradeMapping(n, loLim, upLim);
						tablePercentGradeMapping.getItems().add(pgM);
						percentGradeMappingList.add(pgM);
					}
					else {
						System.out.println("Prozent-Note-Zuordnung " + row + " wurde nicht importiert.");
						showWarningAlert("Prozent-Note-Zuordnung " + row + " wurde nicht importiert.");
						all_rows_valid = false;
					}
				}
				else {
					System.out.println("Prozent-Note-Zuordnung " + row + " wurde nicht importiert.");
					showWarningAlert("Prozent-Note-Zuordnung " + row + " wurde nicht importiert.");
					all_rows_valid = false;
				}
				nr_rows++;
			}
			inp_map.close();
			if(all_rows_valid && percentGradeMappingList.size() == nr_rows) {
				showInformationAlert("Die Prozent-Note-Zuordnung " + mapFile_str + "\nwurde vollst\u00E4ndig importiert.");
				percentGradeMappingList_imported = true;
			}
			else {
				showErrorAlert("Die Prozent-Note-Zuordnung " + mapFile_str + "\nwurde nicht importiert.");
				percentGradeMappingList_imported = false;
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			percentGradeMappingList_imported = false;
			showErrorAlert("Die Prozent-Note-Zuordnung " + mapFile_str + " konnte nicht eingelesen werden\n" + ex);
		} catch (NumberFormatException ex) {
			System.err.println(ex);
			percentGradeMappingList_imported = false;
			showErrorAlert("Die Prozent-Note-Zuordnung " + mapFile_str + " konnte nicht eingelesen werden\n" + ex);
		}	
		return;
	}
	
	private boolean checkPriorSteps(Stage stage) {
		
		boolean allPriorStepsDone = true;
		if(cpCritList.isEmpty()) {
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
			createCompTable(stage);
			allPriorStepsDone = false;
			return allPriorStepsDone;
		}
		for (CompetenceCrit cC: cpCritList) {
			if(cC.getPerfCritList().isEmpty()) {
				showInformationAlert("F\u00FCr die Kompetenz " + cC.getDescription() + 
				               "\nwurden noch keine Performanz-Kriterien festgelegt.");
				createPerfTables(stage);
				allPriorStepsDone = false;
				return allPriorStepsDone;
			}
		}
		if(ratingMappingList.isEmpty()) {
			showInformationAlert("Es wurde noch keine Kompetenz-Bewertungsskala festgelegt.");
			createRatingMappingTable(stage);
			allPriorStepsDone = false;
			return allPriorStepsDone;
		}
		if(studList_imported == false) {
			showInformationAlert("Es wurde noch keine Kursliste eingelesen.");
			allPriorStepsDone = false;
			return allPriorStepsDone;
		}
		return allPriorStepsDone;
	}	
	
	private void readRatingData(Stage stage) {
		
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false ) {
			return;
		}
		int ratingListSize = 0;
		for (CompetenceCrit cC: cpCritList) {
			ratingListSize += cC.getPerfCritList().size();
		}
		//System.out.println("ratingListSize: " + ratingListSize);
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		if(initialDir != null && !initialDir.equals("")) {
			fileChooser.setInitialDirectory(new File(initialDir));
		}
		File ratingFile = fileChooser.showOpenDialog(stage);
		if (ratingFile == null) {
			showWarningAlert("Keine Datei ausgew\u00E4hlt oder Datei nicht lesbar");  
			return;
		}
		String ratingFile_str = ratingFile.getName();
		//System.out.println("Datei der Kompetenzbewertungstabelle " + ratingFile_str + "\nwird eingelesen.");
		int nr_rows = 0;
		try {
			Scanner inp_rating = new Scanner(ratingFile,"UTF-8");
			boolean all_rows_valid = true;
			
			String row = inp_rating.nextLine();
			String comp_s[] = row.split(";");
			int nrOfComp = 0;
			for (int si=0; si < comp_s.length; si++) {
				String comp = comp_s[si];
				if (!comp.equals("")) {
					nrOfComp++;
					int indexOfComp = -1;
					for(int ci = 0; ci < cpCritList.size(); ci++) {
						String c_str = cpCritList.get(ci).getDescription();
						//System.out.println(ci + " " + c_str + " Kompetenz "  + comp);
						if(comp.equals(c_str)) {	
							indexOfComp = ci;
							//System.out.println(ci + " " + c_str + " Kompetenz "  + comp);
							break;
						}
					}
					if(indexOfComp == -1) {
						all_rows_valid = false;
						showWarningAlert("Fehler beim Lesen der Bewertungen:\nKompetenz " + comp + " nicht in Kompetenz-Liste gefunden");
						System.out.println("Fehler beim Lesen der Bewertungen: Kompetenz " + comp + " nicht in Kompetenz-Liste gefunden");
					}
				}
			}	
			if(nrOfComp < cpCritList.size()) {
				all_rows_valid = false;
				showWarningAlert("Fehler beim Lesen der Bewertungen:\nNur " + nrOfComp + " statt " + cpCritList.size() + " Kompetenzen gefunden");
				System.out.println("Fehler beim Lesen der Bewertungen: Nur " + nrOfComp + " statt " + cpCritList.size() + " Kompetenzen gefunden");
			}
			
			row = inp_rating.nextLine();
			String perf_s[] = row.split(";");
			int nrOfPerf = 0;
			for (int si=3; si < perf_s.length; si++) {
				String perf = perf_s[si];
				if (!perf.equals("")) {
					nrOfPerf++;
					int indexOfPerf = -1;
					for (CompetenceCrit cC: cpCritList) {
						for(int pi = 0; pi < cC.getPerfCritList().size(); pi++) {
							String p_str = cC.getPerfCritList().get(pi).getDescription();
							//System.out.println(pi + " " + p_str + " Performanz "  + perf);
							if(perf.equals(p_str)) {	
								indexOfPerf = pi;
								//System.out.println(pi + " " + p_str + " Performanz "  + perf);
								break;
							}
						}
					}
					if(indexOfPerf == -1) {
						all_rows_valid = false;
						showWarningAlert("Fehler beim Lesen der Bewertungen:\nPerformanz " + perf + " nicht in Performanz-Liste gefunden");
						System.out.println("Fehler beim Lesen der Bewertungen: Performanz " + perf + " nicht in Performanz-Liste gefunden");
					}
				}
			}
			int nrOfAllPerf = 0;
			for (CompetenceCrit cC: cpCritList) {
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					nrOfAllPerf++;
				}
			}
			if(nrOfPerf < nrOfAllPerf) {
				all_rows_valid = false;
				showWarningAlert("Fehler beim Lesen der Bewertungen:\nNur " + nrOfPerf + " statt " + nrOfAllPerf + " Performanzen gefunden");
				System.out.println("Fehler beim Lesen der Bewertungen: Nur " + nrOfPerf + " statt " + nrOfAllPerf + " Performanzen gefunden");
			}
			
			boolean ratingDescriptionFlag = true;
			
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				row = inp_rating.nextLine();
				//System.out.println(row);
				String s[] = row.split(";");
				String inp_vorname = s[0];
				String inp_nachname = s[1];
				String inp_kurs = s[2];
				String inp_name = inp_nachname + " " + inp_vorname + " " + inp_kurs;
				
				if (!inp_name.equals(name) || ((s.length - 3) != ratingListSize)) {
					System.out.println("Bewertung der Person " + name + " wurde nicht importiert.");
					showWarningAlert("Bewertung der Person\n" + name + "\nwurde nicht importiert.");
					all_rows_valid = false;
				}
				else {
					int inp_i = 3; 
					boolean ratingFlag = true;
					for (CompetenceCrit cC: cpCritList) {
						for (PerformanceCrit pC: cC.getPerfCritList()) {
							int indexOfName = -1;
							for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
								String n = pC.getPerfOfStudList().get(ni).getName();
								//System.out.println(ni + " " + n);
								if(name.equals(n)) {
									indexOfName = ni;
									break;
								}
							}
							if (indexOfName != -1) {
								if (inp_i < s.length) { 
									String r = s[inp_i];
									
									int indexOfRating = -1;
									for(int ri = 0; ri < ratingMappingList.size(); ri++) {
										String r_str = ratingMappingList.get(ri).getDescription();
										//System.out.println(ri + " " + r_str + " rating "  + r);
										if(r.equals(r_str)) {	
											indexOfRating = ri;
											break;
										}
									}
									if(indexOfRating == -1) {
										//showWarningAlert("Fehler beim Lesen der Bewertungen:\n Bezeichnung " + r + " nicht in Liste gefunden");
										System.out.println("Fehler beim Lesen der Bewertungen: Bezeichnung " + r + " nicht in Liste gefunden");
										pC.getPerfOfStudList().get(indexOfName).setRating(getInitRating());
										//showWarningAlert("Bewertung der Person\n" + name + "\nwurde nicht importiert.");
										ratingDescriptionFlag = false;
										all_rows_valid = false;
									}	
									else {
										//System.out.println("indexOfName : " + indexOfName);
										pC.getPerfOfStudList().get(indexOfName).setRating(r);
									}
								}
								else {
									System.out.println("Bewertung der Person " + name + " wurde nicht importiert.");
									showWarningAlert("Bewertung der Person\n" + name + "\nwurde nicht importiert.");
									ratingFlag = false;
									break;
								}
								inp_i++;
							}
							else {
								System.out.println("Name der Person " + name + " nicht in Kursliste der Performanzen gefunden.");
								//showWarningAlert("Name der Person\n" + name + "\n nicht in Kursliste der Performanzen gefunden.");
								ratingFlag = false;
								break;
							}
						}
						if (!ratingDescriptionFlag) {
							showWarningAlert("Andere Bewertungsbezeichnungen bei Person\n" + name + "\nals in der festgelegten Bewertungsskala");
							break;
						}
						if (!ratingFlag) {
							break;
						}
					}
					try {
						st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.err.println("Fehler in readRatingData bei Notenberechnung von " + name + " " + e.getMessage());
						if(resultStage.isShowing()) {
							resultStage.close();
						}
					}
					nr_rows++;
				}
			}
			inp_rating.close();
			if(all_rows_valid && studList.size() == nr_rows && ratingDescriptionFlag) {
				showInformationAlert("Die Bewertungstabelle wurde vollst\u00E4ndig importiert.");
			}
			else if(all_rows_valid == false && nr_rows > 0) {
				showWarningAlert("Die Bewertungstabelle wurde nur unvollst\u00E4ndig importiert.");
			}
			else {
				showErrorAlert("Die Bewertungstabelle wurde nicht importiert.");
			}
			if (!ratingDescriptionFlag) {
				showWarningAlert("Die Bewertungstabelle enth\u00E4lt andere Bezeichnungen\nals die Bewertungsskala.");	
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			showErrorAlert("Die Bewertungstabelle konnte nicht eingelesen werden\n" + ex);
		} catch (NumberFormatException ex) {
			System.err.println(ex);
			showErrorAlert("Die Bewertungstabelle konnte nicht eingelesen werden\n" + ex);
		}	
		
		return;
	}
	
	private void editRatingPerCompetenceTable(Stage stage, int ci, int pi) {
		
		//System.out.println("ci : " + ci + " pi: " + pi);
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false && !ratingPerCompetenceStage.isShowing()) {
			return;
		}
		if(cpCritList.isEmpty()) {
			showInformationAlert("Es wurden noch keine Kompetenz-Kriterien festgelegt");
			return;
		}
		for (CompetenceCrit cC: cpCritList) {
			if(cC.getPerfCritList().isEmpty()) {
				showInformationAlert("F\u00FCr die Kompetenz " + cC.getDescription() + 
				               "\nwurden noch keine Performanz-Kriterien festgelegt.");
				return;
			}
		}
		
		if(ci >= cpCritList.size()) {
			ci = cpCritList.size() - 1;
		}
		
		if(pi >= cpCritList.get(ci).getPerfCritList().size()) {
			pi = cpCritList.get(ci).getPerfCritList().size() - 1;
		}	
		actual_ci = ci;
		actual_pi = pi;
		
		// Setze initialen Wert der Bewertung für das Performanzkriterium falls noch nicht gesetzt
		if(cpCritList.get(ci).getPerfCritList().get(pi).getIinitRatingFlag() == false) {
			
			String initRating = this.getInitRating();
			
			for (Student st: studList) {
				cpCritList.get(ci).getPerfCritList().get(pi).addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
				cpCritList.get(ci).getPerfCritList().get(pi).setIinitRatingFlag(true);
			}
		}
		
		
		ObservableList<PerformanceOfStudent> observablePfOfStudList =
			FXCollections.<PerformanceOfStudent>observableArrayList(cpCritList.get(ci).getPerfCritList().get(pi).getPerfOfStudList());			
		
		TableView<PerformanceOfStudent> tablePerformanceOfStudent = new TableView<PerformanceOfStudent>();
		
		TableColumn<PerformanceCrit, String> pfCritDcCol =
			new TableColumn<>(cpCritList.get(ci).getPerfCritList().get(pi).getDescription());
		
		TableColumn<PerformanceOfStudent, String> pfOfStudVnCol = cpCritList.get(ci).getPerfCritList().get(pi).getPfOfStudVnCol();
		TableColumn<PerformanceOfStudent, String> pfOfStudNnCol = cpCritList.get(ci).getPerfCritList().get(pi).getPfOfStudNnCol();
		TableColumn<PerformanceOfStudent, String> pfOfStudKsCol = cpCritList.get(ci).getPerfCritList().get(pi).getPfOfStudKsCol();
		TableColumn<PerformanceOfStudent, String> pfOfStudRtCol = cpCritList.get(ci).getPerfCritList().get(pi).getPfOfStudRtCol();
		
		final int f_ci = ci;
		final int f_pi = pi;
		
		pfOfStudRtCol.setOnEditCommit(event -> {
		    TableColumn.CellEditEvent evn = 
				(TableColumn.CellEditEvent<PerformanceOfStudent, String>) event;
			int rowId = evn.getTablePosition().getRow();
			int colId = evn.getTablePosition().getColumn();
			String oldVal = evn.getOldValue().toString();
			//System.out.println("In Zeile : " + rowId + " steht der Wert " + oldVal);
			//String newVal = evn.getNewValue().toString().toUpperCase();
			String newVal = evn.getNewValue().toString();
			//System.out.println("Erhalte in Zeile : " + rowId + " den Wert " + newVal);
			String selectedVorname = ((PerformanceOfStudent) evn.getTableView().getItems().get(evn.getTablePosition().getRow()))
									.getVorname();
			String selectedNachname = ((PerformanceOfStudent) evn.getTableView().getItems().get(evn.getTablePosition().getRow()))
									.getNachname();	
			String selectedKurs = ((PerformanceOfStudent) evn.getTableView().getItems().get(evn.getTablePosition().getRow()))
									.getKurs();										
			//System.out.println("In Spalte : " + colId + " In Zeile : " + rowId + " " + 
			//                    selectedVorname + " " + selectedNachname + " steht der Wert " + oldVal);
            
			String name = selectedNachname + " " + selectedVorname + " " + selectedKurs;
			
			int indexOfRating = -1;
			for(int ri = 0; ri < ratingMappingList.size(); ri++) {
				String r_str = ratingMappingList.get(ri).getDescription();
				//System.out.println(ri + " " + r_str + " rating "  + rating);
				if(newVal.equals(r_str)) {	
					indexOfRating = ri;
					break;
				}
			}
			
			if(indexOfRating != -1) {
				
				((PerformanceOfStudent) evn.getTableView().getItems().get(rowId)).setRating(newVal);  
				
				int indexOfName = -1;
				for(int ni = 0; ni < cpCritList.get(f_ci).getPerfCritList().get(f_pi).getPerfOfStudList().size(); ni++) {
					String n = cpCritList.get(f_ci).getPerfCritList().get(f_pi).getPerfOfStudList().get(ni).getName();
					if(name.equals(n)) {
						//System.out.println("name " + name + " " + ni + " " + n);
						indexOfName = ni;
						break;
					}
				}
				cpCritList.get(f_ci).getPerfCritList().get(f_pi).getPerfOfStudList().get(indexOfName).setRating(newVal);
				
				int indexOfNameOfStudList = -1;
				for(int ni = 0; ni < studList.size(); ni++) {
					String n = studList.get(ni).getName();
					if(name.equals(n)) {	
						indexOfNameOfStudList = ni;
						break;
					}
				}
				
				try {
					studList.get(indexOfNameOfStudList).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
				} catch (Exception e) {
					System.err.println("Fehler in editRatingPerCompetenceTable bei Notenberechnung von " + name + " " + e.getMessage());
				}
				
				//if(ratingPerStudentStage.isShowing() && actual_name_i == indexOfNameOfStudList) {
				//	editRatingPerStudentTable(stage, actual_name_i);
				//}
				csvFilesSaved = false;
				pdfFilesSaved = false;
			}
			else {
				showInformationAlert(newVal + " ist keine g\u00FCltige Bezeichnung der Kompetenz-Bewertungsskala");
				evn.getTableView().refresh();
				((PerformanceOfStudent) evn.getTableView().getItems().get(rowId)).setRating(oldVal);
			}
        });
		
		tablePerformanceOfStudent.getColumns().addAll(pfOfStudVnCol, pfOfStudNnCol, pfOfStudKsCol, pfOfStudRtCol);
		
		tablePerformanceOfStudent.setEditable(true);
		
		tablePerformanceOfStudent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
	
		tablePerformanceOfStudent.setOnKeyPressed(event -> {
			TablePosition<PerformanceOfStudent, Double> pos = tablePerformanceOfStudent.getFocusModel().getFocusedCell() ;
			//if (pos != null && event.getCode().isLetterKey()) {
			if (pos != null) {
				tablePerformanceOfStudent.edit(pos.getRow(), pos.getTableColumn());
			}
		});
	
		tablePerformanceOfStudent.setItems(observablePfOfStudList);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
						  "-fx-border-width: 2;" +
						  "-fx-border-insets: 5;" + 
						  "-fx-border-radius: 5;" + 
						  "-fx-border-color: blue;";
	
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		int pfOfStudTableWidth = 400;
		int pfOfStudTableOffset = 0;
		
		Label sfLabel = new Label("Kurslistendatei: " + studAbsoluteFile_str);
		sfLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 13px;" +
						 "-fx-text-fill: black");
		gridPane.add(sfLabel, 0, 0, GridPane.REMAINING, 1);
		
		Label cpLabel = new Label(cpCritList.get(ci).getDescription());
		cpLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 16px;" +
						 "-fx-font-weight: bold;" +
						 "-fx-text-fill: blue");
		gridPane.add(cpLabel, 0, 1);
		
		Label pfLabel = new Label(cpCritList.get(ci).getPerfCritList().get(pi).getDescription());
		
		if(cpCritList.get(ci).getPerfCritList().get(pi).getWeight() >= 0.0) {
			pfLabel.setStyle("-fx-font-family: Helvetica;" +  
							 "-fx-font-size: 16px;" +
							 "-fx-font-weight: bold;" +
							 "-fx-text-fill: green");
		}
		else {
			pfLabel.setStyle("-fx-font-family: Helvetica;" +  
							 "-fx-font-size: 16px;" +
							 "-fx-font-weight: bold;" +
							 "-fx-text-fill: red");
		}
		gridPane.add(pfLabel, 0, 2);
		
		VBox rootPfOfStudTable = new VBox(tablePerformanceOfStudent);
		rootPfOfStudTable.setStyle(styleStr);
		rootPfOfStudTable.setPrefWidth(600);
		rootPfOfStudTable.setPrefHeight(400);
		rootPfOfStudTable.setLayoutX(0);
		rootPfOfStudTable.setLayoutY(0);
		
		gridPane.add(rootPfOfStudTable, 0, 3);
		
		boolean nextTableFlag = false;
		boolean backTableFlag = false;
		
		int nextci = 0;
		int nextpi = 0;
		
		if(pi == cpCritList.get(ci).getPerfCritList().size() - 1 && ci < cpCritList.size() - 1) {
			nextci = ci + 1;
			nextpi = 0;
			nextTableFlag = true;
		}
		else if (pi < cpCritList.get(ci).getPerfCritList().size() - 1) {
			nextci = ci;
			nextpi = pi + 1;
			nextTableFlag = true;
		}
		else {
			nextTableFlag = false;
		}
		
		int backci = 0;
		int backpi = 0;
		
		if(pi > 0 && ci >= 0) {
			backci = ci;
			backpi = pi - 1;
			backTableFlag = true;
		}
		else if (pi == 0 && ci > 0) {
			backci = ci - 1;
			backpi = cpCritList.get(backci).getPerfCritList().size() - 1;
			backTableFlag = true;
		}
		else {
			backTableFlag = false;
		}
		
		if (nextTableFlag) {
			final int f_nextci = nextci;
			final int f_nextpi = nextpi;
			Button nextBtn = new Button();
			nextBtn.setText("Weiter");
			nextBtn.setOnAction(event -> editRatingPerCompetenceTable(stage, f_nextci, f_nextpi));
			gridPane.add(nextBtn, 1, 4);
		}
		else {
			Button calcBtn = new Button();
			calcBtn.setText("Berechne Noten");
			calcBtn.setOnAction(event -> calcGrades(stage));
			gridPane.add(calcBtn, 1, 4);
		}
		
		if (backTableFlag) {
			final int f_backci = backci;
			final int f_backpi = backpi;
		    Button backBtn = new Button();
			backBtn.setText("Zur\u00FCck");
			backBtn.setOnAction(event -> editRatingPerCompetenceTable(stage, f_backci, f_backpi));
			gridPane.add(backBtn, 0, 4);
		}
		else {
			Button backBtn = new Button();
			backBtn.setText("Zur\u00FCck");
			backBtn.setOnAction(event -> createInputTables(stage));
			gridPane.add(backBtn, 0, 4);
		}
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(770);
		anchorPane.setPrefHeight(500);
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		ScrollPane sp = new ScrollPane(anchorPane);
		Scene scene = new Scene(sp);
		//Scene scene = new Scene(anchorPane);
		ratingPerCompetenceStage.setScene(scene);
		
		ratingPerCompetenceStage.setTitle("Bewertung pro Kompetenz - KOBS");
		
		if(this.lastOpenRatingPerCompetenceStage == null) {
			ratingPerCompetenceStage.setX(400);
			ratingPerCompetenceStage.setY(200);
		}
		else {
			ratingPerCompetenceStage.setX(this.lastOpenRatingPerCompetenceStage.getX());
			ratingPerCompetenceStage.setY(this.lastOpenRatingPerCompetenceStage.getY());
		}
		ratingPerCompetenceStage.setWidth(790);
		ratingPerCompetenceStage.setHeight(650);
		ratingPerCompetenceStage.show();
		ratingPerCompetenceStage.toFront();
		this.lastOpenRatingPerCompetenceStage = ratingPerCompetenceStage;
	}
	
	private void editRatingPerStudentTable(Stage stage, int name_i) {
		
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false) {
			return;
		}
		
		actual_name_i = name_i;
		
		String vorname = studList.get(name_i).getVorname();
		String nachname = studList.get(name_i).getNachname();
		String kurs = studList.get(name_i).getKurs();
		String name = nachname + " " + vorname + " " + kurs;
		
		for(int ci = 0; ci < cpCritList.size(); ci++) {
			for (PerformanceCrit pC: cpCritList.get(ci).getPerfCritList()) {
				// Setze initialen Wert der Bewertung für das Performanzkriterium falls noch nicht gesetzt
				if(pC.getIinitRatingFlag() == false) {
					String initRating = this.getInitRating();
					for (Student st: studList) {
						pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
						pC.setIinitRatingFlag(true);
					}
				}
			}
		}
		
		ratingPerStudentTableList.clear();
		
		for(int ci = 0; ci < cpCritList.size(); ci++) {
			
			ArrayList<RatingPerStudent> ratingPerStudentList = new ArrayList<RatingPerStudent>();
			
			for (PerformanceCrit pC: cpCritList.get(ci).getPerfCritList()) {
				int indexOfName = -1;
				for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
					String n = pC.getPerfOfStudList().get(ni).getName();
					//System.out.println(ni + " " + n);
					if(name.equals(n)) {
						indexOfName = ni;
						break;
					}
				}
				//String r = pC.getPerfOfStudList().get(indexOfName).getRating();
				StringProperty r = new SimpleStringProperty();
				r.bindBidirectional(pC.getPerfOfStudList().get(indexOfName).ratingProperty());
				ratingPerStudentList.add(new RatingPerStudent(pC.getDescription(), r));
			}
			
			ObservableList<RatingPerStudent> observableRatingPerStudentList = FXCollections.observableArrayList(ratingPerStudentList);
			
			ratingPerStudentTableList.add(new TableView<RatingPerStudent>(observableRatingPerStudentList));
			
			TableColumn<RatingPerStudent, String> nameCpCritDeCol = new TableColumn<>(cpCritList.get(ci).getDescription());
			
			TableColumn<RatingPerStudent, String> ratingPerStudentPfCol = new TableColumn<>("Performanz");	
			ratingPerStudentPfCol.setId("performance");
			ratingPerStudentPfCol.setMinWidth(60);
			ratingPerStudentPfCol.setCellValueFactory(new PropertyValueFactory<>("performance"));
			
			TableColumn<RatingPerStudent, String> ratingPerStudentRtCol = new TableColumn<>("Bewertung");	
			ratingPerStudentRtCol.setId("rating");
			ratingPerStudentRtCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
			
			ratingPerStudentRtCol.setCellFactory(TextFieldTableCell.<RatingPerStudent>forTableColumn());
			
			final int fci = ci;
			ratingPerStudentRtCol.setOnEditCommit(event -> {
				TableColumn.CellEditEvent evn = 
					(TableColumn.CellEditEvent<RatingPerStudent, String>) event;
				int rowId = evn.getTablePosition().getRow();
				int colId = evn.getTablePosition().getColumn();
				String oldVal = evn.getOldValue().toString();
				String selectedPerformance = ((RatingPerStudent) evn.getTableView().getItems().get(evn.getTablePosition().getRow()))
									.getPerformance();
				//System.out.println("In Spalte : " + colId + " In Zeile : " + rowId + " " + 
				//                    selectedPerformance + " steht der Wert " + oldVal);
				//String newVal = evn.getNewValue().toString().toUpperCase();
				String newVal = evn.getNewValue().toString();
				//System.out.println("Erhalte in Zeile : " + rowId + " den Wert " + newVal);
				
				int indexOfRating = -1;
				for(int ri = 0; ri < ratingMappingList.size(); ri++) {
					String r_str = ratingMappingList.get(ri).getDescription();
					//System.out.println(ri + " " + r_str + " rating "  + rating);
					if(newVal.equals(r_str)) {	
						indexOfRating = ri;
						break;
					}
				}
			
				if(indexOfRating != -1) {
				
					((RatingPerStudent) evn.getTableView().getItems().get(rowId)).setRating(newVal);
					
					int indexOfPerf = -1;
					for (int pi = 0; pi < cpCritList.get(fci).getPerfCritList().size(); pi++) {
						String p = cpCritList.get(fci).getPerfCritList().get(pi).getDescription();
						if(selectedPerformance.equals(p)) {
							//System.out.println("selectedPerformance " + selectedPerformance + " " + pi + " " + p);
							indexOfPerf = pi;
							break;
						}
					}							
					
					int indexOfName = -1;
					for(int ni = 0; ni < cpCritList.get(fci).getPerfCritList().get(indexOfPerf).getPerfOfStudList().size(); ni++) {
						String n = cpCritList.get(fci).getPerfCritList().get(rowId).getPerfOfStudList().get(ni).getName();
						if(name.equals(n)) {
							//System.out.println("name " + name + " " + ni + " " + n);
							indexOfName = ni;
							break;
						}
					}
					cpCritList.get(fci).getPerfCritList().get(indexOfPerf).getPerfOfStudList().get(indexOfName).setRating(newVal);
					evn.getTableView().getSelectionModel().selectBelowCell();
					
					int indexOfNameOfStudList = -1;
					for(int ni = 0; ni < studList.size(); ni++) {
						String n = studList.get(ni).getName();
						if(name.equals(n)) {	
							indexOfNameOfStudList = ni;
							break;
						}
					}
					try {
						studList.get(indexOfNameOfStudList).calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
					} catch (Exception e) {
						System.err.println("Fehler in editRatingPerStudentTable bei Notenberechnung von " + name + " " + e.getMessage());
					}
					csvFilesSaved = false;
					pdfFilesSaved = false;
				}
				else {
					showErrorAlert(newVal + " ist keine g\u00FCltige Bezeichnung der Kompetenz-Bewertungsskala");
					evn.getTableView().refresh();
					((RatingPerStudent) evn.getTableView().getItems().get(rowId)).setRating(oldVal);
				}
			});
			
			nameCpCritDeCol.getColumns().addAll(ratingPerStudentPfCol, ratingPerStudentRtCol);
			
			ratingPerStudentTableList.get(ci).getColumns().addAll(nameCpCritDeCol);
			
			ratingPerStudentTableList.get(ci).setEditable(true);
			
			ratingPerStudentTableList.get(ci).setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
			
			ratingPerStudentTableList.get(ci).setOnKeyPressed(event -> {
				TablePosition<RatingPerStudent, String> pos = ratingPerStudentTableList.get(fci).getFocusModel().getFocusedCell();
				//if (pos != null && event.getCode().isLetterKey()) {
				if (pos != null) {
					ratingPerStudentTableList.get(fci).edit(pos.getRow(), pos.getTableColumn());
				}
			});
		}
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		Label sfLabel = new Label("Kurslistendatei: " + studAbsoluteFile_str);
		sfLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 13px;" +
						 "-fx-text-fill: black");
		gridPane.add(sfLabel, 0, 0, GridPane.REMAINING, 1);
		
		Label nameLabel = new Label(vorname + " " + nachname + " \u002D " + kurs);
		nameLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 16px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		gridPane.add(nameLabel, 0, 1, GridPane.REMAINING, 1);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
		                  "-fx-border-width: 2;" +
		                  "-fx-border-insets: 5;" + 
		                  "-fx-border-radius: 5;" + 
		                  "-fx-border-color: blue;";
		
		int ratingPerStudentTableWidth = 250;
		
		int k = 0;
		for(k = 0; k < cpCritList.size(); k++) {
			ratingPerStudentTableList.get(k).setStyle(styleStr);
			ratingPerStudentTableList.get(k).setPrefWidth(ratingPerStudentTableWidth);
			ratingPerStudentTableList.get(k).setPrefHeight(400);
			gridPane.add(ratingPerStudentTableList.get(k), k, 2);
		}
		
		boolean nextTableFlag = false;
		boolean backTableFlag = false;
		
		int nextname_i = 0;
		
		if(name_i < studList.size() - 1) {
			nextname_i = name_i + 1;
			nextTableFlag = true;
		}
		else {
			nextTableFlag = false;
		}
		
		int backname_i = 0;
		
		if (name_i > 0) {
			backname_i = name_i - 1;
			backTableFlag = true;
		}
		else {
			backTableFlag = false;
		}
		
		if (nextTableFlag) {
			final int f_nextname_i = nextname_i;
			Button nextBtn = new Button();
			nextBtn.setText("Weiter");
			nextBtn.setOnAction(event -> editRatingPerStudentTable(stage, f_nextname_i));
			gridPane.add(nextBtn, k, 3);
		}
		else {
			Button calcBtn = new Button();
			calcBtn.setText("Berechne Noten");
			calcBtn.setOnAction(event -> calcGrades(stage));
			gridPane.add(calcBtn, k, 3);
		}
		
		if (backTableFlag) {
			final int f_backname_i = backname_i;
			Button backBtn = new Button();
			backBtn.setText("Zur\u00FCck");
			backBtn.setOnAction(event -> editRatingPerStudentTable(stage, f_backname_i));
			gridPane.add(backBtn, 0, 3);
		}
		else {
			Button backBtn = new Button();
			backBtn.setText("Zur\u00FCck");
			backBtn.setOnAction(event -> createInputTables(stage));
			gridPane.add(backBtn, 0, 3);
		}
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(gridPane);
		int allRepTabWidth = ratingPerStudentTableWidth * cpCritList.size();
		anchorPane.setPrefWidth(allRepTabWidth + 200);
		anchorPane.setPrefHeight(450);
		AnchorPane.setTopAnchor(gridPane, 12.);
		AnchorPane.setLeftAnchor(gridPane, 12.);
		AnchorPane.setRightAnchor(gridPane, 12.);
		AnchorPane.setBottomAnchor(gridPane, 12.);
		
		//Scene scene = new Scene(anchorPane, allRepTabWidth + 200, 470);
		ScrollPane sp = new ScrollPane(anchorPane);
		Scene scene = new Scene(sp, allRepTabWidth + 200, 470);
		ratingPerStudentStage.setScene(scene);
		ratingPerStudentStage.setTitle("Bewertung pro Person - KOBS");
		
		if(this.lastOpenRatingPerStudentStage == null) {
			ratingPerStudentStage.setX(50);
			ratingPerStudentStage.setY(150);
		}
		else {
			ratingPerStudentStage.setX(this.lastOpenRatingPerStudentStage.getX());
			ratingPerStudentStage.setY(this.lastOpenRatingPerStudentStage.getY());
		}
		ratingPerStudentStage.setWidth(allRepTabWidth + 180);
		ratingPerStudentStage.setHeight(590);
		ratingPerStudentStage.show();
		ratingPerStudentStage.toFront();
		this.lastOpenRatingPerStudentStage = ratingPerStudentStage;
	}
	
	private void calcGrades(Stage stage) {		
		if (percentGradeMappingList.isEmpty()) {
			showErrorAlert("Es existiert keine Prozent-Noten-Zuordnung.");
			return;
		}
		if(readyStage.isShowing()) {
			readyStage.close();
		}
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false) {
			return;
		}
		
		for (CompetenceCrit cC: cpCritList) {
			for (PerformanceCrit pC: cC.getPerfCritList()) {
				// Setze initialen Wert der Bewertung für das Performanzkriterium falls noch nicht gesetzt
				if(pC.getIinitRatingFlag() == false) {
					
					String initRating = this.getInitRating();
					for (Student st: studList) {
						pC.addPerfOfStud(st.getVorname(), st.getNachname(), st.getKurs(), initRating);
						pC.setIinitRatingFlag(true);
					}
				}
			}
		}
		
		for (Student st: studList) {
			String vorname = st.getVorname();
			String nachname = st.getNachname();
			String kurs = st.getKurs();
			String name = nachname + " " + vorname + " " + kurs;
			try {
				st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
			} catch (Exception e) {
				System.err.println("Fehler in calcGrades bei Notenberechnung von " + name + " " + e.getMessage());
			}
		}
		
		ObservableList<Student> observableStudList = FXCollections.observableArrayList(studList);
		
		TableView<Student> tableNoten = new TableView<>();
		
		TableColumn<Student, String> studVnCol = new TableColumn<>("Vorname");
		studVnCol.setId("vorname");
		studVnCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		
		TableColumn<Student, String> studNnCol = new TableColumn<>("Nachname");
		studNnCol.setId("nachname");
		studNnCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		
		TableColumn<Student, String> studKsCol = new TableColumn<>("Kurs");
		studKsCol.setId("kurs");
		studKsCol.setCellValueFactory(new PropertyValueFactory<>("kurs"));
		
		TableColumn<Student, Double> studSLPCol = new TableColumn<>("Prozent");
		studSLPCol.setId("sl_percent");
		studSLPCol.setCellValueFactory(new PropertyValueFactory<>("sl_percent"));
		
		TableColumn<Student, String> studSLNCol = new TableColumn<>("Note");
		studSLNCol.setId("sl_note");
		studSLNCol.setCellValueFactory(new PropertyValueFactory<>("sl_note"));
		
		
		tableNoten.getColumns().addAll(studVnCol, studNnCol, studKsCol, studSLPCol, studSLNCol);
		
		tableNoten.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		tableNoten.setItems(observableStudList);
	
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		Label sfLabel = new Label("Kurslistendatei: " + studAbsoluteFile_str);
		sfLabel.setStyle("-fx-font-family: Helvetica;" +  
						 "-fx-font-size: 13px;" +
						 "-fx-text-fill: black");
		gridPane.add(sfLabel, 0, 0, GridPane.REMAINING, 1);
		
		String styleStr = "-fx-font-size: 14px;" +
						  "-fx-padding: 10;" + 
						  "-fx-border-style: solid inside;" + 
						  "-fx-border-width: 2;" +
						  "-fx-border-insets: 5;" + 
						  "-fx-border-radius: 5;" + 
						  "-fx-border-color: blue;";
		
		VBox rootNotenTable = new VBox(tableNoten);
		
		rootNotenTable.setStyle(styleStr);
		rootNotenTable.setPrefWidth(600);
		rootNotenTable.setLayoutX(0);
		rootNotenTable.setLayoutY(0);
		
		gridPane.add(rootNotenTable, 0, 1);
		
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(10);
		gridPane2.setVgap(10);
		
		GridPane gridPane3 = new GridPane();
		gridPane3.setHgap(10);
		gridPane3.setVgap(10);
		
		final int fci = cpCritList.size() - 1;
		final int fpi = cpCritList.get(fci).getPerfCritList().size() - 1;
		Button backBtn = new Button();
		backBtn.setText("Zur\u00FCck");
		backBtn.setOnAction(event -> createInputTables(stage));
		gridPane.add(backBtn, 0, 3);
		
		Label tabLabel = new Label("Tabellen");
		tabLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 16px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(tabLabel, 0, 0);
		
		Label saveLabel = new Label("Speichern der\nTabellen\nals CSV-Dateien");
		saveLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 13px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(saveLabel, 0, 1);
		
		Button saveBtn = new Button();
		saveBtn.setText("Speichern");
		saveBtn.setOnAction(event -> saveResults(stage));
		gridPane3.add(saveBtn, 0, 2);
		
		Label emptyLabel = new Label("");
		emptyLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 16px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(emptyLabel, 0, 3);
		
		Label pdfLabel = new Label("PDF-Dokumente");
		pdfLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 16px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(pdfLabel, 0, 4);
		
		Label headLabel = new Label("Institution, \u00DCberschrift,\nOrt und Datum\nbearbeiten");
		headLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 13px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(headLabel, 0, 5);
		
		Button headBtn = new Button();
		headBtn.setText("Bearbeiten");
		headBtn.setOnAction(event -> createHead(stage));
		gridPane3.add(headBtn, 0,6);
		
		Label expPDFLabel = new Label("Erstellen von\nPDF-Dokumenten");
		expPDFLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 13px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane3.add(expPDFLabel, 0, 8);
		
		Button expPDFBtn = new Button();
		expPDFBtn.setText("Erstellen");
		expPDFBtn.setOnAction(event -> exportResults2PDF(stage));
		gridPane3.add(expPDFBtn, 0, 9);
		
		//gridPane.add(gridPane2, 0, 2);
		gridPane.add(gridPane3, 1, 1);
		
		Label endLabel = new Label("Programm beenden");
		endLabel.setStyle("-fx-font-family: Helvetica;" +  
					   "-fx-font-size: 16px;" +
					   "-fx-font-weight: bold;" +
					   "-fx-text-fill: black");
		gridPane.add(endLabel, 1, 2);
		
		Button nextBtn = new Button();
		nextBtn.setText("Fertig");
		nextBtn.setOnAction(event -> readyWarning(stage));
		gridPane.add(nextBtn, 1, 3);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(820);
		anchorPane.setPrefHeight(640);
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		Scene scene = new Scene(anchorPane);
		resultStage.setScene(scene);
		resultStage.setTitle("Ergebnistabelle - KOBS");
		
		if(this.lastOpenResultStage != null) {
			resultStage.setX(this.lastOpenResultStage.getX());
			resultStage.setY(this.lastOpenResultStage.getY());
		}
		resultStage.setWidth(840);
		resultStage.setHeight(650);
		resultStage.show();
		resultStage.toFront();
		this.lastOpenResultStage = resultStage;
	}
	
	private void readInitPDFText() {
		
		try {
			Path pIn = Paths.get(initialPDFTextPath_str);
			
			List<String> lines = Files.readAllLines(pIn);
			
			titleIndividualPDF = lines.get(0);
			headIndividualPDF = lines.get(1);
			placeIndividualPDF = lines.get(2);
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public void createHead(Stage stage) {
		
		AnchorPane anchorPane = new AnchorPane();
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		readInitPDFText();
		
		TextField instField = new TextField(titleIndividualPDF);
		instField.setPromptText("Institution");
		
		Label instLabel = new Label("Name der Institution in den individuellen PDF-Dokumenten");
		instLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
						   
		gridPane.add(instLabel, 0, 0);
		gridPane.add(instField, 0, 1);
		
		TextArea head = new TextArea();
		head.setPromptText("\u00DCberschrift der Bewertung");
		head.setPrefColumnCount(40);
		head.setPrefRowCount(5);
		head.appendText(headIndividualPDF);
		
		Label headLabel = new Label("\nBewertungs\u00FCberschrift in den individuellen PDF-Dokumenten");
		headLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		gridPane.add(headLabel, 0, 2);
		gridPane.add(head, 0, 3);
		
		TextField placeField = new TextField(placeIndividualPDF);
		placeField.setPromptText("Angabe zu einem Ort");
		
		Label placeLabel = new Label("\nOrtsangabe in den individuellen PDF-Dokumenten");
		placeLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
						   
		gridPane.add(placeLabel, 0, 4);
		gridPane.add(placeField, 0, 5);
		
		
		Label dateLabel = new Label("\nDatum in allen PDF-Dokumenten");
		dateLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		gridPane.add(dateLabel, 0, 6);
		
		LocalDate date = LocalDate.now();
		DatePicker dob = new DatePicker(date); 
		dob.setEditable(false);
		dob.setConverter(locDateStrConv);
		gridPane.add(dob, 0, 7);
		
		LocalDate newDate = dob.getValue();
		//dob.setOnAction(e -> setNewDate(dob));
		
		Label saveLabel = new Label("\nSpeichern der Bearbeitungen");
		saveLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 13px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		gridPane.add(saveLabel, 0, 8);
		
		Button saveHeadBtn = new Button("Speichern");
		saveHeadBtn.setOnAction(event -> saveHead(instField, head, placeField, dob));
		
		gridPane.add(saveHeadBtn, 0, 9);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(450);
		anchorPane.setPrefHeight(570);
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		
		Scene scene = new Scene(anchorPane); 
		//ScrollPane sp = new ScrollPane(anchorPane);
		//Scene scene = new Scene(sp);		
		headStage.setScene(scene);      
		headStage.setTitle("\u00DCberschrift, Ort und Datum f\u00FCr PDF-Dokumente bearbeiten - KOBS");
		if(this.lastOpenHeadStage != null) {
			headStage.setX(this.lastOpenHeadStage.getX());
			headStage.setY(this.lastOpenHeadStage.getY());
		}
		headStage.setWidth(600);
		headStage.setHeight(570);
		headStage.show();
		headStage.toFront();
		this.lastOpenHeadStage = headStage;
	}
	
	void setNewDate(DatePicker dob) {
		LocalDate newDate = dob.getValue();
		//DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY);
		//date_str = newDate.format(formatter); 
		LocalDateStringConverter locDateStrConv =  new LocalDateStringConverter("dd.MM.yyyy");
		date_str = locDateStrConv.toString(newDate);
		date_set = true;
		
		//System.out.println("Datum: " + date_str);
	 }
	
	public void saveHead(TextField title, TextArea head, TextField place, DatePicker dob) {	
		//System.out.println("Titel: " + title.getText());
		titleIndividualPDF = title.getText();
		//System.out.println("\u00DCberschrift:" + head.getText());
		headIndividualPDF = head.getText();
		placeIndividualPDF = place.getText();
		
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(titleIndividualPDF);
		lines.add(headIndividualPDF);
		lines.add(placeIndividualPDF);
		
		try {
			Path pOut = Paths.get(initialPDFTextPath_str);
			Files.write(pOut, lines);
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		LocalDate newDate = dob.getValue();
		//DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		//date_str = newDate.format(formatter);
		date_str = locDateStrConv.toString(newDate);
		
		date_set = true;
	}
	
	private void savePercentGradeMapping() {	
		
		ArrayList<String> lines = new ArrayList<String>();
		String outStr = String.format("untere Grenze\u003Bobere Grenze\u003BNote");
		lines.add(outStr);
		for(PercentGradeMapping pgM : percentGradeMappingList) {
			double loLim = pgM.getLowerLimit();
			double upLim = pgM.getUpperLimit();
			String note = pgM.getDescription();
			outStr = String.format("%.3f\u003B%.3f\u003B%s", loLim, upLim, note);
			outStr = outStr.replace('.', ',');
			lines.add(outStr);
		}
		try {
			Path pOut = Paths.get(initialPercentGradeMappingPath_str);
			Files.write(pOut, lines);
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	private void saveResults(Stage stage) {
		
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false) {
			return;
		}
		if(!initialDirSelected) {
			showInformationAlert("Es wurde noch kein Ordner f\u00FCr Dateien ausgew\u00E4hlt.");
			selectDir(stage);
		}
		try {		
			LocalDate date = LocalDate.now();
			//DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY);
			//String fileDate_str = date.format(formatter);
			String fileDate_str = locDateStrConv.toString(date);
			
			String file_str = this.initialDir + "/" + this.prefix_outputFiles + "_Kompetenz-Bewertungstabelle" + "_" + fileDate_str + ".csv";
			String file_krit_str = this.initialDir + "/" + this.prefix_outputFiles + "_Kompetenz-Kriterien" + "_" + fileDate_str + ".csv";
			String file_noten_str = this.initialDir + "/" + this.prefix_outputFiles + "_Bewertungsergebnis" + "_" + fileDate_str + ".csv";
			String file_skala_str = this.initialDir + "/" + this.prefix_outputFiles + "_Kompetenz-Bewertungsskala" + "_" + fileDate_str + ".csv";
			
			//PrintWriter out_csv = new PrintWriter(file);
			File file = new File(file_str);
			FileWriter out_csv = new FileWriter(file, false);
			File file_krit = new File(file_krit_str);
			FileWriter outKrit_csv = new FileWriter(file_krit, false);
			File file_noten = new File(file_noten_str);
			FileWriter outNoten_csv = new FileWriter(file_noten, false);
			File file_skala = new File(file_skala_str);
			FileWriter outSkala_csv = new FileWriter(file_skala, false);
			
			//out_csv.printf("\u003B\u003B\u003B");
			out_csv.write(String.format("\u003B\u003B\u003B"));
			outKrit_csv.write(String.format("Kompetenz-Kriterium\u003BKK-Gewichtung\u003BPerformanz-Kriterium\u003BPK-Gewichtung\n"));
			outNoten_csv.write(String.format("Vorname\u003BNachname\u003BKurs\u003BProzent\u003BNote\n"));
			outSkala_csv.write(String.format("Bezeichnung\u003BWert\n"));
			
			for (CompetenceCrit cpC: cpCritList) {
				out_csv.write(String.format("%s",cpC.getDescription()));
				
				for (PerformanceCrit pC: cpC.getPerfCritList()) {
					String cpCWe = String.format("%.3f", cpC.getWeight());
					cpCWe = cpCWe.replace('.', ',');
					String pCWe = String.format("%.3f", pC.getWeight());
					pCWe = pCWe.replace('.', ',');
					outKrit_csv.write(String.format("%s\u003B%s\u003B%s\u003B%s\n", cpC.getDescription(), cpCWe, pC.getDescription(), pCWe));
					out_csv.write(String.format("\u003B"));
				}
			}
			outKrit_csv.close();
			
			out_csv.write(String.format("\n"));
			
			out_csv.write(String.format("Vorname\u003BNachname\u003BKurs"));
			
			for (CompetenceCrit cpC: cpCritList) {
				for (PerformanceCrit pC: cpC.getPerfCritList()) {
					out_csv.write(String.format("\u003B%s",pC.getDescription()));
				}
			}
			out_csv.write(String.format("\n"));
			
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				out_csv.write(String.format("%s\u003B%s\u003B%s", vorname, nachname, kurs));
			
				for (CompetenceCrit cC: cpCritList) {
					for (PerformanceCrit pC: cC.getPerfCritList()) {
						int indexOfName = -1;
						for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
							String n = pC.getPerfOfStudList().get(ni).getName();
							//System.out.println(ni + " " + n);
							if(name.equals(n)) {
								indexOfName = ni;
								break;
							}
						}
						String r = pC.getPerfOfStudList().get(indexOfName).getRating();
						out_csv.write(String.format("\u003B%s", r));
					}
				}
				out_csv.write(String.format("\n"));
				try {
					st.calcSLNote(cpCritList, ratingMappingList, percentGradeMappingList);
				} catch (Exception e) {
					System.err.println("Fehler in saveResults bei Notenberechnung von " + name + " " + e.getMessage());
				}
				String prozent_str = String.format("%.1f", st.getSLProzent());
				prozent_str = prozent_str.replace('.', ',');
				outNoten_csv.write(String.format("%s\u003B%s\u003B%s\u003B%s\u003B%s\n", 
								st.getVorname(), st.getNachname(), st.getKurs(), prozent_str, st.getSLNote()));
			}
			out_csv.close();
			outNoten_csv.close();
			
			for(RatingMapping rm: ratingMappingList) {
				String value_str = String.format("%.3f", rm.getValue());
				value_str = value_str.replace('.', ',');
				outSkala_csv.write(String.format("%s\u003B%s\n", rm.getDescription(), value_str));
			}
			outSkala_csv.close();
			showInformationAlert("Die Tabellen wurden als CSV-Dateien gespeichert.");
			csvFilesSaved = true;
			
			savePercentGradeMapping();
			if (Files.exists(Paths.get(initialRatingMappingPath_str))) {
				Files.delete(Paths.get(initialRatingMappingPath_str));
			}
			Files.copy(Paths.get(file_skala_str), Paths.get(initialRatingMappingPath_str));
	
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			showErrorAlert("Der Prozess kann nicht auf die CSV-Dateien zugreifen,\nda sie von einem anderen Prozess verwendet werden");
		} catch (IOException ex) {
			System.err.println(ex);
		}	
	}
	
	private void exportResults2PDF(Stage stage) {
		
		boolean allPriorStepsDone = checkPriorSteps(stage);
		if(allPriorStepsDone == false) {
			return;
		}
		if(!initialDirSelected) {
			showInformationAlert("Es wurde noch kein Ordner f\u00FCr Dateien ausgew\u00E4hlt.");
			selectDir(stage);
		}
		
		readInitPDFText();
		
		LocalDate date = LocalDate.now();
		//DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY);
		//String fileDate_str = date.format(formatter);
		String fileDate_str = locDateStrConv.toString(date);
		if (!date_set) {
			//date_str = date.format(formatter);
			date_str = locDateStrConv.toString(date);
		}
		
		Document document = new Document(PageSize.A4.rotate(), 25, 25, 50, 50);
		
		Document resultDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		
		Document resultPerStudentDoc = new Document(PageSize.A4, 50, 50, 50, 50);
		
		FileOutputStream fileOS_doc = null;
		FileOutputStream fileOS_resDoc = null;
		FileOutputStream fileOS_resPerStudDoc = null;
		
		try {
			
			String ratingTabPDF = this.initialDir + "/" + this.prefix_outputFiles + "_Kompetenz-Bewertungstabellen" + "_" + fileDate_str + ".pdf";
			
			fileOS_doc = new FileOutputStream(ratingTabPDF);
			
            PdfWriter.getInstance(document, fileOS_doc).setInitialLeading(10);
            
			Font helv = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0));
			Font helv_16_b = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(0, 0, 0));
			Font helv_12_blue = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 0, 255));
			Font helv_12_red = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(255, 0, 0));
			Font helv_12_red_b = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0));
			Font helv_14_blue = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0, 0, 255));
			Font helv_12_green = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(60, 195, 0));
			
			Font helv_11_black = new Font(Font.HELVETICA, 11);
			Font helv_12_black = new Font(Font.HELVETICA, 12);
			Font helv_12_black_b = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font helv_11_blue = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(0, 0, 255));
			Font helv_11_red = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(255, 0, 0));
			Font helv_11_green = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(60, 195, 0));
			
			Font timesRoman_11 = new Font(Font.TIMES_ROMAN, 11);
			
			HeaderFooter footer = new HeaderFooter(new Phrase("", timesRoman_11), true);
            footer.setBorder(Rectangle.NO_BORDER);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.setFooter(footer);
			
			HeaderFooter head = new HeaderFooter(new Phrase("Kurslistendatei: " + studFile_str, timesRoman_11), false);
            head.setAlignment(Element.ALIGN_LEFT);
            document.setHeader(head);
			
            document.open();

			//LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
			
			//Font helvetica = new Font(Font.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255));
			Font helvetica = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(0, 0, 0));
			
			String header = "Kompetenz-Bewertungstabellen";
			Paragraph par = new Paragraph(header, helvetica);
            //par.getFont().setStyle(Font.BOLD);
			//par.add(line);
			document.add(par);
			
			//String kurs = "Kurs: " + this.prefix_outputFiles;
			//par = new Paragraph(kurs, helv);
			//document.add(par);
			String datum_str = "Datum: " + date_str;
			par = new Paragraph(datum_str, helv);
			document.add(par);
			
			document.add(Chunk.NEWLINE);
			
			int nrPerfCol = 0;
			for (CompetenceCrit cC: cpCritList) {
				int sizePerfList = cC.getPerfCritList().size();
				nrPerfCol += sizePerfList; 
			}
			
			int NumColumns = nrPerfCol + 3;
            PdfPTable table = new PdfPTable(NumColumns);
			
			table.getDefaultCell().setBorderColor(new Color(0, 0, 0));
            table.getDefaultCell().setPadding(3);
            //table.getDefaultCell().setBorderWidth(2);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			
            PdfPCell cellVorname = new PdfPCell(new Paragraph("Vorname", helv_11_black));
			cellVorname.setRowspan(2);
			cellVorname.setBorderColor(new Color(0, 0, 0));
			cellVorname.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellVorname.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(cellVorname);
			
			PdfPCell cellNachname = new PdfPCell(new Paragraph("Nachname", helv_11_black));
			cellNachname.setRowspan(2);
			cellNachname.setBorderColor(new Color(0, 0, 0));
			cellNachname.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellNachname.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(cellNachname);
			
			PdfPCell cellKurs = new PdfPCell(new Paragraph("Kurs", helv_11_black));
			cellKurs.setRowspan(2);
			cellKurs.setBorderColor(new Color(0, 0, 0));
			cellKurs.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellKurs.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(cellKurs);
			
			for (CompetenceCrit cC: cpCritList) {
				int sizePerfList = cC.getPerfCritList().size();
				PdfPCell cellCC = new PdfPCell(new Paragraph(cC.getDescription(), helv_11_blue));
				cellCC.setColspan(sizePerfList);
				cellCC.setBorderColor(new Color(0, 0, 0));
				cellCC.setHorizontalAlignment(Element.ALIGN_CENTER);
				//cellCC.setVerticalAlignment(Element.ALIGN_CENTER);
				table.addCell(cellCC);
			}
			for (CompetenceCrit cC: cpCritList) {
				PdfPCell cellPC;
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					if(pC.getWeight() >= 0) {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_11_green));
					}
					else {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_11_red));
					}
					cellPC.setHorizontalAlignment(Element.ALIGN_CENTER);
					//cellPC.setVerticalAlignment(Element.ALIGN_CENTER);
					table.addCell(cellPC);
				}
			}
			PdfPCell cellVN;
			PdfPCell cellNN;
			PdfPCell cellKS;
			int i = 1;
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				
				cellVN = new PdfPCell(new Paragraph(vorname, helv_11_black));
				//cellVN.setBorderColor(new Color(0, 0, 255));
				cellVN.setPadding(3);
				cellVN.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellVN.setVerticalAlignment(Element.ALIGN_CENTER);
				
				cellNN = new PdfPCell(new Paragraph(nachname, helv_11_black));
				//cellNN.setBorderColor(new Color(0, 0, 255));
				cellNN.setPadding(3);
				cellNN.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellNN.setVerticalAlignment(Element.ALIGN_CENTER);
				
				cellKS = new PdfPCell(new Paragraph(kurs, helv_11_black));
				//cellKS.setBorderColor(new Color(0, 0, 255));
				cellKS.setPadding(3);
				cellKS.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellKS.setVerticalAlignment(Element.ALIGN_CENTER);
				if (i % 2 == 1) {
					table.getDefaultCell().setGrayFill(0.9f);
					cellVN.setGrayFill(0.9f);
					cellNN.setGrayFill(0.9f);
					cellKS.setGrayFill(0.9f);
				}
				table.addCell(cellVN);
				table.addCell(cellNN);
				table.addCell(cellKS);
				
				for (CompetenceCrit cC: cpCritList) {
					PdfPCell cell;
					for (PerformanceCrit pC: cC.getPerfCritList()) {
						int indexOfName = -1;
						for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
							String n = pC.getPerfOfStudList().get(ni).getName();
							//System.out.println(ni + " " + n);
							if(name.equals(n)) {
								indexOfName = ni;
								break;
							}
						}
						String r = pC.getPerfOfStudList().get(indexOfName).getRating();
						cell = new PdfPCell(new Paragraph(r, helv_11_black));
						cell.setPadding(3);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						if (i % 2 == 1) {
							cell.setGrayFill(0.9f);
						}
						table.addCell(cell);
						if (i % 2 == 1) {
							cell.setGrayFill(1);
						}
					}
				}
				if (i % 2 == 1) {
                    table.getDefaultCell().setGrayFill(1);
					cellVN.setGrayFill(1);
					cellNN.setGrayFill(1);
					cellKS.setGrayFill(1);
                }
				i++;
			}
			
            document.add(table);
			
			for (CompetenceCrit cC: cpCritList) {
				
				document.newPage();
				par = new Paragraph(cC.getDescription(), helv_14_blue);
				par.setAlignment(Element.ALIGN_CENTER);
				document.add(par);
			
				PdfPTable table2 = new PdfPTable(cC.getPerfCritList().size() + 3);
				table2.setSpacingBefore(5);
				table2.setSpacingAfter(10);
				
				table2.getDefaultCell().setBorderColor(new Color(0, 0, 0));
				table2.getDefaultCell().setPadding(3);
				//table.getDefaultCell().setBorderWidth(2);
				table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
				
				cellVorname = new PdfPCell(new Paragraph("Vorname"));
				cellVorname.setBorderColor(new Color(0, 0, 0));
				cellVorname.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cellVorname);
				
				cellNachname = new PdfPCell(new Paragraph("Nachname"));
				cellNachname.setBorderColor(new Color(0, 0, 0));
				cellNachname.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cellNachname);
				
				cellKurs = new PdfPCell(new Paragraph("Kurs"));
				cellKurs.setBorderColor(new Color(0, 0, 0));
				cellKurs.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cellKurs);
				
				PdfPCell cellPC;
				
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					if(pC.getWeight() >= 0) {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_12_green));
					}
					else {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_12_red));
					}
					cellPC.setHorizontalAlignment(Element.ALIGN_CENTER);
					//cellPC.setVerticalAlignment(Element.ALIGN_CENTER);
					table2.addCell(cellPC);
				}
			
				i = 1;
				for (Student st: studList) {
					String vorname = st.getVorname();
					String nachname = st.getNachname();
					String kurs = st.getKurs();
					String name = nachname + " " + vorname + " " + kurs;
					if (i % 2 == 1) {
						table2.getDefaultCell().setGrayFill(0.9f);
					}
					
					table2.addCell(vorname);
					table2.addCell(nachname);
					table2.addCell(kurs);
					
					for (PerformanceCrit pC: cC.getPerfCritList()) {
						int indexOfName = -1;
						for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
							String n = pC.getPerfOfStudList().get(ni).getName();
							//System.out.println(ni + " " + n);
							if(name.equals(n)) {
								indexOfName = ni;
								break;
							}
						}
						String r = pC.getPerfOfStudList().get(indexOfName).getRating();
						
						table2.addCell(r);
					}
					
					if (i % 2 == 1) {
						table2.getDefaultCell().setGrayFill(1);
					}
					i++;
				}
				document.add(table2);
			}	
			
			String resultPDF = this.initialDir + "/" + this.prefix_outputFiles + "_Bewertungsergebnis" + "_" + fileDate_str + ".pdf";
			
			fileOS_resDoc = new FileOutputStream(resultPDF);
			
			PdfWriter.getInstance(resultDoc, fileOS_resDoc).setInitialLeading(10);
			
			resultDoc.setFooter(footer);
			resultDoc.setHeader(head);
			
			resultDoc.open();
			
			header = "Ergebnis der Bewertung";
			par = new Paragraph(header, helvetica);
            //par.add(line);
			resultDoc.add(par);
			
			//par = new Paragraph(kurs, helv);
			//resultDoc.add(par);
			
			par = new Paragraph(datum_str, helv);
			resultDoc.add(par);
			
			resultDoc.add(Chunk.NEWLINE);
			
			NumColumns = 5;
            PdfPTable table3 = new PdfPTable(NumColumns);
			
			table3.setSpacingBefore(5);
			table3.setSpacingAfter(20);
			
			table3.getDefaultCell().setBorderColor(new Color(0, 0, 255));
            table3.getDefaultCell().setPadding(3);
			//table3.getDefaultCell().setBorderWidth(2);
            table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			
			cellVorname = new PdfPCell(new Paragraph("Vorname", helv_12_black_b));
			cellVorname.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellVorname.setVerticalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cellVorname);
			
			cellNachname = new PdfPCell(new Paragraph("Nachname", helv_12_black_b));
			cellNachname.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellNachname.setVerticalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cellNachname);
			
			cellKurs = new PdfPCell(new Paragraph("Kurs", helv_12_black_b));
			cellKurs.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellKurs.setVerticalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cellKurs);
			
			PdfPCell cellProzent = new PdfPCell(new Paragraph("Prozent", helv_12_black_b));
			cellProzent.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellProzent.setVerticalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cellProzent);
			
			PdfPCell cellNote = new PdfPCell(new Paragraph("Note", helv_12_black_b));
			cellNote.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellNote.setVerticalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cellNote);
			
			PdfPCell cellNote2;
			PdfPCell cellPerc;
			i = 1;
			for (Student st: studList) {
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				
				cellVN = new PdfPCell(new Paragraph(vorname, helv_12_black));
				cellVN.setBorderColor(new Color(0, 0, 255));
				cellVN.setPadding(3);
				cellVN.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellVN.setVerticalAlignment(Element.ALIGN_CENTER);
				
				cellNN = new PdfPCell(new Paragraph(nachname, helv_12_black));
				cellNN.setBorderColor(new Color(0, 0, 255));
				cellNN.setPadding(3);
				cellNN.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellNN.setVerticalAlignment(Element.ALIGN_CENTER);
				
				cellKS = new PdfPCell(new Paragraph(kurs, helv_12_black));
				cellKS.setBorderColor(new Color(0, 0, 255));
				cellKS.setPadding(3);
				cellKS.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellKS.setVerticalAlignment(Element.ALIGN_CENTER);
				if (i % 2 == 1) {
					table.getDefaultCell().setGrayFill(0.9f);
					cellVN.setGrayFill(0.9f);
					cellNN.setGrayFill(0.9f);
					cellKS.setGrayFill(0.9f);
				}
				table3.addCell(cellVN);
				table3.addCell(cellNN);
				table3.addCell(cellKS);
				
				String sl_perc_str = String.valueOf(st.getSLProzent()).replace(".",",");
				cellPerc = new PdfPCell(new Paragraph(sl_perc_str, helv_12_black));
				if (i % 2 == 1) {
					cellPerc.setGrayFill(0.9f);
				}
				cellPerc.setBorderColor(new Color(0, 0, 255));
				cellPerc.setPadding(3);
				cellPerc.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellPerc.setVerticalAlignment(Element.ALIGN_CENTER);
				table3.addCell(cellPerc);
				
				//String sl_note_str = String.valueOf(st.getSLNote());
				String sl_note_str = st.getSLNote();
				
				if(st.getSLNote().equals("5") || st.getSLNote().equals("6")) {
					cellNote2 = new PdfPCell(new Paragraph(sl_note_str, helv_12_red));
					if (i % 2 == 1) {
						cellNote2.setGrayFill(0.9f);
					}
					cellNote2.setBorderColor(new Color(0, 0, 255));
					cellNote2.setPadding(3);
					cellNote2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellNote2.setVerticalAlignment(Element.ALIGN_CENTER);
					table3.addCell(cellNote2);
				}
				else {
					cellNote2 = new PdfPCell(new Paragraph(sl_note_str, helv_12_black));
					if (i % 2 == 1) {
						cellNote2.setGrayFill(0.9f);
					}
					cellNote2.setBorderColor(new Color(0, 0, 255));
					cellNote2.setPadding(3);
					cellNote2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellNote2.setVerticalAlignment(Element.ALIGN_CENTER);
					table3.addCell(cellNote2);
				}
				if (i % 2 == 1) {
					table.getDefaultCell().setGrayFill(1);
					cellVN.setGrayFill(1);
					cellNN.setGrayFill(1);
					cellKS.setGrayFill(1);
					cellPerc.setGrayFill(1);
				}
				i++;
			}
			resultDoc.add(table3);
			
			resultDoc.newPage();
			
			header = "Bewertungskriterien und Gewichtung";
			par = new Paragraph(header, helv_16_b);
			par.setAlignment(Element.ALIGN_CENTER);
            resultDoc.add(par);
			PdfPTable table4 = new PdfPTable(4);
			table4.setSpacingBefore(10);
			table4.setSpacingAfter(10);
			
			table4.setTotalWidth(500);
			table4.setLockedWidth(true);
			table4.setWidths(new float[]{2,1,2,1});
			table4.getDefaultCell().setBorderColor(new Color(0, 0, 255));
            table4.getDefaultCell().setPadding(3);
			//table4.getDefaultCell().setBorderWidth(2);
            table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table4.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell cellTab4 = new PdfPCell(new Paragraph("Kompetenz-Kriterium", helv_12_black_b));
			cellTab4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab4.setVerticalAlignment(Element.ALIGN_CENTER);
			table4.addCell(cellTab4);
			
			cellTab4 = new PdfPCell(new Paragraph("Gewichtung", helv_12_black_b));
			cellTab4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab4.setVerticalAlignment(Element.ALIGN_CENTER);
			table4.addCell(cellTab4);
			
			cellTab4 = new PdfPCell(new Paragraph("Performanz-Kriterium", helv_12_black_b));
			cellTab4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab4.setVerticalAlignment(Element.ALIGN_CENTER);
			table4.addCell(cellTab4);
			
			cellTab4 = new PdfPCell(new Paragraph("Gewichtung", helv_12_black_b));
			cellTab4.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab4.setVerticalAlignment(Element.ALIGN_CENTER);
			table4.addCell(cellTab4);
			
			
			for (CompetenceCrit cC: cpCritList) {
				
				int sizePerfList = cC.getPerfCritList().size();
				
				PdfPCell cellCC = new PdfPCell(new Paragraph(cC.getDescription(), helv_12_blue));
				cellCC.setRowspan(sizePerfList);
				cellCC.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellCC.setVerticalAlignment(Element.ALIGN_CENTER);
				table4.addCell(cellCC);
				
				PdfPCell cellCC2 = new PdfPCell(new Paragraph(String.valueOf(cC.getWeight()).replace(".",","), helv_12_black));
				cellCC2.setRowspan(sizePerfList);
				cellCC2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cellCC2.setVerticalAlignment(Element.ALIGN_CENTER);
				table4.addCell(cellCC2);
				
				PdfPCell cellPC;
				
				for (PerformanceCrit pC: cC.getPerfCritList()) {
					
					if(pC.getWeight() >= 0) {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_12_green));
					}
					else {
						cellPC = new PdfPCell(new Paragraph(pC.getDescription(), helv_12_red));
					}
					cellPC.setHorizontalAlignment(Element.ALIGN_CENTER);
					table4.addCell(cellPC);
					
					PdfPCell cellPC2 = new PdfPCell(new Paragraph(String.valueOf(pC.getWeight()).replace(".",","), helv_12_black));
					cellPC2.setHorizontalAlignment(Element.ALIGN_CENTER);
					table4.addCell(cellPC2);
					
				}
			}
			
			resultDoc.add(table4);
			
			resultDoc.add(Chunk.NEWLINE);
			resultDoc.add(Chunk.NEWLINE);
			
			header = "Kompetenz-Bewertungsskala";
			par = new Paragraph(header, helv_16_b);
			par.setAlignment(Element.ALIGN_CENTER);
            resultDoc.add(par);
			PdfPTable table5 = new PdfPTable(2);
			table5.setSpacingBefore(10);
			table5.setSpacingAfter(10);
			
			table5.setTotalWidth(250);
			table5.setLockedWidth(true);
			table5.setWidths(new float[]{1,1});
			table5.getDefaultCell().setBorderColor(new Color(0, 0, 255));
            table5.getDefaultCell().setPadding(3);
			//table4.getDefaultCell().setBorderWidth(2);
            table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table5.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell cellTab5 = new PdfPCell(new Paragraph("Bezeichnung", helv_12_black_b));
			cellTab5.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab5.setVerticalAlignment(Element.ALIGN_CENTER);
			table5.addCell(cellTab5);
			
			cellTab5 = new PdfPCell(new Paragraph("Wert", helv_12_black_b));
			cellTab5.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellTab5.setVerticalAlignment(Element.ALIGN_CENTER);
			table5.addCell(cellTab5);
			
			
			PdfPCell cellRM;
			for (RatingMapping rM: ratingMappingList) {
					
				cellRM = new PdfPCell(new Paragraph(rM.getDescription(), helv_12_black));
				cellRM.setHorizontalAlignment(Element.ALIGN_CENTER);
				table5.addCell(cellRM);
				
				cellRM = new PdfPCell(new Paragraph(String.valueOf(rM.getValue()).replace(".",","), helv_12_black));
				cellRM.setHorizontalAlignment(Element.ALIGN_CENTER);
				table5.addCell(cellRM);
			}
			
			resultDoc.add(table5);
			
			String ratingPerStudPDF = this.initialDir + "/" + this.prefix_outputFiles + "_BewertungProPerson" + "_" + fileDate_str + ".pdf";
			
			fileOS_resPerStudDoc = new FileOutputStream(ratingPerStudPDF);
			
			PdfWriter.getInstance(resultPerStudentDoc, fileOS_resPerStudDoc).setInitialLeading(10);
			
			resultPerStudentDoc.open();
			Font timesRoman_6 = new Font(Font.TIMES_ROMAN, 6);
			Font timesRoman_8 = new Font(Font.TIMES_ROMAN, 8);
			Font timesRoman_13 = new Font(Font.TIMES_ROMAN, 13);
			Font timesRoman_14 = new Font(Font.TIMES_ROMAN, 14);
			Font timesRoman_14_b = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
			Font timesRoman_15 = new Font(Font.TIMES_ROMAN, 15);
			Font timesRoman_16 = new Font(Font.TIMES_ROMAN, 16);
			Font timesRoman_16_b = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
			LineSeparator line2 = new LineSeparator(1, 50, null, Element.ALIGN_LEFT, -2);
			for (Student st: studList) {
				
				resultPerStudentDoc.newPage();
				
				par = new Paragraph(titleIndividualPDF, timesRoman_14);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				resultPerStudentDoc.add(Chunk.NEWLINE);
				
				par = new Paragraph(headIndividualPDF, timesRoman_16_b);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				par = new Paragraph(" ", timesRoman_8);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				String vorname = st.getVorname();
				String nachname = st.getNachname();
				String kurs = st.getKurs();
				String name = nachname + " " + vorname + " " + kurs;
				String name_str = "Name: " + vorname + " " + nachname + " \u002D Kurs: " + kurs;
				
				par = new Paragraph(name_str, timesRoman_15);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				resultPerStudentDoc.add(Chunk.NEWLINE);
				
				par = new Paragraph("Bewertungen zu den Kriterien", timesRoman_16_b);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				par = new Paragraph(" ", timesRoman_6);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				for (CompetenceCrit cC: cpCritList) {
					
					String cC_str = cC.getDescription();
					par = new Paragraph(cC_str, timesRoman_14_b);
					par.setAlignment(Element.ALIGN_CENTER);
					resultPerStudentDoc.add(par);
					//resultPerStudentDoc.add(Chunk.NEWLINE);
					for (PerformanceCrit pC: cC.getPerfCritList()) {
						
						String pC_str = pC.getDescription();
						
						int indexOfName = -1;
						for(int ni = 0; ni < pC.getPerfOfStudList().size(); ni++) {
							String n = pC.getPerfOfStudList().get(ni).getName();
							//System.out.println(ni + " " + n);
							if(name.equals(n)) {
								indexOfName = ni;
								break;
							}
						}
						String r = pC.getPerfOfStudList().get(indexOfName).getRating();
						String perfRat = pC_str + ": " + r;
						par = new Paragraph(perfRat, timesRoman_13);
						par.setAlignment(Element.ALIGN_CENTER);
						resultPerStudentDoc.add(par);
					}
					par = new Paragraph(" ", timesRoman_6);
					par.setAlignment(Element.ALIGN_CENTER);
					resultPerStudentDoc.add(par);
				}
				resultPerStudentDoc.add(Chunk.NEWLINE);
				par = new Paragraph(" ", timesRoman_6);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				par = new Paragraph("Gesamtbewertung", timesRoman_16_b);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				String sl_note_str = String.valueOf(st.getSLNote());
			    String ges_str = "Note: " + sl_note_str; 
				par = new Paragraph(ges_str, timesRoman_15);
				par.setAlignment(Element.ALIGN_CENTER);
				resultPerStudentDoc.add(par);
				
				for(int k = 0; k < 2; k++) {
					resultPerStudentDoc.add(Chunk.NEWLINE);
				}
				String spaceStr = "        ";
				if (!placeIndividualPDF.equals("")) {
					spaceStr = ", ";
				}
				String locDate_str = placeIndividualPDF + spaceStr + date_str + "        ____________________________";
				
				par = new Paragraph(locDate_str, timesRoman_13);
				par.setAlignment(Element.ALIGN_LEFT);
				resultPerStudentDoc.add(par);	
			}
			
			showInformationAlert("Die PDF-Dokumente wurden erstellt.");
			pdfFilesSaved = true;
		} catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
			showErrorAlert("Der Prozess kann nicht auf die PDF-Dateien zugreifen,\nda sie von einem anderen Prozess verwendet werden");
		}
		
		try {
			if (document != null) {
				document.close();
			}
			if (resultDoc != null) {
				resultDoc.close();
			}
			if (resultPerStudentDoc != null) {
				resultPerStudentDoc.close();
			}
			if (fileOS_doc != null) {
				fileOS_doc.close();
			}
			if (fileOS_resDoc != null) {
				fileOS_resDoc.close();
			}
			if (fileOS_resPerStudDoc != null) {
				fileOS_resPerStudDoc.close();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}		
	}
	
	private void readCriteria(Stage stage) {
		
		cpCritList.clear();
		tableCompetenceCrit.getItems().clear();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		
		readInitDir();
		if(initialDir != null && !initialDir.equals("")) {
			fileChooser.setInitialDirectory(new File(initialDir));
		}
		
		File KSL_File = fileChooser.showOpenDialog(stage);
		if (KSL_File == null) {
			showWarningAlert("Keine Datei ausgew\u00E4hlt oder Datei nicht lesbar");  
			return;
		}
		String KSL_str = KSL_File.getName();
		//System.out.println("Kriterienlistendatei: " + KSL_str);
		try { 
			
			Scanner inp_KSL = new Scanner(KSL_File,"UTF-8");
			
			String cp_crit_prev = " ";
			String cp_crit = "XYZ";
			String pf_crit = "";
			String pf_crit_we_str = "";
			double pf_crit_we = 0.0;
			
			int j = 0;
			String inp_header = replaceToUniCode(inp_KSL.nextLine());
			
			//System.out.println(inp_header);
			
			while(inp_KSL.hasNext()) { 			
				String row = inp_KSL.nextLine();
				//System.out.println(row);
				int idx1 = row.indexOf(";");
				int idx2 = row.indexOf(";", idx1 + 1);
				int idx3 = row.indexOf(";", idx2 + 1);
				String s[] = row.split(";");
				if(idx1 != -1 && idx2 != -1 && idx3 != -1 && s.length > 3) {
					if (s[0] != null && !s[0].equals("")) {
						String crit_str = replaceToUniCode(s[0]);
						cp_crit = crit_str;
						if (!cp_crit.equals(cp_crit_prev)) {
							String cp_crit_we_str = s[1].replace(',','.');
							double cp_crit_we = Double.parseDouble(cp_crit_we_str);
							if(0.0 <= cp_crit_we) {
								//System.out.write(String.format("NEUES K-Krit %s Gewicht %.3f\n", cp_crit, cp_crit_we);
								CompetenceCrit cC = new CompetenceCrit(cp_crit, cp_crit_we);
								tableCompetenceCrit.getItems().add(cC);
								cpCritList.add(cC);
								j++;
							}
							else {
								showErrorAlert("Die Gewichtung " + cp_crit_we + " liegt ausserhalb des erlaubten Bereichs.\n" +
											   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");
								System.out.println("Die Zeile\n" + row + "\nwurde nicht importiert.");
								showWarningAlert("Die Zeile\n" + row + "\nwurde nicht importiert.");
								continue;
							}
						}
					}
					if (s[2] != null) {
						String crit_str = replaceToUniCode(s[2]);
						pf_crit = crit_str;
						pf_crit_we_str = s[3].replace(',','.');
						pf_crit_we = Double.parseDouble(pf_crit_we_str);
						if(0.0 <= pf_crit_we) {
							cpCritList.get(j-1).addPerfCrit(pf_crit, pf_crit_we);	
							cp_crit_prev = cp_crit;
						}
						else {
							showErrorAlert("Die Gewichtung " + pf_crit_we + " liegt ausserhalb des erlaubten Bereichs.\n" +
										   "Bitte nur Werte gr\u00F6\u00DFer oder gleich 0 eingeben.");
							System.out.println("Die Zeile\n" + row + "\nwurde nicht importiert.");
							showWarningAlert("Die Zeile\n" + row + "\nwurde nicht importiert.");
							continue;
						}
					}
				}
				else {
					System.out.println("Die Zeile\n" + row + "\nwurde nicht importiert.");
					showWarningAlert("Die Zeile\n" + row + "\nwurde nicht importiert.");
				}
			}
			inp_KSL.close();
			
			if(perfStage.isShowing()) {
				createPerfTables(stage);
			}
			if(ratingPerCompetenceStage.isShowing()) {
				ratingPerCompetenceStage.close();
			}
			if(ratingPerStudentStage.isShowing()) {
				ratingPerStudentStage.close();
			}
			if(resultStage.isShowing()) {
				resultStage.close();
			}
			csvFilesSaved = false;
			pdfFilesSaved = false;
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
			showErrorAlert("Kriterienliste " + KSL_str + " konnte nicht eingelesen werden\n" + ex);
		} catch (NumberFormatException ex) {
			System.err.println(ex);
			showErrorAlert("Kriterienliste " + KSL_str + " konnte nicht eingelesen werden\n" + ex);
		}			
	}
	
	private String getPathToFile(String fileStr) {
		
		String pathToFile = "";
		
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		File fileCurDir;
		try {
			fileCurDir = new File(url.toURI()); 
		} catch (URISyntaxException ex) {
			System.err.println(ex);
			fileCurDir = new File(url.getPath()); 
		}
		String curDir = fileCurDir.getName();
		Path parentPath = Paths.get(fileCurDir.getParent().toString());
		String parentPath_str = parentPath.toFile().toString();
		
		if (Files.exists(Paths.get(fileStr))) {
			pathToFile = parentPath_str + "/" + fileStr;
		}
		else {
			Path parentParentPath = parentPath.getParent();
			String parentParentPath_str = parentParentPath.toFile().toString();
			pathToFile = parentParentPath_str + "/" + fileStr;
		}
		return pathToFile;
	}
	
	@Override
	public void start(Stage stage) {
		
		Locale.setDefault(Locale.GERMANY);
		locDateStrConv =  new LocalDateStringConverter("dd.MM.yyyy");
		try {
			String userHome = System.getProperty("user.home");
			System.out.println("userHome: " + userHome);
			String kobsdir_str = userHome + "/.kobs";
			String osName = System.getProperty("os.name");
			String macOS = "mac";
			if(osName.toLowerCase().contains(macOS)) {
				kobsdir_str = userHome + "/Library/Kobs";
			}
			
			Files.createDirectories(Paths.get(kobsdir_str));
			
			initialDirPath_str = kobsdir_str + "/" + initDirFile_str;
			String initDirPath_str = getPathToFile(initDirFile_str);
			//System.out.println("initDirPath_str: " + initDirPath_str);
			if (!Files.exists(Paths.get(initialDirPath_str))) {
				Files.copy(Paths.get(initDirPath_str), Paths.get(initialDirPath_str)); 
			}
			
			initialPDFTextPath_str = kobsdir_str + "/" + initPDFTextFile_str;
			String initPDFTextPath_str = getPathToFile(initPDFTextFile_str);
			//System.out.println("initPDFTextFile_str: " + initPDFTextFile_str);
			if (!Files.exists(Paths.get(initialPDFTextPath_str))) {
				Files.copy(Paths.get(initPDFTextPath_str), Paths.get(initialPDFTextPath_str)); 
			}
			
			initialPercentGradeMappingPath_str = kobsdir_str + "/" + initPercentGradeMappingFile_str;
			String initPercentGradeMappingPath_str = getPathToFile(initPercentGradeMappingFile_str);
			//System.out.println("initPercentGradeMappingPath_str: " + initPercentGradeMappingPath_str);
			if (!Files.exists(Paths.get(initialPercentGradeMappingPath_str))) {
				Files.copy(Paths.get(initPercentGradeMappingPath_str), Paths.get(initialPercentGradeMappingPath_str)); 
			}
			
			initialRatingMappingPath_str = kobsdir_str + "/" + initRatingMappingFile_str;
			String initRatingMappingPath_str = getPathToFile(initRatingMappingFile_str);
			//System.out.println("initRatingMappingPath_str: " + initRatingMappingPath_str);
			if (!Files.exists(Paths.get(initialRatingMappingPath_str))) {
				Files.copy(Paths.get(initRatingMappingPath_str), Paths.get(initialRatingMappingPath_str)); 
			}
			
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}	
		
		mainStage = new Stage();
		infoStage = new Stage();
		compStage = new Stage();
		perfStage = new Stage();
		resultStage = new Stage();
		headStage = new Stage();
		ratingPerStudentStage = new Stage();
		ratingPerCompetenceStage = new Stage();
		ratMapStage = new Stage();
		percentGradeMapStage = new Stage();
		createInputTables(stage);
		createCompTable(stage);
		readyStage = new Stage();
		
		readInitPercentGradeMappingData();
		readInitRatingMappingData();
	}
	
	private void readInitPercentGradeMappingData() {
		
		try {
			Path pIn = Paths.get(initialPercentGradeMappingPath_str);
			List<String> lines = Files.readAllLines(pIn);
			int row_nr = 0;
			for(String row : lines) {
				int idx1 = row.indexOf(";");
				int idx2 = row.indexOf(";", idx1 + 1);
				//System.out.println(row);
				String s[] = row.split(";");
				
				if( row_nr > 0 && idx1 != -1 && idx2 != -1 && s.length > 2) {
					String loLim_str = s[0].replace(',','.');
					String upLim_str = s[1].replace(',','.');
					String n = replaceToUniCode(s[2]);
					//System.out.println(row);
					double loLim = Double.parseDouble(loLim_str);
					double upLim = Double.parseDouble(upLim_str);
					if( 0.0 <= loLim && loLim <= 100.0 && 0.0 <= upLim && upLim <= 100.0 && loLim <= upLim ) {
						//System.out.println("loLim: " + loLim + " upLim: " + upLim + " Note: " + n);
						PercentGradeMapping pgM = new PercentGradeMapping(n, loLim, upLim);
						percentGradeMappingList.add(pgM);
					}
				}
				row_nr++;
			}
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	private void readInitRatingMappingData() {
		
		try {
			Path pIn = Paths.get(initialRatingMappingPath_str);
			List<String> lines = Files.readAllLines(pIn);
			int row_nr = 0;
			for(String row : lines) {
				
				int idx1 = row.indexOf(";");
				//System.out.println(row);
				String s[] = row.split(";");
				
				if(row_nr > 0 && idx1 != -1 && s.length > 1) {
					String c = replaceToUniCode(s[0]);
					String v_str = s[1].replace(',','.');
					//System.out.println(row);
					double v = Double.parseDouble(v_str);
					if( 0.0 <= v && v <= 1.0 ) {
						//System.out.println("c: " + c + " v: " + v);
						RatingMapping rM = new RatingMapping(c, v);
						ratingMappingList.add(rM);
					}
				}
				row_nr++;
			}
		} catch (FileNotFoundException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void warningFilesNotSaved(Stage stage) {
		AnchorPane anchorPane = new AnchorPane();
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(5);
		
		Label warnLabel = new Label("WARNUNG: ");
		warnLabel.setStyle("-fx-font-family: Helvetica;" +  
							 "-fx-font-size: 18px;" +
							 "-fx-text-fill: red");
		pane.add(warnLabel, 0, 0);					 
		
		String warningStr = "CSV-Dateien nicht gespeichert und PDF-Dokumente nicht erstellt";
		if(csvFilesSaved && !pdfFilesSaved) {
			warningStr = "PDF-Dokumente nicht erstellt";
		}
		if(!csvFilesSaved && pdfFilesSaved) {
			warningStr = "CSV-Dateien nicht gespeichert";
		}
		
		Label breakLabel = new Label(warningStr);
		breakLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 14px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(breakLabel, 0, 1);
		
		Label noBreakLabel = new Label("Beenden abbrechen");
		noBreakLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 14px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(noBreakLabel, 0, 2);
		
		Button breakBtn = new Button();
		breakBtn.setText("Abbrechen");
		breakBtn.setOnAction(event -> calcGrades(stage));
		pane.add(breakBtn, 0, 3);
		
		Label readyLabel = new Label("Programm trotzdem beenden");
		readyLabel.setStyle("-fx-font-family: Helvetica;" +  
						   "-fx-font-size: 14px;" +
						   "-fx-font-weight: bold;" +
						   "-fx-text-fill: black");
		
		pane.add(readyLabel, 1, 2);
		
		Button readyBtn = new Button();
		readyBtn.setText("Beenden");
		readyBtn.setOnAction(event -> ready());
		pane.add(readyBtn, 1, 3);
		
		anchorPane.setStyle(styleAnchorPaneStr);
		
		anchorPane.getChildren().add(pane);
		anchorPane.setPrefWidth(700);
		anchorPane.setPrefHeight(200);
		AnchorPane.setLeftAnchor(pane, 30.);
		AnchorPane.setTopAnchor(pane, 30.);
		
		Scene scene = new Scene(anchorPane, 700, 200);
		readyStage.setScene(scene);
		readyStage.setTitle("KOBS beenden?");
		
		readyStage.setWidth(720);
		readyStage.setHeight(220);
		readyStage.show();
		readyStage.toFront();
	}
	
	private void readyWarning(Stage stage) {
		if (!csvFilesSaved || !pdfFilesSaved) { 
			warningFilesNotSaved(stage);
		}
		else {
			ready();  
		}
	}
	
	private void ready() {
		Platform.exit();  
	}
}
