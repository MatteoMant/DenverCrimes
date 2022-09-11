/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Adiacenza arco = this.boxArco.getValue();
    	if(arco == null) {
    		txtResult.appendText("Seleziona un arco!");
    		return;
    	}
    	
    	txtResult.appendText(String.format("Percorso massimo tra %s e %s:\n", arco.getV1(),arco.getV2()));
    	for(String v : this.model.calcolaPercorso(arco.getV1(), arco.getV2())) {
    		txtResult.appendText(v + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String categoria = this.boxCategoria.getValue();
    	if(categoria == null) {
    		txtResult.appendText("Seleziona una categoria!");
    		return;
    	}
    	Integer mese = this.boxMese.getValue();
    	if(mese == null) {
    		txtResult.appendText("Seleziona un mese!");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText(String.format("# VERTICI: %d\n", this.model.nVertici()));
    	txtResult.appendText(String.format("# ARCHI: %d\n", this.model.nArchi()));
    	
    	this.boxArco.getItems().addAll(this.model.getArchi()); // dopo aver creato il grafo possiamo riempire anche questa tendina
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	// adesso dobbiamo riempire le due tendine prima che il grafo venga creato
    	this.boxCategoria.getItems().addAll(this.model.getCategorie());
    	for(Integer i = 1; i <=12; i++) {
    		this.boxMese.getItems().add(i);
    	}
    }
}
