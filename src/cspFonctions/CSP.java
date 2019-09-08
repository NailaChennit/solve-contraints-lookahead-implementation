package cspFonctions;

import java.util.ArrayList;

public class CSP {
	public Variable [] X;
	//ArrayList<Integer> D;
	public Contrainte [] C;
	ArrayList <Contrainte> ContrainteUnaire;

	public CSP(Variable[] x, Contrainte[] c) {
		super();
		X = x;
		C = c;
	}

	public CSP(CSP csp) {
		X=copyX(csp);
		C=copyC(csp);
		
	}

	
	public CSP(CSP csp,int rien) {
		X=copyX(csp);
		C=copyC(csp);
		ContrainteUnaire=csp.ContrainteUnaire;
	}

	public CSP() {
		// TODO Auto-generated constructor stub
	}

	public Variable[] copyX(CSP csp) {
		
		Variable[] temp=new Variable[csp.X.length];
		for(int i=0;i<csp.X.length;i++) {
			temp[i]=new Variable(csp.X[i].nom,Fonctions.AffectArray(csp.X[i].domaine));
			
		}
		return temp;
	}
	
	public Contrainte[] copyC(CSP csp) {
		
		Contrainte[] temp=new Contrainte[csp.C.length];
		for(int i=0;i<csp.C.length;i++) {
			Variable var1=new Variable(csp.C[i].variable1.nom,Fonctions.AffectArray(csp.C[i].variable1.domaine));
			Variable var2=new Variable(csp.C[i].variable2.nom,Fonctions.AffectArray(csp.C[i].variable2.domaine));

		    temp[i]=new Contrainte(var1,var2,Fonctions.AffectMat(csp.C[i].matrice));
						
		}
		return temp;
	}
}
