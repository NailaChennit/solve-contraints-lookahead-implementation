package cspFonctions;

public class Contrainte {
	Variable variable1;
	Variable variable2;
	int [][] matrice;
	
	public Contrainte(Variable variable1,int [][]matrice) {
		this.variable1 = variable1;
		this.matrice = matrice;
	}
	
	public Contrainte(Variable variable1, Variable variable2) {
		
		this.variable1 = variable1;
		this.variable2 = variable2;
		this.matrice = MatriceContrainte(variable1.domaine.size(),variable2.domaine.size());
	}
	
	
	
	public Contrainte(Variable variable1, Variable variable2,int [][]matrice) {
		super();
		this.variable1 = variable1;
		this.variable2 = variable2;
		this.matrice = matrice;
	}
	
	  
	 	
	
	   int[][] MatriceContrainte(int a, int b){
		    int [][] mat =new int[a][b];
	    	for(int i=0;i<a;i++ )	{
	    		for(int j=0;j<b;j++ ) {
	    			int val =(int) Math.round( Math.random()) ;
	    			mat[i][j]=val;
				}
			}
	    	return mat;
		}

	public Variable getVariable1() {
		return variable1;
	}
	public void setVariable1(Variable variable1) {
		this.variable1 = variable1;
	}
	public Variable getVariable2() {
		return variable2;
	}
	public void setVariable2(Variable variable2) {
		this.variable2 = variable2;
	}
	public int[][] getMatrice() {
		return matrice;
	}
	public void setMatrice(int[][] matrice) {
		this.matrice = matrice;
	}

}
