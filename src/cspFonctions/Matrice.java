package cspFonctions;

import java.util.ArrayList;

public class Matrice {
int [][] matrice;

public static String[][] MptoString (Matrice [][]Mp ){
	String [][] MpString=new String[Mp.length][Mp.length];
	for (int i = 0; i < Mp.length; i++) {
	    for (int j = 0; j < Mp.length; j++) {
	    	MpString[i][j]=MatrixtoString(Mp[i][j].matrice);
	       // System.out.println(MpString[i][j]);

	    }
	    
	}
	    
	return MpString;
}

static String MatrixtoString(int [][]mat) {
	String matString="";
	for (int i = 0; i < mat.length; i++) {
	    for (int j = 0; j < mat[0].length; j++) {
	        matString=matString+Integer.toString(mat[i][j])+" ";
	    }
	    matString=matString+"\n";
	    
	}

	return matString;	
}

}
