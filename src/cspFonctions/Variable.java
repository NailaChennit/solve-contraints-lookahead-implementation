package cspFonctions;
import java.util.ArrayList;

public class Variable {
	int nom;
	ArrayList<Integer> domaine;
	
	public Variable(int nom, ArrayList<Integer> domaine) {
		super();
		this.nom = nom;
		this.domaine = domaine;
	}
	public Variable() {
		// TODO Auto-generated constructor stub
	}
	public int getNom() {
		return nom;
	}
	public void setNom(int nom) {
		this.nom = nom;
	}
	public ArrayList<Integer> getDomaine() {
		return domaine;
	}
	public void setDomaine(ArrayList<Integer> domaine) {
		this.domaine = domaine;
	}


}
