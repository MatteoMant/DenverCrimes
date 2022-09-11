package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		model.creaGrafo("drug-alcohol", 2);
		
		System.out.print(model.getArchiMaggioriPesoMedio());
	}

}
