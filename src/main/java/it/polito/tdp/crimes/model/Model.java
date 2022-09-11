package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<String> best; // percorso migliore (che tocca il maggior numero di vertici)
	// NON abbiamo bisogno di una variabile che tenga conto del peso migliore perchè in questo caso il peso
	// è dato proprio dalla dimensione del percorso (size di best)
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// aggiunta vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		// aggiunta archi
		for (Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
		}
		
		// System.out.println("Grafo creato!");
		// System.out.println("Numero di vertici: " + this.grafo.vertexSet().size());
		// System.out.println("Numero di archi: " + this.grafo.edgeSet().size());
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getArchi(){
		List<Adiacenza> archi = new ArrayList<Adiacenza>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			archi.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), 
						(int) this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}
	
	public List<String> getCategorie(){
		return this.dao.getCategorie();
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio(){
		// per prima cosa scorro gli archi del grafo e calcolo il peso medio
		double pesoTot = 0.0;
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		double avg = pesoTot / this.grafo.edgeSet().size();
		System.out.println("PESO MEDIO: " + avg);
		
		// adesso devo scorrere nuovamente tutti gli archi per prendere quelli con peso maggiore del peso medio
		List<Adiacenza> result = new ArrayList<>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e) > avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), 
						this.grafo.getEdgeTarget(e), (int)(this.grafo.getEdgeWeight(e))));
			}
		}
		return result;
	}
	
	// metodo che prepara la ricorsione
	public List<String> calcolaPercorso(String sorgente, String destinazione){
		this.best = new LinkedList<>(); 
		List<String> parziale = new LinkedList<>(); // lista con cui costruiamo passo passo la nostra soluzione
		parziale.add(sorgente); // non partiamo da zero ma sicuramente partiremo dalla sorgente e quindi possiamo aggiungerla alla soluzione parziale
		cerca(parziale, destinazione);
		return best; // quando il metodo ricorsivo 'cerca' sarà terminato, la soluzione 'best' sarà stata riempita e quindi posso restituirla 
	}
	
	// metodo veramente ricorsivo
	private void cerca(List<String> parziale, String destinazione) {
		// condizione di terminazione
		if (parziale.get(parziale.size()-1).equals(destinazione)) { // controllo se l'ultimo elemento inserito nel percorso coincide con la destinazione
			// ... ma è la soluzione migliore?
			if (parziale.size() > best.size()) {
				best = new LinkedList<>(parziale); // sovrascrivo la soluzione migliore con una "new"
			}
			return; // in ogni caso esco dalla ricorsione perchè non posso più andare avanti
		}
		// scorro i vicini dell'ultimo elemento inserito in "parziale" e provo le varie "strade" alla ricerca del cammino migliore
		for (String v : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) { 
			if (!parziale.contains(v)) { // questo controllo ci permette di evitare la formazione di cicli
				parziale.add(v); // aggiungo il vertice solo se parziale non lo contiene ancora
				cerca(parziale, destinazione);
				parziale.remove(parziale.size()-1); // backtracking
			}
		}
	}
}
