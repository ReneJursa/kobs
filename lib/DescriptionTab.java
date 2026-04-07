package lib;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class DescriptionTab extends Tab {
	TextField descField = new TextField();
	Text text = new Text();
	
	public DescriptionTab(String s) {
		this.setText(s);
		init();
	}

	public void init() {
		
		String text_str = "\nKOBS berechnet Noten als gewichtete Summe von Bewertungen zu einzelnen Kriterien.\n\n" +
					  "Das Programm KOBS ist vollständig in Java geschrieben.\n\n" +
					  "Dieses Programm ist Freie Software: Sie können es unter den Bedingungen der GNU General Public License,\n" +
					  "wie von der Free Software Foundation, Version 3 der Lizenz oder (nach Ihrer Wahl) jeder neueren\n" +
					  "veröffentlichten Version, weiter verteilen und/oder modifizieren. Dieses Programm wird in der Hoffnung\n" +
					  "bereitgestellt, dass es nützlich sein wird, jedoch OHNE JEDE GEWÄHR; sogar ohne die implizite Gewähr der\n" +
					  "MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.\n" +
					  "Siehe die GNU General Public License für weitere Einzelheiten.\n" + 
					  "Sie sollten eine Kopie der GNU General Public License zusammen mit diesem Programm erhalten haben\n" +
					  "Wenn nicht, siehe <https://www.gnu.org/licenses/>.\n\n" +
					  "Copyright \u00A9 2026 Ren\u00E9 Jursa\n\n" +
					  "FEHLZEITEN enth\u00E4lt folgende Third Party Software:\n" +
					  "\u25E6 JavaFX (Open source Java client application platform): https://openjfx.io/" + "\n" + 
					  "\u25E6 OpenPDF (Open source Java library): https://github.com/LibrePDF/OpenPDF" + "\n"; 
		
		text.setText(text_str);
		text.setFont(Font.font("Times New Roman", 13));
		GridPane gridPane = new GridPane();
		gridPane.addRow(0, text);
		this.setContent(gridPane);
	}
}
