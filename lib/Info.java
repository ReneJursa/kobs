package lib;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import static javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Info extends Application {
	
	private String styleScrollPaneStr = "-fx-background-color: snow;";
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		
		Text kobsVersionText = new Text("KOBS 1.2.0");
		kobsVersionText.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		
		kobsVersionText.setTextOrigin(VPos.TOP);
		DescriptionTab descTab = new DescriptionTab("Beschreibung");
		AuthorTab authorTab = new AuthorTab("Autoren");
		LicenseTab licenseTab = new LicenseTab("Lizenz");

		// Add selection a change listener to Tabs
		descTab.setOnSelectionChanged(e -> tabSelectedChanged(e));
		authorTab.setOnSelectionChanged(e -> tabSelectedChanged(e));
		licenseTab.setOnSelectionChanged(e -> tabSelectedChanged(e));

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		// Add a ChangeListsner to the selection model
		tabPane.getSelectionModel().selectedItemProperty()
		       .addListener(this::selectionChanged);

		tabPane.getTabs().addAll(descTab, authorTab, licenseTab);

		HBox hBox = new HBox(tabPane);	
		hBox.setStyle("-fx-padding: 10;");
		
		
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
		gridPane.add(kobsVersionText, 0, 0);
		
		gridPane.add(hBox, 0, 1);
		
		
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setStyle(styleScrollPaneStr);
		anchorPane.getChildren().add(gridPane);
		anchorPane.setPrefWidth(630);
		anchorPane.setPrefHeight(500);
		
		AnchorPane.setLeftAnchor(gridPane, 30.);
		AnchorPane.setTopAnchor(gridPane, 30.);
		ScrollPane sp = new ScrollPane(anchorPane);
		Scene scene = new Scene(sp, 630, 500);
		
		stage.setScene(scene);
		stage.setTitle("Info - KOBS");
		stage.show();
	}

	public void selectionChanged(ObservableValue<? extends Tab> prop, 
	                             Tab oldTab,
	                             Tab newTab) {
		String oldTabText = oldTab == null? "None": oldTab.getText();
		String newTabText = newTab == null? "None": newTab.getText();	
		System.out.println("Selection changed in TabPane: old = " + 
		                   oldTabText + ", new = " + newTabText);
	}

	public void tabSelectedChanged(Event e) {
		Tab tab = (Tab)e.getSource();
		System.out.println("Selection changed event for " + tab.getText() + 
		                   " tab, selected = " + tab.isSelected());
	}
}
